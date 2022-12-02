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
package org.kryptonmc.krypton.entity

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.entity.KryptonRemoveEntityEvent
import org.kryptonmc.krypton.event.entity.KryptonSpawnEntityEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.SectionPos
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.getDataVersion
import org.kryptonmc.krypton.util.nbt.putDataVersion
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPos
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableListTag
import org.kryptonmc.nbt.compound
import space.vectrix.flare.fastutil.Int2ObjectSyncMap
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.function.LongFunction
import kotlin.math.abs

class EntityManager(val world: KryptonWorld) : AutoCloseable {

    private val byId = Int2ObjectSyncMap.hashmap<KryptonEntity>()
    private val byUUID = ConcurrentHashMap<UUID, KryptonEntity>()
    private val byChunk = Long2ObjectSyncMap.hashmap<MutableSet<KryptonEntity>>()
    private val regionFileManager = RegionFileManager(world.folder.resolve("entities"), world.server.config.advanced.synchronizeChunkWrites)
    val entities: MutableCollection<KryptonEntity>
        get() = byId.values

    fun get(id: Int): KryptonEntity? = byId.get(id)

    fun get(uuid: UUID): KryptonEntity? = byUUID.get(uuid)

    fun spawn(entity: KryptonEntity) {
        if (entity.world != world) return
        if (byUUID.containsKey(entity.uuid)) {
            LOGGER.error("UUID collision! UUID for entity with ID ${entity.id} collided with entity with ID ${byUUID.get(entity.uuid)?.id}!")
            LOGGER.warn("Refusing to spawn entity with ID ${entity.id}.")
            return
        }
        world.server.eventManager.fire(KryptonSpawnEntityEvent(entity, world)).thenAccept { event ->
            if (!event.result.isAllowed) return@thenAccept

            forEachEntityInRange(entity.position, world.server.config.world.viewDistance) {
                if (it is KryptonPlayer) entity.addViewer(it)
            }
            val chunk = world.getChunk(entity.position.floorX(), entity.position.floorY(), entity.position.floorZ()) ?: return@thenAccept
            byId.put(entity.id, entity)
            byUUID.put(entity.uuid, entity)
            getByChunk(chunk.position.pack()).add(entity)
        }
    }

    fun spawn(player: KryptonPlayer) {
        if (player.world != world) return

        // TODO: World border
        player.session.send(PacketOutUpdateTime(world.data.time, world.data.dayTime, world.data.gameRules.get(GameRules.DO_DAYLIGHT_CYCLE)))
        if (!player.isVanished) {
            forEachEntityInRange(player.position, player.settings.viewDistance) {
                it.addViewer(player)
                if (it is KryptonPlayer && !it.isVanished) player.addViewer(it)
            }
        }

        val chunk = world.getChunk(player.position.floorX(), player.position.floorY(), player.position.floorZ()) ?: return
        byId.put(player.id, player)
        byUUID.put(player.uuid, player)
        getByChunk(chunk.position.pack()).add(player)
    }

    private fun getByChunk(location: Long): MutableSet<KryptonEntity> =
        byChunk.computeIfAbsent(location, LongFunction { ConcurrentHashMap.newKeySet() })

    fun remove(entity: KryptonEntity) {
        if (entity.world != world) return
        world.server.eventManager.fire(KryptonRemoveEntityEvent(entity, world)).thenAccept { event ->
            if (!event.result.isAllowed) return@thenAccept
            forEachEntityInRange(entity.position, world.server.config.world.viewDistance) {
                if (it is KryptonPlayer) entity.removeViewer(it)
                if (entity is KryptonPlayer) it.removeViewer(entity)
            }

            val chunk = world.getChunk(entity.position.floorX(), entity.position.floorY(), entity.position.floorZ()) ?: return@thenAccept
            byId.remove(entity.id)
            byUUID.remove(entity.uuid)
            val entitiesByChunk = byChunk.get(chunk.position.pack()).apply { remove(entity) }
            if (entitiesByChunk.isEmpty()) byChunk.remove(chunk.position.pack())
            world.scoreboard.onEntityRemoved(entity)
        }
    }

    fun load(chunk: KryptonChunk) {
        val nbt = regionFileManager.read(chunk.x, chunk.z) ?: return
        val version = nbt.getDataVersion()
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !world.server.config.advanced.useDataConverter) {
            DataConversion.sendWarning(LOGGER, "entities for chunk at ${chunk.x}, ${chunk.z}")
            error("Tried to load old chunk from version $version when data conversion is disabled!")
        }

        DataConversion.upgrade(nbt, MCTypeRegistry.ENTITY_CHUNK, version).getList(ENTITIES_TAG, CompoundTag.ID).forEachCompound {
            val id = it.getString(ID_TAG)
            if (id.isBlank()) return@forEachCompound
            val key = try {
                Key.key(id)
            } catch (_: InvalidKeyException) {
                return@forEachCompound
            }
            val type = KryptonRegistries.ENTITY_TYPE.get(key)
            val entity = EntityFactory.create(type, world) ?: return@forEachCompound
            entity.load(it)
            spawn(entity)
        }
    }

    fun save(chunk: KryptonChunk) {
        val entities = byChunk.get(chunk.position.pack()) ?: return
        if (entities.isEmpty()) return
        val entityList = ImmutableListTag.builder(CompoundTag.ID)
        entities.forEach { if (it !is KryptonPlayer) entityList.add(it.saveWithPassengers().build()) }
        regionFileManager.write(chunk.x, chunk.z, compound {
            putDataVersion()
            putInts(POSITION_TAG, chunk.position.x, chunk.position.z)
            put(ENTITIES_TAG, entityList.build())
        })
    }

    fun flush() {
        regionFileManager.flush()
    }

    override fun close() {
        regionFileManager.close()
    }

    private inline fun forEachEntityInRange(location: Vec3d, viewDistance: Int, callback: (KryptonEntity) -> Unit) {
        chunksInRange(location, viewDistance).forEach {
            val chunk = world.getChunkAt(ChunkPos.unpackX(it), ChunkPos.unpackZ(it)) ?: return@forEach
            getByChunk(chunk.position.pack()).forEach(callback)
        }
    }

    companion object {

        private const val ID_TAG = "id"
        private const val POSITION_TAG = "Position"
        private const val ENTITIES_TAG = "Entities"
        private val LOGGER = logger<EntityManager>()

        @JvmStatic
        private fun chunksInRange(location: Vec3d, range: Int): LongArray {
            val area = (range * 2 + 1) * (range * 2 + 1)
            val visible = LongArray(area)
            var dx = 0
            var directionX = 1
            var dz = 0
            var directionZ = -1
            var length = 1
            var corner = 0

            for (i in 0 until area) {
                val chunkX = SectionPos.blockToSection(SectionPos.sectionToBlock(dx) + location.x)
                val chunkZ = SectionPos.blockToSection(SectionPos.sectionToBlock(dz) + location.z)
                visible[i] = ChunkPos.pack(chunkX, chunkZ)

                if (corner % 2 == 0) {
                    dx += directionX
                    if (abs(dx) == length) {
                        corner++
                        directionX = -directionX
                    }
                } else {
                    dz += directionZ
                    if (abs(dz) == length) {
                        corner++
                        directionZ = -directionZ
                        if (corner % 4 == 0) length++
                    }
                }
            }
            return visible
        }
    }
}
