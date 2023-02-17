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

                val section = ChunkSection(y, blocks, biomes, sectionData.getByteArray("BlockLight"), sectionData.getByteArray("SkyLight"))
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
                    if (section.blockLight.isNotEmpty()) putByteArray("BlockLight", section.blockLight)
                    if (section.skyLight.isNotEmpty()) putByteArray("SkyLight", section.skyLight)
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
