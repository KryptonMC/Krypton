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
package org.kryptonmc.krypton.server

import com.google.common.hash.Hashing
import com.mojang.serialization.Dynamic
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.event.login.JoinEvent
import org.kryptonmc.api.event.play.QuitEvent
import org.kryptonmc.api.util.floor
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareCommands
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutDestroyEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutJoinGame
import org.kryptonmc.krypton.packet.out.play.PacketOutKeepAlive
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerPositionAndLook
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutServerDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutTags
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutUnlockRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutWindowItems
import org.kryptonmc.krypton.packet.out.play.UnlockRecipesAction
import org.kryptonmc.krypton.network.Session
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.nextInt
import org.kryptonmc.krypton.util.threadFactory
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.toProtocol
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.chunk.ticket.TicketTypes
import org.kryptonmc.krypton.world.data.PlayerDataManager
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.random.Random

class PlayerManager(private val server: KryptonServer) : ForwardingAudience {

    private val dataManager = PlayerDataManager(CURRENT_DIRECTORY.resolve(server.config.world.name).resolve("playerdata"))
    private val keepAliveExecutor = Executors.newScheduledThreadPool(4, threadFactory("Keep Alive Scheduler"))
    val players = CopyOnWriteArrayList<KryptonPlayer>()
    val playersByName = ConcurrentHashMap<String, KryptonPlayer>()
    val playersByUUID = ConcurrentHashMap<UUID, KryptonPlayer>()

    private val registryHolder = server.registryHolder

    val status = ServerStatus(server.status.motd, ServerStatus.Players(server.status.maxPlayers, players.size), null)
    private var lastStatus = 0L

