/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.packet

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestion
import com.mojang.brigadier.suggestion.Suggestions
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DynamicContainer.dynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.adventure.KryptonAdventureMessage
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.command.argument.ArgumentSignatures
import org.kryptonmc.krypton.coordinate.GlobalPos
import org.kryptonmc.krypton.effect.particle.data.KryptonBlockParticleData
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataSerializers
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.entity.player.RecipeBookSettings
import org.kryptonmc.krypton.entity.player.RecipeBookType
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.network.chat.ChatTypes
import org.kryptonmc.krypton.network.chat.FilterMask
import org.kryptonmc.krypton.network.chat.LastSeenMessages
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.network.chat.RemoteChatSession
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.SignedMessageBody
import org.kryptonmc.krypton.packet.`in`.handshake.PacketInHandshake
import org.kryptonmc.krypton.packet.`in`.login.PacketInEncryptionResponse
import org.kryptonmc.krypton.packet.`in`.login.PacketInLoginStart
import org.kryptonmc.krypton.packet.`in`.login.PacketInPluginResponse
import org.kryptonmc.krypton.packet.`in`.play.PacketInAbilities
import org.kryptonmc.krypton.packet.`in`.play.PacketInChat
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInChatSessionUpdate
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientCommand
import org.kryptonmc.krypton.packet.`in`.play.PacketInClientInformation
import org.kryptonmc.krypton.packet.`in`.play.PacketInCommandSuggestionsRequest
import org.kryptonmc.krypton.packet.`in`.play.PacketInConfirmTeleportation
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
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerOnGround
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerPosition
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerPositionAndRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInSetPlayerRotation
import org.kryptonmc.krypton.packet.`in`.play.PacketInSwingArm
import org.kryptonmc.krypton.packet.`in`.play.PacketInUseItem
import org.kryptonmc.krypton.packet.`in`.play.PacketInUseItemOn
import org.kryptonmc.krypton.packet.`in`.status.PacketInPingRequest
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutEncryptionRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginSuccess
import org.kryptonmc.krypton.packet.out.login.PacketOutPluginRequest
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.EntityAnimations
import org.kryptonmc.krypton.packet.out.play.GameEventTypes
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutAcknowledgeBlockChange
import org.kryptonmc.krypton.packet.out.play.PacketOutAnimation
import org.kryptonmc.krypton.packet.out.play.PacketOutAwardStatistics
import org.kryptonmc.krypton.packet.out.play.PacketOutBlockUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutChatSuggestions
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutCommandSuggestionsResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutCommands
import org.kryptonmc.krypton.packet.out.play.PacketOutDeleteChat
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.packet.out.play.PacketOutDisguisedChat
import org.kryptonmc.krypton.packet.out.play.PacketOutDisplayObjective
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutLogin
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChat
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoRemove
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutRemoveEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetActionBarText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetBlockDestroyStage
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCenterChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerSlot
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCooldown
import org.kryptonmc.krypton.packet.out.play.PacketOutSetDefaultSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHealth
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSubtitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTabListHeaderAndFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleAnimationTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutStopSound
import org.kryptonmc.krypton.packet.out.play.PacketOutSynchronizePlayerPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutSynchronizePlayerPosition.RelativeArgument
import org.kryptonmc.krypton.packet.out.play.PacketOutSystemChat
import org.kryptonmc.krypton.packet.out.play.PacketOutTagQueryResponse
import org.kryptonmc.krypton.packet.out.play.PacketOutTeleportEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEnabledFeatures
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateLight
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateObjectives
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipeBook
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateScore
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTeams
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.packet.out.play.PacketOutWorldEvent
import org.kryptonmc.krypton.packet.out.play.data.ChunkPacketData
import org.kryptonmc.krypton.packet.out.play.data.LightPacketData
import org.kryptonmc.krypton.packet.out.status.PacketOutPingResponse
import org.kryptonmc.krypton.packet.out.status.PacketOutStatusResponse
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.krypton.testutil.ReflectionsFactory
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.world.WorldEvents
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.nbt.ImmutableCompoundTag
import java.nio.ByteBuffer
import java.security.KeyPairGenerator
import java.time.Instant
import java.util.BitSet
import java.util.EnumSet
import java.util.UUID
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PacketReadWriteTest {

    @TestFactory
    fun `ensure all packets have read constructors`(): DynamicNode {
        return dynamicContainer("packets", allPackets { !it.isInterface && !IGNORED_INBOUND_PACKETS.contains(it) }.map {
            dynamicTest(it.simpleName) {
                assertDoesNotThrow { it.getDeclaredConstructor(BinaryReader::class.java) }
            }
        })
    }

    @TestFactory
    fun `ensure packets read and write properly`(): DynamicNode {
        val buffer = ByteBuffer.allocate(65_536)
        val allPackets = Stream.concat(InboundPackets.packets().stream(), OutboundPackets.packets().stream())
        return dynamicContainer("packets", allPackets.map { (packetType, writable) ->
            dynamicTest(packetType.simpleName) {
                val writer = BinaryWriter(buffer)
                assertDoesNotThrow { writable.write(writer) }

                val reader = BinaryReader(buffer.flip())
                val constructor = packetType.getDeclaredConstructor(BinaryReader::class.java)
                assertDoesNotThrow { constructor.newInstance(reader) }

                assertFalse(buffer.hasRemaining())
                buffer.clear()
            }
        })
    }

    @TestFactory
    fun `ensure all packets implement writable and override write`(): DynamicNode {
        return dynamicContainer("packets", allPackets { !it.isInterface }.map {
            dynamicTest(it.simpleName) {
                assertTrue(Writable::class.java.isAssignableFrom(it), "Does not implement Writable!")
                assertDoesNotThrow { it.getDeclaredMethod("write", BinaryWriter::class.java) }
            }
        })
    }

    private object InboundPackets {

        private val SIGNATURE = MessageSignature(ByteArray(256))
        private val LAST_SEEN = LastSeenMessages.Update(82, BitSet(8))
        private val ARGUMENT_SIGNATURES = ArgumentSignatures(listOf(ArgumentSignatures.Entry("hello", SIGNATURE)))
        private val PUBLIC_KEY = KeyPairGenerator.getInstance("RSA").generateKeyPair().public

        private val INSTANCES: List<PacketWrapper> = listOf(
            // Handshake
            PacketInHandshake(0, "localhost", 25565, PacketState.LOGIN),
            // Login
            PacketInEncryptionResponse(ByteArray(16), ByteArray(16)),
            PacketInLoginStart("test", UUID.randomUUID()),
            PacketInPluginResponse(37, ByteArray(16)),
            // Play
            PacketInAbilities(true),
            PacketInChat("Hello world!", Instant.now(), Random.nextLong(), SIGNATURE, LAST_SEEN),
            PacketInChatCommand("/hello_world", Instant.now(), Random.nextLong(), ARGUMENT_SIGNATURES, LAST_SEEN),
            PacketInChatSessionUpdate(RemoteChatSession.Data(UUID.randomUUID(), PlayerPublicKey.Data(Instant.now(), PUBLIC_KEY, ByteArray(16)))),
            PacketInClientCommand(PacketInClientCommand.Action.PERFORM_RESPAWN),
            PacketInClientInformation("en_gb", 8, ChatVisibility.FULL, true, 127, MainHand.LEFT, false, false),
            PacketInCommandSuggestionsRequest(34, "/hello_world"),
            PacketInConfirmTeleportation(72),
            PacketInInteract(87, PacketInInteract.AttackAction, true),
            PacketInInteract(87, PacketInInteract.InteractAction(Hand.OFF), true),
            PacketInInteract(87, PacketInInteract.InteractAtAction(3F, 4F, 5F, Hand.OFF), true),
            PacketInKeepAlive(System.currentTimeMillis()),
            PacketInPlayerAction(PacketInPlayerAction.Action.FINISH_DIGGING, Vec3i(3, 4, 5), Direction.NORTH, Random.nextInt()),
            PacketInPlayerCommand(94, PacketInPlayerCommand.Action.START_SNEAKING, Random.nextInt()),
            PacketInPlayerInput(3F, 4F, 31),
            PacketInPluginMessage(Key.key("krypton", "test"), ByteArray(16)),
            PacketInQueryEntityTag(974, 81),
            PacketInResourcePack(ResourcePack.Status.FAILED_DOWNLOAD),
            PacketInSetCreativeModeSlot(3, KryptonItemStack(ItemTypes.ACACIA_BUTTON.get())),
            PacketInSetHeldItem(5),
            PacketInSetPlayerOnGround(true),
            PacketInSetPlayerPosition(3.5, 4.7, 8.2, true),
            PacketInSetPlayerPositionAndRotation(3.5, 4.7, 8.2, 39F, 87F, true),
            PacketInSetPlayerRotation(39F, 87F, true),
            PacketInSwingArm(Hand.OFF),
            PacketInUseItem(Hand.OFF, Random.nextInt()),
            PacketInUseItemOn(Hand.OFF, BlockHitResult(Vec3d(8.2, 9.1, 1.3), Direction.NORTH, Vec3i(12, 27, 45), false), Random.nextInt()),
            // Status
            PacketInPingRequest(System.currentTimeMillis())
        ).map { packet -> PacketWrapper(packet.javaClass) { writer -> packet.write(writer) } }

        fun packets(): List<PacketWrapper> = INSTANCES
    }

    private object OutboundPackets {

        private val SOUND = KryptonRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ALLAY_DEATH.get())
        private val PARTICLE_TYPE = KryptonRegistries.PARTICLE_TYPE.getId(ParticleTypes.BLOCK.get())
        private val PARTICLE_TYPE_DATA = KryptonBlockParticleData(KryptonBlocks.ACACIA_BUTTON.defaultState)
        private val SIGNATURE = MessageSignature(ByteArray(256))
        private val LAST_SEEN = LastSeenMessages.Packed(listOf(MessageSignature.Packed(SIGNATURE)))
        private val PUBLIC_KEY = KeyPairGenerator.getInstance("RSA").generateKeyPair().public
        private val LIGHT_DATA = LightPacketData(
            true,
            BitSet(16),
            BitSet(16),
            BitSet(16),
            BitSet(16),
            listOf(ByteArray(16), ByteArray(16), ByteArray(16), ByteArray(16)),
            listOf(ByteArray(16), ByteArray(16), ByteArray(16), ByteArray(16))
        )

        private val INSTANCES: List<PacketWrapper> = listOf(
            // Login
            PacketOutEncryptionRequest("test", ByteArray(16), ByteArray(16)),
            PacketOutLoginDisconnect(Component.text("Get out of my server")),
            PacketOutLoginSuccess(UUID.randomUUID(), "test", listOf(KryptonProfileProperty("test", "test", "test"))),
            PacketOutPluginRequest(37, "krypton:test", ByteArray(16)),
            PacketOutSetCompression(128),
            // Play
            PacketOutAbilities(true, true, false, false, 0.05F, 0.1F),
            PacketOutAcknowledgeBlockChange(Random.nextInt()),
            PacketOutAnimation(87, EntityAnimations.SWING_MAIN_ARM),
            PacketOutAwardStatistics(Object2IntOpenHashMap(mapOf(StatisticTypes.BLOCK_MINED.get().getStatistic(Blocks.ACACIA_BUTTON.get()) to 3))),
            PacketOutBlockUpdate(Vec3i(3, 4, 5), KryptonBlocks.ACACIA_BUTTON.defaultState),
            PacketOutBossBar(
                UUID.randomUUID(),
                PacketOutBossBar.AddAction(Component.text("Hello world!"), 0.5F, BossBar.Color.PINK, BossBar.Overlay.NOTCHED_20, 7)
            ),
            PacketOutBossBar(UUID.randomUUID(), PacketOutBossBar.RemoveAction),
            PacketOutBossBar(UUID.randomUUID(), PacketOutBossBar.UpdateProgressAction(0.7F)),
            PacketOutBossBar(UUID.randomUUID(), PacketOutBossBar.UpdateNameAction(Component.text("Goodbye world!"))),
            PacketOutBossBar(UUID.randomUUID(), PacketOutBossBar.UpdateStyleAction(BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS)),
            PacketOutBossBar(UUID.randomUUID(), PacketOutBossBar.UpdateFlagsAction(3)),
            PacketOutChangeDifficulty(Difficulty.NORMAL, true),
            PacketOutChatSuggestions(PacketOutChatSuggestions.Action.SET, listOf("hello", "world")),
            PacketOutChunkDataAndLight(
                3,
                4,
                ChunkPacketData(
                    ImmutableCompoundTag.builder()
                        .putLongArray("WORLD_SURFACE", LongArray(16))
                        .putLongArray("MOTION_BLOCKING", LongArray(16))
                        .build(),
                    ByteArray(256)
                ),
                LIGHT_DATA
            ),
            PacketOutClearTitles(true),
            PacketOutCommands(listOf(
                PacketOutCommands.Node(0, intArrayOf(1, 2, 3), 0, null),
                PacketOutCommands.Node(13, intArrayOf(3, 4, 5), 25, PacketOutCommands.LiteralNodeData("hello")),
                PacketOutCommands.Node(
                    30,
                    intArrayOf(7, 8, 9),
                    0,
                    PacketOutCommands.ArgumentNodeData("world", StringArgumentType.word(), Key.key("krypton", "test"))
                )
            ), 22),
            PacketOutCommandSuggestionsResponse(37, Suggestions(
                StringRange.between(0, 10),
                listOf(
                    Suggestion(StringRange.between(0, 5), "hello", KryptonAdventureMessage(Component.text("Hey!"))),
                    Suggestion(StringRange.between(5, 10), "world", KryptonAdventureMessage(Component.text("Hey!")))
                )
            )),
            PacketOutDeleteChat(MessageSignature.Packed(74)),
            PacketOutDeleteChat(MessageSignature.Packed(MessageSignature(ByteArray(256)))),
            PacketOutDisconnect(Component.text("Get out of my server")),
            PacketOutDisguisedChat(Component.text("Disguised"), RichChatType.bind(ChatTypes.SAY_COMMAND, Component.text("Dave")).toNetwork()),
            PacketOutDisplayObjective(5, "test"),
            PacketOutEntityEvent(87, 33),
            PacketOutEntitySoundEffect(SOUND, Sound.Source.MASTER, 87, 3.5F, 4.7F, Random.nextLong()),
            PacketOutGameEvent(GameEventTypes.BEGIN_RAINING, 1.3F),
            PacketOutInitializeWorldBorder(3.7, 9.2, 1.4, 72.8, 972L, 30_000_000, 273, 887),
            PacketOutKeepAlive(Random.nextLong()),
            PacketOutLogin(
                87,
                false,
                GameMode.CREATIVE,
                GameMode.SURVIVAL,
                setOf(World.OVERWORLD, World.NETHER, World.END),
                KryptonDynamicRegistries.DynamicHolder,
                KryptonDynamicRegistries.DIMENSION_TYPE.getResourceKey(KryptonDimensionTypes.OVERWORLD)!!,
                World.OVERWORLD,
                Random.nextLong(),
                50,
                16,
                8,
                true,
                true,
                false,
                true,
                GlobalPos(World.OVERWORLD, Vec3i(3, 4, 5))
            ),
            PacketOutOpenBook(Hand.OFF),
            PacketOutParticle(PARTICLE_TYPE, true, 3.5, 4.7, 8.2, 0.4F, 0.8F, 0.1F, 0.73F, 5, PARTICLE_TYPE_DATA),
            PacketOutPlayerChat(
                UUID.randomUUID(),
                17,
                SIGNATURE,
                SignedMessageBody.Packed("test", Instant.now(), Random.nextLong(), LAST_SEEN),
                Component.text("test modified"),
                FilterMask.FULLY_FILTERED,
                RichChatType.bind(ChatTypes.CHAT, Component.text("Dave")).toNetwork()
            ),
            PacketOutPlayerInfoRemove(listOf(UUID.randomUUID(), UUID.randomUUID())),
            PacketOutPlayerInfoUpdate(
                EnumSet.of(PacketOutPlayerInfoUpdate.Action.ADD_PLAYER, PacketOutPlayerInfoUpdate.Action.INITIALIZE_CHAT),
                listOf(PacketOutPlayerInfoUpdate.Entry(
                    UUID.randomUUID(),
                    KryptonGameProfile.basic(UUID.randomUUID(), "Dave"),
                    true,
                    Random.nextInt(4000),
                    GameMode.ADVENTURE,
                    Component.text("Dave"),
                    RemoteChatSession.Data(UUID.randomUUID(), PlayerPublicKey.Data(Instant.now(), PUBLIC_KEY, ByteArray(16)))
                ))
            ),
            PacketOutPluginMessage(Key.key("krypton", "test"), ByteArray(16)),
            PacketOutRemoveEntities(intArrayOf(3, 4, 5)),
            PacketOutResourcePack("http://localhost:80/test.zip", "abcdefghijkl", true, Component.text("Accept the texture pack or else")),
            PacketOutSetActionBarText(Component.text("Hello world!")),
            PacketOutSetBlockDestroyStage(87, Vec3i(3, 4, 5), 3),
            PacketOutSetCamera(87),
            PacketOutSetCenterChunk(3, 5),
            PacketOutSetContainerContent(
                7,
                42,
                listOf(KryptonItemStack(ItemTypes.ANVIL.get()), KryptonItemStack(ItemTypes.BLACK_WOOL.get())),
                KryptonItemStack(ItemTypes.BELL.get())
            ),
            PacketOutSetContainerSlot(7, 42, 3, KryptonItemStack(ItemTypes.BEACON.get())),
            PacketOutSetCooldown(ItemTypes.ENDER_PEARL.get().downcast(), 50),
            PacketOutSetDefaultSpawnPosition(Vec3i(3, 4, 5), 97.34F),
            PacketOutSetEntityMetadata(87, listOf(MetadataHolder.Entry(MetadataKey(5, MetadataSerializers.BLOCK_POS), Vec3i(3, 4, 5)))),
            PacketOutSetEntityVelocity(87, 3, 4, 5),
            PacketOutSetHeadRotation(87, 43.8F),
            PacketOutSetHealth(19.87F, 34, 5.6F),
            PacketOutSetHeldItem(8),
            PacketOutSetPassengers(87, intArrayOf(3, 4, 5)),
            PacketOutSetSubtitleText(Component.text("Hello world!")),
            PacketOutSetTabListHeaderAndFooter(Component.text("Header"), Component.text("Footer")),
            PacketOutSetTitleAnimationTimes(40, 50, 60),
            PacketOutSetTitleText(Component.text("Hello world!")),
            PacketOutSoundEffect(SOUND, Sound.Source.MASTER, 3, 4, 5, 6.7F, 8.9F, Random.nextLong()),
            PacketOutSpawnEntity(87, UUID.randomUUID(), KryptonEntityTypes.SKELETON, 3.4, 5.6, 7.8, 9, 4, 1, 72, 3, 4, 5),
            PacketOutSpawnExperienceOrb(87, 3.4, 5.6, 7.8, 94),
            PacketOutSpawnPlayer(87, UUID.randomUUID(), 3.4, 5.6, 7.8, 9, 4),
            PacketOutStopSound(Sound.Source.MUSIC, Key.key("krypton", "test")),
            PacketOutSynchronizePlayerPosition(3.4, 5.6, 7.8, 9.4F, 4.1F, setOf(RelativeArgument.Y), 872, false),
            PacketOutSystemChat(Component.text("Hello world!"), false),
            PacketOutTagQueryResponse(891, ImmutableCompoundTag.builder().build()),
            PacketOutTeleportEntity(87, 3.4, 5.6, 7.8, 9, 4, true),
            PacketOutUnloadChunk(3, 5),
            PacketOutUpdateAttributes(87, listOf(PacketOutUpdateAttributes.AttributeSnapshot(
                KryptonAttributeTypes.FLYING_SPEED,
                87.5,
                setOf(KryptonAttributeModifier(UUID.randomUUID(), "test", 8.8, BasicModifierOperation.MULTIPLY_BASE))
            ))),
            PacketOutUpdateEnabledFeatures(setOf(Key.key("krypton", "test"), Key.key("krypton", "test2"))),
            PacketOutUpdateEntityPosition(87, 3, 4, 5, true),
            PacketOutUpdateEntityPositionAndRotation(87, 3, 4, 5, 9.4F, 4.1F, true),
            PacketOutUpdateEntityRotation(87, 9.4F, 4.1F, true),
            PacketOutUpdateLight(3, 5, LIGHT_DATA),
            PacketOutUpdateObjectives("test", PacketOutUpdateObjectives.Actions.UPDATE_TEXT, Component.text("Hello world!"), 1),
            PacketOutUpdateRecipeBook(
                PacketOutUpdateRecipeBook.Action.ADD,
                listOf(Key.key("krypton", "test")),
                listOf(Key.key("krypton", "test2")),
                RecipeBookSettings().apply {
                    setOpen(RecipeBookType.CRAFTING, true)
                    setFiltered(RecipeBookType.BLAST_FURNACE, true)
                }
            ),
            PacketOutUpdateScore("test", PacketOutUpdateScore.Actions.CREATE_OR_UPDATE, "test2", 982),
            // TODO: Tags packet here
            PacketOutUpdateTeams(
                "test",
                PacketOutUpdateTeams.Action.ADD_MEMBERS,
                PacketOutUpdateTeams.Parameters(Component.text("Test"), 3, "never", "always", 5, Component.text("ab"), Component.text("cd")),
                listOf(Component.text("Hello world!"))
            ),
            PacketOutUpdateTime(Random.nextLong(), Random.nextLong()),
            PacketOutWorldEvent(WorldEvents.ANVIL_USED, Vec3i(3, 4, 5), 47, true),
            // Status
            PacketOutPingResponse(Random.nextLong()),
            PacketOutStatusResponse.create(ServerStatus(
                Component.text("Hello world!"),
                ServerStatus.Players(30, 4, arrayOf(KryptonGameProfile.basic(UUID.randomUUID(), "test"))),
                "abcdefghijkl"
            ))
        ).map { PacketWrapper(it.javaClass, it) }

        fun packets(): List<PacketWrapper> = INSTANCES
    }

    private data class PacketWrapper(val packetType: Class<*>, val writable: Writable)

    companion object {

        private val INBOUND_PACKET_TYPES = findPackets("in")
        private val OUTBOUND_PACKET_TYPES = findPackets("out")
        private val IGNORED_INBOUND_PACKETS = listOf(PacketInStatusRequest::class.java, PacketOutUpdateRecipes::class.java)

        @JvmStatic
        @BeforeAll
        fun `setup registries`() {
            Bootstrapping.loadFactories()
            Bootstrapping.loadRegistries()
            Bootstrapping.loadOtherClasses()
        }

        @JvmStatic
        private fun findPackets(subPackage: String): Set<Class<*>> {
            return ReflectionsFactory.subTypesIn("org.kryptonmc.krypton.packet.$subPackage").getSubTypesOf(Packet::class.java)
        }

        @JvmStatic
        private fun allPackets(filter: Predicate<Class<*>>): Stream<Class<*>> {
            return Stream.concat(INBOUND_PACKET_TYPES.stream(), OUTBOUND_PACKET_TYPES.stream()).filter(filter)
        }
    }
}
