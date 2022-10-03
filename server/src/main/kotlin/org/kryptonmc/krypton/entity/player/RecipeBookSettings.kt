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
package org.kryptonmc.krypton.entity.player

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.nbt.CompoundTag
import java.util.EnumMap

class RecipeBookSettings private constructor(private val settings: MutableMap<RecipeBookType, TypeSettings>) : Writable {

    constructor() : this(createDefaultStatesMap())

    fun isOpen(type: RecipeBookType): Boolean = getSettings(type).open

    fun isFiltered(type: RecipeBookType): Boolean = getSettings(type).filtered

    fun setOpen(type: RecipeBookType, open: Boolean) {
        setSettings(type, open, getSettings(type).filtered)
    }

    fun setFiltered(type: RecipeBookType, filtered: Boolean) {
        setSettings(type, getSettings(type).open, filtered)
    }

    fun write(data: CompoundTag.Builder): CompoundTag.Builder = data.apply {
        TAG_FIELDS.forEach { (type, fields) ->
            val settings = getSettings(type)
            boolean(fields.first, settings.open)
            boolean(fields.second, settings.filtered)
        }
    }

    override fun write(buf: ByteBuf) {
        VALUES.forEach {
            val settings = getSettings(it)
            buf.writeBoolean(settings.open)
            buf.writeBoolean(settings.filtered)
        }
    }

    private fun getSettings(type: RecipeBookType): TypeSettings = settings.getOrDefault(type, TypeSettings.ALL_FALSE)

    private fun setSettings(type: RecipeBookType, open: Boolean, filtered: Boolean) {
        settings.put(type, TypeSettings.from(open, filtered))
    }

    @JvmRecord
    private data class TypeSettings(val open: Boolean, val filtered: Boolean) {

        companion object {

            @JvmField
            val ALL_FALSE: TypeSettings = TypeSettings(false, false)
            private val ALL_TRUE = TypeSettings(true, true)
            private val OPEN_NOT_FILTERED = TypeSettings(true, false)
            private val FILTERED_NOT_OPEN = TypeSettings(false, true)

            @JvmStatic
            fun from(open: Boolean, filtered: Boolean): TypeSettings {
                if (open) {
                    if (filtered) return ALL_TRUE
                    return OPEN_NOT_FILTERED
                }
                if (filtered) return FILTERED_NOT_OPEN
                return ALL_FALSE
            }
        }
    }

    companion object {

        private val TAG_FIELDS = mapOf(
            RecipeBookType.CRAFTING to Pair("isGuiOpen", "isFilteringCraftable"),
            RecipeBookType.FURNACE to Pair("isFurnaceGuiOpen", "isFurnaceFilteringCraftable"),
            RecipeBookType.BLAST_FURNACE to Pair("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable"),
            RecipeBookType.SMOKER to Pair("isSmokerGuiOpen", "isSmokerFilteringCraftable")
        )
        private val VALUES = RecipeBookType.values()

        @JvmStatic
        fun read(data: CompoundTag): RecipeBookSettings {
            val map = EnumMap<RecipeBookType, TypeSettings>(RecipeBookType::class.java)
            TAG_FIELDS.forEach { (type, fields) -> map.put(type, TypeSettings.from(data.getBoolean(fields.first), data.getBoolean(fields.second))) }
            return RecipeBookSettings(map)
        }

        @JvmStatic
        fun read(buf: ByteBuf): RecipeBookSettings {
            val map = EnumMap<RecipeBookType, TypeSettings>(RecipeBookType::class.java)
            VALUES.forEach { map.put(it, TypeSettings.from(buf.readBoolean(), buf.readBoolean())) }
            return RecipeBookSettings(map)
        }

        @JvmStatic
        private fun createDefaultStatesMap(): MutableMap<RecipeBookType, TypeSettings> {
            val map = EnumMap<RecipeBookType, TypeSettings>(RecipeBookType::class.java)
            VALUES.forEach { map.put(it, TypeSettings.ALL_FALSE) }
            return map
        }
    }
}
