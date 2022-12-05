package org.kryptonmc.krypton.item

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.nbt.ImmutableCompoundTag
import kotlin.test.assertEquals

class ItemStackTests {

    @Test
    fun `test empty stack serialization`() {
        val stack = KryptonItemStack.EMPTY
        val serialized = ImmutableCompoundTag.builder()
            .putString("id", KryptonRegistries.ITEM.getKey(ItemTypes.AIR.downcast()).asString())
            .putInt("Count", 1)
            .put("tag", stack.meta.data)
            .build()
        assertEquals(serialized, stack.save())
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load factories and registries`() {
            Bootstrapping.loadFactories()
            Bootstrapping.loadRegistries()
        }
    }
}
