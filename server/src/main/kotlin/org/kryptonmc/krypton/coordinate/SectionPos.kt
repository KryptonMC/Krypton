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
package org.kryptonmc.krypton.coordinate

import org.kryptonmc.krypton.util.math.Maths

class SectionPos private constructor(val x: Int, val y: Int, val z: Int) {

    companion object {

        private const val SECTION_BITS = 4
        private const val SECTION_MASK = 15

        @JvmStatic
        fun blockToSection(value: Int): Int = value shr SECTION_BITS

        @JvmStatic
        fun blockToSection(value: Double): Int = Maths.floor(value) shr SECTION_BITS

        @JvmStatic
        fun sectionToBlock(value: Int): Int = value shl SECTION_BITS

        @JvmStatic
        fun sectionRelative(value: Int): Int = value and SECTION_MASK
    }
}
