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
import org.kryptonmc.api.event.player.JoinEvent
import org.kryptonmc.api.event.player.QuitEvent
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.Session
import org.kryptonmc.krypton.network.handlers.PlayHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutJoinGame
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
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
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.server.ban.BannedIpList
import org.kryptonmc.krypton.server.ban.BannedPlayerList
import org.kryptonmc.krypton.server.op.OperatorEntry
import org.kryptonmc.krypton.server.op.OperatorList
import org.kryptonmc.krypton.server.whitelist.Whitelist
import org.kryptonmc.krypton.server.whitelist.WhitelistedIps
import org.kryptonmc.krypton.statistic.KryptonStatisticsTracker
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.createDirectory
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.util.nextInt
import org.kryptonmc.krypton.util.toAngle
import org.kryptonmc.krypton.util.toProtocol
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.data.PlayerDataManager
import org.kryptonmc.krypton.world.data.WorldResource
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.spongepowered.math.GenericMath
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.nio.file.Path
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.min
import kotlin.random.Random

class PlayerManager(private val server: KryptonServer) : ForwardingAudience {

    private val dataManager = PlayerDataManager(CURRENT_DIRECTORY.resolve(server.config.world.name).resolve("playerdata"))
    val players = CopyOnWriteArrayList<KryptonPlayer>()
    val playersByName = ConcurrentHashMap<String, KryptonPlayer>()
    val playersByUUID = ConcurrentHashMap<UUID, KryptonPlayer>()
    private val statistics = ConcurrentHashMap<UUID, KryptonStatisticsTracker>()

    private val registryHolder = server.registryHolder
    val bannedPlayers = BannedPlayerList(Path.of("banned-players.json"))
    val whitelist = Whitelist(Path.of("whitelist.json"))
    val bannedIps = BannedIpList(Path.of("banned-ips.json"))
    val ops = OperatorList(Path.of("ops.json"))
    val whitlistedIps = WhitelistedIps(Path.of("whitelisted-ips.json"))
    var whitelistEnabled = server.config.server.whitelistEnabled
        set(value) {
            field = value
            server.config.server.whitelistEnabled = value
            server.updateConfig()
        }

    val status = ServerStatus(server.motd, ServerStatus.Players(server.maxPlayers, players.size), null)
    private var lastStatus = 0L

