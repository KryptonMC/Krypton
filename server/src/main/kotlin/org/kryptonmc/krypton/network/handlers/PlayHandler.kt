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
package org.kryptonmc.krypton.network.handlers

import com.mojang.brigadier.StringReader
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.event.ClickEvent.suggestCommand
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.event.command.CommandExecuteEvent
import org.kryptonmc.api.event.player.ChatEvent
import org.kryptonmc.api.event.player.MoveEvent
import org.kryptonmc.api.event.player.PerformActionEvent
import org.kryptonmc.api.event.player.PluginMessageEvent
import org.kryptonmc.api.event.player.ResourcePackStatusEvent
import org.kryptonmc.api.item.meta.MetaKeys
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.handler.KryptonItemTimedHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInAnimation
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInCreativeInventoryAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInChangeHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlaceBlock
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerUseItem
import org.kryptonmc.krypton.packet.out.play.EntityAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutDiggingResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutTabComplete
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityNBTQuery
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract
import org.kryptonmc.krypton.packet.`in`.play.PacketInResourcePackStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInSteerVehicle
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutNBTQueryResponse
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
class PlayHandler(
    override val server: KryptonServer,
    override val session: SessionHandler,
    private val player: KryptonPlayer
) : PacketHandler {

    private val playerManager = server.playerManager
    private var lastKeepAlive = 0L
    private var keepAliveChallenge = 0L
    private var pendingKeepAlive = false

    fun tick() {
        val time = System.currentTimeMillis()
        if (time - lastKeepAlive >= KEEP_ALIVE_INTERVAL) {
            if (pendingKeepAlive) {
                session.disconnect(translatable("disconnect.timeout"))
            } else {
                pendingKeepAlive = true
                lastKeepAlive = time
                keepAliveChallenge = time
                session.send(PacketOutKeepAlive(keepAliveChallenge))
            }
        }
    }

    override fun handle(packet: Packet) = when (packet) {
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

    override fun onDisconnect() {
        playerManager.invalidateStatus()
        playerManager.remove(player)
    }

    private fun handleAnimation(packet: PacketInAnimation) {
        val animation = when (packet.hand) {
            Hand.MAIN -> EntityAnimation.SWING_MAIN_ARM
            Hand.OFF -> EntityAnimation.SWING_OFFHAND
        }

        playerManager.sendToAll(PacketOutAnimation(player.id, animation), player)
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
        server.eventManager.fire(ChatEvent(player, packet.message)).thenAccept {
            if (!it.result.isAllowed) return@thenAccept
            if (it.result.reason !== Component.empty()) {
                server.sendMessage(player, it.result.reason, MessageType.CHAT)
                return@thenAccept
            }

            val name = player.profile.name
            val message = translatable(
                "chat.type.text",
                player.displayName.insertion(name).clickEvent(suggestCommand("/msg $name")).hoverEvent(player),
                text(packet.message)
            )
            server.sendMessage(player, message, MessageType.CHAT)
        }
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
        if (player.gameMode !== GameModes.CREATIVE) return
        val item = packet.clickedItem
        val inValidRange = packet.slot in 1..45
        val hasDamage = MetaKeys.DAMAGE in item.meta && item.meta[MetaKeys.DAMAGE]!! >= 0
        val isValid = item.isEmpty() || (hasDamage && item.amount <= 64 && !item.isEmpty())
        if (inValidRange && isValid) player.inventory[packet.slot.toInt()] = packet.clickedItem
    }

    private fun handleEntityAction(packet: PacketInEntityAction) {
        server.eventManager.fire(PerformActionEvent(player, packet.action)).thenAcceptAsync({
            if (!it.result.isAllowed) return@thenAcceptAsync
            when (it.action) {
                PerformActionEvent.Action.START_SNEAKING -> player.isSneaking = true
                PerformActionEvent.Action.STOP_SNEAKING -> player.isSneaking = false
                PerformActionEvent.Action.START_SPRINTING -> player.isSprinting = true
                PerformActionEvent.Action.STOP_SPRINTING -> player.isSprinting = false
                PerformActionEvent.Action.LEAVE_BED -> Unit // TODO: Sleeping
                PerformActionEvent.Action.START_JUMP_WITH_HORSE,
                PerformActionEvent.Action.STOP_JUMP_WITH_HORSE,
                PerformActionEvent.Action.OPEN_HORSE_INVENTORY -> Unit // TODO: Horses
                PerformActionEvent.Action.START_FLYING_WITH_ELYTRA -> player.isGliding = true
                PerformActionEvent.Action.STOP_FLYING_WITH_ELYTRA -> player.isGliding = false
            }
        }, session.channel.eventLoop())
    }

    private fun handleHeldItemChange(packet: PacketInChangeHeldItem) {
        if (packet.slot !in 0..8) {
            LOGGER.warn("${player.profile.name} tried to change their held item slot to an invalid value!")
            return
        }
        player.inventory.heldSlot = packet.slot.toInt()
    }

    private fun handleKeepAlive(packet: PacketInKeepAlive) {
        if (pendingKeepAlive && packet.id == keepAliveChallenge) {
            val latency = (System.currentTimeMillis() - lastKeepAlive).toInt()
            session.latency = (session.latency * 3 + latency) / 3
            pendingKeepAlive = false
            playerManager.sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.UPDATE_LATENCY, player))
            return
        }
        session.disconnect(translatable("disconnect.timeout"))
    }

    private fun handleAbilities(packet: PacketInAbilities) {
        player.isGliding = packet.isFlying && player.canFly
    }

    private fun handleBlockPlacement(packet: PacketInPlaceBlock) {
        if (!player.canBuild) return // If they can't place blocks, they are irrelevant :)

        val world = player.world
        val chunkX = player.location.floorX() shr 4
        val chunkZ = player.location.floorZ() shr 4
        val chunk = world.chunkManager[ChunkPosition.toLong(chunkX, chunkZ)] ?: return
        val existingBlock = chunk.getBlock(packet.hitResult.position)
        if (existingBlock != Blocks.AIR) return

        val item = player.inventory.mainHand
        val block = BlockLoader.fromKey(item.type.key()) ?: return
        chunk.setBlock(packet.hitResult.position, block)
    }

    private fun handlePlayerDigging(packet: PacketInPlayerDigging) {
        when (packet.status) {
            PacketInPlayerDigging.Status.STARTED -> {
                if (player.gameMode !== GameModes.CREATIVE) return
                val chunkX = packet.location.x() shr 4
                val chunkZ = packet.location.z() shr 4
                val chunk = player.world.chunkManager[ChunkPosition.toLong(chunkX, chunkZ)] ?: return
                val x = packet.location.x()
                val y = packet.location.y()
                val z = packet.location.z()
                chunk.setBlock(x, y, z, Blocks.AIR)

                session.send(PacketOutDiggingResponse(packet.location, 0, PacketInPlayerDigging.Status.FINISHED, true))
                playerManager.sendToAll(PacketOutBlockChange(Blocks.AIR, packet.location))
            }
            PacketInPlayerDigging.Status.UPDATE_STATE -> {
                val handler = server.itemManager.handler(player.inventory[player.inventory.heldSlot].type)
                if (handler !is KryptonItemTimedHandler) return
                handler.finishUse(player, player.hand)
            }
            else -> Unit
        }
    }

    private fun handlePlayerUseItem(packet: PacketInPlayerUseItem) {
        server.itemManager.handler(player.inventory.heldItem(packet.hand).type)?.use(player, packet.hand)
    }

    private fun handleSteerVehicle(packet: PacketInSteerVehicle) {
        // TODO: Handle steering here
        if (packet.isSneaking) player.ejectVehicle()
    }

    private fun handleInteract(packet: PacketInInteract) {
        if (packet.type === PacketInInteract.Type.INTERACT) {
            val target = player.world.entityManager[packet.entityId]
            target?.tryRide(player)
        }
    }

    private fun handlePositionUpdate(packet: PacketInPlayerPosition) {
        val oldLocation = player.location
        val newLocation = Vector3d(packet.x, packet.y, packet.z)
        if (newLocation == oldLocation) return

        player.location = newLocation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation, player.rotation, player.rotation))

        playerManager.sendToAll(PacketOutEntityPosition(
            player.id,
            Positioning.delta(newLocation.x(), oldLocation.x()),
            Positioning.delta(newLocation.y(), oldLocation.y()),
            Positioning.delta(newLocation.z(), oldLocation.z()),
            packet.onGround
        ), player)
        player.updateChunks()
        player.updateMovementStatistics(
            newLocation.x() - oldLocation.x(),
            newLocation.y() - oldLocation.y(),
            newLocation.z() - oldLocation.z()
        )
        player.updateMovementExhaustion(
            newLocation.x() - oldLocation.x(),
            newLocation.y() - oldLocation.y(),
            newLocation.z() - oldLocation.z()
        )
    }

    private fun handleRotationUpdate(packet: PacketInPlayerRotation) {
        val oldRotation = player.rotation
        val newRotation = Vector2f(packet.yaw, packet.pitch)

        player.rotation = newRotation
        server.eventManager.fireAndForget(MoveEvent(player, player.location, player.location, oldRotation, newRotation))

        playerManager.sendToAll(PacketOutEntityRotation(
            player.id,
            packet.yaw,
            packet.pitch,
            packet.onGround
        ), player)
        playerManager.sendToAll(PacketOutHeadLook(player.id, packet.yaw), player)
    }

    private fun handlePositionAndRotationUpdate(packet: PacketInPlayerPositionAndRotation) {
        val oldLocation = player.location
        val oldRotation = player.rotation
        val newLocation = Vector3d(packet.x, packet.y, packet.z)
        val newRotation = Vector2f(packet.yaw, packet.pitch)
        if (newLocation == oldLocation) return

        player.location = newLocation
        player.rotation = newRotation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation, oldRotation, newRotation))

        // TODO: Look in to optimising this (rotation and head look updating) as much as we possibly can
        playerManager.sendToAll(PacketOutEntityPositionAndRotation(
            player.id,
            Positioning.delta(newLocation.x(), oldLocation.x()),
            Positioning.delta(newLocation.y(), oldLocation.y()),
            Positioning.delta(newLocation.z(), oldLocation.z()),
            newRotation.x(),
            newRotation.y(),
            packet.onGround
        ), player)
        playerManager.sendToAll(PacketOutHeadLook(player.id, newRotation.x()), player)
        player.updateChunks()
        player.updateMovementStatistics(
            newLocation.x() - oldLocation.x(),
            newLocation.y() - oldLocation.y(),
            newLocation.z() - oldLocation.z()
        )
        player.updateMovementExhaustion(
            newLocation.x() - oldLocation.x(),
            newLocation.y() - oldLocation.y(),
            newLocation.z() - oldLocation.z()
        )
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

        val parseResults = server.commandManager.dispatcher.parse(reader, player)
        server.commandManager.suggest(parseResults).thenAccept {
            session.send(PacketOutTabComplete(packet.id, it))
        }
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
        player.session.send(PacketOutNBTQueryResponse(packet.transactionId, entity.saveWithPassengers().build()))
    }

    private fun handleResourcePackStatus(packet: PacketInResourcePackStatus) {
        if (packet.status == ResourcePack.Status.DECLINED && server.config.server.resourcePack.forced) {
            player.session.disconnect(translatable("multiplayer.requiredTexturePrompt.disconnect"))
            return
        }
        server.eventManager.fireAndForget(ResourcePackStatusEvent(player, packet.status))
    }

    companion object {

        private const val KEEP_ALIVE_INTERVAL = 15000L
        private val LOGGER = logger<PlayHandler>()
    }
}
