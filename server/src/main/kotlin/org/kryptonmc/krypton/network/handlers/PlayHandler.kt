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
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.text.Component
import net.kyori.adventure.translation.Translator
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
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.network.SessionHandler
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
import org.kryptonmc.krypton.packet.out.play.EntityAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutCommandSuggestionsResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChatMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutTagQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityRotation
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.SectionPos
import org.kryptonmc.krypton.util.Vec3dImpl
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.chunk.ChunkPos
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
class PlayHandler(override val server: KryptonServer, override val session: SessionHandler, private val player: KryptonPlayer) : PacketHandler {

    private val playerManager = server.playerManager
    private val sessionManager = server.sessionManager
    private var lastKeepAlive = 0L
    private var keepAliveChallenge = 0L
    private var pendingKeepAlive = false
    private val lastChatTimestamp = AtomicReference(Instant.EPOCH)

    fun tick() {
        val time = System.currentTimeMillis()
        if (time - lastKeepAlive < KEEP_ALIVE_INTERVAL) return
        if (pendingKeepAlive) {
            session.disconnect(Messages.Disconnect.TIMEOUT.build())
            return
        }
        pendingKeepAlive = true
        lastKeepAlive = time
        keepAliveChallenge = time
        session.send(PacketOutKeepAlive(keepAliveChallenge))
    }

    override fun handle(packet: Packet) {
        when (packet) {
            is PacketInSwingArm -> handleSwingArm(packet)
            is PacketInChatCommand -> handleChatCommand(packet)
            is PacketInChatMessage -> handleChatMessage(packet)
            is PacketInClientInformation -> handleClientInformation(packet)
            is PacketInSetCreativeModeSlot -> handleSetCreativeModeSlot(packet)
            is PacketInPlayerCommand -> handlePlayerCommand(packet)
            is PacketInSetHeldItem -> handleSetHeldItem(packet)
            is PacketInKeepAlive -> handleKeepAlive(packet)
            is PacketInAbilities -> handleAbilities(packet)
            is PacketInUseItemOn -> handleUseItemOn(packet)
            is PacketInPlayerAction -> handlePlayerAction(packet)
            is PacketInSetPlayerPosition -> handlePositionUpdate(packet)
            is PacketInSetPlayerRotation -> handleRotationUpdate(packet)
            is PacketInSetPlayerPositionAndRotation -> handlePositionAndRotationUpdate(packet)
            is PacketInPluginMessage -> handlePluginMessage(packet)
            is PacketInCommandSuggestionsRequest -> handleCommandSuggestionsRequest(packet)
            is PacketInClientCommand -> handleClientCommand(packet)
            is PacketInQueryEntityTag -> handleEntityTagQuery(packet)
            is PacketInInteract -> handleInteract(packet)
            is PacketInPlayerInput -> handlePlayerInput(packet)
            is PacketInUseItem -> handleUseItem(packet)
            is PacketInResourcePack -> handleResourcePack(packet)
            else -> Unit
        }
    }

    override fun onDisconnect() {
        sessionManager.invalidateStatus()
        playerManager.remove(player)
    }

    private fun handleSwingArm(packet: PacketInSwingArm) {
        val animation = when (packet.hand) {
            Hand.MAIN -> EntityAnimation.SWING_MAIN_ARM
            Hand.OFF -> EntityAnimation.SWING_OFFHAND
        }
        sessionManager.sendGrouped(PacketOutAnimation(player.id, animation)) { it !== player }
    }

    private fun handleChatCommand(packet: PacketInChatCommand) {
        if (!Chat.isValidMessage(packet.command)) session.disconnect(ILLEGAL_CHAT_CHARACTERS_MESSAGE)
        if (!verifyChatMessage(packet.command, packet.timestamp)) return
        server.eventManager.fire(KryptonCommandExecuteEvent(player, packet.command)).thenAcceptAsync({
            if (!it.result.isAllowed) return@thenAcceptAsync
            server.commandManager.dispatch(player, packet.command)
        }, session.channel.eventLoop())
    }

    private fun handleChatMessage(packet: PacketInChatMessage) {
        // Sanity check message content
        if (!Chat.isValidMessage(packet.message)) session.disconnect(ILLEGAL_CHAT_CHARACTERS_MESSAGE)
        // Fire the chat event
        server.eventManager.fire(KryptonChatEvent(player, packet.message)).thenAcceptAsync({ event ->
            if (!event.result.isAllowed) return@thenAcceptAsync
            if (!verifyChatMessage(packet.message, packet.signature.timestamp)) return@thenAcceptAsync

            val message = event.result.reason ?: Messages.CHAT_TYPE_TEXT.build(player, packet.message)
            if (!packet.signature.verify(player.publicKey, message, player.uuid)) {
                LOGGER.warn("Chat message ${packet.message} with invalid signature sent by ${player.name}. Ignoring...")
                return@thenAcceptAsync
            }
            val typeId = KryptonRegistries.CHAT_TYPE.getId(ChatTypes.CHAT)
            val outPacket = PacketOutPlayerChatMessage(Component.text(packet.message), message, typeId, player.asChatSender(), packet.signature)
            server.sessionManager.sendGrouped(outPacket) { it !== player }
            server.console.sendMessage(player, message, MessageType.CHAT)
        }, session.channel.eventLoop())
    }

