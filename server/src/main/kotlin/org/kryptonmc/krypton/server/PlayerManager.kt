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
package org.kryptonmc.krypton.server

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.player.KryptonJoinEvent
import org.kryptonmc.krypton.event.player.KryptonQuitEvent
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.FramedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutLogin
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetDefaultSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutSynchronizePlayerPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipeBook
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTags
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTeams
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.server.ban.BannedIpList
import org.kryptonmc.krypton.server.ban.BannedPlayerList
import org.kryptonmc.krypton.server.whitelist.Whitelist
import org.kryptonmc.krypton.server.whitelist.WhitelistedIps
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.frame
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.pool.daemonThreadFactory
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.data.PlayerDataManager
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.kryptonmc.serialization.Dynamic
import org.kryptonmc.serialization.nbt.NbtOps
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

class PlayerManager(private val server: KryptonServer) {

    private val executor = Executors.newFixedThreadPool(8, daemonThreadFactory("Player Executor #%d"))
    val dataManager: PlayerDataManager
    val players: MutableList<KryptonPlayer> = CopyOnWriteArrayList()
    val playersByName: MutableMap<String, KryptonPlayer> = ConcurrentHashMap()
    val playersByUUID: MutableMap<UUID, KryptonPlayer> = ConcurrentHashMap()

    val bannedPlayers: BannedPlayerList = BannedPlayerList(Path.of("banned-players.json"))
    val whitelist: Whitelist = Whitelist(Path.of("whitelist.json"))
    val bannedIps: BannedIpList = BannedIpList(Path.of("banned-ips.json"))
    val whitelistedIps: WhitelistedIps = WhitelistedIps(Path.of("whitelisted-ips.json"))
    var whitelistEnabled: Boolean = server.config.server.whitelistEnabled
        set(value) {
            field = value
            server.updateConfig("server.whitelist-enabled", value)
        }

    private val brandPacket by lazy { FramedPacket(PacketOutPluginMessage(BRAND_KEY, BRAND_MESSAGE).frame()) }

    init {
        val playerDataFolder = Path.of(server.config.world.name).resolve("playerdata")
        if (server.config.advanced.serializePlayerData && !Files.exists(playerDataFolder)) {
            try {
                Files.createDirectories(playerDataFolder)
            } catch (exception: Exception) {
                LOGGER.error("Unable to create player data directory!", exception)
            }
        }
        dataManager = PlayerDataManager(playerDataFolder, server.config.advanced.serializePlayerData)
    }

