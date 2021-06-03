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
package org.kryptonmc.krypton.packet.handlers

import com.mojang.brigadier.StringReader
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.event.ClickEvent.suggestCommand
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.event.play.ChatEvent
import org.kryptonmc.api.event.play.ClientSettingsEvent
import org.kryptonmc.api.event.play.MoveEvent
import org.kryptonmc.api.event.play.PluginMessageEvent
import org.kryptonmc.api.inventory.item.ItemStack
import org.kryptonmc.api.inventory.item.Material
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.krypton.entity.metadata.MovementFlags
import org.kryptonmc.krypton.entity.metadata.Optional
import org.kryptonmc.krypton.entity.metadata.PlayerMetadata
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.`in`.play.DiggingStatus
import org.kryptonmc.krypton.packet.`in`.play.EntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInAnimation
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInCreativeInventoryAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInHeldItemChange
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerBlockPlacement
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.out.play.PacketOutAcknowledgePlayerDigging
import org.kryptonmc.krypton.packet.out.play.chunk.PacketOutBlockChange
import org.kryptonmc.krypton.packet.out.play.chat.PacketOutTabComplete
import org.kryptonmc.krypton.packet.out.play.entity.EntityAnimation
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityAnimation
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMetadata
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityHeadLook
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityPosition
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.entity.PacketOutEntityMovement.PacketOutEntityRotation
import org.kryptonmc.krypton.packet.session.Session
import org.kryptonmc.krypton.packet.session.SessionManager
import org.kryptonmc.krypton.packet.state.PacketState
import org.kryptonmc.krypton.registry.Registries
import org.kryptonmc.krypton.util.calculatePositionChange
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.world.block.KryptonBlock
import java.util.Locale
import kotlin.math.max

/**
 * This is the largest and most important of the four packet handlers, as the play state is where the
 * vast majority of packets reside.
 *
 * As mentioned above, this is the packet handler for the [Play][org.kryptonmc.krypton.packet.state.PacketState.PLAY] state.
 * This handles all supported inbound packets in the play state.
 */
