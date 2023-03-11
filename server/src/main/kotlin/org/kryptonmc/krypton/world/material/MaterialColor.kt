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
package org.kryptonmc.krypton.world.material

import org.kryptonmc.api.item.data.DyeColor
import java.util.EnumMap

@JvmRecord
data class MaterialColor(val id: Int, val color: Int) {

    init {
        VALUES[checkIdInRange(id)] = this
    }

    companion object {

        private const val TOTAL_COLORS = 64
        private val VALUES = arrayOfNulls<MaterialColor>(TOTAL_COLORS)
        // If we have this be a field, we get an initialization cycle that leads to the entries being set to null
        // Since MaterialColors requires the loading of MaterialColor, this class is always loaded first, before MaterialColors is loaded, so all
        // the constants will always be null when we try to access them.
        // Therefore, we must lazy init the map so it initializes after MaterialColors.
        private val BY_DYE_COLOR by lazy { initializeDyeColorMap() }

        @JvmStatic
        fun fromId(id: Int): MaterialColor = VALUES[checkIdInRange(id)] ?: MaterialColors.NONE

        @JvmStatic
        fun fromDyeColor(color: DyeColor): MaterialColor =
            checkNotNull(BY_DYE_COLOR.get(color)) { "Could not find material colour for dye colour $color! This is a bug!" }

        @JvmStatic
        private fun checkIdInRange(id: Int): Int {
            if (id < 0 || id >= TOTAL_COLORS) throw IndexOutOfBoundsException("Material colour ID must be between 0 and $TOTAL_COLORS!")
            return id
        }

        @JvmStatic
        private fun initializeDyeColorMap(): Map<DyeColor, MaterialColor> = EnumMap<_, MaterialColor>(DyeColor::class.java).apply {
            put(DyeColor.WHITE, MaterialColors.SNOW)
            put(DyeColor.ORANGE, MaterialColors.COLOR_ORANGE)
            put(DyeColor.MAGENTA, MaterialColors.COLOR_MAGENTA)
            put(DyeColor.LIGHT_BLUE, MaterialColors.COLOR_LIGHT_BLUE)
            put(DyeColor.YELLOW, MaterialColors.COLOR_YELLOW)
            put(DyeColor.LIME, MaterialColors.COLOR_LIGHT_GREEN)
            put(DyeColor.PINK, MaterialColors.COLOR_PINK)
            put(DyeColor.GRAY, MaterialColors.COLOR_GRAY)
            put(DyeColor.LIGHT_GRAY, MaterialColors.COLOR_LIGHT_GRAY)
            put(DyeColor.CYAN, MaterialColors.COLOR_CYAN)
            put(DyeColor.PURPLE, MaterialColors.COLOR_PURPLE)
            put(DyeColor.BLUE, MaterialColors.COLOR_BLUE)
            put(DyeColor.BROWN, MaterialColors.COLOR_BROWN)
            put(DyeColor.GREEN, MaterialColors.COLOR_GREEN)
            put(DyeColor.RED, MaterialColors.COLOR_RED)
            put(DyeColor.BLACK, MaterialColors.COLOR_BLACK)
        }
    }
}
