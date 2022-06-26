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
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.event.command.CommandExecuteEvent
import org.kryptonmc.api.event.player.AttackEntityEvent
import org.kryptonmc.api.event.player.ChatEvent
import org.kryptonmc.api.event.player.InteractAtEntityEvent
import org.kryptonmc.api.event.player.InteractEntityEvent
import org.kryptonmc.api.event.player.MoveEvent
import org.kryptonmc.api.event.player.PerformActionEvent
import org.kryptonmc.api.event.player.PerformActionEvent.Action as EntityAction
import org.kryptonmc.api.event.player.PlaceBlockEvent
import org.kryptonmc.api.event.player.PluginMessageEvent
import org.kryptonmc.api.event.player.ResourcePackStatusEvent
import org.kryptonmc.api.event.player.RotateEvent
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.item.handler.ItemTimedHandler
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInAnimation
import org.kryptonmc.krypton.packet.`in`.play.PacketInChangeHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInCreativeInventoryAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityNBTQuery
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract.Type as InteractType
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlaceBlock
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging.Status as DiggingStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerUseItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInResourcePackStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInSteerVehicle
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.out.play.EntityAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutNBTQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutTabComplete
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import java.util.Locale

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

    fun tick() {
        val time = System.currentTimeMillis()
        if (time - lastKeepAlive < KEEP_ALIVE_INTERVAL) return
        if (pendingKeepAlive) {
            session.disconnect(Component.translatable("disconnect.timeout"))
            return
        }
        pendingKeepAlive = true
        lastKeepAlive = time
        keepAliveChallenge = time
        session.send(PacketOutKeepAlive(keepAliveChallenge))
    }

    override fun handle(packet: Packet) {
        when (packet) {
            is PacketInAnimation -> handleAnimation(packet)
            is PacketInChat -> handleChat(packet)
            is PacketInClientSettings -> handleClientSettings(packet)
            is PacketInCreativeInventoryAction -> handleCreativeInventoryAction(packet)
            is PacketInEntityAction -> handleEntityAction(packet)
            is PacketInChangeHeldItem -> handleHeldItemChange(packet)
            is PacketInKeepAlive -> handleKeepAlive(packet)
            is PacketInAbilities -> handleAbilities(packet)
            is PacketInPlaceBlock -> handleBlockPlacement(packet)
            is PacketInPlayerDigging -> handlePlayerDigging(packet)
            is PacketInPlayerPosition -> handlePositionUpdate(packet)
            is PacketInPlayerRotation -> handleRotationUpdate(packet)
            is PacketInPlayerPositionAndRotation -> handlePositionAndRotationUpdate(packet)
            is PacketInPluginMessage -> handlePluginMessage(packet)
            is PacketInTabComplete -> handleTabComplete(packet)
            is PacketInClientStatus -> handleClientStatus(packet)
            is PacketInEntityNBTQuery -> handleEntityNBTQuery(packet)
            is PacketInInteract -> handleInteract(packet)
            is PacketInSteerVehicle -> handleSteerVehicle(packet)
            is PacketInPlayerUseItem -> handlePlayerUseItem(packet)
            is PacketInResourcePackStatus -> handleResourcePackStatus(packet)
            else -> Unit
        }
    }

    override fun onDisconnect() {
        sessionManager.invalidateStatus()
        playerManager.remove(player)
    }

    private fun handleAnimation(packet: PacketInAnimation) {
        val animation = when (packet.hand) {
            Hand.MAIN -> EntityAnimation.SWING_MAIN_ARM
            Hand.OFF -> EntityAnimation.SWING_OFFHAND
        }
        sessionManager.sendGrouped(PacketOutAnimation(player.id, animation)) { it !== player }
    }

    private fun handleChat(packet: PacketInChat) {
        if (packet.message.startsWith("/")) { // This is a command, process it as one.
            val command = packet.message.substring(1)
            server.eventManager.fire(CommandExecuteEvent(player, command)).thenAcceptAsync({
                if (!it.result.isAllowed) return@thenAcceptAsync
                server.commandManager.dispatch(player, command)
            }, player.session.channel.eventLoop())
            return
        }

        // Fire the chat event
        server.eventManager.fire(ChatEvent(player, packet.message)).thenAcceptAsync({
            if (!it.result.isAllowed) return@thenAcceptAsync
            if (it.result.reason != null) {
                server.sendMessage(player, it.result.reason!!, MessageType.CHAT)
                return@thenAcceptAsync
            }

            val name = player.profile.name
            val displayName = player.displayName.insertion(name).clickEvent(ClickEvent.suggestCommand("/msg $name")).hoverEvent(player)
            server.sendMessage(player, Component.translatable("chat.type.text", displayName, Component.text(packet.message)), MessageType.CHAT)
        }, session.channel.eventLoop())
    }

    private fun handleClientSettings(packet: PacketInClientSettings) {
        player.locale = Locale.forLanguageTag(packet.locale)
        player.chatVisibility = packet.chatVisibility
        player.skinSettings = packet.skinSettings.toByte()
        player.mainHand = packet.mainHand
        player.filterText = packet.filterText
        player.allowsListing = packet.allowsListing
    }

    private fun handleCreativeInventoryAction(packet: PacketInCreativeInventoryAction) {
        if (player.gameMode != GameMode.CREATIVE) return
        val item = packet.clickedItem
        val inValidRange = packet.slot >= 1 && packet.slot < KryptonPlayerInventory.SIZE
        val isValid = item.isEmpty() || item.meta.damage >= 0 && item.amount <= 64 && !item.isEmpty()
        if (inValidRange && isValid) player.inventory[packet.slot] = packet.clickedItem
    }

    private fun handleEntityAction(packet: PacketInEntityAction) {
        server.eventManager.fire(PerformActionEvent(player, packet.action)).thenAcceptAsync({
            if (!it.result.isAllowed) return@thenAcceptAsync
            when (it.action) {
                EntityAction.START_SNEAKING -> player.isSneaking = true
                EntityAction.STOP_SNEAKING -> player.isSneaking = false
                EntityAction.START_SPRINTING -> player.isSprinting = true
                EntityAction.STOP_SPRINTING -> player.isSprinting = false
                EntityAction.LEAVE_BED -> Unit // TODO: Sleeping
                EntityAction.START_JUMP_WITH_HORSE, EntityAction.STOP_JUMP_WITH_HORSE, EntityAction.OPEN_HORSE_INVENTORY -> Unit // TODO: Horses
                EntityAction.START_FLYING_WITH_ELYTRA -> player.isGliding = true
                EntityAction.STOP_FLYING_WITH_ELYTRA -> player.isGliding = false
            }
        }, session.channel.eventLoop())
    }

    private fun handleHeldItemChange(packet: PacketInChangeHeldItem) {
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
        session.disconnect(Component.translatable("disconnect.timeout"))
    }

    private fun handleAbilities(packet: PacketInAbilities) {
        player.isGliding = packet.isFlying && player.canFly
    }

    private fun handleBlockPlacement(packet: PacketInPlaceBlock) {
        if (!player.canBuild) return // If they can't place blocks, they are irrelevant :)

        val world = player.world
        val x = packet.x
        val y = packet.y
        val z = packet.z
        val event = server.eventManager.fireSync(PlaceBlockEvent(player, world.getBlock(x, y, z), packet.hand, x, y, z, packet.face, packet.isInside))
        if (!event.result.isAllowed) return

        val chunkX = player.location.floorX() shr 4
        val chunkZ = player.location.floorZ() shr 4
        val chunk = world.chunkManager[ChunkPosition.toLong(chunkX, chunkZ)] ?: return
        val existingBlock = chunk.getBlock(x, y, z)
        if (existingBlock != Blocks.AIR) return

        val item = player.inventory.mainHand
        val block = BlockLoader.fromKey(item.type.key()) ?: return
        chunk.setBlock(x, y, z, block)
    }

    private fun handlePlayerDigging(packet: PacketInPlayerDigging) {
        when (packet.status) {
            DiggingStatus.STARTED, DiggingStatus.FINISHED, DiggingStatus.CANCELLED -> player.blockHandler.handleBlockBreak(packet)
            DiggingStatus.RELEASE_USE_ITEM -> {
                val handler = player.inventory[player.inventory.heldSlot].type.handler()
                if (handler !is ItemTimedHandler) return
                handler.finishUse(player, player.hand)
            }
            else -> Unit
        }
    }

    private fun handlePlayerUseItem(packet: PacketInPlayerUseItem) {
        player.inventory.heldItem(packet.hand).type.handler().use(player, packet.hand)
    }

    private fun handleSteerVehicle(packet: PacketInSteerVehicle) {
        // TODO: Handle steering here
        if (packet.isSneaking) player.ejectVehicle()
    }

    private fun handleInteract(packet: PacketInInteract) {
        val target = player.world.entityManager[packet.entityId] ?: return
        val hand = packet.hand
        val x = target.location.x() + packet.x
        val y = target.location.y() + packet.y
        val z = target.location.z() + packet.z
        player.isSneaking = packet.sneaking
        if (player.location.distanceSquared(target.location) >= 36.0) return
        when (packet.type) {
            InteractType.INTERACT -> {
                server.eventManager.fire(InteractEntityEvent(player, target, hand!!)).thenAcceptAsync({
                    if (!it.result.isAllowed) return@thenAcceptAsync
                    target.tryRide(player)
                    player.interactOn(target, hand)
                }, session.channel.eventLoop())
            }
            InteractType.ATTACK -> server.eventManager.fireAndForget(AttackEntityEvent(player, target)) // TODO
            InteractType.INTERACT_AT -> server.eventManager.fireAndForget(InteractAtEntityEvent(player, target, hand!!, x, y, z)) // TODO
        }
    }

    private fun handlePositionUpdate(packet: PacketInPlayerPosition) {
        val oldLocation = player.location
        if (oldLocation.x() == packet.x && oldLocation.y() == packet.y && oldLocation.z() == packet.z) {
            // We haven't moved at all. We can avoid constructing the Vector3d object entirely, and just
            // fast nope out.
            return
        }
        val newLocation = Vector3d(packet.x, packet.y, packet.z)

        player.location = newLocation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation))

        val newPacket = PacketOutEntityPosition(
            player.id,
            Positioning.delta(newLocation.x(), oldLocation.x()),
            Positioning.delta(newLocation.y(), oldLocation.y()),
            Positioning.delta(newLocation.z(), oldLocation.z()),
            packet.onGround
        )
        sessionManager.sendGrouped(player.viewers, newPacket)
        onMove(newLocation, oldLocation)
    }

    private fun handleRotationUpdate(packet: PacketInPlayerRotation) {
        val oldRotation = player.rotation
        val newRotation = Vector2f(packet.yaw, packet.pitch)

        player.rotation = newRotation
        server.eventManager.fireAndForget(RotateEvent(player, oldRotation, newRotation))

        sessionManager.sendGrouped(PacketOutEntityRotation(player.id, packet.yaw, packet.pitch, packet.onGround)) { it !== player }
        sessionManager.sendGrouped(PacketOutHeadLook(player.id, packet.yaw)) { it !== player }
    }

    private fun handlePositionAndRotationUpdate(packet: PacketInPlayerPositionAndRotation) {
        val oldLocation = player.location
        val oldRotation = player.rotation
        val newLocation = Vector3d(packet.x, packet.y, packet.z)
        val newRotation = Vector2f(packet.yaw, packet.pitch)
        if (newLocation == oldLocation) return

        player.location = newLocation
        player.rotation = newRotation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation))
        server.eventManager.fireAndForget(RotateEvent(player, oldRotation, newRotation))

        // TODO: Look in to optimising this (rotation and head look updating) as much as we possibly can
        val deltaX = Positioning.delta(newLocation.x(), oldLocation.x())
        val deltaY = Positioning.delta(newLocation.y(), oldLocation.y())
        val deltaZ = Positioning.delta(newLocation.z(), oldLocation.z())
        val newYaw = newRotation.x()
        val newPitch = newRotation.y()
        val positionPacket = PacketOutEntityPositionAndRotation(player.id, deltaX, deltaY, deltaZ, newYaw, newPitch, packet.onGround)
        sessionManager.sendGrouped(positionPacket) { it !== player }
        sessionManager.sendGrouped(PacketOutHeadLook(player.id, newRotation.x())) { it !== player }
        onMove(newLocation, oldLocation)
    }

    private fun handlePluginMessage(packet: PacketInPluginMessage) {
        val channel = try {
            Key.key(packet.channel)
        } catch (exception: InvalidKeyException) {
            LOGGER.warn("Invalid plugin message channel ${packet.channel}.")
            return
        }
        server.eventManager.fireAndForget(PluginMessageEvent(player, channel, packet.data))
    }

    private fun handleTabComplete(packet: PacketInTabComplete) {
        val reader = StringReader(packet.command)
        if (reader.canRead() && reader.peek() == '/') reader.skip()
        server.commandManager.suggest(player, reader)
            .thenAcceptAsync({ session.send(PacketOutTabComplete(packet.id, it)) }, session.channel.eventLoop())
    }

    private fun handleClientStatus(packet: PacketInClientStatus) {
        when (packet.action) {
            PacketInClientStatus.Action.PERFORM_RESPAWN -> Unit // TODO
            PacketInClientStatus.Action.REQUEST_STATS -> player.statistics.send()
        }
    }

    private fun handleEntityNBTQuery(packet: PacketInEntityNBTQuery) {
        if (!player.hasPermission(KryptonPermission.ENTITY_QUERY.node)) return
        val entity = player.world.entityManager[packet.entityId] ?: return
        session.send(PacketOutNBTQueryResponse(packet.transactionId, EntityFactory.serializer(entity.type).saveWithPassengers(entity).build()))
    }

    private fun handleResourcePackStatus(packet: PacketInResourcePackStatus) {
        if (packet.status == ResourcePack.Status.DECLINED && server.config.server.resourcePack.forced) {
            session.disconnect(Component.translatable("multiplayer.requiredTexturePrompt.disconnect"))
            return
        }
        server.eventManager.fireAndForget(ResourcePackStatusEvent(player, packet.status))
    }

    private fun onMove(new: Vector3d, old: Vector3d) {
        player.updateChunks()
        player.updateMovementStatistics(new.x() - old.x(), new.y() - old.y(), new.z() - old.z())
        player.updateMovementExhaustion(new.x() - old.x(), new.y() - old.y(), new.z() - old.z())
    }

    companion object {

        private const val KEEP_ALIVE_INTERVAL = 15000L
        private val LOGGER = logger<PlayHandler>()
    }
}