class PlayHandler(
    override val server: KryptonServer,
    private val sessionManager: SessionManager,
    override val session: Session
) : PacketHandler {

    override fun handle(packet: Packet) = when (packet) {
        is PacketInAnimation -> handleAnimation(packet)
        is PacketInChat -> handleChat(packet)
        is PacketInClientSettings -> handleClientSettings(packet)
        is PacketInCreativeInventoryAction -> handleCreativeInventoryAction(packet)
        is PacketInEntityAction -> handleEntityAction(packet)
        is PacketInHeldItemChange -> handleHeldItemChange(packet)
        is PacketInKeepAlive -> handleKeepAlive(packet)
        is PacketInPlayerAbilities -> handleAbilities(packet)
        is PacketInPlayerBlockPlacement -> handleBlockPlacement(packet)
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

        val animationPacket = PacketOutEntityAnimation(session.id, animation)
        sessionManager.sendPackets(animationPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handleChat(packet: PacketInChat) {
        if (packet.message.startsWith("/")) {
            server.commandManager.dispatch(player, packet.message.removePrefix("/"))
            return
        }

        val event = ChatEvent(player, packet.message)
        server.eventBus.call(event)
        if (event.isCancelled) return

        val message = translatable("chat.type.text", listOf(
            text(session.profile.name).insertion(session.profile.name).clickEvent(suggestCommand("/msg ${session.profile.name}")).hoverEvent(player),
            text(packet.message)
        ))
        server.sendMessage(player, message, MessageType.CHAT)
    }

    private fun handleClientSettings(packet: PacketInClientSettings) {
        val event = ClientSettingsEvent(
            session.player,
            Locale(packet.settings.locale),
            packet.settings.viewDistance.toInt(),
            packet.settings.chatColors,
            packet.settings.skinSettings
        )
        server.eventBus.call(event)
        session.settings = packet.settings

        val metadataPacket = PacketOutEntityMetadata(
            session.id,
            PlayerMetadata(mainHand = packet.settings.mainHand, skinFlags = packet.settings.skinSettings)
        )
        session.sendPacket(metadataPacket)
        sessionManager.sendPackets(metadataPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handleCreativeInventoryAction(packet: PacketInCreativeInventoryAction) {
        if (packet.clickedItem.id == 0) return // ignore air
        val type = Material.KEYS.value(Registries.ITEMS[packet.clickedItem.id] ?: return)
        val item = type?.let { ItemStack(it, packet.clickedItem.count.toInt()) } ?: ItemStack.EMPTY
        player.inventory[packet.slot.toInt()] = item
    }

    private fun handleEntityAction(packet: PacketInEntityAction) {
        val metadata = when (packet.action) {
            EntityAction.START_SNEAKING -> PlayerMetadata(movementFlags = MovementFlags(isCrouching = true))
            EntityAction.STOP_SNEAKING -> PlayerMetadata(movementFlags = MovementFlags(isCrouching = false))
            EntityAction.LEAVE_BED -> PlayerMetadata(bedPosition = Optional(null))
            EntityAction.START_SPRINTING -> PlayerMetadata(movementFlags = MovementFlags(isSprinting = true))
            EntityAction.STOP_SPRINTING -> PlayerMetadata(movementFlags = MovementFlags(isSprinting = false))
            EntityAction.START_FLYING_WITH_ELYTRA -> PlayerMetadata(movementFlags = MovementFlags(isFlying = true))
            else -> return // the rest of these are unsupported
        }

        val metadataPacket = PacketOutEntityMetadata(session.id, metadata)
        sessionManager.sendPackets(metadataPacket) {
            it != session && it.currentState == PacketState.PLAY
        }
    }

    private fun handleHeldItemChange(packet: PacketInHeldItemChange) {
        if (packet.slot !in 0..9) {
            Messages.NETWORK.INVALID_HELD_SLOT.warn(LOGGER, player.name)
            return
        }
        player.inventory.heldSlot = packet.slot.toInt()
    }

    private fun handleKeepAlive(packet: PacketInKeepAlive) {
        if (session.lastKeepAliveId == packet.keepAliveId) {
            sessionManager.updateLatency(session, max(packet.keepAliveId - session.lastKeepAliveId, 0L).toInt())
            return
        }
        session.disconnect(translatable("disconnect.timeout"))
    }

    private fun handleAbilities(packet: PacketInPlayerAbilities) {
        player.abilities.isFlying = packet.isFlying && player.abilities.canFly
    }

    private fun handleBlockPlacement(packet: PacketInPlayerBlockPlacement) {
        if (!player.abilities.canBuild) return // if they can't place blocks, they are irrelevant :)

        val world = session.player.world
        val chunk = world.chunks.firstOrNull { session.player.location in it.position } ?: return
        val existingBlock = chunk.getBlock(packet.location)
        if (existingBlock.type != Material.AIR) return

        val item = session.player.inventory.mainHand ?: return
        val block = KryptonBlock(item.type, chunk, packet.location.toLocation(world))

        sessionManager.sendPackets(PacketOutBlockChange(if (chunk.setBlock(block)) block else existingBlock)) { it.currentState == PacketState.PLAY }
    }

    private fun handlePlayerDigging(packet: PacketInPlayerDigging) {
        if (player.gamemode != Gamemode.CREATIVE) return
        if (packet.status != DiggingStatus.STARTED) return

        val chunk = player.world.chunks.firstOrNull { packet.location in it.position } ?: return
        val block = KryptonBlock(Material.AIR, chunk, packet.location.toLocation(player.world))
        chunk.setBlock(block)

        session.sendPacket(PacketOutAcknowledgePlayerDigging(packet.location, 0, DiggingStatus.FINISHED, true))
        sessionManager.sendPackets(PacketOutBlockChange(block)) { it.currentState == PacketState.PLAY }
    }

    private fun handlePositionUpdate(packet: PacketInPlayerPosition) {
        val oldLocation = session.player.location
        val newLocation = oldLocation.copy(x = packet.x, y = packet.y, z = packet.z)
        if (newLocation == oldLocation) return

        session.player.location = newLocation
        server.eventBus.call(MoveEvent(session.player, oldLocation, newLocation))

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
        player.updateChunks()
    }

    private fun handleRotationUpdate(packet: PacketInPlayerRotation) {
        val oldLocation = session.player.location
        val newLocation = oldLocation.copy(yaw = packet.yaw, pitch = packet.pitch)

        session.player.location = newLocation
        server.eventBus.call(MoveEvent(session.player, oldLocation, newLocation))

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

    private fun handlePositionAndRotationUpdate(packet: PacketInPlayerPositionAndRotation) {
        val oldLocation = session.player.location
        val newLocation = oldLocation.copy(x = packet.x, y = packet.y, z = packet.z, yaw = packet.yaw, pitch = packet.pitch)
        if (newLocation == oldLocation) return

        session.player.location = newLocation
        server.eventBus.call(MoveEvent(session.player, oldLocation, newLocation))

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
        player.updateChunks()
    }

    private fun handlePluginMessage(packet: PacketInPluginMessage) {
        server.eventBus.call(PluginMessageEvent(session.player, packet.channel, packet.data))
    }

    private fun handleTabComplete(packet: PacketInTabComplete) {
        val reader = StringReader(packet.command)
        if (reader.canRead() && reader.peek() == '/') reader.skip()

        val parseResults = server.commandManager.dispatcher.parse(reader, player)
        server.commandManager.suggest(parseResults).thenAccept {
            session.sendPacket(PacketOutTabComplete(packet.id, it))
        }
    }

    private val player = session.player

    companion object {

        private val LOGGER = logger<PlayHandler>()
    }
}