    private fun verifyChatMessage(message: String, timestamp: Instant): Boolean {
        if (!updateChatOrder(timestamp)) {
            LOGGER.warn("Out of order chat message $message received from ${player.profile.name}. Disconnecting...")
            session.disconnect(Messages.Disconnect.OUT_OF_ORDER_CHAT.build())
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

    private fun handleClientInformation(packet: PacketInClientInformation) {
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

    private fun handleSetCreativeModeSlot(packet: PacketInSetCreativeModeSlot) {
        if (player.gameMode != GameMode.CREATIVE) return
        val item = packet.clickedItem
        val inValidRange = packet.slot >= 1 && packet.slot < KryptonPlayerInventory.SIZE
        val isValid = item.isEmpty() || item.meta.damage >= 0 && item.amount <= 64 && !item.isEmpty()
        if (inValidRange && isValid) player.inventory.set(packet.slot, packet.clickedItem)
    }

    private fun handlePlayerCommand(packet: PacketInPlayerCommand) {
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
        }, session.channel.eventLoop())
    }

    private fun handleSetHeldItem(packet: PacketInSetHeldItem) {
        if (packet.slot !in 0..8) {
            LOGGER.warn("${player.profile.name} tried to change their held item slot to an invalid value!")
            return
        }
        player.inventory.heldSlot = packet.slot
    }

    private fun handleKeepAlive(packet: PacketInKeepAlive) {
        if (pendingKeepAlive && packet.id == keepAliveChallenge) {
            session.latency = (session.latency * 3 + (System.currentTimeMillis() - lastKeepAlive).toInt()) / 3
            pendingKeepAlive = false
            sessionManager.sendGrouped(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.UPDATE_LATENCY, player))
            return
        }
        session.disconnect(Messages.Disconnect.TIMEOUT.build())
    }

    private fun handleAbilities(packet: PacketInAbilities) {
        player.isGliding = packet.isFlying && player.abilities.canFly
    }

    // TODO: This entire thing needs to be rewritten
    private fun handleUseItemOn(packet: PacketInUseItemOn) {
        if (!player.canBuild) return // If they can't place blocks, they are irrelevant :)

        val world = player.world
        val position = packet.hitResult.position
        val state = world.getBlock(position)
        val face = packet.hitResult.direction
        val isInside = packet.hitResult.isInside
        val event = server.eventManager.fireSync(KryptonPlaceBlockEvent(player, state, packet.hand, position, face, isInside))
        if (!event.result.isAllowed) return

        val chunkX = SectionPos.blockToSection(player.position.x)
        val chunkZ = SectionPos.blockToSection(player.position.z)
        val chunk = world.chunkManager.get(ChunkPos.pack(chunkX, chunkZ)) ?: return
        val existingBlock = chunk.getBlock(position)
        if (!existingBlock.eq(KryptonBlocks.AIR)) return
        chunk.setBlock(position, KryptonRegistries.BLOCK.get(player.inventory.mainHand.type.key()).defaultState, false)
    }

    private fun handlePlayerAction(packet: PacketInPlayerAction) {
        when (packet.action) {
            PlayerAction.START_DIGGING, PlayerAction.FINISH_DIGGING, PlayerAction.CANCEL_DIGGING -> player.gameModeSystem.handleBlockBreak(packet)
            PlayerAction.RELEASE_USE_ITEM -> {
                val handler = player.inventory.get(player.inventory.heldSlot).type.handler()
                if (handler !is ItemTimedHandler) return
                handler.finishUse(player, player.hand)
            }
            else -> Unit
        }
    }

    private fun handleUseItem(packet: PacketInUseItem) {
        player.inventory.heldItem(packet.hand).type.handler().use(player, packet.hand)
    }

    private fun handlePlayerInput(packet: PacketInPlayerInput) {
        // TODO: Handle steering here
        if (packet.isSneaking) player.ejectVehicle()
    }

    private fun handleInteract(packet: PacketInInteract) {
        val target = player.world.entityManager.get(packet.entityId)
        player.isSneaking = packet.sneaking
        if (target == null) return
        if (player.position.distanceSquared(target.position) >= INTERACTION_RANGE_SQUARED) return
        packet.action.handle(InteractionHandler())
    }

