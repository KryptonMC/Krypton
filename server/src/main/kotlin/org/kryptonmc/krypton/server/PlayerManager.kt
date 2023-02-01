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
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.event.player.KryptonJoinEvent
import org.kryptonmc.krypton.event.player.KryptonQuitEvent
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.OutgoingChatMessage
import org.kryptonmc.krypton.network.chat.PlayerChatMessage
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameEventTypes
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeDifficulty
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutInitializeWorldBorder
import org.kryptonmc.krypton.packet.out.play.PacketOutLogin
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerContent
import org.kryptonmc.krypton.packet.out.play.PacketOutSetDefaultSpawnPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeldItem
import org.kryptonmc.krypton.packet.out.play.PacketOutSynchronizePlayerPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEnabledFeatures
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipeBook
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateRecipes
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTeams
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTime
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.locale.DisconnectMessages
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateTags
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.registry.dynamic.LayeredRegistryAccess
import org.kryptonmc.krypton.registry.dynamic.RegistryAccess
import org.kryptonmc.krypton.registry.dynamic.RegistryLayer
import org.kryptonmc.krypton.registry.network.RegistrySerialization
import org.kryptonmc.krypton.tags.TagSerializer
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.biome.BiomeManager
import org.kryptonmc.krypton.world.data.PlayerDataManager
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.flag.FeatureFlags
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.kryptonmc.serialization.Dynamic
import org.kryptonmc.serialization.nbt.NbtOps
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.function.Function
import java.util.function.Predicate

