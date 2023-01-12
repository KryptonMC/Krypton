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
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInSwingArm
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetHeldItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatMessage
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
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutCommandSuggestionsResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutCommands
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
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEnabledFeatures
import org.kryptonmc.krypton.packet.out.play.PacketOutWorldEvent
import org.kryptonmc.krypton.packet.out.status.PacketOutPingResponse
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse

object PacketRegistry {

    private val byEncoded = Int2ObjectOpenHashMap<PacketConstructor>()
    private val toId = Reference2IntOpenHashMap<Class<*>>().apply { defaultReturnValue(-1) }

    fun getOutboundPacketId(clazz: Class<*>): Int = toId.getInt(clazz)

    fun getInboundPacket(state: PacketState, id: Int, buf: ByteBuf): Packet? {
        val constructor = byEncoded.get(encodeInboundLookupKey(state, id)) ?: return null
        return constructor.create(buf)
    }

    @Suppress("LongMethod", "MagicNumber")
    fun bootstrap() {
        // Handshake
        registerInbound(PacketState.HANDSHAKE, 0x00) { PacketInHandshake(it) }

        // Status
        registerInbound(PacketState.STATUS, 0x00) { PacketInStatusRequest }
        registerInbound(PacketState.STATUS, 0x01) { PacketInPingRequest(it) }

        registerOutbound<PacketOutStatusResponse>(0x00)
        registerOutbound<PacketOutPingResponse>(0x01)

        // Login
        registerInbound(PacketState.LOGIN, 0x00) { PacketInLoginStart(it) }
        registerInbound(PacketState.LOGIN, 0x01) { PacketInEncryptionResponse(it) }
        registerInbound(PacketState.LOGIN, 0x02) { PacketInPluginResponse(it) }

        registerOutbound<PacketOutLoginDisconnect>(0x00)
        registerOutbound<PacketOutEncryptionRequest>(0x01)
        registerOutbound<PacketOutLoginSuccess>(0x02)
        registerOutbound<PacketOutSetCompression>(0x03)
        registerOutbound<PacketOutPluginRequest>(0x04)

        // Play
        registerInbound(PacketState.PLAY, 0x00) { PacketInConfirmTeleportation(it) }
        registerInbound(PacketState.PLAY, 0x04) { PacketInChatCommand(it) }
        registerInbound(PacketState.PLAY, 0x05) { PacketInChatMessage(it) }
        registerInbound(PacketState.PLAY, 0x06) { PacketInClientCommand(it) }
        registerInbound(PacketState.PLAY, 0x07) { PacketInClientInformation(it) }
        registerInbound(PacketState.PLAY, 0x08) { PacketInCommandSuggestionsRequest(it) }
        registerInbound(PacketState.PLAY, 0x0C) { PacketInPluginMessage(it) }
        registerInbound(PacketState.PLAY, 0x0E) { PacketInQueryEntityTag(it) }
        registerInbound(PacketState.PLAY, 0x0F) { PacketInInteract(it) }
        registerInbound(PacketState.PLAY, 0x11) { PacketInKeepAlive(it) }
        registerInbound(PacketState.PLAY, 0x13) { PacketInSetPlayerPosition(it) }
        registerInbound(PacketState.PLAY, 0x14) { PacketInSetPlayerPositionAndRotation(it) }
        registerInbound(PacketState.PLAY, 0x15) { PacketInSetPlayerRotation(it) }
        registerInbound(PacketState.PLAY, 0x16) { PacketInSetPlayerOnGround(it) }
        registerInbound(PacketState.PLAY, 0x1B) { PacketInAbilities(it) }
        registerInbound(PacketState.PLAY, 0x1C) { PacketInPlayerAction(it) }
        registerInbound(PacketState.PLAY, 0x1D) { PacketInPlayerCommand(it) }
        registerInbound(PacketState.PLAY, 0x1E) { PacketInPlayerInput(it) }
        registerInbound(PacketState.PLAY, 0x24) { PacketInResourcePack(it) }
        registerInbound(PacketState.PLAY, 0x28) { PacketInSetHeldItem(it) }
        registerInbound(PacketState.PLAY, 0x2B) { PacketInSetCreativeModeSlot(it) }
        registerInbound(PacketState.PLAY, 0x2F) { PacketInSwingArm(it) }
        registerInbound(PacketState.PLAY, 0x31) { PacketInUseItemOn(it) }
        registerInbound(PacketState.PLAY, 0x32) { PacketInUseItem(it) }

        registerOutbound<PacketOutSpawnEntity>(0x00)
        registerOutbound<PacketOutSpawnExperienceOrb>(0x01)
        registerOutbound<PacketOutSpawnPlayer>(0x02)
        registerOutbound<PacketOutAnimation>(0x03)
        registerOutbound<PacketOutAwardStatistics>(0x04)
        registerOutbound<PacketOutAcknowledgeBlockChange>(0x05)
        registerOutbound<PacketOutSetBlockDestroyStage>(0x06)
        registerOutbound<PacketOutBlockUpdate>(0x09)
        registerOutbound<PacketOutBossBar>(0x0A)
        registerOutbound<PacketOutChangeDifficulty>(0x0B)
        registerOutbound<PacketOutClearTitles>(0x0C)
        registerOutbound<PacketOutCommandSuggestionsResponse>(0x0D)
        registerOutbound<PacketOutCommands>(0x0E)
        registerOutbound<PacketOutSetContainerContent>(0x10)
        registerOutbound<PacketOutSetContainerSlot>(0x12)
        registerOutbound<PacketOutSetCooldown>(0x13)
        registerOutbound<PacketOutPluginMessage>(0x15)
        registerOutbound<PacketOutDisconnect>(0x17)
        registerOutbound<PacketOutEntityEvent>(0x19)
        registerOutbound<PacketOutUnloadChunk>(0x1B)
        registerOutbound<PacketOutGameEvent>(0x1C)
        registerOutbound<PacketOutInitializeWorldBorder>(0x1E)
        registerOutbound<PacketOutKeepAlive>(0x1F)
        registerOutbound<PacketOutChunkDataAndLight>(0x20)
        registerOutbound<PacketOutWorldEvent>(0x21)
        registerOutbound<PacketOutParticle>(0x22)
        registerOutbound<PacketOutUpdateLight>(0x23)
        registerOutbound<PacketOutLogin>(0x24)
        registerOutbound<PacketOutUpdateEntityPosition>(0x27)
        registerOutbound<PacketOutUpdateEntityPositionAndRotation>(0x28)
        registerOutbound<PacketOutUpdateEntityRotation>(0x29)
        registerOutbound<PacketOutOpenBook>(0x2B)
        registerOutbound<PacketOutAbilities>(0x30)
        registerOutbound<PacketOutPlayerChatMessage>(0x31)
        registerOutbound<PacketOutPlayerInfo>(0x36)
        registerOutbound<PacketOutSynchronizePlayerPosition>(0x38)
        registerOutbound<PacketOutUpdateRecipeBook>(0x39)
        registerOutbound<PacketOutRemoveEntities>(0x3A)
        registerOutbound<PacketOutResourcePack>(0x3C)
        registerOutbound<PacketOutSetHeadRotation>(0x3E)
        registerOutbound<PacketOutSetActionBarText>(0x42)
        registerOutbound<PacketOutSetCamera>(0x48)
        registerOutbound<PacketOutSetHeldItem>(0x49)
        registerOutbound<PacketOutSetCenterChunk>(0x4A)
        registerOutbound<PacketOutSetDefaultSpawnPosition>(0x4C)
        registerOutbound<PacketOutDisplayObjective>(0x4D)
        registerOutbound<PacketOutSetEntityMetadata>(0x4E)
        registerOutbound<PacketOutSetEntityVelocity>(0x50)
        registerOutbound<PacketOutSetHealth>(0x53)
        registerOutbound<PacketOutUpdateObjectives>(0x54)
        registerOutbound<PacketOutSetPassengers>(0x55)
        registerOutbound<PacketOutUpdateTeams>(0x56)
        registerOutbound<PacketOutUpdateScore>(0x57)
        registerOutbound<PacketOutSetSubtitleText>(0x59)
        registerOutbound<PacketOutUpdateTime>(0x5A)
        registerOutbound<PacketOutSetTitleText>(0x5B)
        registerOutbound<PacketOutSetTitleAnimationTimes>(0x5C)
        registerOutbound<PacketOutEntitySoundEffect>(0x5D)
        registerOutbound<PacketOutSoundEffect>(0x5E)
        registerOutbound<PacketOutStopSound>(0x5F)
        registerOutbound<PacketOutSystemChatMessage>(0x60)
        registerOutbound<PacketOutSetTabListHeaderAndFooter>(0x61)
        registerOutbound<PacketOutTagQueryResponse>(0x62)
        registerOutbound<PacketOutTeleportEntity>(0x64)
        registerOutbound<PacketOutUpdateAttributes>(0x66)
        registerOutbound<PacketOutUpdateEnabledFeatures>(0x67)
        registerOutbound<PacketOutUpdateRecipes>(0x69)
        registerOutbound<PacketOutUpdateTags>(0x6A)
    }

    @JvmStatic
    private fun registerInbound(state: PacketState, id: Int, creator: PacketConstructor) {
        byEncoded.put(encodeInboundLookupKey(state, id), creator)
    }

    @JvmStatic
    private inline fun <reified T> registerOutbound(id: Int) {
        toId.put(T::class.java, id)
    }

    // This encoding places the state in the upper 16 bits, and the ID in the lower 16 bits.
    @JvmStatic
    @Suppress("MagicNumber") // Explained above
    private fun encodeInboundLookupKey(state: PacketState, id: Int): Int = state.ordinal shl 16 or (id and 0xFFFF)

    private fun interface PacketConstructor {

        /**
         * This returns InboundPacket rather than just Packet to try and ensure that all our inbound packets implement this interface.
         */
        fun create(buf: ByteBuf): InboundPacket<*>
    }
}
