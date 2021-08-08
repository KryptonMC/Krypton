/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.brigadier.StringReader
import org.kryptonmc.api.block.BoundingBox

fun String.toBoundingBoxList(): List<BoundingBox> {
    if (isEmpty()) return emptyList()
    val boxes = mutableListOf<BoundingBox>()
    val reader = StringReader(this)
    if (reader.read() != '[' || reader.peek() == ']') return emptyList()
    while (reader.canRead()) {
        boxes += reader.readBoundingBox()
        if (reader.peek() == ']') break
        reader.expect(", ")
    }
    return boxes
}

private fun StringReader.readBoundingBox(): BoundingBox {
    expect("AABB[")
    val minX = readDouble()
    expect(", ")
    val minY = readDouble()
    expect(", ")
    val minZ = readDouble()
    expect("] -> [")
    val maxX = readDouble()
    expect(", ")
    val maxY = readDouble()
    expect(", ")
    val maxZ = readDouble()
    expect("]")
    return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
}