    fun add(player: KryptonPlayer, session: Session): CompletableFuture<Void> = dataManager.load(player).thenAccept { nbt ->
        val profile = player.profile

        val dimension = if (nbt != null) Dynamic(NBTOps, nbt["Dimension"]).parseDimension().resultOrPartial(LOGGER::error).orElse(World.OVERWORLD) else World.OVERWORLD
        val world = server.worldManager.worlds[dimension] ?: kotlin.run {
            LOGGER.warn("Unknown respawn dimension $dimension! Defaulting to overworld...")
            server.overworld()
        }
        player.world = world
        world.players += player
        LOGGER.info("Player ${profile.name} logged in with entity ID ${player.id} at (${player.location.x}, ${player.location.y}, ${player.location.z})")

        // Join the game
        player.updateAbilities()
        val reducedDebugInfo = world.gameRules[GameRules.REDUCED_DEBUG_INFO]
        val doImmediateRespawn = world.gameRules[GameRules.DO_IMMEDIATE_RESPAWN]
        session.sendPacket(PacketOutJoinGame(
            player.id,
            world.data.isHardcore,
            player.gamemode,
            player.oldGamemode,
            server.worldManager.worlds.keys,
            registryHolder,
            world.dimensionType,
            world.dimension,
            Hashing.sha256().hashLong(server.worldData.worldGenerationSettings.seed).asLong(),
            server.status.maxPlayers,
            server.config.world.viewDistance,
            reducedDebugInfo,
            doImmediateRespawn,
            world.isDebug,
            server.worldData.worldGenerationSettings.isFlat
        ))
        session.sendPacket(PacketOutPluginMessage(BRAND_KEY, BRAND_MESSAGE))
        session.sendPacket(PacketOutServerDifficulty(world.difficulty))

        // Player data stuff
        session.sendPacket(PacketOutAbilities(player.abilities))
        session.sendPacket(PacketOutChangeHeldItem(player.inventory.heldSlot))
        session.sendPacket(PacketOutDeclareCommands(server.commandManager.dispatcher.root))
        session.sendPacket(PacketOutDeclareRecipes)
        session.sendPacket(PacketOutUnlockRecipes(UnlockRecipesAction.INIT))
        session.sendPacket(PacketOutTags(server.resources.tags.write(registryHolder)))
        session.sendPacket(PacketOutEntityStatus(player.id, if (world.gameRules[GameRules.REDUCED_DEBUG_INFO]) 22 else 23))
        sendOperatorStatus(player)
        invalidateStatus()

        // Fire join event and send result message
        val joinResult = server.eventManager.fireSync(JoinEvent(player)).result
        if (!joinResult.isAllowed) {
            // Use default reason if denied without specified reason
            val reason = joinResult.reason.takeIf { it != Component.empty() } ?: Component.translatable("multiplayer.disconnect.kicked")
            session.disconnect(reason)
            return@thenAccept
        }
        server.sendMessage(joinResult.reason)
        session.sendPacket(PacketOutPlayerPositionAndLook(player.location))

        // Update player list
        sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, player))
        session.sendPacket(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, player))
        session.sendPacket(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, players))

        // Send entity data
        // TODO: Move this all to an entity manager
        sendToAll(PacketOutSpawnPlayer(player))
        players.forEach { session.sendPacket(PacketOutSpawnPlayer(it)) }
        sendToAll(PacketOutMetadata(player.id, player.data.all))
        players.forEach { session.sendPacket(PacketOutMetadata(it.id, it.data.all)) }
        sendToAll(PacketOutAttributes(player.id, player.attributes.syncable))
        players.forEach { session.sendPacket(PacketOutAttributes(it.id, it.attributes.syncable)) }
        sendToAll(PacketOutHeadLook(player.id, player.location.yaw.toAngle()))
        players.forEach { session.sendPacket(PacketOutHeadLook(it.id, it.location.yaw.toAngle())) }

        // Add the player to the list and cache maps
        players += player
        playersByName[player.name] = player
        playersByUUID[player.uuid] = player

        // Send the initial chunk stream
        val location = player.location
        val chunkX = location.blockX shr 4
        val chunkZ = location.blockZ shr 4
        val chunkPosition = ChunkPosition(chunkX, chunkZ)
        session.sendPacket(PacketOutUpdateViewPosition(chunkX, chunkZ))
        world.chunkCache.addRegionTicket(TicketTypes.LOGIN, chunkPosition, 31, chunkPosition.toLong())
        world.chunkCache[chunkX, chunkZ].thenApply {
            world.chunkCache.chunkManager.getChunkIfPresent(ChunkPosition.toLong(chunkX, chunkZ))?.entityTickingFuture ?: CompletableFuture.completedFuture(it)
        }.thenAccept {
            world.chunkCache.trackPlayer(player)

            // TODO: Custom boss events, texture pack, mob effects
            sendWorldInfo(world, player)

            // Send inventory data
            val items = player.inventory.networkItems
            session.sendPacket(PacketOutWindowItems(player.inventory.id, player.inventory.incrementStateId(), items, player.inventory.mainHand))

            ServerStorage.PLAYER_COUNT.getAndIncrement()

            keepAliveExecutor.scheduleAtFixedRate({
                val keepAliveId = System.currentTimeMillis()
                session.lastKeepAliveId = keepAliveId
                session.sendPacket(PacketOutKeepAlive(keepAliveId))
            }, 0, 20, TimeUnit.SECONDS)
        }
    }

    fun remove(player: KryptonPlayer) {
        server.eventManager.fire(QuitEvent(player)).thenAccept {
            dataManager.save(player)

            // Remove from caches
            players.remove(player)
            playersByName.remove(player.name)
            playersByUUID.remove(player.uuid)

            // Send destroy and info
            sendToAll(PacketOutDestroyEntities(player.id))
            sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.REMOVE_PLAYER, player))

            // Send quit message and decrement overall count
            server.sendMessage(it.message)
            ServerStorage.PLAYER_COUNT.getAndDecrement()
        }
    }

    fun sendToAll(packet: Packet) = players.forEach { it.session.sendPacket(packet) }

    fun sendToAll(packet: Packet, except: KryptonPlayer) = players.forEach {
        if (it != except) it.session.sendPacket(packet)
    }

    fun sendToAll(packet: Packet, world: KryptonWorld) = players.forEach {
        if (it.world == world) it.session.sendPacket(packet)
    }

    fun broadcast(packet: Packet, world: KryptonWorld, x: Double, y: Double, z: Double, radius: Double, except: KryptonPlayer) = players.forEach {
        if (it == except || it.world != world) return@forEach
        val offsetX = x - it.location.x
        val offsetY = y - it.location.y
        val offsetZ = z - it.location.z
        if (offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ < radius * radius) it.session.sendPacket(packet)
    }

    fun broadcast(packet: Packet, world: KryptonWorld, position: Vector3d, radius: Double, except: KryptonPlayer) = broadcast(packet, world, position.x(), position.y(), position.z(), radius, except)

    fun broadcast(packet: Packet, world: KryptonWorld, position: Vector3i, radius: Double, except: KryptonPlayer) = broadcast(packet, world, position.x().toDouble(), position.y().toDouble(), position.z().toDouble(), radius, except)

    fun disconnectAll() = players.forEach {
        it.session.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"))
    }

    private fun saveAll() = players.forEach { dataManager.save(it) }

    fun shutdown() {
        saveAll()
        keepAliveExecutor.shutdown()
    }

    fun tick(time: Long) {
        if (time - lastStatus >= UPDATE_STATUS_INTERVAL) {
            lastStatus = time
            status.players.online = players.size
            val sampleSize = min(players.size, MAXIMUM_SAMPLED_PLAYERS)
            val playerOffset = nextInt(Random, 0, players.size - sampleSize)
            val sample = Array(sampleSize) { players[it + playerOffset].profile }.apply { shuffle() }
            status.players.sample = sample
        }
    }

    fun invalidateStatus() {
        lastStatus = 0L
    }

    private fun sendOperatorStatus(player: KryptonPlayer) {
        // TODO: Get status from ops.json and send it to the client
    }

    private fun sendWorldInfo(world: KryptonWorld, player: KryptonPlayer) {
        player.session.sendPacket(PacketOutInitializeWorldBorder(world.border))
        player.session.sendPacket(PacketOutTimeUpdate(world.data.time, world.data.dayTime, world.data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]))
        player.session.sendPacket(PacketOutSpawnPosition(world.data.spawnX, world.data.spawnY, world.data.spawnZ, world.data.spawnAngle))
        if (world.isRaining) {
            player.session.sendPacket(PacketOutChangeGameState(GameState.BEGIN_RAINING))
            player.session.sendPacket(PacketOutChangeGameState(GameState.RAIN_LEVEL_CHANGE, world.getRainLevel(1F)))
            player.session.sendPacket(PacketOutChangeGameState(GameState.THUNDER_LEVEL_CHANGE, world.getThunderLevel(1F)))
        }
    }

    override fun audiences() = players

    companion object {

        private const val UPDATE_STATUS_INTERVAL = 5000L
        private const val MAXIMUM_SAMPLED_PLAYERS = 12
        private val LOGGER = logger<PlayerManager>()
        private val BRAND_KEY = Key.key("brand")
        private val BRAND_MESSAGE = "Krypton".toProtocol()
    }
}