    fun add(player: KryptonPlayer, session: Session): CompletableFuture<Void> = dataManager.load(player).thenAccept { nbt ->
        val profile = player.profile
        val name = server.profileCache[profile.uuid]?.name ?: profile.name
        server.profileCache.add(profile)
        val dimension = if (nbt != null) Dynamic(NBTOps, nbt["Dimension"]).parseDimension().resultOrPartial(LOGGER::error).orElse(World.OVERWORLD) else World.OVERWORLD
        val world = server.worldManager.worlds[dimension] ?: kotlin.run {
            LOGGER.warn("Unknown respawn dimension $dimension! Defaulting to overworld...")
            server.overworld()
        }
        player.world = world
        world.players += player
        LOGGER.info("Player ${profile.name} logged in with entity ID ${player.id} at (${player.location.x}, ${player.location.y}, ${player.location.z})")

        // Join the game
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
            server.maxPlayers,
            server.config.world.viewDistance,
            reducedDebugInfo,
            doImmediateRespawn,
            world.isDebug,
            server.worldData.worldGenerationSettings.isFlat
        ))
        session.sendPacket(PacketOutPluginMessage(BRAND_KEY, BRAND_MESSAGE))
        session.sendPacket(PacketOutServerDifficulty(world.difficulty))

        // Player data stuff
        session.sendPacket(PacketOutAbilities(player))
        session.sendPacket(PacketOutChangeHeldItem(player.inventory.heldSlot))
        session.sendPacket(PacketOutDeclareRecipes)
        session.sendPacket(PacketOutUnlockRecipes(UnlockRecipesAction.INIT))
        session.sendPacket(PacketOutTags(server.resources.tags.write(registryHolder)))
        session.sendPacket(PacketOutEntityStatus(player.id, if (world.gameRules[GameRules.REDUCED_DEBUG_INFO]) 22 else 23))
        sendCommands(player)
        player.statistics.invalidate()
        invalidateStatus()

        // Fire join event and send result message
        val joinResult = server.eventManager.fireSync(JoinEvent(player, profile.name.equals(name, true))).result
        if (!joinResult.isAllowed) {
            // Use default reason if denied without specified reason
            val reason = joinResult.message.takeIf { it != Component.empty() } ?: Component.translatable("multiplayer.disconnect.kicked")
            session.disconnect(reason)
            return@thenAccept
        }
        server.sendMessage(joinResult.message)
        session.sendPacket(PacketOutPlayerPositionAndLook(player.location))

        // Update player list
        sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, player))
        session.sendPacket(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, player))
        session.sendPacket(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, players))

        // Send entity data
        // TODO: Move this all to an entity manager
        sendToAll(PacketOutSpawnPlayer(player))
        sendToAll(PacketOutMetadata(player.id, player.data.all))
        sendToAll(PacketOutAttributes(player.id, player.attributes.values.filter { it.type.sendToClient }))
        sendToAll(PacketOutHeadLook(player.id, player.location.yaw.toAngle()))
        players.forEach { online ->
            session.sendPacket(PacketOutSpawnPlayer(online))
            session.sendPacket(PacketOutMetadata(online.id, online.data.all))
            session.sendPacket(PacketOutAttributes(online.id, online.attributes.values.filter { it.type.sendToClient }))
            session.sendPacket(PacketOutHeadLook(online.id, online.location.yaw.toAngle()))
        }

        // Add the player to the list and cache maps
        players += player
        playersByName[player.name] = player
        playersByUUID[player.uuid] = player
        world.addEntity(player)

        // Send the initial chunk stream
        player.updateChunks(true)

        // TODO: Custom boss events, resource pack pack, mob effects
        sendWorldInfo(world, player)

        // Send inventory data
        session.sendPacket(PacketOutWindowItems(player.inventory.id, player.inventory.incrementStateId(), player.inventory.networkWriter, player.inventory.mainHand))
    }

    fun remove(player: KryptonPlayer) {
        server.eventManager.fire(QuitEvent(player)).thenAccept {
            player.incrementStatistic(CustomStatistics.LEAVE_GAME)
            save(player)
            player.world.chunkManager.removePlayer(player)

            // Remove from caches
            player.world.removeEntity(player)
            players.remove(player)

            if (playersByUUID[player.uuid] == player) {
                playersByName.remove(player.name)
                playersByUUID.remove(player.uuid)
                statistics.remove(player.uuid)
            }

            // Send info and quit message
            sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.REMOVE_PLAYER, player))
            server.sendMessage(it.message)
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

    fun saveAll() = players.forEach { dataManager.save(it) }

    fun addToOperators(profile: KryptonGameProfile) {
        ops += OperatorEntry(profile, server.config.server.opPermissionLevel, true)
        if (server.player(profile.uuid) != null) sendCommands(server.player(profile.uuid)!!)
    }

    fun removeFromOperators(profile: KryptonGameProfile) {
        ops -= profile
        if (server.player(profile.uuid) != null) sendCommands(server.player(profile.uuid)!!)
    }

    fun getStatistics(player: KryptonPlayer): KryptonStatisticsTracker {
        val uuid = player.uuid
        statistics[uuid]?.let { return it }
        val folder = server.worldResource(WorldResource.STATISTICS_FOLDER).createDirectory()
        // TODO: Deal with the old data files
        return KryptonStatisticsTracker(player, folder.resolve("$uuid.json")).apply { this@PlayerManager.statistics[uuid] = this }
    }

    fun sendCommands(player: KryptonPlayer) {
        val permissionLevel = player.server.getPermissionLevel(player.profile)
        sendCommands(player, permissionLevel)
    }

    private fun sendCommands(player: KryptonPlayer, permissionLevel: Int) {
        val status = (permissionLevel + 24).clamp(24, 28)
        player.session.sendPacket(PacketOutEntityStatus(player.id, status))
        server.commandManager.sendCommands(player)
    }

    private fun save(player: KryptonPlayer) {
        dataManager.save(player)
        statistics[player.uuid]?.save()
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
        players.forEach { (it.session.handler as? PlayHandler)?.tick() }
    }

    fun invalidateStatus() {
        lastStatus = 0L
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
