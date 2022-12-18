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
import org.junit.jupiter.api.DynamicContainer.dynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipes
import org.kryptonmc.krypton.testutil.ReflectionsFactory
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.test.assertTrue

class PacketReadWriteTests {

    @TestFactory
    fun `ensure all packets have read constructors`(): DynamicNode =
        dynamicContainer("packets", allPackets { !it.isInterface && !IGNORED_PACKETS.contains(it) }.map {
            dynamicTest(it.simpleName) {
                assertDoesNotThrow { it.getDeclaredConstructor(ByteBuf::class.java) }
            }
        })

    @TestFactory
    fun `ensure all packets implement writable and override write`(): DynamicNode =
        dynamicContainer("packets", allPackets { !it.isInterface }.map {
            dynamicTest(it.simpleName) {
                assertTrue(Writable::class.java.isAssignableFrom(it), "Does not implement Writable!")
                assertDoesNotThrow { it.getDeclaredMethod("write", ByteBuf::class.java) }
            }
        })

    companion object {

        private val INBOUND_PACKETS = findPackets("in")
        private val OUTBOUND_PACKETS = findPackets("out")
        private val IGNORED_PACKETS = listOf(PacketInStatusRequest::class.java, PacketOutUpdateRecipes::class.java)

        @JvmStatic
        private fun findPackets(subPackage: String): Set<Class<*>> =
            ReflectionsFactory.subTypesIn("org.kryptonmc.krypton.packet.$subPackage").getSubTypesOf(Packet::class.java)

        @JvmStatic
        private fun allPackets(filter: Predicate<Class<*>>): Stream<Class<*>> =
            Stream.concat(INBOUND_PACKETS.stream(), OUTBOUND_PACKETS.stream()).filter(filter)
    }
}
