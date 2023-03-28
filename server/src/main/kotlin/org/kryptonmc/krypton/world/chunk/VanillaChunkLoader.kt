package org.kryptonmc.krypton.world.chunk

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.nbt.getDataVersion
import org.kryptonmc.krypton.util.nbt.putDataVersion
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.palette.PaletteHolder
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.data.ChunkSection
import org.kryptonmc.krypton.world.chunk.data.Heightmap
import org.kryptonmc.krypton.world.region.RegionFileManager
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
import java.nio.file.Path
import java.util.EnumSet

class VanillaChunkLoader(worldFolder: Path) : ChunkLoader {

    private val regionManager = RegionFileManager(worldFolder.resolve("region"))
    private val entityRegionManager = RegionFileManager(worldFolder.resolve("entities"))

    override fun loadChunk(world: KryptonWorld, pos: ChunkPos): KryptonChunk? {
        val nbt = regionManager.read(pos.x, pos.z) ?: return null
        return loadData(world, pos, nbt)
    }

    private fun loadData(world: KryptonWorld, pos: ChunkPos, nbt: CompoundTag): KryptonChunk {
        val dataVersion = nbt.getDataVersion()

        // Don't upgrade if the version is not older than our version.
        val data = if (dataVersion < KryptonPlatform.worldVersion) DataConversion.upgrade(nbt, MCTypeRegistry.CHUNK, dataVersion, true) else nbt
        val heightmaps = data.getCompound("Heightmaps")
        val biomeRegistry = world.registryHolder.getRegistry(ResourceKeys.BIOME) as? KryptonRegistry<Biome>
            ?: error("Cannot find biome registry in $world!")

        val sectionList = data.getList("sections", CompoundTag.ID)
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

        val carvingMasks = if (data.contains("CarvingMasks", CompoundTag.ID)) {
            val masks = data.getCompound("CarvingMasks")
            Pair(masks.getByteArray("AIR"), masks.getByteArray("LIQUID"))
        } else {
            null
        }
        val structureData = if (data.contains("Structures", CompoundTag.ID)) data.getCompound("Structures") else null

        val chunk = KryptonChunk(
            world,
            pos,
            sections,
            data.getLong("LastUpdate"),
            data.getLong("inhabitedTime"),
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

    override fun loadAllEntities(chunk: KryptonChunk) {
        val nbt = entityRegionManager.read(chunk.x, chunk.z) ?: return

        val dataVersion = nbt.getDataVersion()
        val data = if (dataVersion < KryptonPlatform.worldVersion) DataConversion.upgrade(nbt, MCTypeRegistry.ENTITY_CHUNK, dataVersion) else nbt

        data.getList(ENTITIES_TAG, CompoundTag.ID).forEachCompound {
            val id = it.getString(ENTITY_ID_TAG)
            if (id.isBlank()) return@forEachCompound

            val key = try {
                Key.key(id)
            } catch (_: InvalidKeyException) {
                return@forEachCompound
            }
            val type = KryptonRegistries.ENTITY_TYPE.get(key)

            val entity = EntityFactory.create(type, chunk.world) ?: return@forEachCompound
            entity.load(it)
            chunk.world.entityManager.spawnEntity(entity)
        }
    }

    override fun saveChunk(chunk: KryptonChunk) {
        val data = saveData(chunk)
        regionManager.write(chunk.x, chunk.z, data)
    }

    private fun saveData(chunk: KryptonChunk): CompoundTag {
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

    override fun saveAllEntities(chunk: KryptonChunk) {
        val entities = chunk.world.entityTracker.entitiesInChunk(chunk.position)
        if (entities.isEmpty()) return

        val entityList = ImmutableListTag.builder(CompoundTag.ID)
        entities.forEach { if (it !is KryptonPlayer) entityList.add(it.saveWithPassengers().build()) }

        entityRegionManager.write(chunk.x, chunk.z, compound {
            putDataVersion()
            putInts(ENTITY_POSITION_TAG, chunk.position.x, chunk.position.z)
            put(ENTITIES_TAG, entityList.build())
        })
    }

    override fun close() {
        regionManager.close()
        entityRegionManager.close()
    }

    companion object {

        private const val ENTITY_ID_TAG = "id"
        private const val ENTITY_POSITION_TAG = "Position"
        private const val ENTITIES_TAG = "Entities"
    }
}
