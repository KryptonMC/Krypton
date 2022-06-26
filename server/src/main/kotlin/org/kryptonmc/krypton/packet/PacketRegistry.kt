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
package org.kryptonmc.krypton.packet

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInAnimation
import org.kryptonmc.krypton.packet.`in`.play.PacketInChangeHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatPreview
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientSettings
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInCreativeInventoryAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInEntityNBTQuery
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlaceBlock
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerDigging
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerMovement
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerUseItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInResourcePackStatus
import org.kryptonmc.krypton.packet.`in`.play.PacketInSteerVehicle
import org.kryptonmc.krypton.packet.`in`.play.PacketInTabComplete
import org.kryptonmc.krypton.packet.`in`.play.PacketInTeleportConfirm
import org.kryptonmc.krypton.packet.`in`.status.PacketInPing
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutPluginRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutAcknowledgeBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutActionBar
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockBreakAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import org.kryptonmc.krypton.packet.out.play.PacketOutCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutChatPreview
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareCommands
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutDestroyEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisplayObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityTeleport
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutJoinGame
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutNBTQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutNamedSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChat
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerListHeaderFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerPositionAndLook
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCooldown
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSlot
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutStatistics
import org.kryptonmc.krypton.packet.out.play.PacketOutStopSound
import org.kryptonmc.krypton.packet.out.play.PacketOutSubTitle
import org.kryptonmc.krypton.packet.out.play.PacketOutSystemChat
import org.kryptonmc.krypton.packet.out.play.PacketOutTabComplete
import org.kryptonmc.krypton.packet.out.play.PacketOutTags
import org.kryptonmc.krypton.packet.out.play.PacketOutTeam
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutTitle
import org.kryptonmc.krypton.packet.out.play.PacketOutTitleTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUnlockRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateHealth
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateLight
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateScore
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutWindowItems
import org.kryptonmc.krypton.packet.out.status.PacketOutPong
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.util.IdentityHashStrategy

object PacketRegistry {

    private val byEncoded = Int2ObjectOpenHashMap<PacketConstructor>()
    private val toId = Object2IntOpenCustomHashMap<Class<*>>(IdentityHashStrategy).apply { defaultReturnValue(-1) }

    fun lookup(clazz: Class<*>): Int = toId.getInt(clazz)

    fun lookup(state: PacketState, id: Int, buf: ByteBuf): Packet? {
        val constructor = byEncoded[encode(state, id)] ?: return null
        return constructor.create(buf)
    }

