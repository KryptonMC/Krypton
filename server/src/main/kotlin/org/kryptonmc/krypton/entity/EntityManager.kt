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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.entity.KryptonRemoveEntityEvent
import org.kryptonmc.krypton.event.entity.KryptonSpawnEntityEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.util.DataConversion
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound
import org.spongepowered.math.vector.Vector3d
import space.vectrix.flare.fastutil.Int2ObjectSyncMap
import space.vectrix.flare.fastutil.Long2ObjectSyncMap
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.function.LongFunction

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
            val location = entity.location

            forEachEntityInRange(location, world.server.config.world.viewDistance) {
                if (it is KryptonPlayer) entity.addViewer(it)
            }
            val chunk = world.getChunk(entity.location.floorX(), entity.location.floorY(), entity.location.floorZ()) ?: return@thenAccept
            byId.put(entity.id, entity)
            byUUID.put(entity.uuid, entity)
            getByChunk(chunk.position.toLong()).add(entity)
        }
    }

    fun spawn(player: KryptonPlayer) {
        if (player.world != world) return
        val location = player.location

        // TODO: World border
        player.session.send(PacketOutUpdateTime(world.data.time, world.data.dayTime, world.data.gameRules.get(GameRules.DO_DAYLIGHT_CYCLE)))
        if (!player.isVanished) {
            forEachEntityInRange(location, player.viewDistance) {
                it.addViewer(player)
                if (it is KryptonPlayer && !it.isVanished) player.addViewer(it)
            }
        }

        val chunk = world.getChunk(player.location.floorX(), player.location.floorY(), player.location.floorZ()) ?: return
        byId.put(player.id, player)
        byUUID.put(player.uuid, player)
        getByChunk(chunk.position.toLong()).add(player)
    }

    private fun getByChunk(location: Long): MutableSet<KryptonEntity> =
        byChunk.computeIfAbsent(location, LongFunction { ConcurrentHashMap.newKeySet() })

    fun remove(entity: KryptonEntity) {
        if (entity.world != world) return
        world.server.eventManager.fire(KryptonRemoveEntityEvent(entity, world)).thenAccept { event ->
            if (!event.result.isAllowed) return@thenAccept
            forEachEntityInRange(entity.location, world.server.config.world.viewDistance) {
                if (it is KryptonPlayer) entity.removeViewer(it)
                if (entity is KryptonPlayer) it.removeViewer(entity)
            }

            val chunk = world.getChunk(entity.location.floorX(), entity.location.floorY(), entity.location.floorZ()) ?: return@thenAccept
            byId.remove(entity.id)
            byUUID.remove(entity.uuid)
            val entitiesByChunk = byChunk.get(chunk.position.toLong()).apply { remove(entity) }
            if (entitiesByChunk.isEmpty()) byChunk.remove(chunk.position.toLong())
            world.scoreboard.removeEntity(entity)
        }
    }

    fun load(chunk: KryptonChunk) {
        val nbt = regionFileManager.read(chunk.x, chunk.z) ?: return
        val version = if (nbt.contains("DataVersion", IntTag.ID)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !world.server.config.advanced.useDataConverter) {
            DataConversion.sendWarning(LOGGER, "entities for chunk at ${chunk.x}, ${chunk.z}")
            error("Tried to load old chunk from version $version when data conversion is disabled!")
        }

        DataConversion.upgrade(nbt, MCTypeRegistry.ENTITY_CHUNK, version).getList("Entities", CompoundTag.ID).forEachCompound {
            val id = it.getString("id")
            if (id.isBlank()) return@forEachCompound
            val key = try {
                Key.key(id)
            } catch (_: InvalidKeyException) {
                return@forEachCompound
            }
            val type = Registries.ENTITY_TYPE.get(key)
            val entity = EntityFactory.create(type, world) ?: return@forEachCompound
            EntityFactory.serializer(entity.type).load(entity, it)
            spawn(entity)
        }
    }

    fun save(chunk: KryptonChunk) {
        val entities = byChunk.get(chunk.position.toLong()) ?: return
        if (entities.isEmpty()) return
        val entityList = ListTag.immutableBuilder(CompoundTag.ID)
        entities.forEach {
            if (it is KryptonPlayer) return@forEach
            entityList.add(EntityFactory.serializer(it.type).saveWithPassengers(it).build())
        }
        regionFileManager.write(chunk.x, chunk.z, compound {
            int("DataVersion", KryptonPlatform.worldVersion)
            ints("Position", chunk.position.x, chunk.position.z)
            put("Entities", entityList.build())
        })
    }

    fun flush() {
        regionFileManager.flush()
    }

    override fun close() {
        regionFileManager.close()
    }

    private inline fun forEachEntityInRange(location: Vector3d, viewDistance: Int, callback: (KryptonEntity) -> Unit) {
        location.chunksInRange(viewDistance).forEach {
            val chunk = world.getChunkAt(it.chunkX(), it.chunkZ()) ?: return@forEach
            getByChunk(chunk.position.toLong()).forEach(callback)
        }
    }

    companion object {

        private val LOGGER = logger<EntityManager>()
    }
}

private fun Long.chunkX(): Int = (this and 4294967295L).toInt()

private fun Long.chunkZ(): Int = (this ushr 32 and 4294967295L).toInt()

private fun Vector3d.chunksInRange(range: Int): LongArray {
    val area = (range * 2 + 1) * (range * 2 + 1)
    val visible = LongArray(area)
    var xDistance = 0
    var xDirection = 1
    var zDistance = 0
    var zDirection = -1
    var length = 1
    var corner = 0

    for (i in 0 until area) {
        val chunkX = (xDistance * 16 + x()).toChunkCoordinate()
        val chunkZ = (zDistance * 16 + z()).toChunkCoordinate()
        visible[i] = ChunkPosition.toLong(chunkX, chunkZ)

        if (corner % 2 == 0) {
            xDistance += xDirection
            if (kotlin.math.abs(xDistance) == length) {
                corner++
                xDirection = -xDirection
            }
        } else {
            zDistance += zDirection
            if (kotlin.math.abs(zDistance) == length) {
                corner++
                zDirection = -zDirection
                if (corner % 4 == 0) length++
            }
        }
    }
    return visible
}

private fun Double.toChunkCoordinate(): Int = Math.floorDiv(Maths.floor(this), 16)
