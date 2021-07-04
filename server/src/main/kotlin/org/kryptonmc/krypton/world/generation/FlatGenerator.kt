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
package org.kryptonmc.krypton.world.generation

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.util.toKey
import org.kryptonmc.krypton.world.transform

data class FlatGenerator(val settings: FlatGeneratorSettings) : Generator(ID) {

    override fun toNBT() = NBTCompound()
        .setString("type", ID.toString())
        .set("settings", settings.toNBT())

    companion object {

        val ID = key("flat")
    }
}

data class FlatGeneratorSettings(
    val layers: List<FlatLayer>,
    val biome: Key,
    override val structures: GeneratorStructures
) : GeneratorSettings() {

    override fun toNBT() = NBTCompound()
        .set("layers", NBTList<NBTCompound>(NBTTypes.TAG_Compound).apply { layers.forEach { add(it.toNBT()) } })
        .setString("biome", biome.toString())
        .set("structures", structures.toNBT())

    companion object {

        fun fromNBT(nbt: NBTCompound) = FlatGeneratorSettings(
            nbt.getList<NBTCompound>("layers").map {
                FlatLayer(it.getString("block").toKey(), it.getInt("height"))
            },
            nbt.getString("biome").ifEmpty { "minecraft:plains" }.toKey(),
            nbt.getCompound("structures").let { nbtStructures ->
                val stronghold = nbtStructures.getCompound("stronghold")
                val structures = nbtStructures.getCompound("structures")
                GeneratorStructures(
                    GeneratorStronghold(
                        stronghold.getInt("distance"),
                        stronghold.getInt("count"),
                        stronghold.getInt("spread")
                    ),
                    structures.transform { (key, value) ->
                        key.toKey() to (value as NBTCompound).let {
                            GeneratorStructure(it.getInt("spacing"), it.getInt("separation"), it.getInt("salt"))
                        }
                    }
                )
            }
        )
    }
}

data class FlatLayer(
    val block: Key,
    val height: Int
) {

    fun toNBT() = NBTCompound()
        .setInt("height", height)
        .setString("block", block.toString())
}
