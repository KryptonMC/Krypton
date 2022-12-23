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
            .putString("id", KryptonRegistries.ITEM.getKey(ItemTypes.AIR.get().downcast()).asString())
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
