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
package org.kryptonmc.krypton.entity.player

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongArraySet
import it.unimi.dsi.fastutil.longs.LongSet
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent.ShowEntity
import net.kyori.adventure.text.event.HoverEvent.showEntity
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffect
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.item.toItemStack
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutActionBar
import org.kryptonmc.krypton.packet.out.play.PacketOutCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkData
import org.kryptonmc.krypton.packet.out.play.PacketOutClearTitles
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutEntitySoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityTeleport
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutNamedSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerListHeaderFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetSlot
import org.kryptonmc.krypton.packet.out.play.PacketOutSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutStopSound
import org.kryptonmc.krypton.packet.out.play.PacketOutSubTitle
import org.kryptonmc.krypton.packet.out.play.PacketOutTitle
import org.kryptonmc.krypton.packet.out.play.PacketOutTitleTimes
import org.kryptonmc.krypton.packet.out.play.PacketOutUnloadChunk
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateLight
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateHealth
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.statistic.KryptonStatisticTypes
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.util.BossBarManager
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.scoreboard.KryptonScore
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.net.InetSocketAddress
import java.time.Duration
import java.util.Locale
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.function.UnaryOperator
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonPlayer(
    val session: SessionHandler,
    override val profile: KryptonGameProfile,
    world: KryptonWorld,
    override val address: InetSocketAddress = InetSocketAddress("127.0.0.1", 1)
) : KryptonLivingEntity(world, EntityTypes.PLAYER, ATTRIBUTES), Player {

    var permissionFunction = PermissionFunction.ALWAYS_NOT_SET

    override val name = Component.text(profile.name)
    override var uuid: UUID
        get() = profile.uuid
        set(_) = Unit // Player UUIDs are read only.

    override var canFly = false
    override var canBuild = false
    override var canInstantlyBuild = false
    override var walkingSpeed = 0.1F
    override var flyingSpeed = 0.05F
    override val inventory = KryptonPlayerInventory(this)
    override val scoreboard = world.scoreboard
    override var locale: Locale? = null
    override val statistics = server.playerManager.getStatistics(this)
    override val teamRepresentation = name
    override val pushedByFluid = !isFlying

    // TODO: Per-player view distance, see issue #49
    override val viewDistance = server.config.world.viewDistance
    override var time = 0L

    override val permissionLevel: Int
        get() = server.getPermissionLevel(profile)

    private var camera: KryptonEntity = this
        set(value) {
            val old = field
            field = value
            if (old != field) {
                session.send(PacketOutCamera(field.id))
                teleport(field.location)
            }
        }

    /**
     * Doing this avoids a strange issue, where loading the player data will
     * set the game mode value, which in turn updates the abilities, and sends
     * the abilities packet before the client has constructed a local player
     * (done when we send the join game packet), which in turn causes a
     * [NullPointerException] to occur client-side because it attempts to get
     * the abilities from a null player.
     */
    private var internalGamemode = GameModes.SURVIVAL

    var oldGameMode: GameMode? = null
    override var gameMode: GameMode
        get() = internalGamemode
        set(value) {
            if (value === internalGamemode) return
            oldGameMode = internalGamemode
            internalGamemode = value
            updateAbilities()
            onAbilitiesUpdate()
            server.playerManager.sendToAll(PacketOutPlayerInfo(
                PacketOutPlayerInfo.PlayerAction.UPDATE_GAMEMODE,
                this
            ))
            session.send(PacketOutChangeGameState(GameState.CHANGE_GAMEMODE, Registries.GAME_MODES.idOf(value).toFloat()))
            if (value !== GameModes.SPECTATOR) camera = this
        }
    override val direction: Direction
        get() = Directions.ofPitch(rotation.y().toDouble())
    val canUseGameMasterBlocks: Boolean
        get() = canInstantlyBuild && permissionLevel >= 2

    override val dimensionType: DimensionType
        get() = world.dimensionType
    override val dimension: ResourceKey<World>
        get() = world.dimension

    val viewableEntities: MutableSet<KryptonEntity> = ConcurrentHashMap.newKeySet()

    private var respawnPosition: Vector3i? = null
    private var respawnForced = false
    private var respawnAngle = 0F
    private var respawnDimension = World.OVERWORLD

    private var previousCentralX = 0
    private var previousCentralZ = 0
    private val visibleChunks = LongArraySet()

    init {
        data.add(MetadataKeys.PLAYER.ADDITIONAL_HEARTS)
        data.add(MetadataKeys.PLAYER.SCORE)
        data.add(MetadataKeys.PLAYER.SKIN_FLAGS)
        data.add(MetadataKeys.PLAYER.MAIN_HAND)
        data.add(MetadataKeys.PLAYER.LEFT_SHOULDER)
        data.add(MetadataKeys.PLAYER.RIGHT_SHOULDER)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        uuid = profile.uuid
        (tag["previousPlayerGameType"] as? IntTag)?.value?.let { oldGameMode = Registries.GAME_MODES[it] }
        internalGamemode = Registries.GAME_MODES[tag.getInt("playerGameType")] ?: GameModes.SURVIVAL
        inventory.load(tag.getList("Inventory", CompoundTag.ID))
        inventory.heldSlot = tag.getInt("SelectedItemSlot")
        score = tag.getInt("Score")
        foodLevel = tag.getInt("foodLevel")
        foodTickTimer = tag.getInt("foodTickTimer")
        foodExhaustionLevel = tag.getFloat("foodExhaustionLevel")
        foodSaturationLevel = tag.getFloat("foodSaturationLevel")
        if (tag.contains("abilities", CompoundTag.ID)) {
            val abilitiesData = tag.getCompound("abilities")
            isInvulnerable = abilitiesData.getBoolean("invulnerable")
            isFlying = abilitiesData.getBoolean("flying")
            canFly = abilitiesData.getBoolean("mayfly")
            canBuild = abilitiesData.getBoolean("mayBuild")
            canInstantlyBuild = abilitiesData.getBoolean("instabuild")
            walkingSpeed = abilitiesData.getFloat("walkSpeed")
            flyingSpeed = abilitiesData.getFloat("flySpeed")
        }
        attribute(AttributeTypes.MOVEMENT_SPEED)!!.baseValue = walkingSpeed.toDouble()

        // NBT data for entities sitting on the player's shoulders, e.g. parrots
        if (tag.contains("ShoulderEntityLeft", CompoundTag.ID)) {
            leftShoulder = tag.getCompound("ShoulderEntityLeft")
        }
        if (tag.contains("ShoulderEntityRight", CompoundTag.ID)) {
            rightShoulder = tag.getCompound("ShoulderEntityRight")
        }

        // Respawn data
        if (
            tag.contains("SpawnX", 99) &&
            tag.contains("SpawnY", 99) &&
            tag.contains("SpawnZ", 99)
        ) {
            respawnPosition = Vector3i(
                tag.getInt("SpawnX"),
                tag.getInt("SpawnY"),
                tag.getInt("SpawnZ")
            )
            respawnForced = tag.getBoolean("SpawnForced")
            respawnAngle = tag.getFloat("SpawnAngle")
            if (tag.containsKey("SpawnDimension")) {
                val dimension = Codecs.DIMENSION.parse(NBTOps, tag["SpawnDimension"]!!)
                respawnDimension = dimension.resultOrPartial(LOGGER::error).orElse(World.OVERWORLD)
            }
        }
    }

    override fun save() = super.save().apply {
        int("DataVersion", KryptonPlatform.worldVersion)
        put("Inventory", inventory.save())
        int("SelectedItemSlot", inventory.heldSlot)
        int("Score", score)
        int("foodLevel", foodLevel)
        int("foodTickTimer", foodTickTimer)
        float("foodExhaustionLevel", foodExhaustionLevel)
        float("foodSaturationLevel", foodSaturationLevel)
        compound("abilities") {
            boolean("flying", isFlying)
            boolean("invulnerable", isInvulnerable)
            boolean("mayfly", canFly)
            boolean("mayBuild", canBuild)
            boolean("instabuild", canInstantlyBuild)
            float("walkSpeed", walkingSpeed)
            float("flySpeed", flyingSpeed)
        }
        if (leftShoulder.isNotEmpty()) put("ShoulderEntityLeft", leftShoulder)
        if (rightShoulder.isNotEmpty()) put("ShoulderEntityRight", rightShoulder)
        int("playerGameType", Registries.GAME_MODES.idOf(internalGamemode))
        oldGameMode?.let { int("previousPlayerGameType", Registries.GAME_MODES.idOf(it)) }
        string("Dimension", dimension.location.asString())
        respawnPosition?.let { position ->
            int("SpawnX", position.x())
            int("SpawnY", position.y())
            int("SpawnZ", position.z())
            float("SpawnAngle", respawnAngle)
            boolean("SpawnForced", respawnForced)
            Codecs.DIMENSION.encodeStart(NBTOps, respawnDimension)
                .resultOrPartial(LOGGER::error)
                .ifPresent { put("SpawnDimension", it) }
        }
    }

    override fun tick() {
        super.tick()
        hungerMechanic()
    }

    private fun hungerMechanic() {
        // TODO: Hunger in general -> min/max to replace if/else, more actions for exhaustion
        foodTickTimer++
        if (foodExhaustionLevel > 4f) {
            foodExhaustionLevel -= 4.0f
            if (foodSaturationLevel > 0) {
                if (foodSaturationLevel - 1 >= 0) {
                    foodSaturationLevel--
                } else {
                    foodSaturationLevel = 0f
                }
            } else {
                if (foodLevel - 1 >= 0) {
                    foodLevel--
                } else {
                    foodLevel = 0
                }
            }
        }

        when (server.config.world.difficulty) {
            Difficulty.EASY -> {
                if (health > 10 && foodLevel == 0 && foodTickTimer == 80) { // starving
                    health-- // deduct half a heart
                }
            }
            Difficulty.NORMAL -> {
                if (health > 1 && foodLevel == 0 && foodTickTimer == 80) {
                    health--
                }
            }
            Difficulty.HARD -> {
                if (foodLevel == 0 && foodTickTimer == 80) {
                    health--
                }
            }
            Difficulty.PEACEFUL -> {
                if (foodLevel < 20 && foodTickTimer % 20 == 0) {
                    foodLevel++ // increase player food level
                }
            }
        }

        if (foodTickTimer == 80) {
            if (foodLevel >= 18) {
                health++ // regenerate health
                if (foodExhaustionLevel + 3f < 4f) {
                    foodExhaustionLevel += 3f
                } else {
                    foodExhaustionLevel = 4f
                }
                foodSaturationLevel -= 3
            }
            foodTickTimer = 0 // reset tick timer
        }
    }

    override fun addViewer(player: KryptonPlayer): Boolean {
        if (player === this) return false
        player.session.send(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.ADD_PLAYER, this))
        return super.addViewer(player)
    }

    override fun removeViewer(player: KryptonPlayer): Boolean {
        if (player === this || !super.removeViewer(player)) return false
        player.session.send(PacketOutPlayerInfo(PacketOutPlayerInfo.PlayerAction.REMOVE_PLAYER, this))
        return true
    }

    override fun spawnParticles(effect: ParticleEffect, location: Vector3d) {
        if (effect !is KryptonParticleEffect) return
        val packet = PacketOutParticle(effect, location)
        when (effect.data) {
            // Send multiple packets based on the quantity
            is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> repeat(effect.quantity) {
                session.send(packet)
            }
            // Send particles to player at location
            else -> session.send(packet)
        }
    }

    override fun teleport(position: Vector3d) {
        val oldLocation = location
        location = position

        if (
            abs(location.x() - oldLocation.x()) > 8 ||
            abs(location.y() - oldLocation.y()) > 8 ||
            abs(location.z() - oldLocation.z()) > 8
        ) {
            session.send(PacketOutEntityTeleport(id, location, rotation, isOnGround))
        } else {
            session.send(PacketOutEntityPosition(
                id,
                Positioning.delta(location.x(), oldLocation.x()),
                Positioning.delta(location.y(), oldLocation.y()),
                Positioning.delta(location.z(), oldLocation.z()),
                isOnGround
            ))
        }
        updateChunks()
    }

    override fun teleport(player: Player) = teleport(player.location)

    override fun getPermissionValue(permission: String) = permissionFunction[permission]

    override fun getSpawnPacket() = PacketOutSpawnPlayer(this)

    override fun sendPluginMessage(channel: Key, message: ByteArray) {
        if (channel in DEBUG_CHANNELS) {
            SERVER_LOGGER.warn("A plugin attempted to send a plugin message on a debug channel. These channels will " +
                    "only function correctly with a modified client.")
        }
        session.send(PacketOutPluginMessage(channel, message))
    }

    override fun sendResourcePack(pack: ResourcePack) {
        session.send(PacketOutResourcePack(pack))
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        session.send(PacketOutChat(message, type, source.uuid()))
    }

    override fun sendActionBar(message: Component) {
        session.send(PacketOutActionBar(message))
    }

    override fun sendPlayerListHeaderAndFooter(header: Component, footer: Component) {
        session.send(PacketOutPlayerListHeaderFooter(header, footer))
    }

    override fun showTitle(title: Title) {
        title.times()?.let { session.send(PacketOutTitleTimes(it)) }
        session.send(PacketOutSubTitle(title.subtitle()))
        session.send(PacketOutTitle(title.title()))
    }

    override fun <T> sendTitlePart(part: TitlePart<T>, value: T) {
        if (part === TitlePart.TITLE) session.send(PacketOutTitle(value as Component))
        if (part === TitlePart.SUBTITLE) session.send(PacketOutSubTitle(value as Component))
        if (part === TitlePart.TIMES) session.send(PacketOutTitleTimes(value as Title.Times))
        throw IllegalArgumentException("Unknown TitlePart")
    }

    fun sendTitle(title: Component) {
        session.send(PacketOutTitle(title))
    }

    fun sendSubtitle(subtitle: Component) {
        session.send(PacketOutSubTitle(subtitle))
    }

    fun sendTitleTimes(fadeIn: Duration, stay: Duration, fadeOut: Duration) {
        session.send(PacketOutTitleTimes(Title.Times.of(fadeIn, stay, fadeOut)))
    }

    override fun clearTitle() {
        session.send(PacketOutClearTitles(false))
    }

    override fun resetTitle() {
        session.send(PacketOutClearTitles(true))
    }

    override fun showBossBar(bar: BossBar) = BossBarManager.addBar(bar, this)

    override fun hideBossBar(bar: BossBar) = BossBarManager.removeBar(bar, this)

    override fun playSound(sound: Sound) = playSound(sound, location.x(), location.y(), location.z())

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val type = InternalRegistries.SOUND_EVENT[sound.name()]
        session.send(if (type != null) {
            PacketOutSoundEffect(sound, type, x, y, z)
        } else {
            PacketOutNamedSoundEffect(sound, x, y, z)
        })
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        val entity = when {
            emitter === Sound.Emitter.self() -> this
            emitter is KryptonEntity -> emitter
            else -> error("Sound emitter must be an entity or self(), was $emitter")
        }

        val event = Registries.SOUND_EVENT[sound.name()]
        if (event != null) {
            session.send(PacketOutEntitySoundEffect(event, sound.source(), entity.id, sound.volume(), sound.pitch()))
        } else {
            session.send(PacketOutNamedSoundEffect(
                sound.name(),
                sound.source(),
                entity.location.x(),
                entity.location.y(),
                entity.location.z(),
                sound.volume(),
                sound.pitch()
            ))
        }
    }

    override fun stopSound(stop: SoundStop) {
        session.send(PacketOutStopSound(stop))
    }

    override fun openBook(book: Book) {
        val item = book.toItemStack()
        val slot = inventory.items.size + inventory.heldSlot
        val stateId = inventory.stateId
        session.send(PacketOutSetSlot(0, stateId, slot, item))
        session.send(PacketOutOpenBook(hand))
        session.send(PacketOutSetSlot(0, stateId, slot, inventory.mainHand))
    }

    private fun updateAbilities() {
        when (internalGamemode) {
            GameModes.CREATIVE -> {
                isInvulnerable = true
                canFly = true
                canInstantlyBuild = true
            }
            GameModes.SPECTATOR -> {
                isInvulnerable = true
                canFly = true
                isFlying = true
                canInstantlyBuild = false
            }
            else -> Unit
        }
        canBuild = internalGamemode.canBuild
    }

    fun updateChunks(firstLoad: Boolean = false) {
        var previousChunks: LongSet? = null
        val newChunks = LongArrayList()

        val oldCentralX = previousCentralX
        val oldCentralZ = previousCentralZ
        val centralX = location.floorX() shr 4
        val centralZ = location.floorZ() shr 4
        val radius = server.config.world.viewDistance

        if (firstLoad) {
            for (x in centralX - radius..centralX + radius) {
                for (z in centralZ - radius..centralZ + radius) newChunks.add(ChunkPosition.toLong(x, z))
            }
        } else if (abs(centralX - previousCentralX) > radius || abs(centralZ - previousCentralZ) > radius) {
            visibleChunks.clear()
            for (x in centralX - radius..centralX + radius) {
                for (z in centralZ - radius..centralZ + radius) newChunks.add(ChunkPosition.toLong(x, z))
            }
        } else if (previousCentralX != centralX || previousCentralZ != centralZ) {
            previousChunks = LongArraySet(visibleChunks)
            for (x in centralX - radius..centralX + radius) {
                for (z in centralZ - radius..centralZ + radius) {
                    val pos = ChunkPosition.toLong(x, z)
                    if (visibleChunks.contains(pos)) previousChunks.remove(pos) else newChunks.add(pos)
                }
            }
        } else return

        previousCentralX = centralX
        previousCentralZ = centralZ

        newChunks.sortWith { a, b ->
            var dx = 16 * a.toInt() + 8 - location.x()
            var dz = 16 * (a shr 32).toInt() + 8 - location.z()
            val da = dx * dx + dz * dz
            dx = 16 * b.toInt() + 8 - location.x()
            dz = 16 * (b shr 32).toInt() + 8 - location.z()
            val db = dx * dx + dz * dz
            da.compareTo(db)
        }

        visibleChunks += newChunks
        world.chunkManager.addPlayer(
            this,
            centralX,
            centralZ,
            if (firstLoad) centralX else oldCentralX,
            if (firstLoad) centralZ else oldCentralZ,
            radius
        ).thenRun {
            session.send(PacketOutUpdateViewPosition(centralX, centralZ))
            newChunks.forEach {
                val chunk = world.chunkManager[it] ?: return@forEach
                session.send(PacketOutUpdateLight(chunk))
                session.send(PacketOutChunkData(chunk))
            }

            previousChunks?.forEach {
                session.send(PacketOutUnloadChunk(it.toInt(), (it shr 32).toInt()))
                visibleChunks -= it
            }
            previousChunks?.clear()
        }
    }

    override fun hasCorrectTool(block: Block): Boolean =
        !block.requiresCorrectTool || inventory.mainHand.type.handler().isCorrectTool(block)

    override fun destroySpeed(block: Block): Float {
        var speed = inventory.mainHand.destroySpeed(block)
        if (!isOnGround) speed /= 5F
        return speed
    }

    override fun disconnect(text: Component) {
        session.disconnect(text)
    }

    private fun onAbilitiesUpdate() {
        session.send(PacketOutAbilities(this))
        updateInvisibility()
        server.playerManager.sendToAll(PacketOutMetadata(id, data.dirty), world)
    }

    private fun updateInvisibility() {
        removeEffectParticles()
        isInvisible = internalGamemode === GameModes.SPECTATOR
    }

    override fun incrementStatistic(statistic: Statistic<*>, amount: Int) {
        statistics.increment(statistic, amount)
        scoreboard.forEachObjective(statistic, teamRepresentation) { it.add(amount) }
    }

    override fun incrementStatistic(key: Key, amount: Int) = incrementStatistic(KryptonStatisticTypes.CUSTOM[key], amount)

    override fun decrementStatistic(statistic: Statistic<*>, amount: Int) {
        statistics.decrement(statistic, amount)
        scoreboard.forEachObjective(statistic, teamRepresentation, KryptonScore::reset)
    }

    override fun resetStatistic(statistic: Statistic<*>) {
        statistics[statistic] = 0
        scoreboard.forEachObjective(statistic, teamRepresentation, KryptonScore::reset)
    }

    fun updateMovementStatistics(deltaX: Double, deltaY: Double, deltaZ: Double) {
        // TODO: Walking underwater, walking on water, climbing
        if (isSwimming) {
            val value = (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F).roundToInt()
            if (value > 0) incrementStatistic(CustomStatistics.SWIM_ONE_CM, value)
        } else if (isOnGround) {
            val value = (sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F).roundToInt()
            if (value > 0) when {
                isSprinting -> incrementStatistic(CustomStatistics.SPRINT_ONE_CM, value)
                isCrouching -> incrementStatistic(CustomStatistics.CROUCH_ONE_CM, value)
                else -> incrementStatistic(CustomStatistics.WALK_ONE_CM, value)
            }
        } else if (isFallFlying) {
            val value = (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F).roundToInt()
            incrementStatistic(CustomStatistics.AVIATE_ONE_CM, value)
        } else {
            val value = (sqrt(deltaX * deltaX + deltaZ * deltaZ ) * 100F).roundToInt()
            if (value > FLYING_ACHIEVEMENT_MINIMUM_SPEED) incrementStatistic(CustomStatistics.FLY_ONE_CM, value)
        }
    }

    fun updateMovementExhaustion(deltaX: Double, deltaY: Double, deltaZ: Double) {
        if (isSwimming) {
            val value = sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
            if (value > 0) foodExhaustionLevel += 0.01F * value.toFloat()
        } else if (isOnGround) {
            val value = sqrt(deltaX * deltaX + deltaZ * deltaZ)
            if (value > 0) when {
                isSprinting -> foodExhaustionLevel += 0.1F * value.toFloat()
            }
        }
    }

    override var absorption: Float
        get() = data[MetadataKeys.PLAYER.ADDITIONAL_HEARTS]
        set(value) = data.set(MetadataKeys.PLAYER.ADDITIONAL_HEARTS, value)

    var score: Int
        get() = data[MetadataKeys.PLAYER.SCORE]
        set(value) = data.set(MetadataKeys.PLAYER.SCORE, value)

    override var health: Float = 1.0f
        get() = super.health
        set(value) {
            super.health = value
            field = value
            session.send(PacketOutUpdateHealth(health, foodLevel, foodSaturationLevel))
        }

    override var foodLevel: Int = 20
        set(value) {
            field = value
            session.send(PacketOutUpdateHealth(health, foodLevel, foodSaturationLevel))
        }

    var foodTickTimer: Int = 0

    override var foodExhaustionLevel: Float = 0f

    override var foodSaturationLevel: Float = 5f
        set(value) {
            field = value
            session.send(PacketOutUpdateHealth(health, foodLevel, foodSaturationLevel))
        }


    var skinSettings: Byte
        get() = data[MetadataKeys.PLAYER.SKIN_FLAGS]
        set(value) = data.set(MetadataKeys.PLAYER.SKIN_FLAGS, value)

    override var mainHand: MainHand
        get() = if (data[MetadataKeys.PLAYER.MAIN_HAND] == 0.toByte()) MainHand.LEFT else MainHand.RIGHT
        set(value) = data.set(MetadataKeys.PLAYER.MAIN_HAND, if (value == MainHand.LEFT) 0 else 1)

    var leftShoulder: CompoundTag
        get() = data[MetadataKeys.PLAYER.LEFT_SHOULDER]
        set(value) = data.set(MetadataKeys.PLAYER.LEFT_SHOULDER, value)

    var rightShoulder: CompoundTag
        get() = data[MetadataKeys.PLAYER.RIGHT_SHOULDER]
        set(value) = data.set(MetadataKeys.PLAYER.RIGHT_SHOULDER, value)

    companion object {

        val DEFAULT_PERMISSIONS = PermissionProvider { PermissionFunction.ALWAYS_NOT_SET }
        private const val FLYING_ACHIEVEMENT_MINIMUM_SPEED = 25
        private val DEBUG_CHANNELS = setOf(
            key("debug/paths"),
            key("debug/neighbors_update"),
            key("debug/caves"),
            key("debug/structures"),
            key("debug/worldgen_attempt"),
            key("debug/poi_ticket_count"),
            key("debug/poi_added"),
            key("debug/poi_removed"),
            key("debug/village_sections"),
            key("debug/goal_selector"),
            key("debug/brain"),
            key("debug/bee"),
            key("debug/hive"),
            key("debug/game_test_add_marker"),
            key("debug/game_test_clear"),
            key("debug/raids")
        )
        private val LOGGER = logger<KryptonPlayer>()
        private val SERVER_LOGGER = logger<KryptonServer>()
        private val ATTRIBUTES = KryptonLivingEntity.attributes()
            .add(AttributeTypes.ATTACK_DAMAGE, 1.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.1)
            .add(AttributeTypes.ATTACK_SPEED)
            .add(AttributeTypes.LUCK)
            .build()
    }
}
