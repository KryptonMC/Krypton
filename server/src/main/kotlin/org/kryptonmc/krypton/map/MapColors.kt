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
package org.kryptonmc.krypton.map

import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.api.map.color.MapColor

object MapColors {

    private val COLORS = MapColor.values()
    private val COLORS_BY_ENCODED_ID = Byte2ObjectOpenHashMap<MapColor>().apply { COLORS.forEach { put(encode(it), it) } }
    private val COLORS_BY_RGB = Int2ObjectOpenHashMap<MapColor>().apply { COLORS.forEach { put(it.color.rgb, it) } }

    @JvmStatic
    fun fromId(id: Int): MapColor = COLORS[id]

    @JvmStatic
    fun fromEncoded(value: Int): MapColor = requireNotNull(COLORS_BY_ENCODED_ID.get(value.toByte())) {
        "Could not find map colour for encoded value $value!"
    }

    @JvmStatic
    fun encode(color: MapColor): Byte = ((color.type.ordinal shl 2) or (color.brightness.ordinal and 3)).toByte()

    /**
     * This uses a lazy evaluation strategy, where it checks if there is
     * already a mapped RGB colour before trying to interpolate the RGB value
     * to its nearest neighbour.
     *
     * Because of the way the evaluation works, in the event that there are two
     * possible neighbours (the value is exactly in the middle of two possible
     * colours), it will always pick the lowest of the two, as that is the one
     * that will appear first in the array of [MapColor] values.
     *
     * The evaluation is taken from Minestom. Original source can be found here:
     * https://github.com/Minestom/Minestom/blob/ab5734334c75c18708d21ffe36ceb55c9aefa793/src/main/java/net/minestom/server/map/MapColors.java#L229
     */
    @JvmStatic
    fun fromRGB(value: Int): MapColor {
        val color = COLORS_BY_RGB.get(value)
        if (color != null) return color
        var closest: MapColor? = null
        var closestDistance = Int.MAX_VALUE
        COLORS.forEach {
            val rgbRed = (value shr 16) and 0xFF
            val rgbGreen = (value shr 8) and 0xFF
            val rgbBlue = value and 0xFF
            val redDistance = rgbRed - it.color.red
            val greenDistance = rgbGreen - it.color.green
            val blueDistance = rgbBlue - it.color.blue
            val distance = redDistance * redDistance + greenDistance * greenDistance + blueDistance * blueDistance
            if (distance < closestDistance) {
                closest = it
                closestDistance = distance
            }
        }
        COLORS_BY_RGB.put(value, closest)
        return requireNotNull(closest) { "Could not interpolate $value to a valid MapColor! This should not occur!" }
    }
}
