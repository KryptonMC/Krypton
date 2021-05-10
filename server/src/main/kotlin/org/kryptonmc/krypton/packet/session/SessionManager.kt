/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.session

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.api.event.events.login.JoinEvent
import org.kryptonmc.krypton.api.event.events.play.QuitEvent
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.entity.metadata.MovementFlags
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.handlers.PlayHandler
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.play.BorderAction
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareCommands
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutHeldItemChange
import org.kryptonmc.krypton.packet.out.play.PacketOutJoinGame
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerAction
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo.PlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerPositionAndLook
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutServerDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutTags
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutUnlockRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutWorldBorder
import org.kryptonmc.krypton.packet.out.play.UnlockRecipesAction
import org.kryptonmc.krypton.packet.out.play.chunk.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityDestroy
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityHeadLook
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityProperties.Companion.DEFAULT_PLAYER_ATTRIBUTES
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.entity.spawn.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.window.PacketOutWindowItems
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.util.Angle
import org.kryptonmc.krypton.util.concurrent.NamedThreadFactory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.toProtocol
import org.kryptonmc.krypton.world.Gamerule
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.floor

/**
 * The session manager is, as the name suggests, responsible for managing sessions. It was a replacement for
 * the whacky logic that used to go on in the old `PacketHandler`, which was even more of a god object than it
 * was before it was removed.
 */
// TODO: Look into this and see if it should be replaced by something better
class SessionManager(private val server: KryptonServer) {

    val sessions: MutableSet<Session> = ConcurrentHashMap.newKeySet()

    private val keepAliveExecutor = Executors.newScheduledThreadPool(8, NamedThreadFactory("Keep Alive Thread #%d"))

    init {
        Runtime.getRuntime().addShutdownHook(Thread { keepAliveExecutor.shutdown() })
    }

    fun beginPlayState(session: Session) {
        session.sendPacket(PacketOutLoginSuccess(session.profile.uuid, session.profile.name))
        session.handler = PlayHandler(server, this, session)

        val event = JoinEvent(session.player)
        server.eventBus.call(event)
        if (event.isCancelled) {
            session.disconnect(event.cancelledReason)
            return
        }

        val world = server.worldManager.default
        if (server.config.world.forceDefaultGamemode) world.gamemode = server.config.world.gamemode
        world.players += session.player
        server.players += session.player

        // load the player's data and populate the provided player with their loaded data
        server.playerDataManager.load(world, session.player)
        if (server.config.world.forceDefaultGamemode) session.player.gamemode = server.config.world.gamemode
        val spawnLocation = session.player.location
        session.player.updateAbilities()

        session.currentState = PacketState.PLAY
        session.sendPacket(PacketOutJoinGame(
            session.id,
            server.config.world.hardcore,
            world,
            session.player.gamemode,
            null,
            session.player.dimension,
            server.config.status.maxPlayers,
            server.config.world.viewDistance
        ))
        session.sendPacket(PacketOutPluginMessage(BRAND_MESSAGE.first, BRAND_MESSAGE.second))
        session.sendPacket(PacketOutServerDifficulty(server.config.world.difficulty, true))

        val metadata = PlayerMetadata(
            MovementFlags(isFlying = session.player.abilities.isFlying),
            PlayerMetadata.airTicks,
            PlayerMetadata.customName,
            PlayerMetadata.isCustomNameVisible,
            PlayerMetadata.isSilent,
            PlayerMetadata.hasNoGravity,
            PlayerMetadata.pose,
            PlayerMetadata.handFlags,
            PlayerMetadata.health,
            PlayerMetadata.potionEffectColor,
            PlayerMetadata.isPotionEffectAmbient,
            PlayerMetadata.arrowsInEntity,
            PlayerMetadata.absorptionHealth,
            Optional(null), // setting this to null tells the client it's not sleeping
            PlayerMetadata.additionalHearts,
            PlayerMetadata.score,
            PlayerMetadata.skinFlags,
            PlayerMetadata.mainHand,
            PlayerMetadata.leftShoulderEntityData,
            PlayerMetadata.rightShoulderEntityData
        )

        session.sendPacket(PacketOutAbilities(session.player.abilities))
        session.sendPacket(PacketOutHeldItemChange(session.player.inventory.heldSlot))
        session.sendPacket(PacketOutDeclareRecipes())
        session.sendPacket(PacketOutTags)
        if (world.gamerules[Gamerule.REDUCED_DEBUG_INFO]?.equals("true") == true) session.sendPacket(PacketOutEntityStatus(session.id, 22))
        session.sendPacket(PacketOutDeclareCommands(server.commandManager.dispatcher.root))
        session.sendPacket(PacketOutUnlockRecipes(UnlockRecipesAction.INIT))
        session.sendPacket(PacketOutPlayerPositionAndLook(session.player.location))
        server.sendMessage(event.message)
        session.sendPacket(PacketOutPlayerInfo(
            PlayerAction.UPDATE_LATENCY,
            listOf(PlayerInfo(latency = session.latency, profile = session.profile))
        ))

        val playerInfos = sessions.filter { it.currentState == PacketState.PLAY }.map {
            PlayerInfo(0, it.player.gamemode, it.profile, text(it.profile.name))
        }
        if (playerInfos.isNotEmpty()) session.sendPacket(PacketOutPlayerInfo(PlayerAction.ADD_PLAYER, playerInfos))

        GlobalScope.launch(Dispatchers.IO) { handlePlayStateBegin(session) }

        sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                session.sendPacket(PacketOutSpawnPlayer(it.player))
                session.sendPacket(PacketOutEntityMetadata(it.id, metadata))
                session.sendPacket(PacketOutEntityProperties(it.id, session.player.attributes))
                session.sendPacket(PacketOutEntityHeadLook(it.id, it.player.location.yaw.toAngle()))
            }

