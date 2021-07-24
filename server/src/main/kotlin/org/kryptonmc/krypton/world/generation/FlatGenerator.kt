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

import com.mojang.serialization.Dynamic
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.util.toKey
import org.kryptonmc.nbt.compound

data class FlatGenerator(val settings: FlatGeneratorSettings) : Generator(ID) {

    override fun toNBT() = compound {
        string("type", ID.toString())
        put("settings", settings.toNBT())
    }

    companion object {

        val ID = key("flat")
    }
}

data class FlatGeneratorSettings(
    val layers: List<FlatLayer>,
    val biome: Key,
    override val structures: GeneratorStructures
) : GeneratorSettings() {

    override fun toNBT() = compound {
        list("layers") { layers.forEach { add(it.toNBT()) } }
        string("biome", biome.toString())
        put("structures", structures.toNBT())
    }

    companion object {

        fun of(data: Dynamic<*>) = FlatGeneratorSettings(
            data["layers"].asList { FlatLayer(it["block"].asString("").toKey(), it["height"].asInt(0)) },
            data["biome"].asString("minecraft:plains").toKey(),
            data["structures"].let { dynamic ->
                val stronghold = dynamic["stronghold"]
                val structures = dynamic["structures"].asMap({ it.asString("").toKey() }, {
                    GeneratorStructure(it["spacing"].asInt(0), it["separation"].asInt(0), it["salt"].asInt(0))
                })
                GeneratorStructures(
                    GeneratorStronghold(
                        stronghold["distance"].asInt(0),
                        stronghold["count"].asInt(0),
                        stronghold["spread"].asInt(0)
                    ),
                    structures
                )
            }
        )
    }
}

data class FlatLayer(
    val block: Key,
    val height: Int
) {

    fun toNBT() = compound {
        int("height", height)
        string("block", block.toString())
    }
}