class PlayerManager(
    private val server: KryptonServer,
    private val registries: LayeredRegistryAccess<RegistryLayer>
) {

    private val executor = Executors.newFixedThreadPool(8, daemonThreadFactory("Player Executor #%d"))
    private val dataManager = createDataManager(server)
    private val players = CopyOnWriteArrayList<KryptonPlayer>()
    private val playersByName = ConcurrentHashMap<String, KryptonPlayer>()
    private val playersByUUID = ConcurrentHashMap<UUID, KryptonPlayer>()
    private val synchronizedRegistries = RegistryAccess.ImmutableImpl(RegistrySerialization.networkedRegistries(registries)).freeze()

    fun players(): List<KryptonPlayer> = players

    fun dataFolder(): Path = dataManager.folder

    private fun createDataManager(server: KryptonServer): PlayerDataManager {
        val playerDataFolder = Path.of(server.config.world.name).resolve("playerdata")
        if (server.config.advanced.serializePlayerData && !Files.exists(playerDataFolder)) {
            try {
                Files.createDirectories(playerDataFolder)
            } catch (exception: Exception) {
                LOGGER.error("Unable to create player data directory!", exception)
            }
        }
        return PlayerDataManager(playerDataFolder, server.config.advanced.serializePlayerData)
    }

    fun getPlayer(name: String): KryptonPlayer? = playersByName.get(name)

    fun getPlayer(uuid: UUID): KryptonPlayer? = playersByUUID.get(uuid)

    fun addPlayer(player: KryptonPlayer): CompletableFuture<Void> = dataManager.load(player, executor).thenAcceptAsync({ nbt ->
        val profile = player.profile
        val name = server.profileCache.getProfile(profile.uuid)?.name ?: profile.name
        server.profileCache.addProfile(profile)
        val dimension = if (nbt != null) {
            KryptonDimensionType.parseLegacy(Dynamic(NbtOps.INSTANCE, nbt.get("Dimension")))
                .resultOrPartial { LOGGER.error(it) }
                .orElse(World.OVERWORLD)
        } else {
            World.OVERWORLD
        }
        if (nbt != null) server.userManager.updateUser(profile.uuid, nbt)

        val world = server.worldManager.worlds.get(dimension) ?: server.worldManager.default
        player.world = world
        world.players.add(player)
        val location = player.position
        LOGGER.info("Player ${profile.name} logged in with entity ID ${player.id} at $location")

        // Join the game
        val reducedDebugInfo = world.gameRules().getBoolean(GameRuleKeys.REDUCED_DEBUG_INFO)
        val doImmediateRespawn = world.gameRules().getBoolean(GameRuleKeys.DO_IMMEDIATE_RESPAWN)
        player.connection.send(PacketOutLogin(
            player.id,
            world.data.isHardcore,
            player.gameModeSystem.gameMode(),
            player.gameModeSystem.previousGameMode(),
            server.worldManager.worlds.keys,
            synchronizedRegistries,
            KryptonDynamicRegistries.DIMENSION_TYPE.getResourceKey(world.dimensionType)!!,
            world.dimension,
            BiomeManager.obfuscateSeed(world.seed),
            server.config.status.maxPlayers,
            server.config.world.viewDistance,
            server.config.world.simulationDistance,
            reducedDebugInfo,
            !doImmediateRespawn,
            false,
            false,
            null
        ))
        player.connection.send(PacketOutUpdateEnabledFeatures(FeatureFlags.REGISTRY.toNames(world.enabledFeatures())))
        player.connection.write(PacketOutPluginMessage(BRAND_KEY, BRAND_MESSAGE))
        player.connection.send(PacketOutChangeDifficulty.from(world.difficulty))

        // Player data stuff
        player.connection.send(PacketOutAbilities.create(player.abilities))
        player.connection.send(PacketOutSetHeldItem(player.inventory.heldSlot))
        player.connection.write(PacketOutUpdateRecipes.CACHED)
        player.connection.write(PacketOutUpdateTags(TagSerializer.serializeTagsToNetwork(registries)))
        player.connection.send(PacketOutEntityEvent(player.id, if (reducedDebugInfo) ENABLE_REDUCED_DEBUG_SCREEN else DISABLE_REDUCED_DEBUG_SCREEN))
        sendCommands(player)
        player.statisticsTracker.invalidate()
        player.connection.write(PacketOutUpdateRecipeBook.CACHED_INIT)
        updateScoreboard(world.scoreboard, player)
        server.connectionManager.invalidateStatus()

        // Add the player to the list and cache maps
        players.add(player)
        playersByName.put(player.profile.name, player)
        playersByUUID.put(player.uuid, player)

        // Fire join event and send result message
        val joinResult = server.eventManager.fireSync(KryptonJoinEvent(player, !profile.name.equals(name, true))).result
        if (!joinResult.isAllowed) {
            // Use default reason if denied without specified reason
            val reason = joinResult.message ?: DisconnectMessages.KICKED
            player.connection.disconnect(reason)
            return@thenAcceptAsync
        }
        val joinMessage = joinResult.message ?: getDefaultJoinMessage(player, joinResult.hasJoinedBefore)
        broadcastSystemMessage(joinMessage, false)
        player.connection.send(PacketOutSynchronizePlayerPosition.fromPlayer(player))
        player.connection.send(PacketOutPlayerInfoUpdate.createPlayerInitializing(players))
        server.connectionManager.sendGroupedPacket(players, PacketOutPlayerInfoUpdate.createPlayerInitializing(ImmutableLists.of(player)))
        world.spawnPlayer(player)

        // Send the initial chunk stream
        player.chunkViewingSystem.loadInitialChunks()

        // TODO: Custom boss events, resource pack pack, mob effects
        sendWorldInfo(world, player)
        if (server.config.server.resourcePack.uri.isNotEmpty()) {
            val resourcePack = server.config.server.resourcePack
            player.connection.send(PacketOutResourcePack(resourcePack.uri, resourcePack.hash, resourcePack.forced, resourcePack.prompt))
        }

        // Send inventory data
        player.connection.send(PacketOutSetContainerContent.fromPlayerInventory(player.inventory))
    }, executor)

    fun removePlayer(player: KryptonPlayer) {
        server.eventManager.fire(KryptonQuitEvent(player)).thenAcceptAsync({ event ->
            player.statisticsTracker.incrementStatistic(CustomStatistics.LEAVE_GAME.get())
            savePlayer(player)
            player.world.chunkManager.removePlayer(player)

            // Remove from caches
            player.world.removeEntity(player)
            player.world.players.remove(player)
            players.remove(player)
            playersByName.remove(player.profile.name)
            playersByUUID.remove(player.uuid)

            // Send info and quit message
            server.connectionManager.invalidateStatus()
//            server.sessionManager.sendGrouped(PacketOutPlayerInfoRemove(player))
            if (event.quitMessage != null) server.sendMessage(event.quitMessage!!)
        }, executor)
    }

    fun broadcast(packet: Packet, world: KryptonWorld, x: Double, y: Double, z: Double, radius: Double, except: KryptonPlayer?) {
        server.connectionManager.sendGroupedPacket(packet) {
            if (it === except || it.world !== world) return@sendGroupedPacket false
            val dx = x - it.position.x
            val dy = y - it.position.y
            val dz = z - it.position.z
            dx * dx + dy * dy + dz * dz < radius * radius
        }
    }

    fun broadcast(packet: Packet, world: KryptonWorld, pos: BlockPos, radius: Double, except: KryptonPlayer?) {
        broadcast(packet, world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), radius, except)
    }

    fun broadcastSystemMessage(message: Component, overlay: Boolean) {
        broadcastSystemMessage(message, { message }, overlay)
    }

    fun broadcastSystemMessage(message: Component, messageExtractor: Function<KryptonPlayer, Component?>, overlay: Boolean) {
        server.console.sendSystemMessage(message)
        players.forEach { player ->
            messageExtractor.apply(player)?.let { player.sendSystemMessage(it, overlay) }
        }
    }

    fun broadcastChatMessage(message: PlayerChatMessage, source: CommandSourceStack, type: RichChatType.Bound) {
        broadcastChatMessage(message, { source.shouldFilterMessageTo(it) }, source.getPlayer(), type)
    }

    fun broadcastChatMessage(message: PlayerChatMessage, source: KryptonPlayer, type: RichChatType.Bound) {
        broadcastChatMessage(message, { source.shouldFilterMessageTo(it) }, source, type)
    }

    private fun broadcastChatMessage(message: PlayerChatMessage, filterPredicate: Predicate<KryptonPlayer>, source: KryptonPlayer?,
                                     type: RichChatType.Bound) {
        val trusted = verifyChatTrusted(message)
        server.console.logChatMessage(message.decoratedContent(), type, if (trusted) null else "Not Secure")
        val outgoingMessage = OutgoingChatMessage.create(message)

        var fullyFiltered = false
        players.forEach {
            val filter = filterPredicate.test(it)
            it.sendChatMessage(outgoingMessage, filter, type)
            fullyFiltered = fullyFiltered or (filter && message.isFullyFiltered())
        }

        if (fullyFiltered && source != null) source.sendSystemMessage(CHAT_FILTERED_FULL)
    }

    private fun verifyChatTrusted(message: PlayerChatMessage): Boolean = message.hasSignature() && !message.hasExpired(Instant.now())

    fun disconnectAll() {
        players.forEach { it.disconnect(DisconnectMessages.SERVER_SHUTDOWN) }
    }

    fun saveAll() {
        players.forEach(::savePlayer)
    }

    private fun sendCommands(player: KryptonPlayer) {
        player.connection.send(PacketOutEntityEvent(player.id, OP_PERMISSION_LEVEL_4))
        server.commandManager.updateCommands(player)
    }

    private fun savePlayer(player: KryptonPlayer) {
        dataManager.save(player)?.let { server.userManager.updateUser(player.uuid, it) }
        player.statisticsTracker.save()
    }

    private fun sendWorldInfo(world: KryptonWorld, player: KryptonPlayer) {
        player.connection.send(PacketOutInitializeWorldBorder.create(world.border))
        player.connection.send(PacketOutUpdateTime.create(world.data))
        player.connection.send(PacketOutSetDefaultSpawnPosition(world.data.spawnPos(), world.data.spawnAngle))
        if (world.isRaining) {
            player.connection.send(PacketOutGameEvent(GameEventTypes.BEGIN_RAINING))
            player.connection.send(PacketOutGameEvent(GameEventTypes.RAIN_LEVEL_CHANGE, world.getRainLevel(1F)))
            player.connection.send(PacketOutGameEvent(GameEventTypes.THUNDER_LEVEL_CHANGE, world.getThunderLevel(1F)))
        }
    }

    private fun updateScoreboard(scoreboard: KryptonScoreboard, player: KryptonPlayer) {
        val objectives = HashSet<Objective>()
        scoreboard.teams.forEach { player.connection.send(PacketOutUpdateTeams.create(it)) }
        for (objective in scoreboard.displayObjectives()) {
            if (objectives.contains(objective)) continue
            scoreboard.getStartTrackingPackets(objective).forEach(player.connection::send)
            objectives.add(objective)
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val BRAND_KEY = Key.key("brand")
        // The word "Krypton" encoded in to UTF-8 and then prefixed with the length, which in this case is 7.
        private val BRAND_MESSAGE = byteArrayOf(7, 75, 114, 121, 112, 116, 111, 110)
        private val CHAT_FILTERED_FULL = Component.translatable("chat.filtered_full")

        private const val ENABLE_REDUCED_DEBUG_SCREEN = 22
        private const val DISABLE_REDUCED_DEBUG_SCREEN = 23
        private const val OP_PERMISSION_LEVEL_4 = 28

        @JvmStatic
        private fun getDefaultJoinMessage(player: KryptonPlayer, joinedBefore: Boolean): Component {
            val key = if (joinedBefore) "multiplayer.player.joined.renamed" else "multiplayer.player.joined"
            return Component.translatable(key, NamedTextColor.YELLOW, player.displayName)
        }
    }
}
