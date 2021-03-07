package org.kryptonmc.krypton.packet

import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.event.ClickEvent.suggestCommand
import net.kyori.adventure.text.event.HoverEvent.ShowEntity
import net.kyori.adventure.text.event.HoverEvent.showEntity
import org.kryptonmc.krypton.*
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.auth.exceptions.AuthenticationException
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.entity.Hand
import org.kryptonmc.krypton.entity.entities.Player
import org.kryptonmc.krypton.entity.metadata.MovementFlags
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.play.*
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.*
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.data.*
import org.kryptonmc.krypton.packet.out.login.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.play.*
import org.kryptonmc.krypton.packet.out.play.chat.*
import org.kryptonmc.krypton.packet.out.play.entity.*
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.*
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.session.Session
import org.kryptonmc.krypton.session.SessionManager
import org.kryptonmc.krypton.space.toAngle
import org.kryptonmc.krypton.world.Location
import java.util.*
import javax.crypto.spec.SecretKeySpec
import kotlin.math.max

class PacketHandler(private val sessionManager: SessionManager, private val server: Server) {

    private val verifyToken by lazy {
        val bytes = ByteArray(4)
        server.random.nextBytes(bytes)
        bytes
    }

    fun handle(session: Session, packet: Packet) {
        when (packet) {
            is PacketInHandshake -> handleHandshake(session, packet)
            is PacketInPing -> handlePing(session, packet)
            is PacketInStatusRequest -> handleStatusPacket(session)
            is PacketInLoginStart -> handleLoginStart(session, packet)
            is PacketInEncryptionResponse -> handleEncryptionResponse(session, packet)
            is PacketInClientSettings -> handleClientSettings(session, packet)
            is PacketInPlayerPosition -> handlePositionUpdate(session, packet)
            is PacketInPlayerRotation -> handleRotationUpdate(session, packet)
            is PacketInPlayerPositionAndRotation -> handlePositionAndRotationUpdate(session, packet)
            is PacketInChat -> handleChat(session, packet)
            is PacketInKeepAlive -> handleKeepAlive(session, packet)
            is PacketInAnimation -> handleAnimation(session, packet)
            is PacketInTeleportConfirm -> Unit // we can ignore this for now
            is PacketInEntityAction -> handleEntityAction(session, packet)
        }
    }

    private fun handleHandshake(session: Session, packet: PacketInHandshake) {
        when (val nextState = packet.data.nextState) {
            PacketState.LOGIN -> {
                session.currentState = PacketState.LOGIN
                if (packet.data.protocol != ServerInfo.PROTOCOL) {
                    val key = when {
                        packet.data.protocol < ServerInfo.PROTOCOL -> "multiplayer.disconnect.outdated_client"
                        packet.data.protocol > ServerInfo.PROTOCOL -> "multiplayer.disconnect.outdated_server"
                        else -> "multiplayer.disconnect.incompatible"
                    }
                    val reason = translatable {
                        key(key)
                        args(text { content(ServerInfo.VERSION) })
                    }
                    session.sendPacket(PacketOutDisconnect(reason))
                    session.disconnect()
                    return
                }
                if (ServerStorage.PLAYER_COUNT.get() >= server.config.status.maxPlayers) {
                    session.sendPacket(PacketOutDisconnect(translatable { key("multiplayer.disconnect.server_full") }))
                    session.disconnect()
                }
            }
            PacketState.STATUS -> session.currentState = PacketState.STATUS
            else -> throw UnsupportedOperationException("Invalid next state $nextState")
        }
    }

    private fun handleClientSettings(session: Session, packet: PacketInClientSettings) {
        session.settings = packet.settings
        session.sendPacket(PacketOutEntityMetadata(
            session.id,
            PlayerMetadata(mainHand = packet.settings.mainHand, skinFlags = packet.settings.skinFlags)
        ))
    }

    private fun handleStatusPacket(session: Session) {
        val players = sessionManager.sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .map { PlayerInfo(it.profile.name, it.profile.uuid) }
            .toSet()

        session.sendPacket(PacketOutStatusResponse(StatusResponse(
            ServerVersion(ServerInfo.VERSION, ServerInfo.PROTOCOL),
            Players(server.config.status.maxPlayers, ServerStorage.PLAYER_COUNT.get(), players),
            server.config.status.motd
        )))
    }

    private fun handlePing(session: Session, packet: PacketInPing) {
        session.sendPacket(PacketOutPong(packet.payload))
    }

    private fun handleLoginStart(session: Session, packet: PacketInLoginStart) {
        session.player = Player(ServerStorage.NEXT_ENTITY_ID.getAndIncrement())
        session.player.name = packet.name

        if (!server.config.server.onlineMode) {
            val offlineUUID = UUID.nameUUIDFromBytes("OfflinePlayer:${packet.name}".encodeToByteArray())
            session.profile = GameProfile(offlineUUID, packet.name, emptyList())
            sessionManager.beginPlayState(session)
            return
        }

        session.sendPacket(PacketOutEncryptionRequest(server.encryption.publicKey, verifyToken))
    }

