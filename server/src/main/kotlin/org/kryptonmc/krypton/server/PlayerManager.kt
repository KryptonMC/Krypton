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
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.event.player.JoinEvent
import org.kryptonmc.api.event.player.QuitEvent
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.network.handlers.PlayHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutDeclareRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityStatus
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutJoinGame
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerPositionAndLook
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutTags
import org.kryptonmc.krypton.packet.out.play.PacketOutTeam
import org.kryptonmc.krypton.packet.out.play.PacketOutTimeUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutUnlockRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutWindowItems
import org.kryptonmc.krypton.packet.out.status.ServerStatus
import org.kryptonmc.krypton.server.ban.BannedIpList
import org.kryptonmc.krypton.server.ban.BannedPlayerList
import org.kryptonmc.krypton.server.whitelist.Whitelist
import org.kryptonmc.krypton.server.whitelist.WhitelistedIps
import org.kryptonmc.krypton.statistic.KryptonStatisticsTracker
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.tryCreateDirectory
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.data.PlayerDataManager
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.spongepowered.math.vector.Vector3i
import java.nio.file.Path
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.min
import kotlin.random.Random

class PlayerManager(private val server: KryptonServer) : ForwardingAudience {

    val dataManager = PlayerDataManager(Path.of(server.config.world.name).resolve("playerdata"))
    val players = CopyOnWriteArrayList<KryptonPlayer>()
    val playersByName = ConcurrentHashMap<String, KryptonPlayer>()
    val playersByUUID = ConcurrentHashMap<UUID, KryptonPlayer>()
    private val statistics = ConcurrentHashMap<UUID, KryptonStatisticsTracker>()

    val bannedPlayers = BannedPlayerList(Path.of("banned-players.json"))
    val whitelist = Whitelist(Path.of("whitelist.json"))
    val bannedIps = BannedIpList(Path.of("banned-ips.json"))
    val whitelistedIps = WhitelistedIps(Path.of("whitelisted-ips.json"))
    var whitelistEnabled = server.config.server.whitelistEnabled
        set(value) {
            field = value
            server.updateConfig("server.whitelist-enabled", value)
        }

    val status = ServerStatus(server.motd, ServerStatus.Players(server.maxPlayers, players.size), null)
    private var lastStatus = 0L

