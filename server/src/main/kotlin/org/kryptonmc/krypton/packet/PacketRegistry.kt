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
import org.kryptonmc.krypton.packet.`in`.play.PacketInSwingArm
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatPreview
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientInformation
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetCreativeModeSlot
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInQueryEntityTag
import org.kryptonmc.krypton.packet.`in`.play.PacketInInteract
import org.kryptonmc.krypton.packet.`in`.play.PacketInKeepAlive
import org.kryptonmc.krypton.packet.`in`.play.PacketInUseItemOn
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerAction
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerOnGround
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInUseItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInPluginMessage
import org.kryptonmc.krypton.packet.`in`.play.PacketInResourcePack
import org.kryptonmc.krypton.packet.`in`.play.PacketInPlayerInput
import org.kryptonmc.krypton.packet.`in`.play.PacketInCommandSuggestionsRequest
import org.kryptonmc.krypton.packet.`in`.play.PacketInConfirmTeleportation
import org.kryptonmc.krypton.packet.`in`.status.PacketInPingRequest
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutPluginRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutAcknowledgeBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutSetActionBarText
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutAwardStatistics
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutChatPreview
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutCommandSuggestionsResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutCommands
import org.kryptonmc.krypton.packet.out.play.PacketOutCustomSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutRemoveEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisplayObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutLogin
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutTagQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateObjectives
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChatMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTabListHeaderAndFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutSynchronizePlayerPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetBlockDestroyStage
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerSlot
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCooldown
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSetDefaultSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutStopSound
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSubtitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSystemChatMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTags
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTeams
import org.kryptonmc.krypton.packet.out.play.PacketOutTeleportEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleAnimationTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipeBook
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHealth
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateLight
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateScore
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCenterChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutWorldEvent
import org.kryptonmc.krypton.packet.out.status.PacketOutPingResponse
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.util.IdentityHashStrategy

object PacketRegistry {

    private val byEncoded = Int2ObjectOpenHashMap<PacketConstructor>()
    private val toId = Object2IntOpenCustomHashMap<Class<*>>(IdentityHashStrategy).apply { defaultReturnValue(-1) }

    fun lookup(clazz: Class<*>): Int = toId.getInt(clazz)

    fun lookup(state: PacketState, id: Int, buf: ByteBuf): Packet? {
        val constructor = byEncoded.get(encode(state, id)) ?: return null
        return constructor.create(buf)
    }

