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
package org.kryptonmc.krypton.item

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.nbt.ImmutableCompoundTag
import kotlin.test.assertEquals

class ItemStackTest {

    @Test
    fun `ensure empty stack serializes correctly`() {
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