    private fun handleEncryptionResponse(session: Session, packet: PacketInEncryptionResponse) {
        sessionManager.verifyToken(session, verifyToken, packet.verifyToken)

        val sharedSecret = server.encryption.decrypt(packet.secret)
        val secretKey = SecretKeySpec(sharedSecret, "AES")
        sessionManager.enableEncryption(session, secretKey)

        try {
            session.profile = SessionService.authenticateUser(session.player.name, sharedSecret, server.encryption.publicKey, server.config.server.ip)
        } catch (exception: AuthenticationException) {
            session.sendPacket(PacketOutDisconnect(translatable { key("multiplayer.disconnect.unverified_username") }))
            session.disconnect()
            return
        }
        sessionManager.enableCompression(session, server.config.server.compressionThreshold)

        sessionManager.beginPlayState(session)
    }

    private fun handlePositionUpdate(session: Session, packet: PacketInPlayerPosition) {
        val oldLocation = session.player.location
        val newLocation = Location(session.player.location.world, packet.x, packet.y, packet.z)
        session.player.location = newLocation

        val positionPacket = PacketOutEntityPosition(
            session.id,
            calculatePositionChange(newLocation.x, oldLocation.x),
            calculatePositionChange(newLocation.y, oldLocation.y),
            calculatePositionChange(newLocation.z, oldLocation.z),
            packet.onGround
        )

        sessionManager.sendPackets(positionPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handleRotationUpdate(session: Session, packet: PacketInPlayerRotation) {
        val rotationPacket = PacketOutEntityRotation(
            session.id,
            packet.yaw.toAngle(),
            packet.pitch.toAngle(),
            packet.onGround
        )
        val headLookPacket = PacketOutEntityHeadLook(session.id, packet.yaw.toAngle())

        sessionManager.sendPackets(rotationPacket, headLookPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handlePositionAndRotationUpdate(session: Session, packet: PacketInPlayerPositionAndRotation) {
        val oldLocation = session.player.location
        val newLocation = Location(session.player.location.world, packet.x, packet.y, packet.z, packet.yaw, packet.pitch)
        session.player.location = newLocation

        val positionAndRotationPacket = PacketOutEntityPositionAndRotation(
            session.id,
            calculatePositionChange(newLocation.x, oldLocation.x),
            calculatePositionChange(newLocation.y, oldLocation.y),
            calculatePositionChange(newLocation.z, oldLocation.z),
            newLocation.yaw.toAngle(),
            newLocation.pitch.toAngle(),
            packet.onGround
        )
        val headLookPacket = PacketOutEntityHeadLook(session.id, newLocation.yaw.toAngle())

        sessionManager.sendPackets(positionAndRotationPacket, headLookPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handleChat(session: Session, packet: PacketInChat) {
        val chatPacket = PacketOutChat(
            translatable {
                key("chat.type.text")
                args(text {
                    content(session.profile.name)
                    insertion(session.profile.name)
                    clickEvent(suggestCommand("/msg ${session.profile.name}"))
                    hoverEvent(showEntity(ShowEntity.of(
                        Key.key("minecraft", "player"),
                        session.profile.uuid,
                        text { content(session.profile.name) }
                    )))
                }, text { content(packet.message) })
            },
            ChatPosition.CHAT_BOX,
            session.profile.uuid
        )

        sessionManager.sendPackets(chatPacket) {
            it.currentState == PacketState.PLAY && it.settings.chatMode == ChatMode.ENABLED
        }
    }

    private fun handleKeepAlive(session: Session, packet: PacketInKeepAlive) {
        if (session.lastKeepAliveId == packet.keepAliveId) {
            sessionManager.updateLatency(session, max((packet.keepAliveId - session.lastKeepAliveId), 0L).toInt())
            return
        }
        session.sendPacket(PacketOutPlayDisconnect(translatable { key("disconnect.timeout") }))
        session.disconnect()
    }

    private fun handleAnimation(session: Session, packet: PacketInAnimation) {
        val animation = when (packet.hand) {
            Hand.MAIN -> EntityAnimation.SWING_MAIN_ARM
            Hand.OFF -> EntityAnimation.SWING_OFFHAND
        }

        val animationPacket = PacketOutEntityAnimation(session.id, animation)

        sessionManager.sendPackets(animationPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handleEntityAction(session: Session, packet: PacketInEntityAction) {
        val metadata = when (packet.action) {
            EntityAction.START_SNEAKING -> PlayerMetadata(movementFlags = MovementFlags(isCrouching = true))
            EntityAction.STOP_SNEAKING -> PlayerMetadata(movementFlags = MovementFlags(isCrouching = false))
            EntityAction.LEAVE_BED -> PlayerMetadata(bedPosition = Optional(null))
            EntityAction.START_SPRINTING -> PlayerMetadata(movementFlags = MovementFlags(isSprinting = true))
            EntityAction.STOP_SPRINTING -> PlayerMetadata(movementFlags = MovementFlags(isSprinting = false))
            EntityAction.START_FLYING_WITH_ELYTRA -> PlayerMetadata(movementFlags = MovementFlags(isFlying = true))
            else -> return
        }

        val metadataPacket = PacketOutEntityMetadata(session.id, metadata)

        sessionManager.sendPackets(metadataPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }
}

fun calculatePositionChange(new: Double, old: Double) = ((new * 32 - old * 32) * 128).toInt().toShort()