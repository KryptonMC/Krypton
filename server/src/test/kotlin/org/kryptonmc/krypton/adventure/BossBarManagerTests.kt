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

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.BeforeEach
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import org.kryptonmc.krypton.util.MockPacketGroupingAudience
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Consumer
import kotlin.properties.Delegates
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BossBarManagerTests {

    private var bar by Delegates.notNull<BossBar>()
    private var holder by Delegates.notNull<BossBarManager.BossBarHolder>()

    @Test
    fun `test add bar`() {
        var packetSent = false
        val member = createMember<PacketOutBossBar.AddAction> {
            packetSent = true
            assertEquals(bar.name(), it.name)
            assertEquals(bar.progress(), it.progress)
            assertEquals(bar.color(), it.color)
            assertEquals(bar.overlay(), it.overlay)
            assertEquals(bar.flags(), decodeFlags(it.flags))
        }
        BossBarManager.addBar(bar, member)
        assertTrue(packetSent, "Packet was not sent!")
        packetSent = false
        holder.subscribers.add(member)
        BossBarManager.addBar(bar, member)
        @Suppress("KotlinConstantConditions") // This would still be set with the player receiving a packet
        assertFalse(packetSent, "Packet was sent!")
    }

    @Test
    fun `test add bar grouped`() {
        val audience = MockPacketGroupingAudience(setOf(NoActionMember(), NoActionMember()))
        BossBarManager.addBar(bar, audience)
        assertEquals(1, audience.sentPackets.size)
        val expectedPacket = PacketOutBossBar(holder.id, PacketOutBossBar.AddAction(bar))
        assertEquals(expectedPacket, audience.sentPackets[0])
        BossBarManager.addBar(bar, audience)
        assertEquals(1, audience.sentPackets.size) // No further packets should have been sent
    }

    @Test
    fun `test remove bar`() {
        var packetSent = false
        val member = createMember<PacketOutBossBar.RemoveAction> { packetSent = true }
        holder.subscribers.add(member)
        BossBarManager.removeBar(bar, member)
        assertTrue(packetSent, "Packet was not sent!")
        packetSent = false
        BossBarManager.removeBar(bar, member)
        @Suppress("KotlinConstantConditions") // This would still be set with the player receiving a packet
        assertFalse(packetSent, "Packet was sent!")
    }

    @Test
    fun `test remove bar grouped`() {
        val members = setOf(NoActionMember(), NoActionMember())
        val audience = MockPacketGroupingAudience(members)
        holder.subscribers.addAll(members)
        BossBarManager.removeBar(bar, audience)
        assertEquals(1, audience.sentPackets.size)
        val expectedPacket = PacketOutBossBar(holder.id, PacketOutBossBar.RemoveAction)
        assertEquals(expectedPacket, audience.sentPackets[0])
        BossBarManager.removeBar(bar, audience)
        assertEquals(1, audience.sentPackets.size) // No further packets should have been sent
    }

    @Test
    fun `test name update`() {
        val name = Component.text("My shiny new name!")
        var packetSent = false
        val member = createMember<PacketOutBossBar.UpdateTitleAction> {
            packetSent = true
            assertEquals(name, it.name)
        }
        holder.subscribers.add(member)
        bar.name(name)
        assertTrue(packetSent, "Packet was not sent!")
    }

    @Test
    fun `test progress update`() {
        val progress = ThreadLocalRandom.current().nextFloat()
        var packetSent = false
        val member = createMember<PacketOutBossBar.UpdateProgressAction> {
            packetSent = true
            assertEquals(progress, it.progress)
        }
        holder.subscribers.add(member)
        bar.progress(progress)
        assertTrue(packetSent, "Packet was not sent!")
    }

    @Test
    fun `test color update`() {
        var packetSent = false
        val member = createMember<PacketOutBossBar.UpdateStyleAction> {
            packetSent = true
            assertEquals(BossBar.Color.PURPLE, it.color)
        }
        holder.subscribers.add(member)
        bar.color(BossBar.Color.PURPLE)
        assertTrue(packetSent, "Packet was not sent!")
    }

    @Test
    fun `test overlay update`() {
        var packetSent = false
        val member = createMember<PacketOutBossBar.UpdateStyleAction> {
            packetSent = true
            assertEquals(BossBar.Overlay.NOTCHED_12, it.overlay)
        }
        holder.subscribers.add(member)
        bar.overlay(BossBar.Overlay.NOTCHED_12)
        assertTrue(packetSent, "Packet was not sent!")
    }

    @Test
    fun `test flags update`() {
        var packetSent = false
        val member = createMember<PacketOutBossBar.UpdateFlagsAction> {
            packetSent = true
            assertTrue(it.flags and 1 != 0)
            assertTrue(it.flags and 2 != 0)
            assertTrue(it.flags and 4 == 0)
        }
        holder.subscribers.add(member)
        bar.removeFlag(BossBar.Flag.CREATE_WORLD_FOG)
        assertTrue(packetSent, "Packet was not sent!")
    }

    @BeforeEach
    fun `reset bar and holder`() {
        bar = BossBar.bossBar(Component.text("Test"), 0.7F, BossBar.Color.PINK, BossBar.Overlay.PROGRESS)
            .addFlag(BossBar.Flag.DARKEN_SCREEN)
            .addFlag(BossBar.Flag.PLAY_BOSS_MUSIC)
            .addFlag(BossBar.Flag.CREATE_WORLD_FOG)
        holder = BossBarManager.getOrCreate(bar)
    }

    private inline fun <reified A : PacketOutBossBar.Action> createMember(handler: Consumer<A>): MockMember<A> = MockMember(A::class.java, handler)

    private inner class MockMember<A : PacketOutBossBar.Action>(
        private val actionType: Class<*>,
        private val handler: Consumer<A>
    ) : NetworkAudienceMember {

        @Suppress("UNCHECKED_CAST")
        override fun sendPacket(packet: Packet) {
            assertIs<PacketOutBossBar>(packet, "Expected packet to be a boss bar packet!")
            assertEquals(actionType, packet.action.javaClass, "Expected packet action to be $actionType!")
            assertEquals(holder.id, packet.uuid, "Expected packet boss bar ID to be ${holder.id}, was ${packet.uuid}!")
            handler.accept(packet.action as A)
        }
    }

    private class NoActionMember : NetworkAudienceMember {

        override fun sendPacket(packet: Packet) {
            // Does nothing
        }
    }

    companion object {

        @JvmStatic
        private fun decodeFlags(flags: Int): Set<BossBar.Flag> = buildSet {
            if (flags and 1 != 0) add(BossBar.Flag.DARKEN_SCREEN)
            if (flags and 2 != 0) add(BossBar.Flag.PLAY_BOSS_MUSIC)
            if (flags and 4 != 0) add(BossBar.Flag.CREATE_WORLD_FOG)
        }
    }
}
