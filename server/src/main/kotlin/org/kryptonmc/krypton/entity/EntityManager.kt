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
import org.kryptonmc.api.event.entity.EntityRemoveEvent
import org.kryptonmc.api.event.entity.EntitySpawnEvent
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.util.forEachEntityInRange
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.sendDataConversionWarning
import org.kryptonmc.krypton.util.upgradeData
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.KryptonChunk
import org.kryptonmc.krypton.world.region.RegionFileManager
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.compound
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

    operator fun get(id: Int): KryptonEntity? = byId[id]

    operator fun get(uuid: UUID): KryptonEntity? = byUUID[uuid]

    operator fun get(chunk: KryptonChunk): Set<KryptonEntity> =
        byChunk.computeIfAbsent(ChunkPosition.toLong(chunk.position.x, chunk.position.z), LongFunction { ConcurrentHashMap.newKeySet() })

    fun spawn(entity: KryptonEntity) {
        if (entity.world != world) return
        if (byUUID.containsKey(entity.uuid)) {
            LOGGER.error("UUID collision! UUID for entity with ID ${entity.id} collided with entity with ID ${byUUID[entity.uuid]?.id}!")
            LOGGER.warn("Refusing to spawn entity with ID ${entity.id}.")
            return
        }
        world.server.eventManager.fire(EntitySpawnEvent(entity, world)).thenAccept { event ->
            if (!event.result.isAllowed) return@thenAccept
            val location = entity.location

            forEachEntityInRange(location, world.server.config.world.viewDistance) {
                if (it is KryptonPlayer) entity.addViewer(it)
            }
            val chunk = world.getChunk(entity.location.floorX(), entity.location.floorY(), entity.location.floorZ()) ?: return@thenAccept
            byId[entity.id] = entity
            byUUID[entity.uuid] = entity
            byChunk.computeIfAbsent(chunk.position.toLong(), LongFunction { ConcurrentHashMap.newKeySet() }).add(entity)
        }
    }

    fun spawn(player: KryptonPlayer) {
        if (player.world != world) return
        val location = player.location

        // TODO: World border
        player.session.send(PacketOutTimeUpdate(world.data.time, world.data.dayTime, world.data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]))
        if (!player.isVanished) {
            forEachEntityInRange(location, player.viewDistance) {
                it.addViewer(player)
                if (it is KryptonPlayer && !it.isVanished) player.addViewer(it)
            }
        }

        val chunk = world.getChunk(player.location.floorX(), player.location.floorY(), player.location.floorZ()) ?: return
        byId[player.id] = player
        byUUID[player.uuid] = player
        byChunk.computeIfAbsent(chunk.position.toLong(), LongFunction { ConcurrentHashMap.newKeySet() }).add(player)
    }

    fun remove(entity: KryptonEntity) {
        if (entity.world != world) return
        world.server.eventManager.fire(EntityRemoveEvent(entity, world)).thenAccept { event ->
            if (!event.result.isAllowed) return@thenAccept
            forEachEntityInRange(entity.location, world.server.config.world.viewDistance) {
                if (it is KryptonPlayer) entity.removeViewer(it)
                if (entity is KryptonPlayer) it.removeViewer(entity)
            }

            val chunk = world.getChunk(entity.location.floorX(), entity.location.floorY(), entity.location.floorZ()) ?: return@thenAccept
            byId.remove(entity.id)
            byUUID.remove(entity.uuid)
            val entitiesByChunk = byChunk[chunk.position.toLong()].apply { remove(entity) }
            if (entitiesByChunk.isEmpty()) byChunk.remove(chunk.position.toLong())
            world.scoreboard.removeEntity(entity)
        }
    }

    fun load(chunk: KryptonChunk) {
        val nbt = regionFileManager.read(chunk.x, chunk.z) ?: return
        val version = if (nbt.contains("DataVersion", IntTag.ID)) nbt.getInt("DataVersion") else -1
        // We won't upgrade data if use of the data converter is disabled.
        if (version < KryptonPlatform.worldVersion && !world.server.config.server.useDataConverter) {
            LOGGER.sendDataConversionWarning("entities for chunk at ${chunk.x}, ${chunk.z}")
            error("Tried to load old chunk from version $version when data conversion is disabled!")
        }

        nbt.upgradeData(MCTypeRegistry.ENTITY_CHUNK, version).getList("Entities", CompoundTag.ID).forEachCompound {
            val id = it.getString("id")
            if (id.isBlank()) return@forEachCompound
            val key = try {
                Key.key(id)
            } catch (exception: InvalidKeyException) {
                return@forEachCompound
            }
            val type = Registries.ENTITY_TYPE[key]
            val entity = EntityFactory.create(type, world) ?: return@forEachCompound
            EntityFactory.serializer(entity.type).load(entity, it)
            spawn(entity)
        }
    }

    fun save(chunk: KryptonChunk) {
        val x = chunk.position.x
        val z = chunk.position.z
        val entities = byChunk[chunk.position.toLong()] ?: return
        if (entities.isEmpty()) return
        val entityList = MutableListTag(elementType = CompoundTag.ID)
        val root = compound {
            int("DataVersion", KryptonPlatform.worldVersion)
            ints("Position", x, z)
            put("Entities", entityList)
        }
        entities.forEach {
            if (it is KryptonPlayer) return@forEach // Do not save players in here.
            entityList.add(EntityFactory.serializer(it.type).saveWithPassengers(it).build())
        }
        regionFileManager.write(chunk.x, chunk.z, root)
    }

    fun flush() {
        regionFileManager.flush()
    }

    override fun close() {
        regionFileManager.close()
    }

    companion object {

        private val LOGGER = logger<EntityManager>()
    }
}