    fun bootstrap() {
        // Handshake
        register(PacketState.HANDSHAKE, 0x00, ::PacketInHandshake)

        // Status
        register(PacketState.STATUS, 0x00) { PacketInStatusRequest }
        register(PacketState.STATUS, 0x01, ::PacketInPing)

        register<PacketOutStatusResponse>(0x00)
        register<PacketOutPong>(0x01)

        // Login
        register(PacketState.LOGIN, 0x00, ::PacketInLoginStart)
        register(PacketState.LOGIN, 0x01, ::PacketInEncryptionResponse)
        register(PacketState.LOGIN, 0x02, ::PacketInPluginResponse)

        register<PacketOutLoginDisconnect>(0x00)
        register<PacketOutEncryptionRequest>(0x01)
        register<PacketOutLoginSuccess>(0x02)
        register<PacketOutSetCompression>(0x03)
        register<PacketOutPluginRequest>(0x04)

        // Play
        register(PacketState.PLAY, 0x00, ::PacketInTeleportConfirm)
        register(PacketState.PLAY, 0x03, ::PacketInCommand)
        register(PacketState.PLAY, 0x04, ::PacketInChat)
        register(PacketState.PLAY, 0x05, ::PacketInChatPreview)
        register(PacketState.PLAY, 0x06, ::PacketInClientStatus)
        register(PacketState.PLAY, 0x07, ::PacketInClientSettings)
        register(PacketState.PLAY, 0x08, ::PacketInTabComplete)
        register(PacketState.PLAY, 0x0C, ::PacketInPluginMessage)
        register(PacketState.PLAY, 0x0E, ::PacketInEntityNBTQuery)
        register(PacketState.PLAY, 0x0F, ::PacketInInteract)
        register(PacketState.PLAY, 0x11, ::PacketInKeepAlive)
        register(PacketState.PLAY, 0x13, ::PacketInPlayerPosition)
        register(PacketState.PLAY, 0x14, ::PacketInPlayerPositionAndRotation)
        register(PacketState.PLAY, 0x15, ::PacketInPlayerRotation)
        register(PacketState.PLAY, 0x16, ::PacketInPlayerMovement)
        register(PacketState.PLAY, 0x1B, ::PacketInAbilities)
        register(PacketState.PLAY, 0x1C, ::PacketInPlayerDigging)
        register(PacketState.PLAY, 0x1D, ::PacketInEntityAction)
        register(PacketState.PLAY, 0x1E, ::PacketInSteerVehicle)
        register(PacketState.PLAY, 0x23, ::PacketInResourcePackStatus)
        register(PacketState.PLAY, 0x27, ::PacketInChangeHeldItem)
        register(PacketState.PLAY, 0x2A, ::PacketInCreativeInventoryAction)
        register(PacketState.PLAY, 0x2E, ::PacketInAnimation)
        register(PacketState.PLAY, 0x30, ::PacketInPlaceBlock)
        register(PacketState.PLAY, 0x31, ::PacketInPlayerUseItem)

        register<PacketOutSpawnEntity>(0x00)
        register<PacketOutSpawnExperienceOrb>(0x01)
        register<PacketOutSpawnPlayer>(0x02)
        register<PacketOutAnimation>(0x03)
        register<PacketOutStatistics>(0x04)
        register<PacketOutAcknowledgeBlockChange>(0x05)
        register<PacketOutBlockBreakAnimation>(0x06)
        register<PacketOutBlockChange>(0x09)
        register<PacketOutBossBar>(0x0A)
        register<PacketOutDifficulty>(0x0B)
        register<PacketOutChatPreview>(0x0C)
        register<PacketOutClearTitles>(0x0D)
        register<PacketOutTabComplete>(0x0E)
        register<PacketOutDeclareCommands>(0x0F)
        register<PacketOutWindowItems>(0x11)
        register<PacketOutSetSlot>(0x13)
        register<PacketOutSetCooldown>(0x14)
        register<PacketOutPluginMessage>(0x15)
        register<PacketOutNamedSoundEffect>(0x16)
        register<PacketOutDisconnect>(0x17)
        register<PacketOutEntityStatus>(0x18)
        register<PacketOutUnloadChunk>(0x1A)
        register<PacketOutChangeGameState>(0x1B)
        register<PacketOutInitializeWorldBorder>(0x1D)
        register<PacketOutKeepAlive>(0x1E)
        register<PacketOutChunkDataAndLight>(0x1F)
        register<PacketOutEffect>(0x20)
        register<PacketOutParticle>(0x21)
        register<PacketOutUpdateLight>(0x22)
        register<PacketOutJoinGame>(0x23)
        register<PacketOutEntityPosition>(0x26)
        register<PacketOutEntityPositionAndRotation>(0x27)
        register<PacketOutEntityRotation>(0x28)
        register<PacketOutOpenBook>(0x2A)
        register<PacketOutAbilities>(0x2F)
        register<PacketOutPlayerChat>(0x30)
        register<PacketOutPlayerInfo>(0x34)
        register<PacketOutPlayerPositionAndLook>(0x36)
        register<PacketOutUnlockRecipes>(0x37)
        register<PacketOutDestroyEntities>(0x38)
        register<PacketOutResourcePack>(0x3A)
        register<PacketOutHeadLook>(0x3C)
        register<PacketOutActionBar>(0x40)
        register<PacketOutCamera>(0x46)
        register<PacketOutChangeHeldItem>(0x47)
        register<PacketOutUpdateViewPosition>(0x48)
        register<PacketOutSpawnPosition>(0x4A)
        register<PacketOutDisplayObjective>(0x4C)
        register<PacketOutMetadata>(0x4D)
        register<PacketOutEntityVelocity>(0x4F)
        register<PacketOutUpdateHealth>(0x52)
        register<PacketOutObjective>(0x53)
        register<PacketOutSetPassengers>(0x54)
        register<PacketOutTeam>(0x55)
        register<PacketOutUpdateScore>(0x56)
        register<PacketOutSubTitle>(0x58)
        register<PacketOutTimeUpdate>(0x59)
        register<PacketOutTitle>(0x5A)
        register<PacketOutTitleTimes>(0x5B)
        register<PacketOutEntitySoundEffect>(0x5C)
        register<PacketOutSoundEffect>(0x5D)
        register<PacketOutStopSound>(0x5E)
        register<PacketOutSystemChat>(0x5F)
        register<PacketOutPlayerListHeaderFooter>(0x60)
        register<PacketOutNBTQueryResponse>(0x61)
        register<PacketOutEntityTeleport>(0x63)
        register<PacketOutAttributes>(0x65)
        register<PacketOutDeclareRecipes>(0x67)
        register<PacketOutTags>(0x68)
    }

    @JvmStatic
    private fun register(state: PacketState, id: Int, creator: PacketConstructor) {
        byEncoded[encode(state, id)] = creator
    }

    @JvmStatic
    private inline fun <reified T> register(id: Int) {
        toId[T::class.java] = id
    }

    @JvmStatic
    private fun encode(state: PacketState, id: Int): Int = (state.ordinal shl 16) or (id and 0xFFFF)

    private fun interface PacketConstructor {

        fun create(buf: ByteBuf): Packet
    }
}
