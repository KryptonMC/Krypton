/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.network.handlers

import com.mojang.brigadier.StringReader
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.translation.Translator
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.CommandSigningContext
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.player.KryptonPlayerSettings
import org.kryptonmc.krypton.entity.player.KryptonSkinParts
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.event.command.KryptonCommandExecuteEvent
import org.kryptonmc.krypton.event.player.KryptonPlayerChatEvent
import org.kryptonmc.krypton.event.player.KryptonPlayerMoveEvent
import org.kryptonmc.krypton.event.player.interact.KryptonPlayerPlaceBlockEvent
import org.kryptonmc.krypton.event.player.KryptonPluginMessageReceivedEvent
import org.kryptonmc.krypton.event.player.KryptonPlayerResourcePackStatusEvent
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.item.handler.ItemTimedHandler
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.network.PacketSendListener
import org.kryptonmc.krypton.network.chat.ChatUtil
import org.kryptonmc.krypton.network.chat.ChatTypes
import org.kryptonmc.krypton.network.chat.LastSeenMessages
import org.kryptonmc.krypton.network.chat.LastSeenMessagesValidator
import org.kryptonmc.krypton.network.chat.MessageSignatureCache
import org.kryptonmc.krypton.network.chat.PlayerChatMessage
import org.kryptonmc.krypton.network.chat.RemoteChatSession
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.SignableCommand
import org.kryptonmc.krypton.network.chat.SignedMessageBody
import org.kryptonmc.krypton.network.chat.SignedMessageChain
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatSessionUpdate
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientInformation
import org.kryptonmc.krypton.packet.`in`.play.PacketInCommandSuggestionsRequest
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction.Action as PlayerAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerCommand.Action as EntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerInput
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInQueryEntityTag
import org.kryptonmc.krypton.packet.`in`.play.PacketInResourcePack
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetCreativeModeSlot
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInSwingArm
import org.kryptonmc.krypton.packet.`in`.play.PacketInUseItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInUseItemOn
import org.kryptonmc.krypton.packet.out.play.EntityAnimations
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutCommandSuggestionsResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisguisedChat
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChat
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSystemChat
import org.kryptonmc.krypton.packet.out.play.PacketOutTagQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityRotation
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.util.FutureChain
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.event.player.action.KryptonPlayerStartSneakingEvent
import org.kryptonmc.krypton.event.player.action.KryptonPlayerStartSprintingEvent
import org.kryptonmc.krypton.event.player.action.KryptonPlayerStopSneakingEvent
import org.kryptonmc.krypton.event.player.action.KryptonPlayerStopSprintingEvent
import org.kryptonmc.krypton.event.player.interact.KryptonPlayerAttackEntityEvent
import org.kryptonmc.krypton.event.player.interact.KryptonPlayerInteractAtEntityEvent
import org.kryptonmc.krypton.event.player.interact.KryptonPlayerInteractWithEntityEvent
import org.kryptonmc.krypton.locale.DisconnectMessages
import org.kryptonmc.krypton.locale.MinecraftTranslationManager
import java.time.Duration
import java.time.Instant
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicReference

/**
 * This is the largest and most important of the four packet handlers, as the
 * play state is where the vast majority of packets reside.
 *
 * As mentioned above, this is the packet handler for the
 * [Play][org.kryptonmc.krypton.packet.PacketState.PLAY] state.
 *
 * This handles all supported inbound packets in the play state.
 */
