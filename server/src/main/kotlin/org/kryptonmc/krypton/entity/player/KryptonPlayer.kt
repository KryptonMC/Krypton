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
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.space.Location
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.scoreboard.Scoreboard
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffect
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.attribute.KryptonAttribute
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
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityTeleport
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutNamedSoundEffect
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerListHeaderFooter
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
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
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.statistic.KryptonStatisticTypes
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.calculatePositionChange
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.bossbar.BossBarManager
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.spongepowered.math.vector.Vector3i
import java.net.InetSocketAddress
import java.time.Duration
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.function.UnaryOperator
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

class KryptonPlayer(
    val session: SessionHandler,
    override val profile: KryptonGameProfile,
    world: KryptonWorld,
    override val address: InetSocketAddress = InetSocketAddress("127.0.0.1", 1)
) : KryptonLivingEntity(world, EntityTypes.PLAYER), Player {

    var permissionFunction = PermissionFunction.ALWAYS_NOT_SET

    override val name = profile.name
    override var uuid = profile.uuid

    override var canFly = false
    override var canBuild = false
    override var canInstantlyBuild = false
    override var walkingSpeed = 0.1F
    override var flyingSpeed = 0.05F
    override val inventory = KryptonPlayerInventory(this)
    override var scoreboard: Scoreboard? = null
    override var locale: Locale? = null
    override val statistics = server.playerManager.getStatistics(this)

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
    private var internalGamemode = Gamemode.SURVIVAL

    var oldGamemode: Gamemode? = null
    override var gamemode: Gamemode
        get() = internalGamemode
        set(value) {
            if (value == internalGamemode) return
            oldGamemode = internalGamemode
            internalGamemode = value
            updateAbilities()
            onAbilitiesUpdate()
            server.playerManager.sendToAll(PacketOutPlayerInfo(
                PacketOutPlayerInfo.PlayerAction.UPDATE_GAMEMODE,
                this
            ))
            session.send(PacketOutChangeGameState(GameState.CHANGE_GAMEMODE, value.ordinal.toFloat()))
            if (value != Gamemode.SPECTATOR) camera = this
        }
    override val direction: Direction
        get() = Direction.fromPitch(location.pitch.toDouble())
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
        oldGamemode = Gamemode.fromId((tag["previousPlayerGameType"] as? IntTag)?.value ?: -1)
        internalGamemode = Gamemode.fromId(tag.getInt("playerGameType")) ?: Gamemode.SURVIVAL
        inventory.load(tag.getList("Inventory", CompoundTag.ID))
        inventory.heldSlot = tag.getInt("SelectedItemSlot")
        score = tag.getInt("Score")
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
        attributes.getOrPut(AttributeTypes.MOVEMENT_SPEED) {
            KryptonAttribute(AttributeTypes.MOVEMENT_SPEED)
        }.baseValue = walkingSpeed.toDouble()

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
        int("playerGameType", internalGamemode.ordinal)
        oldGamemode?.let { int("previousPlayerGameType", it.ordinal) }
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

    override fun spawnParticles(effect: ParticleEffect, location: Location) {
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

    override fun teleport(position: Position) {
        val oldLocation = location
        location = Location(position.x, position.y, position.z)

        if (
            abs(location.x - oldLocation.x) > 8 ||
            abs(location.y - oldLocation.y) > 8 ||
            abs(location.z - oldLocation.z) > 8
        ) {
            session.send(PacketOutEntityTeleport(id, location, isOnGround))
        } else {
            session.send(PacketOutEntityPosition(
                id,
                calculatePositionChange(location.x, oldLocation.x),
                calculatePositionChange(location.y, oldLocation.y),
                calculatePositionChange(location.z, oldLocation.z),
                isOnGround
            ))
        }
        updateChunks()
    }

    override fun teleport(player: Player) = teleport(player.location)

    override fun getPermissionValue(permission: String) = permissionFunction[permission]

    override fun getSpawnPacket() = PacketOutSpawnPlayer(this)

    override fun sendPluginMessage(channel: Key, message: ByteArray) {
        if (channel !in PREREGISTERED_CHANNELS) require(channel in server.channels) {
            "Channel must be registered with the server to have data sent over it!"
        }
        if (channel in DEBUG_CHANNELS) {
            SERVER_LOGGER.warn("A plugin attempted to send a plugin message on a debug channel. These channels will " +
                    "only function correctly with a modified client.")
        }
        session.send(PacketOutPluginMessage(channel, message))
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
        session.send(PacketOutTitle(title.title()))
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

    override fun playSound(sound: Sound) = playSound(sound, location.x, location.y, location.z)

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val type = InternalRegistries.SOUND_EVENT[sound.name()]
        session.send(if (type != null) {
            PacketOutSoundEffect(sound, type, Vector(x, y, z))
        } else {
            PacketOutNamedSoundEffect(sound, Vector(x, y, z))
        })
    }

    override fun stopSound(stop: SoundStop) {
        session.send(PacketOutStopSound(stop))
    }

    override fun openBook(book: Book) {
        val item = book.toItemStack()
        val slot = inventory.heldSlot
        session.send(PacketOutSetSlot(inventory.id, inventory.incrementStateId(), slot, item))
        session.send(PacketOutOpenBook(hand))
        session.send(PacketOutSetSlot(inventory.id, inventory.incrementStateId(), slot, inventory.mainHand))
    }

    override fun identity() = Identity.identity(uuid)

    override fun asHoverEvent(op: UnaryOperator<ShowEntity>) = showEntity(
        op.apply(ShowEntity.of(key("minecraft", "player"), uuid, displayName))
    )

    private fun updateAbilities() {
        when (internalGamemode) {
            Gamemode.CREATIVE -> {
                isInvulnerable = true
                canFly = true
                canInstantlyBuild = true
            }
            Gamemode.SPECTATOR -> {
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
        val centralX = location.blockX shr 4
        val centralZ = location.blockZ shr 4
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
            var dx = 16 * a.toInt() + 8 - location.x
            var dz = 16 * (a shr 32).toInt() + 8 - location.z
            val da = dx * dx + dz * dz
            dx = 16 * b.toInt() + 8 - location.x
            dz = 16 * (b shr 32).toInt() + 8 - location.z
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
        !block.requiresCorrectTool || inventory.mainHand.type.handler.isCorrectTool(block)

    override fun getDestroySpeed(block: Block): Float {
        var speed = inventory.mainHand.getDestroySpeed(block)
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
        isInvisible = internalGamemode == Gamemode.SPECTATOR
    }

    override fun incrementStatistic(statistic: Statistic<*>, amount: Int) = statistics.increment(statistic, amount)

    override fun incrementStatistic(key: Key, amount: Int) =
        incrementStatistic(KryptonStatisticTypes.CUSTOM[key], amount)

    override fun decrementStatistic(statistic: Statistic<*>, amount: Int) = statistics.decrement(statistic, amount)

    override fun resetStatistic(statistic: Statistic<*>) = statistics.set(statistic, 0)

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

    override var absorption: Float
        get() = data[MetadataKeys.PLAYER.ADDITIONAL_HEARTS]
        set(value) = data.set(MetadataKeys.PLAYER.ADDITIONAL_HEARTS, value)

    var score: Int
        get() = data[MetadataKeys.PLAYER.SCORE]
        set(value) = data.set(MetadataKeys.PLAYER.SCORE, value)

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
        private val PREREGISTERED_CHANNELS = DEBUG_CHANNELS + key("brand")
        private val LOGGER = logger<KryptonPlayer>()
        private val SERVER_LOGGER = logger<KryptonServer>()
    }
}