    fun add(player: KryptonPlayer, session: SessionHandler): CompletableFuture<Void> = dataManager.load(player).thenAccept { nbt ->
        val profile = player.profile
        val name = server.profileCache[profile.uuid]?.name ?: profile.name
        server.profileCache.add(profile)
        val dimension = if (nbt != null) nbt["Dimension"]?.parseDimension() ?: World.OVERWORLD else World.OVERWORLD
        if (nbt != null) server.userManager.updateUser(profile.uuid, nbt)

        val world = server.worldManager.worlds[dimension] ?: kotlin.run {
            LOGGER.warn("Unknown respawn dimension $dimension! Defaulting to overworld...")
            server.worldManager.default
        }
        player.world = world
        world.players.add(player)
        LOGGER.info("Player ${profile.name} logged in with entity ID ${player.id} at" +
                "(${player.location.x()}, ${player.location.y()}, ${player.location.z()})")

        // Join the game
        val reducedDebugInfo = world.gameRules[GameRules.REDUCED_DEBUG_INFO]
        val doImmediateRespawn = world.gameRules[GameRules.DO_IMMEDIATE_RESPAWN]
        session.send(PacketOutJoinGame(
            player.id,
            world.data.isHardcore,
            player.gameMode,
            player.oldGameMode,
            server.worldManager.worlds.keys,
            world.dimensionType,
            world.dimension,
            Hashing.sha256().hashLong(server.worldManager.default.data.worldGenerationSettings.seed).asLong(),
            server.maxPlayers,
            server.config.world.viewDistance,
            server.config.world.simulationDistance,
            reducedDebugInfo,
            doImmediateRespawn,
            world.isDebug,
            server.worldManager.default.data.worldGenerationSettings.isFlat
        ))
        session.send(PacketOutPluginMessage(BRAND_KEY, BRAND_MESSAGE))
        session.send(PacketOutDifficulty(world.difficulty))

        // Player data stuff
        session.send(PacketOutAbilities(player))
        session.send(PacketOutChangeHeldItem(player.inventory.heldSlot))
        session.send(PacketOutDeclareRecipes)
        session.send(PacketOutUnlockRecipes(PacketOutUnlockRecipes.Action.INIT))
        session.send(PacketOutTags)
        session.send(PacketOutEntityStatus(player.id, if (reducedDebugInfo) 22 else 23))
        sendCommands(player)
        player.statistics.invalidate()
        updateScoreboard(world.scoreboard, player)
        invalidateStatus()

        // Add the player to the list and cache maps
        players.add(player)
        playersByName[player.profile.name] = player
        playersByUUID[player.uuid] = player

        // Fire join event and send result message
        val joinResult = server.eventManager.fireSync(JoinEvent(player, !profile.name.equals(name, true))).result
        if (!joinResult.isAllowed) {
            // Use default reason if denied without specified reason
            val reason = joinResult.message ?: Component.translatable("multiplayer.disconnect.kicked")
            session.disconnect(reason)
            return@thenAccept
        }
        val joinMessage = joinResult.message ?: Component.translatable(
            if (joinResult.hasJoinedBefore) "multiplayer.player.joined.renamed" else "multiplayer.player.joined",
            NamedTextColor.YELLOW,
            player.displayName
        )
        server.sendMessage(joinMessage)
        session.send(PacketOutPlayerPositionAndLook(player.location, player.rotation))
        session.send(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.ADD_PLAYER, player))
        world.spawnPlayer(player)

        // Send the initial chunk stream
        player.updateChunks(true)

        // TODO: Custom boss events, resource pack pack, mob effects
        sendWorldInfo(world, player)
        if (server.config.server.resourcePack.uri.isNotEmpty()) {
            val resourcePack = server.config.server.resourcePack
            player.session.send(PacketOutResourcePack(resourcePack.uri, resourcePack.hash, resourcePack.forced, resourcePack.prompt))
        }

