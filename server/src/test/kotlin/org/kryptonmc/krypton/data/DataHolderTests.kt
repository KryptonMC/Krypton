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
package org.kryptonmc.krypton.data

import net.kyori.adventure.text.Component
import org.junit.jupiter.api.BeforeAll
import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.MutableDataProvider
import org.kryptonmc.krypton.util.Bootstrap
import kotlin.test.Test
import kotlin.test.assertEquals

class DataHolderTests {

    @Test
    fun `test getting with immutable and mutable`() {
        var name: Component = Component.text("hello world!")
        val provider = MutableDataProvider.Builder(Keys.CUSTOM_NAME, DummyMutableDataHolder::class.java)
            .get { name }
            .set { _, value -> name = value }
            .build()
        val holder = DummyMutableDataHolder(setOf(Keys.CUSTOM_NAME)).addProvider(provider)
        assertEquals(name, holder[Keys.CUSTOM_NAME])
        val newName = Component.text("goodbye world!")
        holder[Keys.CUSTOM_NAME] = newName
        assertEquals(newName, holder[Keys.CUSTOM_NAME])
    }

    companion object {

        @BeforeAll
        @JvmStatic
        fun `preload bootstrap`() {
            Bootstrap.preload()
        }
    }
}
