/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.chunk

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.nbt.getDataVersion
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.palette.PaletteHolder
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.data.ChunkSection
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import org.kryptonmc.nbt.ImmutableListTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.kryptonmc.nbt.compound
import org.kryptonmc.serialization.nbt.NbtOps
import java.util.EnumSet

@Suppress("StringLiteralDuplication")
object ChunkSerialization {

    @JvmStatic
    fun read(world: KryptonWorld, pos: ChunkPos, data: CompoundTag): KryptonChunk {
        val dataVersion = data.getDataVersion()

        // Don't upgrade if the version is not older than our version.
        val newData = if (dataVersion < KryptonPlatform.worldVersion) DataConversion.upgrade(data, MCTypeRegistry.CHUNK, dataVersion, true) else data
        val heightmaps = newData.getCompound("Heightmaps")
        val biomeRegistry = world.registryHolder.getRegistry(ResourceKeys.BIOME) as? KryptonRegistry<Biome>
            ?: error("Cannot find biome registry in $world!")

        val sectionList = newData.getList("sections", CompoundTag.ID)
        val sections = arrayOfNulls<ChunkSection>(world.sectionCount())
        for (i in 0 until sectionList.size()) {
            val sectionData = sectionList.getCompound(i)
            val y = sectionData.getByte("Y").toInt()

            val index = world.getSectionIndexFromSectionY(y)
            if (index >= 0 && index < sections.size) {
                val blocks = if (sectionData.contains("block_states", CompoundTag.ID)) {
                    PaletteHolder.readBlocks(sectionData.getCompound("block_states"))
                } else {
                    PaletteHolder(PaletteHolder.Strategy.BLOCKS, KryptonBlocks.AIR.defaultState)
                }

                val biomes = if (sectionData.contains("biomes", CompoundTag.ID)) {
                    PaletteHolder.readBiomes(sectionData.getCompound("biomes"), biomeRegistry)
                } else {
                    PaletteHolder(PaletteHolder.Strategy.biomes(biomeRegistry), biomeRegistry.get(BiomeKeys.PLAINS)!!)
                }

                val blockLight = if (sectionData.contains("BlockLight", ByteArrayTag.ID)) sectionData.getByteArray("BlockLight") else null
                val skyLight = if (sectionData.contains("SkyLight", ByteArrayTag.ID)) sectionData.getByteArray("SkyLight") else null
                val section = ChunkSection(y, blocks, biomes, blockLight, skyLight)
                sections[index] = section
            }
        }

        val carvingMasks = if (newData.contains("CarvingMasks", CompoundTag.ID)) {
            val masks = newData.getCompound("CarvingMasks")
            Pair(masks.getByteArray("AIR"), masks.getByteArray("LIQUID"))
        } else {
            null
        }
        val structureData = if (newData.contains("Structures", CompoundTag.ID)) newData.getCompound("Structures") else null

        val chunk =  KryptonChunk(
            world,
            pos,
            sections,
            newData.getLong("LastUpdate"),
            newData.getLong("inhabitedTime"),
            carvingMasks,
            structureData
        )

        val noneOf = EnumSet.noneOf(Heightmap.Type::class.java)
        Heightmap.Type.POST_FEATURES.forEach {
            if (heightmaps.contains(it.name, LongArrayTag.ID)) chunk.setHeightmap(it, heightmaps.getLongArray(it.name)) else noneOf.add(it)
        }
        Heightmap.prime(chunk, noneOf)

        return chunk
    }

    @JvmStatic
    fun write(chunk: KryptonChunk): CompoundTag {
        val data = buildCompound {
            putInt("DataVersion", KryptonPlatform.worldVersion)
            if (chunk.carvingMasks != null) {
                compound("CarvingMasks") {
                    putByteArray("AIR", chunk.carvingMasks.first)
                    putByteArray("LIQUID", chunk.carvingMasks.second)
                }
            }
            putLong("LastUpdate", chunk.lastUpdate)
            putList("Lights", ListTag.ID)
            putList("LiquidsToBeTicked", ListTag.ID)
            putList("LiquidTicks", ListTag.ID)
            putLong("InhabitedTime", chunk.inhabitedTime)
            putList("PostProcessing", ListTag.ID)
            putString("Status", "full")
            putList("TileEntities", CompoundTag.ID)
            putList("TileTicks", CompoundTag.ID)
            putList("ToBeTicked", ListTag.ID)
            if (chunk.structures != null) put("Structures", chunk.structures)
            putInt("xPos", chunk.position.x)
            putInt("zPos", chunk.position.z)
        }

        val sectionList = ImmutableListTag.builder(CompoundTag.ID)
        for (i in chunk.minimumLightSection() until chunk.maximumLightSection()) {
            val sectionIndex = chunk.world.getSectionIndexFromSectionY(i)
            // TODO: Handle light sections below and above the world
            if (sectionIndex >= 0 && sectionIndex < chunk.sections().size) {
                val section = chunk.sections()[sectionIndex]
                val sectionData = compound {
                    putByte("Y", i.toByte())
                    put("block_states", section.blocks.write { KryptonBlockState.CODEC.encodeStart(it, NbtOps.INSTANCE).result().get() })
                    put("biomes", section.biomes.write { StringTag.of(it.key().asString()) })
                    if (section.blockLight != null) putByteArray("BlockLight", section.blockLight)
                    if (section.skyLight != null) putByteArray("SkyLight", section.skyLight)
                }
                sectionList.add(sectionData)
            }
        }
        data.put("sections", sectionList.build())

        val heightmapData = ImmutableCompoundTag.builder()
        chunk.heightmaps.forEach { if (it.key in Heightmap.Type.POST_FEATURES) heightmapData.putLongArray(it.key.name, it.value.rawData()) }
        data.put("Heightmaps", heightmapData.build())
        return data.build()
    }
}
