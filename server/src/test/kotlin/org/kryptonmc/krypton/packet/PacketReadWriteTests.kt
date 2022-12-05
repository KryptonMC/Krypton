package org.kryptonmc.krypton.packet

import io.netty.buffer.ByteBuf
import org.junit.jupiter.api.DynamicContainer.dynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.testutil.ReflectionsFactory
import java.util.stream.Stream
import kotlin.test.assertTrue

class PacketReadWriteTests {

    /* FIXME: Re-enable when the special packets that do not have read constructors are fixed
    @TestFactory
    fun `ensure all packets have read constructors`(): DynamicNode =
        dynamicContainer("packets", Stream.concat(INBOUND_PACKETS.stream(), OUTBOUND_PACKETS.stream()).filter { !it.isInterface }.map {
            dynamicTest(it.simpleName) {
                assertDoesNotThrow { it.getDeclaredConstructor(ByteBuf::class.java) }
            }
        })
     */

    @TestFactory
    fun `ensure all packets implement writable and override write`(): DynamicNode =
        dynamicContainer("packets", Stream.concat(INBOUND_PACKETS.stream(), OUTBOUND_PACKETS.stream()).filter { !it.isInterface }.map {
            dynamicTest(it.simpleName) {
                assertTrue(Writable::class.java.isAssignableFrom(it), "Does not implement Writable!")
                assertDoesNotThrow { it.getDeclaredMethod("write", ByteBuf::class.java) }
            }
        })

    companion object {

        private val INBOUND_PACKETS = findPackets("in")
        private val OUTBOUND_PACKETS = findPackets("out")

        @JvmStatic
        private fun findPackets(subPackage: String): Set<Class<*>> =
            ReflectionsFactory.subTypesIn("org.kryptonmc.krypton.packet.$subPackage").getSubTypesOf(Packet::class.java)
    }
}
