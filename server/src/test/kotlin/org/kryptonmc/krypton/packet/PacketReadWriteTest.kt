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

import org.junit.jupiter.api.DynamicContainer.dynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.`in`.status.PacketInStatusRequest
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipes
import org.kryptonmc.krypton.testutil.ReflectionsFactory
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.test.assertTrue

class PacketReadWriteTest {

    @TestFactory
    fun `ensure all packets have read constructors`(): DynamicNode =
        dynamicContainer("packets", allPackets { !it.isInterface && !IGNORED_PACKETS.contains(it) }.map {
            dynamicTest(it.simpleName) {
                assertDoesNotThrow { it.getDeclaredConstructor(BinaryReader::class.java) }
            }
        })

    @TestFactory
    fun `ensure all packets implement writable and override write`(): DynamicNode =
        dynamicContainer("packets", allPackets { !it.isInterface }.map {
            dynamicTest(it.simpleName) {
                assertTrue(Writable::class.java.isAssignableFrom(it), "Does not implement Writable!")
                assertDoesNotThrow { it.getDeclaredMethod("write", BinaryWriter::class.java) }
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
