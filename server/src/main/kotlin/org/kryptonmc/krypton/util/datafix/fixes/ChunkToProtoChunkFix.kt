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
import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import it.unimi.dsi.fastutil.shorts.ShortArrayList
import org.kryptonmc.krypton.util.datafix.References
import java.util.Arrays

class ChunkToProtoChunkFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val inputType = inputSchema.getType(References.CHUNK)
        val outputType = outputSchema.getType(References.CHUNK)
        val inputLevelType = inputType.findFieldType("Level")
        val outputLevelType = outputType.findFieldType("Level")
        val tileTicksType = inputLevelType.findFieldType("TileTicks")
        val levelFinder = fieldFinder("Level", inputLevelType)
        val tileTicksFinder = fieldFinder("TileTicks", tileTicksType)
        return TypeRewriteRule.seq(
            fixTypeEverywhereTyped("ChunkToProtoChunkFix", inputType, outputType) { typed ->
                typed.updateTyped(levelFinder, outputLevelType) { level ->
                    val tileTicks = level.getOptionalTyped(tileTicksFinder).flatMap { it.write().result() }.flatMap { it.asStreamOpt().result() }
                    var data = level[remainderFinder()]
                    val mobsSpawned = data["TerrainPopulated"].asBoolean(false) && (!data["LightPopulated"].asNumber().result().isPresent || data["LightPopulated"].asBoolean(false))
                    data = data.set("Status", data.createString(if (mobsSpawned) "mobs_spawned" else "empty"))
                    data = data.set("hasLegacyStructureData", data.createBoolean(true))
                    val chunkData = if (mobsSpawned) {
                        val optionalBiomes = data["Biomes"].asByteBufferOpt().result()
                        if (optionalBiomes.isPresent) {
                            val biomes = optionalBiomes.get()
                            val biomesArray = IntArray(256)
                            for (i in biomesArray.indices) {
                                if (i < biomes.capacity()) biomesArray[i] = biomes[i].toInt() and 255
                            }
                            data = data.set("Biomes", data.createIntList(Arrays.stream(biomesArray)))
                        }
                        var temp = data
                        val toBeTicked = (0..15).map { ShortArrayList() }
                        if (tileTicks.isPresent) {
                            tileTicks.get().forEach {
                                val x = it["x"].asInt(0)
                                val y = it["y"].asInt(0)
                                val z = it["z"].asInt(0)
                                val packed = packOffsetCoordinates(x, y, z)
                                toBeTicked[y shr 4] += packed
                            }
                            data = data.set("ToBeTicked", data.createList(toBeTicked.stream().map { temp.createList(it.stream().map(temp::createShort)) }))
                        }
                        DataFixUtils.orElse(level.set(remainderFinder(), data).write().result(), data)
                    } else {
                        data
                    }
                    outputLevelType.readTyped(chunkData).result().orElseThrow { IllegalStateException("Could not read the new chunk data!") }.first
                }
            },
            writeAndRead("Structure biome inject", inputSchema.getType(References.STRUCTURE_FEATURE), outputSchema.getType(References.STRUCTURE_FEATURE))
        )
    }
}

private fun packOffsetCoordinates(x: Int, y: Int, z: Int) = (x and 15 or ((y and 15) shl 4) or ((z and 15) shl 8)).toShort()
