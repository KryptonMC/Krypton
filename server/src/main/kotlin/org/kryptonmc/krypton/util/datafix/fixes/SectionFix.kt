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

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.fieldFinder
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.named
import com.mojang.datafixers.DSL.remainderType
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.Typed
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Dynamic
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.datafix.PackedBitStorage
import org.kryptonmc.krypton.util.datafix.References
import java.util.Arrays
import java.util.stream.Collectors
import kotlin.math.max

sealed class SectionFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    protected abstract class Section(typed: Typed<*>, schema: Schema) {

        private val blockStateType = named(References.BLOCK_STATE.typeName(), remainderType())
        protected val paletteFinder = fieldFinder("Palette", list(blockStateType))
        protected val palette: MutableList<Dynamic<*>>
        val index: Int

        var storage: PackedBitStorage? = null
            protected set

        init {
            check(schema.getType(References.BLOCK_STATE) == blockStateType) { "Block state type is not what was expected! Expected $blockStateType, got ${schema.getType(References.BLOCK_STATE)}!" }
            val paletteData = typed.getOptional(paletteFinder)
            palette = paletteData.map { list -> list.stream().map { it.second }.collect(Collectors.toList()) }.orElse(emptyList())
            val data = typed[DSL.remainderFinder()]
            index = data["Y"].asInt(0)
            readStorage(data)
        }

        protected abstract val skippable: Boolean

        protected fun readStorage(data: Dynamic<*>) {
            if (skippable) {
                storage = null
                return
            }
            val states = data["BlockStates"].asLongStream().toArray()
            val bits = max(4, palette.size.ceillog2())
            storage = PackedBitStorage(bits, SIZE, states)
        }

        fun write(typed: Typed<*>): Typed<*> = if (storage == null) typed else typed.update(DSL.remainderFinder()) {
            it.set("BlockStates", it.createLongList(Arrays.stream(storage!!.data)))
        }.set(paletteFinder, palette.stream().map { Pair.of(References.BLOCK_STATE.typeName(), it) }.collect(Collectors.toList()))

        val isSkippable: Boolean
            get() = storage == null
    }

    companion object {

        private const val NORTH_WEST_MASK = 128
        private const val WEST_MASK = 64
        private const val SOUTH_WEST_MASK = 32
        private const val SOUTH_MASK = 16
        private const val SOUTH_EAST_MASK = 8
        private const val EAST_MASK = 4
        private const val NORTH_EAST_MASK = 2
        private const val NORTH_MASK = 1
        private const val SIZE = 4096

        fun sideMask(west: Boolean, east: Boolean, north: Boolean, south: Boolean): Int {
            var i = 0
            when {
                north -> i = if (east) i or NORTH_EAST_MASK else if (west) i or NORTH_WEST_MASK else i or NORTH_MASK
                south -> i = if (west) i or SOUTH_WEST_MASK else if (east) i or SOUTH_EAST_MASK else i or SOUTH_MASK
                east -> i = i or EAST_MASK
                west -> i = i or WEST_MASK
            }
            return i
        }
    }
}
