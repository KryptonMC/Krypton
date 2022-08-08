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
package org.kryptonmc.krypton.world.block.predicate

import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.material.Material
import org.kryptonmc.krypton.world.material.Materials
import java.util.function.Predicate

class BlockMaterialPredicate private constructor(private val material: Material) : Predicate<KryptonBlockState?> {

    override fun test(t: KryptonBlockState?): Boolean = t != null && t.material === material

    companion object {

        private val AIR = Predicate<KryptonBlockState?> { it != null && it.isAir }

        @JvmStatic
        fun of(material: Material): Predicate<KryptonBlockState?> = if (material === Materials.AIR) AIR else BlockMaterialPredicate(material)
    }
}