class PlayPacketHandler(
    private val server: KryptonServer,
    override val connection: NettyConnection,
    private val player: KryptonPlayer
) : TickablePacketHandler {

    private var lastKeepAlive = System.currentTimeMillis()
    private var keepAliveChallenge = 0L
    private var pendingKeepAlive = false

    private val lastChatTimestamp = AtomicReference(Instant.EPOCH)
    private var chatSession: RemoteChatSession? = null
    private var signedMessageDecoder = if (server.config.advanced.enforceSecureProfiles) {
        SignedMessageChain.Decoder.REJECT_ALL
    } else {
        SignedMessageChain.Decoder.unsigned(player.uuid)
    }
    private val lastSeenMessages = LastSeenMessagesValidator(20)
    private val messageSignatureCache = MessageSignatureCache.createDefault()
    private val chatMessageChain = FutureChain(server)

    override fun tick() {
        val time = System.currentTimeMillis()
        if (time - lastKeepAlive < KEEP_ALIVE_INTERVAL) return
        if (pendingKeepAlive) {
            disconnect(DisconnectMessages.TIMEOUT)
            return
        }
        pendingKeepAlive = true
        lastKeepAlive = time
        keepAliveChallenge = time
        connection.send(PacketOutKeepAlive(keepAliveChallenge))
    }

    fun disconnect(reason: Component) {
        connection.send(PacketOutDisconnect(reason), PacketSendListener.thenRun { connection.disconnect(reason) })
        connection.setReadOnly()
    }

    override fun onDisconnect(message: Component) {
        chatMessageChain.close()
        val translated = MinecraftTranslationManager.render(message)
        LOGGER.info("${player.name} was disconnected: ${PlainTextComponentSerializer.plainText().serialize(translated)}")
        player.disconnect()
        server.playerManager.removePlayer(player)
    }

    fun handleSwingArm(packet: PacketInSwingArm) {
        val animation = when (packet.hand) {
            Hand.MAIN -> EntityAnimations.SWING_MAIN_ARM
            Hand.OFF -> EntityAnimations.SWING_OFFHAND
        }
        server.connectionManager.sendGroupedPacket(PacketOutAnimation(player.id, animation)) { it !== player }
    }

    fun handleChatCommand(packet: PacketInChatCommand) {
        if (!ChatUtil.isValidMessage(packet.command)) disconnect(DisconnectMessages.ILLEGAL_CHARACTERS)

        val event = server.eventNode.fire(KryptonCommandExecuteEvent(player, packet.command))
        if (!event.isAllowed()) return

        val command = event.result?.command ?: packet.command
        val lastSeen = tryHandleChat(command, packet.timestamp, packet.lastSeenMessages) ?: return

        val source = player.createCommandSourceStack()
        val parsed = server.commandManager.parse(source, command)
        val arguments = try {
            collectSignedArguments(packet, SignableCommand.of(parsed), lastSeen)
        } catch (exception: SignedMessageChain.DecodeException) {
            handleMessageDecodeFailure(exception)
            return
        }

        server.commandManager.dispatch(source.withSigningContext(CommandSigningContext.SignedArguments(arguments)), command)
    }

    private fun collectSignedArguments(packet: PacketInChatCommand, command: SignableCommand<*>,
                                       lastSeenMessages: LastSeenMessages): Map<String, PlayerChatMessage> {
        val result = Object2ObjectOpenHashMap<String, PlayerChatMessage>()
        command.arguments.forEach {
            val signature = packet.argumentSignatures.get(it.name())
            val body = SignedMessageBody(it.value, packet.timestamp, packet.salt, lastSeenMessages)
            result.put(it.name(), signedMessageDecoder.unpack(signature, body))
        }
        return result
    }

    fun handleChat(packet: PacketInChat) {
        // Sanity check message content
        if (!ChatUtil.isValidMessage(packet.message)) disconnect(DisconnectMessages.ILLEGAL_CHARACTERS)

        // Fire the chat event
        val event = server.eventNode.fire(KryptonPlayerChatEvent(player, packet.message))
        if (!event.isAllowed()) return

        val lastSeen = tryHandleChat(packet.message, packet.timestamp, packet.lastSeenMessages) ?: return
        val message = try {
            getSignedMessage(packet, lastSeen)
        } catch (exception: SignedMessageChain.DecodeException) {
            handleMessageDecodeFailure(exception)
            return
        }

        val unsignedContent = event.result?.message ?: message.decoratedContent()
        chatMessageChain.append {
            CompletableFuture.supplyAsync({ broadcastChatMessage(message.withUnsignedContent(unsignedContent)) }, it)
        }
    }

    private fun handleMessageDecodeFailure(exception: SignedMessageChain.DecodeException) {
        if (exception.shouldDisconnect) {
            disconnect(exception.asComponent())
        } else {
            player.sendSystemMessage(exception.asComponent().color(NamedTextColor.RED))
        }
    }

    private fun tryHandleChat(message: String, timestamp: Instant, update: LastSeenMessages.Update): LastSeenMessages? {
        if (!updateChatOrder(timestamp)) {
            LOGGER.warn("Out of order chat message '$message' received from ${player.profile.name}. Disconnecting...")
            disconnect(DisconnectMessages.OUT_OF_ORDER_CHAT)
            return null
        }
        if (player.settings.chatVisibility == ChatVisibility.HIDDEN) {
            connection.send(PacketOutSystemChat(Component.translatable("chat.disabled.options", NamedTextColor.RED), false))
            return null
        }
        val lastSeen = unpackAndApplyLastSeen(update)
        player.resetLastActionTime()
        return lastSeen
    }

    private fun unpackAndApplyLastSeen(update: LastSeenMessages.Update): LastSeenMessages? {
        val updated = synchronized(lastSeenMessages) { lastSeenMessages.applyUpdate(update) }
        if (updated == null) {
            LOGGER.warn("Failed to validate message acknowledgements from ${player.profile.name}. Disconnecting...")
            disconnect(DisconnectMessages.CHAT_VALIDATION_FAILED)
        }
        return updated
    }

    private fun updateChatOrder(timestamp: Instant): Boolean {
        do {
            val current = lastChatTimestamp.get()
            if (timestamp.isBefore(current)) return false
        } while (!lastChatTimestamp.compareAndSet(current, timestamp))
        return true
    }

    private fun getSignedMessage(packet: PacketInChat, lastSeenMessages: LastSeenMessages): PlayerChatMessage =
        signedMessageDecoder.unpack(packet.signature, SignedMessageBody(packet.message, packet.timestamp, packet.salt, lastSeenMessages))

    private fun broadcastChatMessage(message: PlayerChatMessage) {
        server.playerManager.broadcastChatMessage(message, player, RichChatType.bind(ChatTypes.CHAT, player))
    }

    private fun addPendingMessage(message: PlayerChatMessage) {
        val signature = message.signature ?: return
        messageSignatureCache.push(message)
        val trackedMessageCount = synchronized(lastSeenMessages) {
            lastSeenMessages.addPending(signature)
            lastSeenMessages.trackedMessagesCount()
        }
        if (trackedMessageCount > 4096) disconnect(Component.translatable("multiplayer.disconnect.too_many_pending_chats"))
    }

    fun sendPlayerChatMessage(message: PlayerChatMessage, type: RichChatType.Bound) {
        val packet = PacketOutPlayerChat(message.link.sender, message.link.index, message.signature, message.signedBody.pack(messageSignatureCache),
            message.unsignedContent, message.filterMask, type.toNetwork())
        connection.send(packet)
        addPendingMessage(message)
    }

    fun sendDisguisedChatMessage(message: Component, type: RichChatType.Bound) {
        connection.send(PacketOutDisguisedChat(message, type.toNetwork()))
    }

    fun handleChatSessionUpdate(packet: PacketInChatSessionUpdate) {
        val session = packet.chatSession
        val currentKey = chatSession?.publicKey?.data
        val newKey = session.publicKey
        if (currentKey == newKey) return // Nothing to update
        if (currentKey != null && newKey.expiryTime.isBefore(currentKey.expiryTime)) {
            disconnect(PlayerPublicKey.EXPIRED_KEY)
            return
        }
        try {
            resetPlayerChatState(session.validate(player.profile, SignatureValidator.YGGDRASIL, Duration.ZERO))
        } catch (exception: PlayerPublicKey.ValidationException) {
            LOGGER.error("Failed to validate public key!", exception)
            disconnect(exception.asComponent())
        }
    }

    private fun resetPlayerChatState(session: RemoteChatSession) {
        chatSession = session
        signedMessageDecoder = session.createMessageDecoder(player.uuid)
        chatMessageChain.append {
            player.setChatSession(session)
            server.connectionManager.sendGroupedPacket(PacketOutPlayerInfoUpdate(PacketOutPlayerInfoUpdate.Action.INITIALIZE_CHAT, player))
            CompletableFuture.completedFuture(null)
        }
    }

    fun handleClientInformation(packet: PacketInClientInformation) {
        player.settings = KryptonPlayerSettings(
            Translator.parseLocale(packet.locale),
            packet.viewDistance,
            packet.chatVisibility,
            packet.chatColors,
            KryptonSkinParts(packet.skinSettings.toInt()),
            packet.mainHand,
            packet.filterText,
            packet.allowsListing
        )
    }

    fun handleSetCreativeModeSlot(packet: PacketInSetCreativeModeSlot) {
        if (player.gameMode != GameMode.CREATIVE) return
        val item = packet.clickedItem
        val inValidRange = packet.slot >= 1 && packet.slot < KryptonPlayerInventory.SIZE
        val isValid = item.isEmpty() || item.meta.damage >= 0 && item.amount <= 64 && !item.isEmpty()
        if (inValidRange && isValid) player.inventory.setItem(packet.slot, packet.clickedItem)
    }

    fun handlePlayerCommand(packet: PacketInPlayerCommand) {
        when (packet.action) {
            EntityAction.START_SNEAKING -> {
                if (!server.eventNode.fire(KryptonPlayerStartSneakingEvent(player)).isAllowed()) return
                player.isSneaking = true
            }
            EntityAction.STOP_SNEAKING -> {
                if (!server.eventNode.fire(KryptonPlayerStopSneakingEvent(player)).isAllowed()) return
                player.isSneaking = false
            }
            EntityAction.START_SPRINTING -> {
                if (!server.eventNode.fire(KryptonPlayerStartSprintingEvent(player)).isAllowed()) return
                player.isSprinting = true
            }
            EntityAction.STOP_SPRINTING -> {
                if (!server.eventNode.fire(KryptonPlayerStopSprintingEvent(player)).isAllowed()) return
                player.isSprinting = false
            }
            EntityAction.STOP_SLEEPING -> Unit // TODO: Sleeping
            EntityAction.START_HORSE_JUMP, EntityAction.STOP_HORSE_JUMP -> Unit // TODO: Horse jumping
            EntityAction.OPEN_INVENTORY -> Unit // TODO: Open vehicle inventory
            EntityAction.START_GLIDING -> if (!player.tryStartGliding()) player.stopGliding()
        }
    }

    fun handleSetHeldItem(packet: PacketInSetHeldItem) {
        if (packet.slot < 0 || packet.slot > 8) {
            LOGGER.warn("${player.profile.name} tried to change their held item slot to an invalid value!")
            return
        }
        player.inventory.heldSlot = packet.slot
    }

    fun handleKeepAlive(packet: PacketInKeepAlive) {
        if (pendingKeepAlive && packet.id == keepAliveChallenge) {
            connection.updateLatency(lastKeepAlive)
            pendingKeepAlive = false
            server.connectionManager.sendGroupedPacket(PacketOutPlayerInfoUpdate(PacketOutPlayerInfoUpdate.Action.UPDATE_LATENCY, player))
            return
        }
        disconnect(DisconnectMessages.TIMEOUT)
    }

    fun handleAbilities(packet: PacketInAbilities) {
        player.abilities.flying = packet.isFlying && player.abilities.canFly
    }

    // TODO: This entire thing needs to be rewritten
    fun handleUseItemOn(packet: PacketInUseItemOn) {
        if (!player.canBuild) return // If they can't place blocks, they are irrelevant :)

        val world = player.world
        val position = packet.hitResult.position
        val state = world.getBlock(position)
        val face = packet.hitResult.direction
        val isInside = packet.hitResult.isInside
        val event = server.eventNode.fire(KryptonPlayerPlaceBlockEvent(player, state, packet.hand, position, face, isInside))
        if (!event.isAllowed()) return

        val chunk = world.chunkManager.getChunk(ChunkPos.forEntityPosition(player.position)) ?: return
        val existingBlock = chunk.getBlock(position)
        if (!existingBlock.eq(KryptonBlocks.AIR)) return
        chunk.setBlock(position, KryptonRegistries.BLOCK.get(player.inventory.mainHand.type.key()).defaultState, false)
    }

    fun handlePlayerAction(packet: PacketInPlayerAction) {
        when (packet.action) {
            PlayerAction.START_DIGGING, PlayerAction.FINISH_DIGGING, PlayerAction.CANCEL_DIGGING -> player.gameModeSystem.handleBlockBreak(packet)
            PlayerAction.RELEASE_USE_ITEM -> {
                val handler = player.inventory.getItem(player.inventory.heldSlot).type.handler()
                if (handler is ItemTimedHandler) handler.finishUse(player, player.hand)
            }
            else -> Unit
        }
    }

    fun handleUseItem(packet: PacketInUseItem) {
        player.inventory.getHeldItem(packet.hand).type.handler().use(player, packet.hand)
    }

    fun handlePlayerInput(packet: PacketInPlayerInput) {
        // TODO: Handle steering here
        if (packet.isSneaking()) player.ejectVehicle()
    }

    fun handleInteract(packet: PacketInInteract) {
        val target = player.world.entityManager.getById(packet.entityId) ?: return
        if (player.position.distanceSquared(target.position) >= INTERACTION_RANGE_SQUARED) return

        when (packet.action) {
            is PacketInInteract.InteractAction -> onInteract(target, packet.action.hand)
            is PacketInInteract.AttackAction -> onAttack(target)
            is PacketInInteract.InteractAtAction -> onInteractAt(target, packet.action.hand, packet.action.x, packet.action.y, packet.action.z)
        }
    }

    private fun onInteract(target: KryptonEntity, hand: Hand) {
        val event = server.eventNode.fire(KryptonPlayerInteractWithEntityEvent(player, target, hand))
        if (!event.isAllowed()) return

        // TODO: Re-implement interactions and call a handler here
    }

    private fun onInteractAt(target: KryptonEntity, hand: Hand, x: Float, y: Float, z: Float) {
        val clickedPosition = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())
        val event = server.eventNode.fire(KryptonPlayerInteractAtEntityEvent(player, target, hand, clickedPosition))
        if (!event.isAllowed()) return

        // TODO: Re-implement interactions and call a handler here
    }

    private fun onAttack(target: KryptonEntity) {
        val event = server.eventNode.fire(KryptonPlayerAttackEntityEvent(player, target))
        if (!event.isAllowed()) return

        // TODO: Re-implement interactions and call a handler here
    }

    fun handlePlayerPosition(packet: PacketInSetPlayerPosition) {
        updatePosition(packet.x, packet.y, packet.z) { dx, dy, dz -> PacketOutUpdateEntityPosition(player.id, dx, dy, dz, packet.onGround) }
    }

    fun handlePlayerRotation(packet: PacketInSetPlayerRotation) {
        if (!updateRotation(packet.yaw, packet.pitch)) return
        server.connectionManager.sendGroupedPacket(PacketOutUpdateEntityRotation(player.id, packet.yaw, packet.pitch, packet.onGround)) {
            it !== player
        }
        server.connectionManager.sendGroupedPacket(PacketOutSetHeadRotation(player.id, packet.yaw)) { it !== player }
    }

    fun handlePlayerPositionAndRotation(packet: PacketInSetPlayerPositionAndRotation) {
        updatePosition(packet.x, packet.y, packet.z) { dx, dy, dz ->
            PacketOutUpdateEntityPositionAndRotation(player.id, dx, dy, dz, packet.yaw, packet.pitch, packet.onGround)
        }
        // It may seem weird at first that we update the rotation afterwards, considering we use the rotation in the update packet above.
        // However, we're always using the packet's values, which are the updated values, and we'll have to send that packet anyway, so
        // there's no point checking whether the rotation has changed or not beforehand.
        if (updateRotation(packet.yaw, packet.pitch)) {
            server.connectionManager.sendGroupedPacket(PacketOutSetHeadRotation(player.id, packet.yaw)) { it !== player }
        }
    }

    private fun updatePosition(x: Double, y: Double, z: Double, updatePacket: MovementPacketSupplier) {
        val oldPosition = player.position
        if (oldPosition.x == x && oldPosition.y == y && oldPosition.z == z) {
            // We haven't moved at all. We can avoid constructing the Vector3d object entirely,
            // avoid calling the move event, and just fast nope out.
            return
        }

        val newPosition = oldPosition.withCoordinates(x, y, z)
        val event = server.eventNode.fire(KryptonPlayerMoveEvent(player, oldPosition, newPosition))
        if (!event.isAllowed()) return

        player.position = event.result?.newPosition ?: newPosition

        val dx = Positioning.calculateDelta(x, oldPosition.x)
        val dy = Positioning.calculateDelta(y, oldPosition.y)
        val dz = Positioning.calculateDelta(z, oldPosition.z)
        player.viewingSystem.sendToViewers(updatePacket.get(dx, dy, dz))
        onMove(newPosition, oldPosition)
    }

    private fun updateRotation(yaw: Float, pitch: Float): Boolean {
        val oldPosition = player.position
        if (oldPosition.yaw == yaw && oldPosition.pitch == pitch) {
            // We haven't rotated at all. We can avoid calling the event and just fast nope out.
            return false
        }

        val newPosition = oldPosition.withRotation(yaw, pitch)
        val event = server.eventNode.fire(KryptonPlayerMoveEvent(player, oldPosition, newPosition))
        if (!event.isAllowed()) return false

        player.position = event.result?.newPosition ?: newPosition
        return true
    }

    fun handlePluginMessage(packet: PacketInPluginMessage) {
        server.eventNode.fire(KryptonPluginMessageReceivedEvent(player, packet.channel, packet.data))
    }

    fun handleCommandSuggestionsRequest(packet: PacketInCommandSuggestionsRequest) {
        val reader = StringReader(packet.command)
        if (reader.canRead() && reader.peek() == '/') reader.skip()
        val parseResults = server.commandManager.parse(player.createCommandSourceStack(), reader)
        server.commandManager.suggest(parseResults)
            .thenAcceptAsync({ connection.send(PacketOutCommandSuggestionsResponse(packet.id, it)) }, connection.executor())
    }

    fun handleClientCommand(packet: PacketInClientCommand) {
        when (packet.action) {
            PacketInClientCommand.Action.PERFORM_RESPAWN -> Unit // TODO
            PacketInClientCommand.Action.REQUEST_STATS -> player.statisticsTracker.sendUpdated()
        }
    }

    fun handleEntityTagQuery(packet: PacketInQueryEntityTag) {
        if (!player.hasPermission(KryptonPermission.ENTITY_QUERY.node)) return
        val entity = player.world.entityManager.getById(packet.entityId) ?: return
        connection.send(PacketOutTagQueryResponse(packet.transactionId, entity.saveWithPassengers().build()))
    }

    fun handleResourcePack(packet: PacketInResourcePack) {
        if (packet.status == ResourcePack.Status.DECLINED && server.config.server.resourcePack.forced) {
            disconnect(DisconnectMessages.REQUIRED_TEXTURE_PROMPT)
            return
        }
        server.eventNode.fire(KryptonPlayerResourcePackStatusEvent(player, packet.status))
    }

    private fun onMove(new: Position, old: Position) {
        player.chunkViewingSystem.updateChunks()
        player.updateMovementStatistics(new.x - old.x, new.y - old.y, new.z - old.z)
        player.hungerSystem.updateMovementExhaustion(new.x - old.x, new.y - old.y, new.z - old.z)
    }

    private fun interface MovementPacketSupplier {

        fun get(dx: Short, dy: Short, dz: Short): Packet
    }

    companion object {

        private const val INTERACTION_RANGE_SQUARED = 6.0 * 6.0
        private const val KEEP_ALIVE_INTERVAL = 15000L
        private val LOGGER = LogManager.getLogger()
    }
}
