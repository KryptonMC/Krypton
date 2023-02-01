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
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.translation.Translator
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.player.KryptonPlayerSettings
import org.kryptonmc.krypton.entity.player.KryptonSkinParts
import org.kryptonmc.krypton.event.command.KryptonCommandExecuteEvent
import org.kryptonmc.krypton.event.player.KryptonChatEvent
import org.kryptonmc.krypton.event.player.KryptonMoveEvent
import org.kryptonmc.krypton.event.player.KryptonPerformActionEvent
import org.kryptonmc.krypton.event.player.KryptonPlaceBlockEvent
import org.kryptonmc.krypton.event.player.KryptonPluginMessageEvent
import org.kryptonmc.krypton.event.player.KryptonResourcePackStatusEvent
import org.kryptonmc.krypton.event.player.KryptonRotateEvent
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.item.handler.ItemTimedHandler
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.network.PacketSendListener
import org.kryptonmc.krypton.network.chat.Chat
import org.kryptonmc.krypton.network.chat.ChatTypes
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientInformation
import org.kryptonmc.krypton.packet.`in`.play.PacketInCommandSuggestionsRequest
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerCommand
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
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChatMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutTagQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityRotation
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.coordinate.KryptonVec3d
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.locale.DisconnectMessages
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import org.kryptonmc.api.event.player.PerformActionEvent.Action as EntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction.Action as PlayerAction

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

    private var lastKeepAlive = 0L
    private var keepAliveChallenge = 0L
    private var pendingKeepAlive = false
    private val lastChatTimestamp = AtomicReference(Instant.EPOCH)

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
        val playerNameText = PlainTextComponentSerializer.plainText().serialize(player.name)
        LOGGER.info("$playerNameText was disconnected: ${PlainTextComponentSerializer.plainText().serialize(message)}")
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
        if (!Chat.isValidMessage(packet.command)) disconnect(DisconnectMessages.ILLEGAL_CHARACTERS)
        if (!verifyChatMessage(packet.command, packet.timestamp)) return
        server.eventManager.fire(KryptonCommandExecuteEvent(player, packet.command)).thenAcceptAsync({
            if (it.result.isAllowed) {
                server.commandManager.dispatch(player, it.result.command ?: packet.command)
            }
        }, connection.executor())
    }

    fun handleChatMessage(packet: PacketInChatMessage) {
        // Sanity check message content
        if (!Chat.isValidMessage(packet.message)) disconnect(DisconnectMessages.ILLEGAL_CHARACTERS)
        // Fire the chat event
        server.eventManager.fire(KryptonChatEvent(player, packet.message))
            .thenAcceptAsync({ event -> processChatEvent(event, packet) }, connection.executor())
    }

    private fun processChatEvent(event: KryptonChatEvent, packet: PacketInChatMessage) {
        if (!event.result.isAllowed) return
        if (!verifyChatMessage(packet.message, packet.signature.timestamp)) return

        val message = event.result.reason ?: createChatMessage(player, packet.message)
        if (!packet.signature.verify(player.publicKey, message, player.uuid)) {
            LOGGER.warn("Chat message ${packet.message} with invalid signature sent by ${player.name}! Ignoring...")
            return
        }
        val typeId = KryptonRegistries.CHAT_TYPE.getId(ChatTypes.CHAT)
        val outPacket = PacketOutPlayerChatMessage(Component.text(packet.message), message, typeId, player.asChatSender(), packet.signature)
        server.connectionManager.sendGroupedPacket(outPacket) { it !== player }
        // TODO: Review whether this is actually correct
        server.console.sendMessage(message, ChatType.CHAT.bind(player.name))
    }

    private fun verifyChatMessage(message: String, timestamp: Instant): Boolean {
        if (!updateChatOrder(timestamp)) {
            LOGGER.warn("Out of order chat message $message received from ${player.profile.name}. Disconnecting...")
            disconnect(DisconnectMessages.OUT_OF_ORDER_CHAT)
            return false
        }
        if (isChatExpired(timestamp)) {
            LOGGER.warn("Expired chat message $message received from ${player.profile.name}. Are we out of sync with the client?")
        }
        return true
    }

    private fun updateChatOrder(timestamp: Instant): Boolean {
        do {
            val current = lastChatTimestamp.get()
            if (timestamp.isBefore(current)) return false
        } while (!lastChatTimestamp.compareAndSet(current, timestamp))
        return true
    }

    private fun isChatExpired(timestamp: Instant): Boolean = Instant.now().isAfter(timestamp.plus(CHAT_EXPIRE_DURATION))

    private fun resetLastActionTime(): Boolean {
        // TODO: Fix chat (again)
        if (player.settings.chatVisibility == ChatVisibility.HIDDEN) {
//            val systemTypeId = InternalRegistries.CHAT_TYPE.idOf(ChatTypes.SYSTEM)
//            session.send(PacketOutSystemChatMessage(Component.translatable("chat.disabled.options", NamedTextColor.RED), systemTypeId))
            return false
        }
        player.resetLastActionTime()
        return true
    }

    fun handleClientInformation(packet: PacketInClientInformation) {
        player.settings = KryptonPlayerSettings(
            Translator.parseLocale(packet.locale),
            packet.viewDistance,
            packet.chatVisibility,
            packet.chatColors,
            KryptonSkinParts(packet.skinSettings.toInt()),
            packet.mainHand,
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
        server.eventManager.fire(KryptonPerformActionEvent(player, packet.action)).thenAcceptAsync({
            if (!it.result.isAllowed) return@thenAcceptAsync
            when (it.action) {
                EntityAction.START_SNEAKING -> player.isSneaking = true
                EntityAction.STOP_SNEAKING -> player.isSneaking = false
                EntityAction.START_SPRINTING -> player.isSprinting = true
                EntityAction.STOP_SPRINTING -> player.isSprinting = false
                EntityAction.LEAVE_BED -> Unit // TODO: Sleeping
                EntityAction.START_JUMP_WITH_HORSE, EntityAction.STOP_JUMP_WITH_HORSE, EntityAction.OPEN_HORSE_INVENTORY -> Unit // TODO: Horses
                EntityAction.START_FLYING_WITH_ELYTRA -> if (!player.tryStartGliding()) player.stopGliding()
                else -> error("This should be impossible! Action for player command was not a valid action! Action: ${it.action}")
            }
        }, connection.executor())
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
            server.connectionManager.sendGroupedPacket(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.UPDATE_LATENCY, player))
            return
        }
        disconnect(DisconnectMessages.TIMEOUT)
    }

    fun handleAbilities(packet: PacketInAbilities) {
        player.isGliding = packet.isFlying && player.abilities.canFly
    }

    // TODO: This entire thing needs to be rewritten
    fun handleUseItemOn(packet: PacketInUseItemOn) {
        if (!player.canBuild) return // If they can't place blocks, they are irrelevant :)

        val world = player.world
        val position = packet.hitResult.position
        val state = world.getBlock(position)
        val face = packet.hitResult.direction
        val isInside = packet.hitResult.isInside
        val event = server.eventManager.fireSync(KryptonPlaceBlockEvent(player, state, packet.hand, position, face, isInside))
        if (!event.result.isAllowed) return

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
        val target = player.world.entityManager.getById(packet.entityId)
        player.isSneaking = packet.sneaking
        if (target == null) return
        if (player.position.distanceSquared(target.position) >= INTERACTION_RANGE_SQUARED) return
        packet.action.handle(InteractionHandler())
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

        val newPosition = KryptonVec3d(x, y, z)
        player.position = newPosition
        server.eventManager.fireAndForget(KryptonMoveEvent(player, oldPosition, newPosition))

        val dx = Positioning.calculateDelta(x, oldPosition.x)
        val dy = Positioning.calculateDelta(y, oldPosition.y)
        val dz = Positioning.calculateDelta(z, oldPosition.z)
        player.viewingSystem.sendToViewers(updatePacket.get(dx, dy, dz))
        onMove(newPosition, oldPosition)
    }

    private fun updateRotation(yaw: Float, pitch: Float): Boolean {
        val oldYaw = player.yaw
        val oldPitch = player.pitch
        if (oldYaw == yaw && oldPitch == pitch) {
            // We haven't rotated at all. We can avoid calling the event and just fast nope out.
            return false
        }

        player.yaw = yaw
        player.pitch = pitch
        server.eventManager.fireAndForget(KryptonRotateEvent(player, oldYaw, oldPitch, yaw, pitch))
        return true
    }

    fun handlePluginMessage(packet: PacketInPluginMessage) {
        server.eventManager.fireAndForget(KryptonPluginMessageEvent(player, packet.channel, packet.data))
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
        server.eventManager.fireAndForget(KryptonResourcePackStatusEvent(player, packet.status))
    }

    private fun onMove(new: Vec3d, old: Vec3d) {
        player.chunkViewingSystem.updateChunks()
        player.updateMovementStatistics(new.x - old.x, new.y - old.y, new.z - old.z)
        player.hungerSystem.updateMovementExhaustion(new.x - old.x, new.y - old.y, new.z - old.z)
    }

    private inner class InteractionHandler : PacketInInteract.Handler {

        override fun onInteract(hand: Hand) {
            // TODO: Re-implement interactions and call a handler here
        }

        override fun onInteractAt(hand: Hand, x: Float, y: Float, z: Float) {
            // TODO: Re-implement interactions and call a handler here
        }

        override fun onAttack() {
            // TODO: Re-implement interactions and call a handler here
        }
    }

    private fun interface MovementPacketSupplier {

        fun get(dx: Short, dy: Short, dz: Short): Packet
    }

    companion object {

        private const val INTERACTION_RANGE_SQUARED = 6.0 * 6.0
        private const val KEEP_ALIVE_INTERVAL = 15000L
        private val LOGGER = LogManager.getLogger()
        private val CHAT_EXPIRE_DURATION = Duration.ofMinutes(5)

        @JvmStatic
        private fun createChatMessage(player: KryptonPlayer, message: String): Component {
            val name = player.profile.name
            val displayName = player.displayName.style { it.insertion(name).clickEvent(ClickEvent.suggestCommand("/msg $name")).hoverEvent(player) }
            return Component.translatable("chat.type.text", displayName, Component.text(message))
        }
    }
}
