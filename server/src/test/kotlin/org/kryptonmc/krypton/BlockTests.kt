package org.kryptonmc.krypton

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.BlockFace
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.meta.AttachFace
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.block.property.forEnum
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.world.block.KryptonBlockManager
import org.kryptonmc.krypton.world.block.handler.DummyBlockHandler
import org.kryptonmc.krypton.world.block.property.BooleanProperty
import org.kryptonmc.krypton.world.block.property.EnumProperty
import org.kryptonmc.krypton.world.block.property.IntProperty
import java.lang.reflect.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class BlockTests {

    @Test
    fun `test face opposites`() {
        assertEquals(BlockFace.TOP, BlockFace.BOTTOM.opposite)
        assertEquals(BlockFace.BOTTOM, BlockFace.TOP.opposite)
        assertEquals(BlockFace.SOUTH, BlockFace.NORTH.opposite)
        assertEquals(BlockFace.NORTH, BlockFace.SOUTH.opposite)
        assertEquals(BlockFace.EAST, BlockFace.WEST.opposite)
        assertEquals(BlockFace.WEST, BlockFace.EAST.opposite)
    }

    @Test
    fun `test handler registration and retrieval`() {
        val handler = DummyBlockHandler
        KryptonBlockManager.register(Key.key("air"), handler)
        assertEquals(handler, KryptonBlockManager.handler(Key.key("air")))
    }

    @Test
    fun `cover block constants`() {
        Bootstrap.preload()
        Blocks::class.java.declaredFields.forEach {
            if (!Modifier.isStatic(it.modifiers)) error("Non-static field found in Blocks object!")
            assertNotNull(it.get(null))
        }
    }

    @Test
    fun `test static property constructors`() {
        assertIs<BooleanProperty>(Property.forBoolean("hello_world"))
        assertIs<IntProperty>(Property.forInt("hello_world"))
        assertIs<EnumProperty<*>>(Property.forEnum("hello_world", AttachFace.values()))
    }
}
