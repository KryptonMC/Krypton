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
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.event.ClickEvent.suggestCommand
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.event.play.ChatEvent
import org.kryptonmc.api.event.play.ClientSettingsEvent
import org.kryptonmc.api.event.play.MoveEvent
import org.kryptonmc.api.event.play.PluginMessageEvent
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.DiggingStatus
import org.kryptonmc.krypton.packet.`in`.play.EntityAction
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
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.out.play.EntityAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutDiggingResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutTabComplete
import org.kryptonmc.krypton.network.Session
import org.kryptonmc.krypton.util.calculatePositionChange
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.toSkinSettings
import org.kryptonmc.krypton.world.block.KryptonBlockLoader
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import java.util.Locale

/**
 * This is the largest and most important of the four packet handlers, as the play state is where the
 * vast majority of packets reside.
 *
 * As mentioned above, this is the packet handler for the [Play][org.kryptonmc.krypton.network.PacketState.PLAY] state.
 * This handles all supported inbound packets in the play state.
 */
class PlayHandler(
    override val server: KryptonServer,
    override val session: Session,
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
                session.sendPacket(PacketOutKeepAlive(keepAliveChallenge))
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
        else -> Unit
    }

    private fun handleAnimation(packet: PacketInAnimation) {
        val animation = when (packet.hand) {
            Hand.MAIN -> EntityAnimation.SWING_MAIN_ARM
            Hand.OFF -> EntityAnimation.SWING_OFFHAND
        }

        playerManager.sendToAll(PacketOutAnimation(player.id, animation), player)
    }

    private fun handleChat(packet: PacketInChat) {
        if (packet.message.startsWith("/")) {
            server.commandManager.dispatch(player, packet.message.removePrefix("/"))
            return
        }

        val event = ChatEvent(player, packet.message)
        server.eventManager.fire(event).thenAccept {
            val result = it.result
            if (!result.isAllowed) return@thenAccept
            if (result.reason !== Component.empty()) {
                server.sendMessage(player, result.reason, MessageType.CHAT)
                return@thenAccept
            }

            val name = player.name
            val message = translatable("chat.type.text", listOf(
                text(name).insertion(name).clickEvent(suggestCommand("/msg $name")).hoverEvent(player),
                text(packet.message)
            ))
            server.sendMessage(player, message, MessageType.CHAT)
        }
    }

    private fun handleClientSettings(packet: PacketInClientSettings) {
        val event = ClientSettingsEvent(
            player,
            Locale(packet.locale),
            packet.viewDistance.toInt(),
            packet.chatColors,
            packet.skinSettings.toSkinSettings()
        )
        server.eventManager.fireAndForget(event)

        player.mainHand = packet.mainHand
        player.skinSettings = packet.skinSettings.toByte()
        playerManager.sendToAll(PacketOutMetadata(
            player.id,
            player.data.dirty
        ))
    }

    private fun handleCreativeInventoryAction(packet: PacketInCreativeInventoryAction) {
        if (packet.clickedItem.type === ItemTypes.AIR) return // ignore air
        player.inventory[packet.slot.toInt()] = packet.clickedItem
    }

    private fun handleEntityAction(packet: PacketInEntityAction) {
        when (packet.action) {
            EntityAction.START_SNEAKING -> player.isCrouching = true
            EntityAction.STOP_SNEAKING -> player.isCrouching = false
            EntityAction.START_SPRINTING -> player.isSprinting = true
            EntityAction.STOP_SPRINTING -> player.isSprinting = false
            EntityAction.LEAVE_BED -> Unit // TODO: Sleeping
            EntityAction.START_JUMP_WITH_HORSE, EntityAction.STOP_JUMP_WITH_HORSE, EntityAction.OPEN_HORSE_INVENTORY -> Unit // TODO: Horses
            EntityAction.START_FLYING_WITH_ELYTRA -> Unit // TODO: Elytra
        }
        playerManager.sendToAll(PacketOutMetadata(player.id, player.data.dirty))
    }

    private fun handleHeldItemChange(packet: PacketInChangeHeldItem) {
        if (packet.slot !in 0..9) {
            Messages.NETWORK.INVALID_HELD_SLOT.warn(LOGGER, player.name)
            return
        }
        player.inventory.heldSlot = packet.slot.toInt()
    }

    private fun handleKeepAlive(packet: PacketInKeepAlive) {
        if (pendingKeepAlive && packet.id == keepAliveChallenge) {
            val latency = (System.currentTimeMillis() - lastKeepAlive).toInt()
            session.latency = (session.latency * 3 + latency) / 3
            pendingKeepAlive = false
            playerManager.sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.UPDATE_LATENCY, player))
            return
        }
        session.disconnect(translatable("disconnect.timeout"))
    }

    private fun handleAbilities(packet: PacketInAbilities) {
        player.isFlying = packet.isFlying && player.canFly
    }

    private fun handleBlockPlacement(packet: PacketInPlaceBlock) {
        if (!player.canBuild) return // if they can't place blocks, they are irrelevant :)

        val world = player.world
        val chunkX = player.location.blockX shr 4
        val chunkZ = player.location.blockZ shr 4
        val chunk = world.chunkManager.chunkMap[ChunkPosition.toLong(chunkX, chunkZ)] ?: return
        val existingBlock = chunk.getBlock(packet.hitResult.position)
        if (existingBlock != Blocks.AIR) return

        val item = player.inventory.mainHand
        val block = KryptonBlockLoader.fromKey(item.type.key) ?: return
        chunk.setBlock(packet.hitResult.position, block)
    }

    private fun handlePlayerDigging(packet: PacketInPlayerDigging) {
        if (player.gamemode != Gamemode.CREATIVE) return
        if (packet.status != DiggingStatus.STARTED) return

        val chunkX = packet.location.x() shr 4
        val chunkZ = packet.location.z() shr 4
        val chunk = player.world.chunkManager.chunkMap[ChunkPosition.toLong(chunkX, chunkZ)] ?: return
        val x = packet.location.x()
        val y = packet.location.y()
        val z = packet.location.z()
        chunk.setBlock(x, y, z, Blocks.AIR)

        session.sendPacket(PacketOutDiggingResponse(packet.location, 0, DiggingStatus.FINISHED, true))
        playerManager.sendToAll(PacketOutBlockChange(Blocks.AIR, packet.location))
    }

    private fun handlePositionUpdate(packet: PacketInPlayerPosition) {
        val oldLocation = player.location
        val newLocation = oldLocation.copy(x = packet.x, y = packet.y, z = packet.z)
        if (newLocation == oldLocation) return

        player.location = newLocation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation))

        playerManager.sendToAll(PacketOutEntityPosition(
            player.id,
            calculatePositionChange(newLocation.x, oldLocation.x),
            calculatePositionChange(newLocation.y, oldLocation.y),
            calculatePositionChange(newLocation.z, oldLocation.z),
            packet.onGround
        ), player)
        player.updateChunks()
    }

    private fun handleRotationUpdate(packet: PacketInPlayerRotation) {
        val oldLocation = player.location
        val newLocation = oldLocation.copy(yaw = packet.yaw, pitch = packet.pitch)

        player.location = newLocation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation))

        playerManager.sendToAll(PacketOutEntityRotation(
            player.id,
            packet.yaw.toAngle(),
            packet.pitch.toAngle(),
            packet.onGround
        ), player)
        playerManager.sendToAll(PacketOutHeadLook(player.id, packet.yaw.toAngle()), player)
    }

    private fun handlePositionAndRotationUpdate(packet: PacketInPlayerPositionAndRotation) {
        val oldLocation = player.location
        val newLocation = oldLocation.copy(x = packet.x, y = packet.y, z = packet.z, yaw = packet.yaw, pitch = packet.pitch)
        if (newLocation == oldLocation) return

        player.location = newLocation
        server.eventManager.fireAndForget(MoveEvent(player, oldLocation, newLocation))

        playerManager.sendToAll(PacketOutEntityPositionAndRotation(
            player.id,
            calculatePositionChange(newLocation.x, oldLocation.x),
            calculatePositionChange(newLocation.y, oldLocation.y),
            calculatePositionChange(newLocation.z, oldLocation.z),
            newLocation.yaw.toAngle(),
            newLocation.pitch.toAngle(),
            packet.onGround
        ), player)
        playerManager.sendToAll(PacketOutHeadLook(player.id, newLocation.yaw.toAngle()), player)
        player.updateChunks()
    }

    private fun handlePluginMessage(packet: PacketInPluginMessage) {
        server.eventManager.fireAndForget(PluginMessageEvent(player, packet.channel, packet.data))
    }

    private fun handleTabComplete(packet: PacketInTabComplete) {
        val reader = StringReader(packet.command)
        if (reader.canRead() && reader.peek() == '/') reader.skip()

        val parseResults = server.commandManager.dispatcher.parse(reader, player)
        server.commandManager.suggest(parseResults).thenAccept {
            session.sendPacket(PacketOutTabComplete(packet.id, it))
        }
    }

    override fun onDisconnect() {
        playerManager.invalidateStatus()
        playerManager.sendMessage(Identity.nil(), translatable("multiplayer.player.left", NamedTextColor.YELLOW, player.displayName), MessageType.SYSTEM)
        playerManager.remove(player)
    }

    companion object {

        private const val LATENCY_CHECK_INTERVAL = 15000
        private const val KEEP_ALIVE_INTERVAL = 15000L
        private val LOGGER = logger<PlayHandler>()
    }
}
