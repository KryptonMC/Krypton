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
package org.kryptonmc.krypton.world.block.pattern

import it.unimi.dsi.fastutil.chars.Char2ObjectMaps
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap
import it.unimi.dsi.fastutil.chars.CharArrayList
import java.util.function.Predicate
import kotlin.Array
import kotlin.Char
import kotlin.String
import kotlin.Suppress
import kotlin.apply
import kotlin.check
import kotlin.require
import java.lang.reflect.Array as JLRArray

class BlockPatternBuilder private constructor() {

    private val pattern = ArrayList<Array<out String>>()
    private val lookup = Char2ObjectOpenHashMap<Predicate<BlockInWorld?>>()
    private var height = 0
    private var width = 0

    init {
        lookup.put(' ', Predicate { true })
    }

    // This declares a series of replacement patterns that we can register predicates for.
    fun aisle(vararg aisle: String): BlockPatternBuilder = apply {
        require(aisle.isNotEmpty() && aisle[0].isNotEmpty()) { "Empty pattern provided for aisle!" }
        if (pattern.isEmpty()) {
            height = aisle.size
            width = aisle[0].length
        }
        require(aisle.size == height) { "Expected aisle with height of $height, but was given one with a height of ${aisle.size}!" }
        aisle.forEach { value ->
            require(value.length == width) {
                "Not all rows in the given aisle are the correct width! Expected $width, found one with ${value.length}!"
            }
            value.forEach { if (!lookup.containsKey(it)) lookup.put(it, null) }
        }
        pattern.add(aisle)
    }

    // This provides the predicates for individual symbols register in the above aisle.
    fun where(symbol: Char, matcher: Predicate<BlockInWorld?>): BlockPatternBuilder = apply { lookup.put(symbol, matcher) }

    fun build(): BlockPattern = BlockPattern(createPattern())

    @Suppress("UNCHECKED_CAST")
    private fun createPattern(): Array<Array<Array<Predicate<BlockInWorld?>>>> {
        ensureAllCharactersMatched()
        val pattern = JLRArray.newInstance(Predicate::class.java, pattern.size, height, width) as Array<Array<Array<Predicate<BlockInWorld?>>>>
        for (x in 0 until this.pattern.size) {
            for (y in 0 until height) {
                for (z in 0 until width) {
                    pattern[x][y][z] = lookup.get(this.pattern[x][y][z])
                }
            }
        }
        return pattern
    }

    private fun ensureAllCharactersMatched() {
        val characters = CharArrayList()
        Char2ObjectMaps.fastForEach(lookup) { if (it.value == null) characters.add(it.charKey) }
        check(characters.isEmpty) { "Predicates for character(s) ${characters.joinToString(",")} are missing!" }
    }

    companion object {

        @JvmStatic
        fun start(): BlockPatternBuilder = BlockPatternBuilder()
    }
}
