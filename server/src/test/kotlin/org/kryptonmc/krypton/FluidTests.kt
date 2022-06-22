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
package org.kryptonmc.krypton

import net.kyori.adventure.key.Key
import org.junit.jupiter.api.BeforeAll
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.world.fluid.KryptonFluid
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FluidTests {

    @Test
    fun `test comparisons`() {
        val fluidOne = KryptonFluid.Builder(Key.key("water"), 5, 78).build()
        val fluidTwo = KryptonFluid.Builder(Key.key("lava"), 8, 97).build()
        assertTrue(fluidOne.compare(fluidOne))
        assertFalse(fluidOne.compare(fluidTwo))
        assertTrue(fluidOne.compare(fluidOne, Fluid.Comparator.IDENTITY))
        assertFalse(fluidOne.compare(fluidTwo, Fluid.Comparator.IDENTITY))
        assertTrue(fluidOne.compare(fluidOne, Fluid.Comparator.ID))
        assertFalse(fluidOne.compare(fluidTwo, Fluid.Comparator.ID))
        assertTrue(fluidOne.compare(fluidOne, Fluid.Comparator.STATE))
        assertFalse(fluidOne.compare(fluidTwo, Fluid.Comparator.STATE))
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun bootstrap() {
            Bootstrap.preload()
        }
    }
}