    fun bootstrap() {
        // Handshake
        register(PacketState.HANDSHAKE, 0x00, ::PacketInHandshake)

        // Status
        register(PacketState.STATUS, 0x00) { PacketInStatusRequest }
        register(PacketState.STATUS, 0x01, ::PacketInPingRequest)

        register<PacketOutStatusResponse>(0x00)
        register<PacketOutPingResponse>(0x01)

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
        register(PacketState.PLAY, 0x00, ::PacketInConfirmTeleportation)
        register(PacketState.PLAY, 0x03, ::PacketInChatCommand)
        register(PacketState.PLAY, 0x04, ::PacketInChatMessage)
        register(PacketState.PLAY, 0x05, ::PacketInChatPreview)
        register(PacketState.PLAY, 0x06, ::PacketInClientCommand)
        register(PacketState.PLAY, 0x07, ::PacketInClientInformation)
        register(PacketState.PLAY, 0x08, ::PacketInCommandSuggestionsRequest)
        register(PacketState.PLAY, 0x0C, ::PacketInPluginMessage)
        register(PacketState.PLAY, 0x0E, ::PacketInQueryEntityTag)
        register(PacketState.PLAY, 0x0F, ::PacketInInteract)
        register(PacketState.PLAY, 0x11, ::PacketInKeepAlive)
        register(PacketState.PLAY, 0x13, ::PacketInSetPlayerPosition)
        register(PacketState.PLAY, 0x14, ::PacketInSetPlayerPositionAndRotation)
        register(PacketState.PLAY, 0x15, ::PacketInSetPlayerRotation)
        register(PacketState.PLAY, 0x16, ::PacketInSetPlayerOnGround)
        register(PacketState.PLAY, 0x1B, ::PacketInAbilities)
        register(PacketState.PLAY, 0x1C, ::PacketInPlayerAction)
        register(PacketState.PLAY, 0x1D, ::PacketInPlayerCommand)
        register(PacketState.PLAY, 0x1E, ::PacketInPlayerInput)
        register(PacketState.PLAY, 0x23, ::PacketInResourcePack)
        register(PacketState.PLAY, 0x27, ::PacketInSetHeldItem)
        register(PacketState.PLAY, 0x2A, ::PacketInSetCreativeModeSlot)
        register(PacketState.PLAY, 0x2E, ::PacketInSwingArm)
        register(PacketState.PLAY, 0x30, ::PacketInUseItemOn)
        register(PacketState.PLAY, 0x31, ::PacketInUseItem)

        register<PacketOutSpawnEntity>(0x00)
        register<PacketOutSpawnExperienceOrb>(0x01)
        register<PacketOutSpawnPlayer>(0x02)
        register<PacketOutAnimation>(0x03)
        register<PacketOutAwardStatistics>(0x04)
        register<PacketOutAcknowledgeBlockChange>(0x05)
        register<PacketOutSetBlockDestroyStage>(0x06)
        register<PacketOutBlockUpdate>(0x09)
        register<PacketOutBossBar>(0x0A)
        register<PacketOutChangeDifficulty>(0x0B)
        register<PacketOutChatPreview>(0x0C)
        register<PacketOutClearTitles>(0x0D)
        register<PacketOutCommandSuggestionsResponse>(0x0E)
        register<PacketOutCommands>(0x0F)
        register<PacketOutSetContainerContent>(0x11)
        register<PacketOutSetContainerSlot>(0x13)
        register<PacketOutSetCooldown>(0x14)
        register<PacketOutPluginMessage>(0x15)
        register<PacketOutCustomSoundEffect>(0x16)
        register<PacketOutDisconnect>(0x17)
        register<PacketOutEntityEvent>(0x18)
        register<PacketOutUnloadChunk>(0x1A)
        register<PacketOutGameEvent>(0x1B)
        register<PacketOutInitializeWorldBorder>(0x1D)
        register<PacketOutKeepAlive>(0x1E)
        register<PacketOutChunkDataAndLight>(0x1F)
        register<PacketOutWorldEvent>(0x20)
        register<PacketOutParticle>(0x21)
        register<PacketOutUpdateLight>(0x22)
        register<PacketOutLogin>(0x23)
        register<PacketOutUpdateEntityPosition>(0x26)
        register<PacketOutUpdateEntityPositionAndRotation>(0x27)
        register<PacketOutUpdateEntityRotation>(0x28)
        register<PacketOutOpenBook>(0x2A)
        register<PacketOutAbilities>(0x2F)
        register<PacketOutPlayerChatMessage>(0x30)
        register<PacketOutPlayerInfo>(0x34)
        register<PacketOutSynchronizePlayerPosition>(0x36)
        register<PacketOutUpdateRecipeBook>(0x37)
        register<PacketOutRemoveEntities>(0x38)
        register<PacketOutResourcePack>(0x3A)
        register<PacketOutSetHeadRotation>(0x3C)
        register<PacketOutSetActionBarText>(0x41)
        register<PacketOutSetCamera>(0x46)
        register<PacketOutSetHeldItem>(0x47)
        register<PacketOutSetCenterChunk>(0x48)
        register<PacketOutSetDefaultSpawnPosition>(0x4A)
        register<PacketOutDisplayObjective>(0x4C)
        register<PacketOutSetEntityMetadata>(0x4D)
        register<PacketOutSetEntityVelocity>(0x4F)
        register<PacketOutSetHealth>(0x52)
        register<PacketOutUpdateObjectives>(0x53)
        register<PacketOutSetPassengers>(0x54)
        register<PacketOutUpdateTeams>(0x55)
        register<PacketOutUpdateScore>(0x56)
        register<PacketOutSetSubtitleText>(0x58)
        register<PacketOutUpdateTime>(0x59)
        register<PacketOutSetTitleText>(0x5A)
        register<PacketOutSetTitleAnimationTimes>(0x5B)
        register<PacketOutEntitySoundEffect>(0x5C)
        register<PacketOutSoundEffect>(0x5D)
        register<PacketOutStopSound>(0x5E)
        register<PacketOutSystemChatMessage>(0x5F)
        register<PacketOutSetTabListHeaderAndFooter>(0x60)
        register<PacketOutTagQueryResponse>(0x61)
        register<PacketOutTeleportEntity>(0x63)
        register<PacketOutUpdateAttributes>(0x65)
        register<PacketOutUpdateRecipes>(0x67)
        register<PacketOutUpdateTags>(0x68)
    }

    @JvmStatic
    private fun register(state: PacketState, id: Int, creator: PacketConstructor) {
        byEncoded.put(encode(state, id), creator)
    }

    @JvmStatic
    private inline fun <reified T> register(id: Int) {
        toId.put(T::class.java, id)
    }

    @JvmStatic
    private fun encode(state: PacketState, id: Int): Int = (state.ordinal shl 16) or (id and 0xFFFF)

    private fun interface PacketConstructor {

        fun create(buf: ByteBuf): Packet
    }
}