    private fun handlePositionUpdate(packet: PacketInSetPlayerPosition) {
        val oldLocation = player.position
        if (oldLocation.x == packet.x && oldLocation.y == packet.y && oldLocation.z == packet.z) {
            // We haven't moved at all. We can avoid constructing the Vector3d object entirely, and just
            // fast nope out.
            return
        }
        val newLocation = Vec3dImpl(packet.x, packet.y, packet.z)

        player.position = newLocation
        server.eventManager.fireAndForget(KryptonMoveEvent(player, oldLocation, newLocation))

        val deltaX = Positioning.delta(newLocation.x, oldLocation.x)
        val deltaY = Positioning.delta(newLocation.y, oldLocation.y)
        val deltaZ = Positioning.delta(newLocation.z, oldLocation.z)
        val newPacket = PacketOutUpdateEntityPosition(player.id, deltaX, deltaY, deltaZ, packet.onGround)
        player.viewingSystem.sendToViewers(newPacket)
        onMove(newLocation, oldLocation)
    }

    private fun handleRotationUpdate(packet: PacketInSetPlayerRotation) {
        val oldYaw = player.yaw
        val oldPitch = player.pitch
        val newYaw = packet.yaw
        val newPitch = packet.pitch

        player.yaw = newYaw
        player.pitch = newPitch
        server.eventManager.fireAndForget(KryptonRotateEvent(player, oldYaw, oldPitch, newYaw, newPitch))

        sessionManager.sendGrouped(PacketOutUpdateEntityRotation(player.id, newYaw, newPitch, packet.onGround)) { it !== player }
        sessionManager.sendGrouped(PacketOutSetHeadRotation(player.id, newYaw)) { it !== player }
    }

    private fun handlePositionAndRotationUpdate(packet: PacketInSetPlayerPositionAndRotation) {
        val oldLocation = player.position
        if (oldLocation.x == packet.x && oldLocation.y == packet.y && oldLocation.z == packet.z) {
            // We haven't moved at all. We can avoid constructing the Vector3d object entirely, and just
            // fast nope out.
            return
        }
        val oldYaw = player.yaw
        val oldPitch = player.pitch

        val newLocation = Vec3dImpl(packet.x, packet.y, packet.z)
        val newYaw = packet.yaw
        val newPitch = packet.pitch

        player.position = newLocation
        player.yaw = packet.yaw
        player.pitch = packet.pitch
        server.eventManager.fireAndForget(KryptonMoveEvent(player, oldLocation, newLocation))
        server.eventManager.fireAndForget(KryptonRotateEvent(player, oldYaw, oldPitch, newYaw, newPitch))

        // TODO: Look in to optimising this (rotation and head look updating) as much as we possibly can
        val deltaX = Positioning.delta(newLocation.x, oldLocation.x)
        val deltaY = Positioning.delta(newLocation.y, oldLocation.y)
        val deltaZ = Positioning.delta(newLocation.z, oldLocation.z)
        val positionPacket = PacketOutUpdateEntityPositionAndRotation(player.id, deltaX, deltaY, deltaZ, newYaw, newPitch, packet.onGround)
        sessionManager.sendGrouped(positionPacket) { it !== player }
        sessionManager.sendGrouped(PacketOutSetHeadRotation(player.id, newYaw)) { it !== player }
        onMove(newLocation, oldLocation)
    }

    private fun handlePluginMessage(packet: PacketInPluginMessage) {
        server.eventManager.fireAndForget(KryptonPluginMessageEvent(player, packet.channel, packet.data))
    }

    private fun handleCommandSuggestionsRequest(packet: PacketInCommandSuggestionsRequest) {
        val reader = StringReader(packet.command)
        if (reader.canRead() && reader.peek() == '/') reader.skip()
        val parseResults = server.commandManager.parse(player.createCommandSourceStack(), reader)
        server.commandManager.suggest(parseResults).thenAccept { session.send(PacketOutCommandSuggestionsResponse(packet.id, it)) }
    }

    private fun handleClientCommand(packet: PacketInClientCommand) {
        when (packet.action) {
            PacketInClientCommand.Action.PERFORM_RESPAWN -> Unit // TODO
            PacketInClientCommand.Action.REQUEST_STATS -> player.statistics.send()
        }
    }

    private fun handleEntityTagQuery(packet: PacketInQueryEntityTag) {
        if (!player.hasPermission(KryptonPermission.ENTITY_QUERY.node)) return
        val entity = player.world.entityManager.get(packet.entityId) ?: return
        session.send(PacketOutTagQueryResponse(packet.transactionId, entity.saveWithPassengers().build()))
    }

    private fun handleResourcePack(packet: PacketInResourcePack) {
        if (packet.status == ResourcePack.Status.DECLINED && server.config.server.resourcePack.forced) {
            session.disconnect(Messages.Disconnect.REQUIRED_TEXTURE_PROMPT.build())
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

    companion object {

        private const val INTERACTION_RANGE_SQUARED = 6.0 * 6.0
        private const val KEEP_ALIVE_INTERVAL = 15000L
        private val LOGGER = logger<PlayHandler>()

        private val CHAT_EXPIRE_DURATION = Duration.ofMinutes(5)
        private val ILLEGAL_CHAT_CHARACTERS_MESSAGE = Messages.Disconnect.ILLEGAL_CHARACTERS.build()
    }
}