        val centerChunk = ChunkPosition(floor(spawnLocation.x / 16.0).toInt(), floor(spawnLocation.z / 16.0).toInt())
        session.sendPacket(PacketOutUpdateViewPosition(centerChunk))
        session.player.updateChunks() // Initial stream

        session.sendPacket(PacketOutEntityMetadata(session.id, metadata))
        session.sendPacket(PacketOutWorldBorder(BorderAction.INITIALIZE, world.border))
        session.sendPacket(PacketOutTimeUpdate(world.time, world.dayTime))
        session.sendPacket(PacketOutSpawnPosition(spawnLocation))
        session.sendPacket(PacketOutEntityProperties(session.id, session.player.attributes))
        session.sendPacket(PacketOutWindowItems(session.player.inventory))

        ServerStorage.PLAYER_COUNT.getAndIncrement()

        keepAliveExecutor.scheduleAtFixedRate({
            val keepAliveId = System.currentTimeMillis()
            session.lastKeepAliveId = keepAliveId
            session.sendPacket(PacketOutKeepAlive(keepAliveId))
        }, 0, 20, TimeUnit.SECONDS)
    }

    private fun handlePlayStateBegin(session: Session) {
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.ADD_PLAYER,
            listOf(
                PlayerInfo(
                    0,
                    session.player.gamemode,
                    session.profile,
                    text(session.profile.name)
                )
            )
        )
        val spawnPlayerPacket = PacketOutSpawnPlayer(session.player)
        val metadataPacket = PacketOutEntityMetadata(session.id, PlayerMetadata)
        val propertiesPacket = PacketOutEntityProperties(session.id, DEFAULT_PLAYER_ATTRIBUTES)
        val headLookPacket = PacketOutEntityHeadLook(session.id, Angle.ZERO)

        sessions.asSequence()
            .filter { it != session }
            .filter { it.currentState == PacketState.PLAY }
            .forEach {
                it.sendPacket(infoPacket)
                it.sendPacket(spawnPlayerPacket)
                it.sendPacket(metadataPacket)
                it.sendPacket(propertiesPacket)
                it.sendPacket(headLookPacket)
            }
    }

    fun handleDisconnection(session: Session) {
        if (session.currentState != PacketState.PLAY) return

        val event = QuitEvent(session.player)
        server.eventBus.call(event)
        GlobalScope.launch(Dispatchers.IO) { server.playerDataManager.save(session.player) }

        val destroyPacket = PacketOutEntityDestroy(listOf(session.id))
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.REMOVE_PLAYER,
            listOf(PlayerInfo(profile = session.profile))
        )

        sendPackets(destroyPacket, infoPacket) { it != session && it.currentState == PacketState.PLAY }
        server.sendMessage(event.message)
        ServerStorage.PLAYER_COUNT.getAndDecrement()
    }

    fun updateLatency(session: Session, latency: Int) {
        session.latency = latency
        val infoPacket = PacketOutPlayerInfo(
            PlayerAction.UPDATE_LATENCY,
            listOf(PlayerInfo(latency, profile = session.profile))
        )

        sessions.asSequence()
            .filter { it.currentState == PacketState.PLAY }
            .forEach { it.sendPacket(infoPacket) }
    }

    fun sendPackets(vararg packets: Packet, predicate: (Session) -> Boolean = { true }) {
        sessions.asSequence().filter(predicate).forEach { session -> packets.forEach(session::sendPacket) }
    }

    fun shutdown() {
        if (sessions.isEmpty()) return
        val reason = translatable("multiplayer.disconnect.server_shutdown")
        sessions.forEach { it.disconnect(reason) }
    }

    companion object {

        private val BRAND_MESSAGE = NamespacedKey(value = "brand") to "Krypton".toProtocol()
        private val LOGGER = logger<SessionManager>()
    }
}
