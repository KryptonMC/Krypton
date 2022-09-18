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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.kyori.adventure.util.HSVLike
import net.kyori.adventure.util.RGBLike
import org.kryptonmc.api.util.Color
import java.util.function.IntFunction

@JvmRecord
data class KryptonColor(override val value: Int) : Color {

    override fun asRGBLike(): RGBLike = RGBLikeImpl()

    override fun asHSVLike(): HSVLike = HSVLike.fromRGB(red, green, blue)

    private inner class RGBLikeImpl : RGBLike {

        override fun red(): Int = red

        override fun green(): Int = green

        override fun blue(): Int = blue
    }

    object Factory : Color.Factory {

        override fun of(value: Int): Color = VALUES.get(value) ?: KryptonColor(value)

        override fun of(hue: Float, saturation: Float, brightness: Float): Color {
            require(hue in 0F..1F) { "Hue must be between 0 and 1!" }
            require(saturation in 0F..1F) { "Saturation must be between 0 and 1!" }
            require(brightness in 0F..1F) { "Brightness must be between 0 and 1!" }
            return of(java.awt.Color.HSBtoRGB(hue, saturation, brightness))
        }
    }

    companion object {

        private val VALUES = Int2ObjectOpenHashMap<KryptonColor>(512)

        init {
            // Preload the values with some common, small constants
            for (i in -128..127) {
                VALUES.put(i, KryptonColor(i))
            }
        }
    }
}