        // Send inventory data
        session.send(PacketOutWindowItems(player.inventory, player.inventory.mainHand))
        player.isLoaded = true
    }

    fun remove(player: KryptonPlayer) {
        server.eventManager.fire(QuitEvent(player)).thenAccept {
            player.statistics.increment(CustomStatistics.LEAVE_GAME)
            save(player)
            player.world.chunkManager.removePlayer(player)

            // Remove from caches
            player.world.removeEntity(player)
            players.remove(player)
            player.world.players.remove(player)

            if (playersByUUID[player.uuid] === player) {
                playersByName.remove(player.profile.name)
                playersByUUID.remove(player.uuid)
                statistics.remove(player.uuid)
            }

            // Send info and quit message
            invalidateStatus()
            sendToAll(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.REMOVE_PLAYER, player))
            server.sendMessage(it.result.reason)
        }
    }

    fun sendToAll(packet: Packet) {
        players.forEach { it.session.send(packet) }
    }

    fun sendToAll(packet: Packet, except: KryptonPlayer) {
        players.forEach { if (it != except) it.session.send(packet) }
    }

    fun sendToAll(packet: Packet, world: KryptonWorld) {
        players.forEach { if (it.world == world) it.session.send(packet) }
    }

    fun broadcast(packet: Packet, world: KryptonWorld, x: Double, y: Double, z: Double, radius: Double, except: KryptonPlayer?) {
        players.forEach {
            if (it == except || it.world != world) return@forEach
            val offsetX = x - it.location.x()
            val offsetY = y - it.location.y()
            val offsetZ = z - it.location.z()
            if (offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ < radius * radius) it.session.send(packet)
        }
    }

    fun broadcast(packet: Packet, world: KryptonWorld, position: Vector3i, radius: Double, except: KryptonPlayer?) {
        broadcast(packet, world, position.x().toDouble(), position.y().toDouble(), position.z().toDouble(), radius, except)
    }

    fun broadcast(packet: Packet, world: KryptonWorld, x: Int, y: Int, z: Int, radius: Double, except: KryptonPlayer?) {
        broadcast(packet, world, x.toDouble(), y.toDouble(), z.toDouble(), radius, except)
    }

    fun disconnectAll() {
        players.forEach { it.session.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown")) }
    }

    fun saveAll() {
        players.forEach(::save)
    }

    fun getStatistics(player: KryptonPlayer): KryptonStatisticsTracker {
        val uuid = player.uuid
        statistics[uuid]?.let { return it }
        val folder = server.worldManager.default.folder.resolve("stats").tryCreateDirectory()
        // TODO: Deal with the old data files
        return KryptonStatisticsTracker(player, folder.resolve("$uuid.json")).apply {
            this@PlayerManager.statistics[uuid] = this
        }
    }

    private fun sendCommands(player: KryptonPlayer) {
        player.session.send(PacketOutEntityStatus(player.id, 28))
        server.commandManager.updateCommands(player)
    }

    private fun save(player: KryptonPlayer) {
        server.userManager.updateUser(player.uuid, dataManager.save(player))
        statistics[player.uuid]?.save()
    }

    fun tick(time: Long) {
        if (time - lastStatus >= UPDATE_STATUS_INTERVAL) {
            lastStatus = time
            status.players.online = players.size
            val sampleSize = min(players.size, MAXIMUM_SAMPLED_PLAYERS)
            val playerOffset = Maths.randomBetween(Random, 0, players.size - sampleSize)
            val sample = Array(sampleSize) { players[it + playerOffset].profile }.apply { shuffle() }
            status.players.sample = sample
        }
        players.forEach { (it.session.handler as? PlayHandler)?.tick() }
    }

    fun invalidateStatus() {
        lastStatus = 0L
    }

    private fun sendWorldInfo(world: KryptonWorld, player: KryptonPlayer) {
        player.session.send(PacketOutInitializeWorldBorder(world.border))
        player.session.send(PacketOutTimeUpdate(world.data.time, world.data.dayTime, world.data.gameRules[GameRules.DO_DAYLIGHT_CYCLE]))
        player.session.send(PacketOutSpawnPosition(world.data.spawnX, world.data.spawnY, world.data.spawnZ, world.data.spawnAngle))
        if (world.isRaining) {
            player.session.send(PacketOutChangeGameState(GameState.BEGIN_RAINING))
            player.session.send(PacketOutChangeGameState(GameState.RAIN_LEVEL_CHANGE, world.getRainLevel(1F)))
            player.session.send(PacketOutChangeGameState(GameState.THUNDER_LEVEL_CHANGE, world.getThunderLevel(1F)))
        }
    }

    private fun updateScoreboard(scoreboard: KryptonScoreboard, player: KryptonPlayer) {
        val objectives = mutableSetOf<Objective>()
        scoreboard.teams.forEach { player.session.send(PacketOutTeam(PacketOutTeam.Action.CREATE, it, it.members)) }
        scoreboard.displayObjectives.forEach { (_, objective) ->
            if (objectives.contains(objective)) return@forEach
            scoreboard.getStartTrackingPackets(objective).forEach { player.session.send(it) }
            objectives.add(objective)
        }
    }

    override fun audiences(): Iterable<Audience> = players

    companion object {

        private const val UPDATE_STATUS_INTERVAL = 5000L
        private const val MAXIMUM_SAMPLED_PLAYERS = 12
        private val LOGGER = logger<PlayerManager>()
        private val BRAND_KEY = Key.key("brand")
        // The word "Krypton" encoded in to UTF-8 and then prefixed with the length, which in this case is 7.
        private val BRAND_MESSAGE = byteArrayOf(7, 75, 114, 121, 112, 116, 111, 110)
    }
}
