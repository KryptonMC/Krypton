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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DSL.remainderType
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.OpticFinder
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.List.ListType
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.isPowerOfTwo
import java.util.Arrays
import kotlin.math.max

class AlignBitStorageFix(outputSchema: Schema) : DataFix(outputSchema, false) {

    override fun makeRule(): TypeRewriteRule {
        val chunkType = inputSchema.getType(References.CHUNK)
        val levelType = chunkType.findFieldType("Level")
        val levelFinder = fieldFinder("Level", levelType)
        val sectionsFinder = levelFinder.type().findField("Sections")
        val sectionType = (sectionsFinder.type() as ListType<*>).element
        val sectionFinder = sectionType.finder()
        val stateType = named(References.BLOCK_STATE.typeName(), remainderType())
        val paletteFinder = fieldFinder("Palette", list(stateType))
        return fixTypeEverywhereTyped("Align BitStorage fix", chunkType, outputSchema.getType(References.CHUNK)) { typed ->
            typed.updateTyped(levelFinder) { it.updateSections(sectionsFinder, sectionFinder, paletteFinder).updateHeightmaps() }
        }
    }

    private fun Typed<*>.updateHeightmaps() = update(remainderFinder()) { section ->
        section.update("Heightmaps") { heightmaps ->
            heightmaps.updateMapValues { values -> values.mapSecond { section.updateBitStorage(it, HEIGHTMAP_SIZE, HEIGHTMAP_BITS) } }
        }
    }

    private fun Typed<*>.updateSections(sectionFinder: OpticFinder<*>, listFinder: OpticFinder<*>, paletteFinder: OpticFinder<List<Pair<String, Dynamic<*>>>>): Typed<*> = updateTyped(sectionFinder) { typed ->
        typed.updateTyped(listFinder) { sections ->
            val storageBits = sections.getOptional(paletteFinder).map { max(4, it.size.ceillog2()) }.orElse(0)
            if (storageBits != 0 && !storageBits.isPowerOfTwo()) sections.update(remainderFinder()) { section ->
                section.update("BlockStates") { section.updateBitStorage(it, SECTION_SIZE, storageBits) }
            } else sections
        }
    }

    private fun Dynamic<*>.updateBitStorage(current: Dynamic<*>, size: Int, bits: Int): Dynamic<*> {
        val currentArray = current.asLongStream().toArray()
        val newArray = currentArray.addPadding(size, bits)
        return createLongList(Arrays.stream(newArray))
    }
}

private const val BIT_TO_LONG_SHIFT = 6
private const val SECTION_SIZE = 4096
private const val HEIGHTMAP_BITS = 9
private const val HEIGHTMAP_SIZE = 256

fun LongArray.addPadding(size: Int, bits: Int): LongArray {
    if (isEmpty()) return this
    val mask = (1L shl bits) - 1L
    val valuesPerLong = 64 / bits
    val array = LongArray((size + valuesPerLong - 1) / valuesPerLong)
    var var5 = 0
    var currentBitOffset = 0
    var var7 = 0L
    var currentLongOffset = 0
    var current = this[0]
    var next = if (size > 1) this[1] else 0L

    for (i in 0 until size) {
        val bitOffset = i * bits
        val longOffset = bitOffset shr BIT_TO_LONG_SHIFT
        val nextLongOffset = (i + 1) * bits - 1 shr BIT_TO_LONG_SHIFT // maybe?
        val var15 = bitOffset xor (longOffset shl BIT_TO_LONG_SHIFT)
        if (longOffset != currentLongOffset) {
            current = next
            next = if (longOffset + 1 < size) this[longOffset + 1] else 0L
            currentLongOffset = longOffset
        }

        val var16 = if (longOffset == nextLongOffset) {
            current ushr var15 and mask
        } else {
            val var17 = 64 - var15
            current ushr var15 or (next shl var17) and mask
        }

        val nextBitOffset = currentBitOffset + bits
        if (nextBitOffset >= 64) {
            array[var5++] = var7
            var7 = var16
            currentBitOffset = bits
        } else {
            var7 = var7 or (var16 shl currentBitOffset)
            currentBitOffset = nextBitOffset
        }

        if (var7 != 0L) array[var5] = var7
    }

    return array
}
