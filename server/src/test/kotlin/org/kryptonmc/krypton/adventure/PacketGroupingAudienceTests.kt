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
package org.kryptonmc.krypton.adventure

import io.mockk.every
import io.mockk.mockk
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.api.InitializerExtension
import org.kryptonmc.krypton.api.Initializers
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.network.chat.ChatTypes
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutCustomSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerChatMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSubtitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleAnimationTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutSetTitleText
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSystemChatMessage
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.FactoryProviderInitializer
import org.kryptonmc.krypton.util.MockPacketGroupingAudience
import org.kryptonmc.krypton.util.RegistryInitializer
import org.kryptonmc.krypton.util.SoundEventInitializer
import org.spongepowered.math.vector.Vector3d
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(InitializerExtension::class)
@Initializers(FactoryProviderInitializer::class, RegistryInitializer::class)
class PacketGroupingAudienceTests {

    private val audience = MockPacketGroupingAudience(setOf(NoActionMember(), NoActionMember()))

    @BeforeEach
    fun resetAudiencePackets() {
        audience.sentPackets.clear()
    }

    @Test
    fun `test send message chooses correct packet`() {
        val message = Component.text("Hello World!")
        audience.sendMessage(Identity.nil(), message, MessageType.SYSTEM)
        assertEquals(1, audience.sentPackets.size)
        val systemChatPacket = PacketOutSystemChatMessage(message, SYSTEM_TYPE)
        assertEquals(systemChatPacket, audience.sentPackets[0])
        resetAudiencePackets()
        val signature = MessageSignature.unsigned()
        audience.sendMessage(Identity.nil(), message, MessageType.CHAT) { signature }
        assertEquals(1, audience.sentPackets.size)
        val sender = ChatSender.fromIdentity(Identity.nil())
        val playerChatPacket = PacketOutPlayerChatMessage(message, null, CHAT_TYPE, sender, signature)
        assertEquals(playerChatPacket, audience.sentPackets[0])
    }

    @Test
    fun `test title part sending`() {
        val title = Component.text("Hello World!")
        val subtitle = Component.text("I am a subtitle!")
        val times = Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(400), Duration.ofMillis(200))
        audience.sendTitlePart(TitlePart.TITLE, title)
        audience.sendTitlePart(TitlePart.SUBTITLE, subtitle)
        audience.sendTitlePart(TitlePart.TIMES, times)
        assertEquals(3, audience.sentPackets.size)
        assertEquals(PacketOutSetTitleText(title), audience.sentPackets[0])
        assertEquals(PacketOutSetSubtitleText(subtitle), audience.sentPackets[1])
        assertEquals(PacketOutSetTitleAnimationTimes(times), audience.sentPackets[2])
        assertThrows<IllegalArgumentException> { audience.sendTitlePart(object : TitlePart<Any> {}, Any()) }
    }

    @Test
    fun `test play sound at location`() {
        val random = ThreadLocalRandom.current()
        val volume = random.nextFloat()
        val pitch = random.nextFloat()
        val seed = random.nextLong()
        val sound = createSound(NO_SOUND, volume, pitch, seed)
        val noTypeSound = createSound(NO_SOUND, volume, pitch, seed)
        val x = random.nextDouble()
        val y = random.nextDouble()
        val z = random.nextDouble()
        audience.playSound(sound, TEST_SOUND_EVENT, x, y, z)
        audience.playSound(noTypeSound, null, x, y, z)
        assertEquals(2, audience.sentPackets.size)
        assertEquals(PacketOutSoundEffect(sound, TEST_SOUND_EVENT, x, y, z), audience.sentPackets[0])
        assertEquals(PacketOutCustomSoundEffect(noTypeSound, x, y, z), audience.sentPackets[1])
    }

    @Test
    fun `test play sound from emitter`() {
        val random = ThreadLocalRandom.current()
        val entityId = random.nextInt()
        val position = Vector3d(random.nextDouble(100.0), random.nextDouble(100.0), random.nextDouble(100.0))
        val volume = random.nextFloat()
        val pitch = random.nextFloat()
        val seed = random.nextLong()
        val sound = createSound(NO_SOUND, volume, pitch, seed)
        val noTypeSound = createSound(NO_SOUND, volume, pitch, seed)
        val emitter = mockk<KryptonEntity> {
            every { id } returns entityId
            every { location } returns position
        }
        audience.playSound(sound, TEST_SOUND_EVENT, emitter)
        audience.playSound(noTypeSound, null, emitter)
        assertEquals(2, audience.sentPackets.size)
        assertEquals(PacketOutEntitySoundEffect(TEST_SOUND_EVENT, sound.source(), entityId, volume, pitch), audience.sentPackets[0])
        assertEquals(PacketOutCustomSoundEffect(noTypeSound, position), audience.sentPackets[1])
        assertThrows<IllegalStateException> { audience.playSound(sound, object : Sound.Emitter {}) }
    }

    private class NoActionMember : NetworkAudienceMember {

        override fun sendPacket(packet: Packet) {
            // Nothing to do
        }
    }

    companion object {

        private const val CHAT_TYPE = 0
        private const val SYSTEM_TYPE = 1
        private val NO_SOUND = Key.key("krypton", "no_sound")
        private val TEST_SOUND_EVENT = KryptonSoundEvent(Key.key("krypton", "test_sound"), 0F)

        @JvmStatic
        private fun createSound(type: Key, volume: Float, pitch: Float, seed: Long): Sound =
            Sound.sound().type(type).source(Sound.Source.PLAYER).volume(volume).pitch(pitch).seed(seed).build()
    }
}