    fun add(player: KryptonPlayer, session: SessionHandler): CompletableFuture<Void> = dataManager.load(player, executor).thenAcceptAsync({ nbt ->
        val profile = player.profile
        val name = server.profileCache.get(profile.uuid)?.name ?: profile.name
        server.profileCache.add(profile)
        val dimension = if (nbt != null) {
            KryptonDimensionType.parseLegacy(Dynamic(NbtOps.INSTANCE, nbt.get("Dimension"))).resultOrPartial(LOGGER::error).orElse(World.OVERWORLD)
        } else {
            World.OVERWORLD
        }
        if (nbt != null) server.userManager.updateUser(profile.uuid, nbt)

        val world = server.worldManager.worlds.get(dimension) ?: server.worldManager.default
        player.world = world
        world.players.add(player)
        val location = player.location
        LOGGER.info("Player ${profile.name} logged in with entity ID ${player.id} at $location")

        // Join the game
        val reducedDebugInfo = world.gameRules.get(GameRules.REDUCED_DEBUG_INFO)
        val doImmediateRespawn = world.gameRules.get(GameRules.DO_IMMEDIATE_RESPAWN)
        session.send(PacketOutLogin(
            player.id,
            world.data.isHardcore,
            player.gameMode,
            player.oldGameMode,
            server.worldManager.worlds.keys,
            PacketOutLogin.createRegistryCodec(),
            KryptonRegistries.DIMENSION_TYPE.getResourceKey(world.dimensionType)!!,
            world.dimension,
            BiomeManager.obfuscateSeed(world.seed),
            server.maxPlayers,
            server.config.world.viewDistance,
            server.config.world.simulationDistance,
            reducedDebugInfo,
            doImmediateRespawn,
            false,
            false,
            null
        ))
        session.write(brandPacket)
        session.write(world.cachedDifficultyPacket)

        // Player data stuff
        session.send(PacketOutAbilities(player.abilities))
        session.send(PacketOutSetHeldItem(player.inventory.heldSlot))
        session.write(PacketOutUpdateRecipes.CACHED)
        session.write(PacketOutUpdateRecipeBook.CACHED_INIT)
        session.write(PacketOutUpdateTags.CACHED)
        session.send(PacketOutEntityEvent(player.id, if (reducedDebugInfo) ENABLE_REDUCED_DEBUG_SCREEN else DISABLE_REDUCED_DEBUG_SCREEN))
        sendCommands(player)
        player.statistics.invalidate()
        updateScoreboard(world.scoreboard, player)
        server.sessionManager.invalidateStatus()

        // Add the player to the list and cache maps
        players.add(player)
        playersByName.put(player.profile.name, player)
        playersByUUID.put(player.uuid, player)

        // Fire join event and send result message
        val joinResult = server.eventManager.fireSync(KryptonJoinEvent(player, !profile.name.equals(name, true))).result
        if (!joinResult.isAllowed) {
            // Use default reason if denied without specified reason
            val reason = joinResult.message ?: Component.translatable("multiplayer.disconnect.kicked")
            session.disconnect(reason)
            return@thenAcceptAsync
        }
        val joinMessage = joinResult.message ?: Component.translatable(
            if (joinResult.hasJoinedBefore) "multiplayer.player.joined.renamed" else "multiplayer.player.joined",
            NamedTextColor.YELLOW,
            player.displayName
        )
        server.sendMessage(joinMessage)
        session.send(PacketOutSynchronizePlayerPosition(player))
        session.send(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.ADD_PLAYER, player))
        world.spawnPlayer(player)

        // Send the initial chunk stream
        player.chunkViewingSystem.loadInitialChunks()

        // TODO: Custom boss events, resource pack pack, mob effects
        sendWorldInfo(world, player)
        if (server.config.server.resourcePack.uri.isNotEmpty()) {
            val resourcePack = server.config.server.resourcePack
            player.session.send(PacketOutResourcePack(resourcePack.uri, resourcePack.hash, resourcePack.forced, resourcePack.prompt))
        }

        // Send inventory data
        session.send(PacketOutSetContainerContent(player.inventory, player.inventory.mainHand))
    }, executor)

    fun remove(player: KryptonPlayer) {
        server.eventManager.fire(KryptonQuitEvent(player)).thenAcceptAsync({ event ->
            player.statistics.increment(CustomStatistics.LEAVE_GAME)
            save(player)
            player.world.chunkManager.removePlayer(player)

            // Remove from caches
            player.world.removeEntity(player)
            player.world.players.remove(player)
            players.remove(player)
            playersByName.remove(player.profile.name)
            playersByUUID.remove(player.uuid)

            // Send info and quit message
            server.sessionManager.invalidateStatus()
            server.sessionManager.sendGrouped(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.REMOVE_PLAYER, player))
            if (event.quitMessage != null) server.sendMessage(event.quitMessage!!)
            server.sessionManager.remove(player.session)
        }, executor)
    }

    fun broadcast(packet: Packet, world: KryptonWorld, x: Double, y: Double, z: Double, radius: Double, except: KryptonPlayer?) {
        server.sessionManager.sendGrouped(packet) {
            if (it === except || it.world !== world) return@sendGrouped false
            val dx = x - it.location.x
            val dy = y - it.location.y
            val dz = z - it.location.z
            dx * dx + dy * dy + dz * dz < radius * radius
        }
    }

    fun broadcast(packet: Packet, world: KryptonWorld, x: Int, y: Int, z: Int, radius: Double, except: KryptonPlayer?) {
        broadcast(packet, world, x.toDouble(), y.toDouble(), z.toDouble(), radius, except)
    }

    fun broadcast(packet: Packet, world: KryptonWorld, pos: BlockPos, radius: Double, except: KryptonPlayer?) {
        broadcast(packet, world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), radius, except)
    }

    fun disconnectAll() {
        players.forEach { it.session.disconnect(Component.translatable("multiplayer.disconnect.server_shutdown")) }
    }

    fun saveAll() {
        players.forEach(::save)
    }

    private fun sendCommands(player: KryptonPlayer) {
        player.session.send(PacketOutEntityEvent(player.id, OP_PERMISSION_LEVEL_4))
        server.commandManager.updateCommands(player)
    }

    private fun save(player: KryptonPlayer) {
        val playerData = dataManager.save(player)
        if (playerData != null) server.userManager.updateUser(player.uuid, playerData)
        player.statistics.save()
    }

    private fun sendWorldInfo(world: KryptonWorld, player: KryptonPlayer) {
        player.session.send(PacketOutInitializeWorldBorder(world.border))
        player.session.send(PacketOutUpdateTime(world.data.time, world.data.dayTime, world.data.gameRules.get(GameRules.DO_DAYLIGHT_CYCLE)))
        player.session.send(PacketOutSetDefaultSpawnPosition(world.data.spawnX, world.data.spawnY, world.data.spawnZ, world.data.spawnAngle))
        if (world.isRaining) {
            player.session.send(PacketOutGameEvent(GameEvent.BEGIN_RAINING))
            player.session.send(PacketOutGameEvent(GameEvent.RAIN_LEVEL_CHANGE, world.getRainLevel(1F)))
            player.session.send(PacketOutGameEvent(GameEvent.THUNDER_LEVEL_CHANGE, world.getThunderLevel(1F)))
        }
    }

    private fun updateScoreboard(scoreboard: KryptonScoreboard, player: KryptonPlayer) {
        val objectives = HashSet<Objective>()
        scoreboard.teams.forEach { player.session.send(PacketOutUpdateTeams.create(it)) }
        for (objective in scoreboard.displayObjectives()) {
            if (objectives.contains(objective)) continue
            scoreboard.getStartTrackingPackets(objective).forEach(player.session::send)
            objectives.add(objective)
        }
    }

    companion object {

        private val LOGGER = logger<PlayerManager>()
        private val BRAND_KEY = Key.key("brand")
        // The word "Krypton" encoded in to UTF-8 and then prefixed with the length, which in this case is 7.
        private val BRAND_MESSAGE = byteArrayOf(7, 75, 114, 121, 112, 116, 111, 110)

        private const val ENABLE_REDUCED_DEBUG_SCREEN = 22
        private const val DISABLE_REDUCED_DEBUG_SCREEN = 23
        private const val OP_PERMISSION_LEVEL_4 = 28
    }
}
