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
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.player.PerformActionEvent
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.MetaKeys
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.provide
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.EquipmentSlot
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEquipable
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.EmptyItemStack
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutActionBar
import org.kryptonmc.krypton.packet.out.play.PacketOutCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChat
import org.kryptonmc.krypton.packet.out.play.PacketOutChunkDataAndLight
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
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateHealth
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateViewPosition
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.service.KryptonVanishService
import org.kryptonmc.krypton.util.BossBarManager
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.NBTOps
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.net.InetSocketAddress
import java.time.Duration
import java.time.Instant
import java.util.Locale
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonPlayer(
    val session: SessionHandler,
    override val profile: KryptonGameProfile,
    world: KryptonWorld,
    override val address: InetSocketAddress
) : KryptonLivingEntity(world, EntityTypes.PLAYER, ATTRIBUTES), Player, KryptonEquipable {

    var permissionFunction = PermissionFunction.ALWAYS_NOT_SET

    override val name = Component.text(profile.name)
    override var uuid: UUID
        get() = profile.uuid
        set(_) = Unit // Player UUIDs are read only.

    // This is a bit hacky, but ensures that data will never be sent in the wrong order for players.
    @Volatile
    var isLoaded = false

    override var canFly = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var canBuild = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var canInstantlyBuild = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var walkingSpeed = 0.1F
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var flyingSpeed = 0.05F
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var isFlying = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var isInvulnerable: Boolean
        get() = super.isInvulnerable
        set(value) {
            super.isInvulnerable = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var isGliding: Boolean
        get() = super.isGliding
        set(value) {
            val action = if (value) PerformActionEvent.Action.START_FLYING_WITH_ELYTRA else PerformActionEvent.Action.STOP_FLYING_WITH_ELYTRA
            val event = server.eventManager.fireSync(PerformActionEvent(this, action))
            if (!event.result.isAllowed) return

            if (action == PerformActionEvent.Action.STOP_FLYING_WITH_ELYTRA) {
                super.isGliding = false
                return
            }
            if (!isOnGround && !super.isGliding && !inWater) {
                val chestplate = inventory.armor(ArmorSlot.CHESTPLATE)
                val damage = chestplate.meta[MetaKeys.DAMAGE] ?: 0
                if (chestplate.type === ItemTypes.ELYTRA && damage < chestplate.type.durability - 1) {
                    super.isGliding = true
                }
            }
        }
    override val inventory = KryptonPlayerInventory(this)
    override val handSlots: Iterable<KryptonItemStack>
        get() = listOf(inventory.mainHand, inventory.offHand)
    override val armorSlots: Iterable<KryptonItemStack>
        get() = inventory.armor

    override val scoreboard = world.scoreboard
    override var locale: Locale? = null
    override val statistics = server.playerManager.getStatistics(this)
    override val cooldowns = KryptonCooldownTracker(this)
    override val teamRepresentation = name
    override val pushedByFluid = !isFlying

    // TODO: Per-player view distance, see issue #49
    override val viewDistance = server.config.world.viewDistance
    override var chatVisibility = ChatVisibility.FULL
    override var filterText = false
    override var allowsListing = true
    override var time = 0L

    private var camera: KryptonEntity = this
        set(value) {
            val old = field
            field = value
            if (old != field) {
                session.send(PacketOutCamera(field.id))
                teleport(field.location)
            }
        }

    var oldGameMode: GameMode? = null
    override var gameMode: GameMode = GameModes.SURVIVAL
        set(value) {
            if (value === field) return
            oldGameMode = field
            field = value
            if (!isLoaded) return
            updateAbilities()
            onAbilitiesUpdate()
            server.playerManager.sendToAll(PacketOutPlayerInfo(
                PacketOutPlayerInfo.Action.UPDATE_GAMEMODE,
                this
            ))
            session.send(PacketOutChangeGameState(GameState.CHANGE_GAMEMODE, Registries.GAME_MODES.idOf(value).toFloat()))
            if (value !== GameModes.SPECTATOR) camera = this
        }
    override val isSpectator: Boolean
        get() = gameMode === GameModes.SPECTATOR
    override val direction: Direction
        get() = Directions.ofPitch(rotation.y().toDouble())
    val canUseGameMasterBlocks: Boolean
        get() = canInstantlyBuild && hasPermission(KryptonPermission.USE_GAME_MASTER_BLOCKS.node)

    override val dimensionType: DimensionType
        get() = world.dimensionType
    override val dimension: ResourceKey<World>
        get() = world.dimension

    override val isOnline: Boolean
        get() = server.player(uuid) === this
    override var hasJoinedBefore: Boolean = false
    override var firstJoined: Instant = Instant.EPOCH
    override var lastJoined: Instant = Instant.now()

    private val vanishService = server.servicesManager.provide<VanishService>()!!
    override val isVanished: Boolean
        get() = vanishService.isVanished(this)

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

    override fun equipment(slot: EquipmentSlot): KryptonItemStack = when {
        slot == EquipmentSlot.MAIN_HAND -> inventory.mainHand
        slot == EquipmentSlot.OFF_HAND -> inventory.offHand
        slot.type == EquipmentSlot.Type.ARMOR -> inventory.armor[slot.index]
        else -> EmptyItemStack
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) {
        if (slot == EquipmentSlot.MAIN_HAND) {
            inventory.setHeldItem(Hand.MAIN, item)
            return
        }
        if (slot == EquipmentSlot.OFF_HAND) {
            inventory.setHeldItem(Hand.OFF, item)
            return
        }
        if (slot.type == EquipmentSlot.Type.ARMOR) {
            inventory.armor[slot.index] = item
            return
        }
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        gameMode = Registries.GAME_MODES[tag.getInt("playerGameType")] ?: GameModes.SURVIVAL
        if (tag.contains("previousPlayerGameType", IntTag.ID)) {
            oldGameMode = Registries.GAME_MODES[tag.getInt("previousPlayerGameType")]
        }
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
        if (tag.contains("SpawnX", 99) && tag.contains("SpawnY", 99) && tag.contains("SpawnZ", 99)) {
            respawnPosition = Vector3i(tag.getInt("SpawnX"), tag.getInt("SpawnY"), tag.getInt("SpawnZ"))
            respawnForced = tag.getBoolean("SpawnForced")
            respawnAngle = tag.getFloat("SpawnAngle")
            if (tag.containsKey("SpawnDimension")) {
                val dimension = Codecs.DIMENSION.parse(NBTOps, tag["SpawnDimension"]!!)
                respawnDimension = dimension.resultOrPartial(LOGGER::error).orElse(World.OVERWORLD)
            }
        }

        if (tag.contains("krypton", CompoundTag.ID)) {
            hasJoinedBefore = true
            val kryptonData = tag.getCompound("krypton")
            if (vanishService is KryptonVanishService && kryptonData.getBoolean("vanished")) vanishService.vanish(this)
            firstJoined = Instant.ofEpochMilli(kryptonData.getLong("firstJoined"))
        }
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("playerGameType", Registries.GAME_MODES.idOf(gameMode))
        if (oldGameMode != null) int("previousPlayerGameType", Registries.GAME_MODES.idOf(oldGameMode!!))

        int("DataVersion", KryptonPlatform.worldVersion)
        put("Inventory", inventory.save())
        int("SelectedItemSlot", inventory.heldSlot)
        int("Score", score)
        int("foodLevel", foodLevel)
        int("foodTickTimer", foodTickTimer)
        float("foodExhaustionLevel", foodExhaustionLevel)
        float("foodSaturationLevel", foodSaturationLevel)

        compound("abilities") {
            boolean("flying", isGliding)
            boolean("invulnerable", isInvulnerable)
            boolean("mayfly", canFly)
            boolean("mayBuild", canBuild)
            boolean("instabuild", canInstantlyBuild)
            float("walkSpeed", walkingSpeed)
            float("flySpeed", flyingSpeed)
        }

        if (leftShoulder.isNotEmpty()) put("ShoulderEntityLeft", leftShoulder)
        if (rightShoulder.isNotEmpty()) put("ShoulderEntityRight", rightShoulder)

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

        val rootVehicle = rootVehicle
        val vehicle = vehicle
        if (vehicle != null && rootVehicle !== this@KryptonPlayer && rootVehicle.hasExactlyOnePlayerPassenger) {
            compound("RootVehicle") {
                uuid("Attach", vehicle.uuid)
                put("Entity", rootVehicle.save().build())
            }
        }

        compound("krypton") {
            if (vanishService is KryptonVanishService) boolean("vanished", isVanished)
            long("firstJoined", firstJoined.toEpochMilli())
            long("lastJoined", lastJoined.toEpochMilli())
        }
    }

    override fun tick() {
        super.tick()
        hungerMechanic()
        if (data.isDirty) session.send(PacketOutMetadata(id, data.dirty))
    }

    private fun hungerMechanic() {
        // TODO: More actions for exhaustion, add constants?
        if (!(gameMode == GameModes.SURVIVAL || gameMode == GameModes.ADVENTURE)) return
        foodTickTimer++

        // Sources:
        //      -> Minecraft Wiki https://minecraft.fandom.com/wiki/Hunger
        //      -> 3 other implementations of this exact mechanic (lines up with wiki)

        // Food System
        // The food exhaustion level accumulates exhaustion from player actions
        // over time. Once the exhaustion level exceeds a threshold of 4, it
        // resets, and then deducts a food saturation level.
        // The food saturation level, in turn, once depleted (at zero), deducts a
        // a food level every tick, only once the exhaustion has been reset again.
        // Additionally, client side, a saturation level of zero is responsible for
        // triggering the shaking of the hunger bar.
        // TLDR: The exhaustion level deducts from the saturation. The saturation level
        // follows the food level and acts as a buffer before any food levels are deducted.
        // -> Player action
        // -> Exhaustion ↑
        // -> If Exhaustion Threshold of 4
        // -> Reset Exhaustion
        // -> Saturation ↓
        // -> If Saturation Threshold of 0
        // -> Food Level ↓
        if (foodExhaustionLevel > 4f) {
            foodExhaustionLevel -= 4f
            if (foodSaturationLevel > 0) {
                foodSaturationLevel = max(foodSaturationLevel - 1, 0f)
            } else {
                foodLevel = max(foodLevel - 1, 0)
            }
        }

        // Starvation System
        // If the food level is zero, every 80 ticks, deduct a half-heart.
        // This system is conditional based on difficulty.
        //      -> Easy: Health must be greater than 10
        //      -> Normal: Health must be greater than 1
        //      -> Hard: There is no minimum health, good luck out there.
        // An exception to this system is in peaceful mode, where every 20 ticks,
        // the food level regenerates instead of deducting a half-heart.
        // NOTE: 80 are 20 ticks are the vanilla timings for hunger and
        // peaceful food regeneration respectively.
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

        // Health Regeneration System
        // Every 80 ticks if the food level is greater than
        // or equal to 18, add a half-heart to the player.
        // Healing of course, comes at an expense to the player's
        // food level, so food exhaustion and saturation are adjusted
        // to bring equilibrium to the hunger system. This is to avoid
        // a player healing infinitely.
        if (foodTickTimer == 80) {
            if (foodLevel >= 18) {
                // Avoid exceeding max health
                if (maxHealth >= health + 1) {
                    // Regenerate health
                    health++
                    // Once a half-heart has been added, increase the exhaustion by 3,
                    // or if that operation were to exceed the threshold, instead, set it to the
                    // threshold value of 4.
                    foodExhaustionLevel = min(foodExhaustionLevel + 3f, 4f)
                    // Force the saturation level to deplete by 3.
                    // So as health comes up, the food "buffer" comes down,
                    // eventually causing the food level to decrease, when saturation
                    // reaches zero.
                    foodSaturationLevel -= 3
                } else {
                    health = maxHealth
                }
            }
            foodTickTimer = 0 // reset tick timer
        }
    }

    override fun addViewer(player: KryptonPlayer): Boolean {
        if (player === this) return false
        player.session.send(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.ADD_PLAYER, this))
        return super.addViewer(player)
    }

    override fun removeViewer(player: KryptonPlayer): Boolean {
        if (player === this || !super.removeViewer(player)) return false
        player.session.send(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.REMOVE_PLAYER, this))
        return true
    }

    override fun spawnParticles(effect: ParticleEffect, location: Vector3d) {
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

    override fun teleport(player: Player) {
        teleport(player.location)
    }

    override fun vanish() {
        vanishService.vanish(this)
    }

    override fun unvanish() {
        vanishService.unvanish(this)
    }

    override fun show(player: Player) {
        vanishService.show(this, player)
    }

    override fun hide(player: Player) {
        vanishService.hide(this, player)
    }

    override fun canSee(player: Player): Boolean = vanishService.canSee(this, player)

    override fun getPermissionValue(permission: String): TriState = permissionFunction[permission]

    override fun getSpawnPacket(): Packet = PacketOutSpawnPlayer(this)

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
        if (type != null) {
            session.send(PacketOutSoundEffect(sound, type, x, y, z))
            return
        }
        session.send(PacketOutNamedSoundEffect(sound, x, y, z))
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
            return
        }
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

    override fun stopSound(stop: SoundStop) {
        session.send(PacketOutStopSound(stop))
    }

    override fun openBook(book: Book) {
        val item = KryptonAdventure.toItemStack(book)
        val slot = inventory.items.size + inventory.heldSlot
        val stateId = inventory.stateId
        session.send(PacketOutSetSlot(0, stateId, slot, item))
        session.send(PacketOutOpenBook(hand))
        session.send(PacketOutSetSlot(0, stateId, slot, inventory.mainHand))
    }

    private fun updateAbilities() {
        when (gameMode) {
            GameModes.CREATIVE -> {
                isInvulnerable = true
                canFly = true
                canInstantlyBuild = true
            }
            GameModes.SPECTATOR -> {
                isInvulnerable = true
                canFly = true
                isGliding = true
                canInstantlyBuild = false
            }
            else -> Unit
        }
        canBuild = gameMode.canBuild
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
                session.send(PacketOutChunkDataAndLight(chunk))
            }

            previousChunks?.forEach {
                session.send(PacketOutUnloadChunk(it.toInt(), (it shr 32).toInt()))
                visibleChunks -= it
            }
            previousChunks?.clear()
        }
    }

    override fun disconnect(text: Component) {
        session.disconnect(text)
    }

    private fun onAbilitiesUpdate() {
        session.send(PacketOutAbilities(this))
        updateInvisibility()
    }

    private fun updateInvisibility() {
        removeEffectParticles()
        isInvisible = gameMode === GameModes.SPECTATOR
    }

    fun updateMovementStatistics(deltaX: Double, deltaY: Double, deltaZ: Double) {
        // TODO: Walking underwater, walking on water, climbing
        if (isSwimming) {
            val value = (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F).roundToInt()
            if (value > 0) statistics.increment(CustomStatistics.SWIM_ONE_CM, value)
            return
        }
        if (isOnGround) {
            val value = (sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F).roundToInt()
            if (value > 0) when {
                isSprinting -> statistics.increment(CustomStatistics.SPRINT_ONE_CM, value)
                isSneaking -> statistics.increment(CustomStatistics.CROUCH_ONE_CM, value)
                else -> statistics.increment(CustomStatistics.WALK_ONE_CM, value)
            }
            return
        }
        if (isGliding) {
            val value = (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F).roundToInt()
            statistics.increment(CustomStatistics.AVIATE_ONE_CM, value)
            return
        }
        val value = (sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F).roundToInt()
        if (value > FLYING_ACHIEVEMENT_MINIMUM_SPEED) statistics.increment(CustomStatistics.FLY_ONE_CM, value)
    }

    fun updateMovementExhaustion(deltaX: Double, deltaY: Double, deltaZ: Double) {
        // Source: https://minecraft.fandom.com/wiki/Hunger#Exhaustion_level_increase
        if (isSwimming) {
            val value = sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
            if (value > 0) foodExhaustionLevel += 0.01F * value.toFloat()
            // 0.01/u is the vanilla level of exhaustion per unit for swimming
            return
        }
        if (isOnGround) {
            val value = sqrt(deltaX * deltaX + deltaZ * deltaZ)
            if (value > 0) when {
                isSprinting -> foodExhaustionLevel += 0.1F * value.toFloat()
                // 0.1/u is the vanilla level of exhaustion per unit for sprinting
            }
            return
        }
    }

    override var absorption: Float
        get() = data[MetadataKeys.PLAYER.ADDITIONAL_HEARTS]
        set(value) = data.set(MetadataKeys.PLAYER.ADDITIONAL_HEARTS, value)

    var score: Int
        get() = data[MetadataKeys.PLAYER.SCORE]
        set(value) = data.set(MetadataKeys.PLAYER.SCORE, value)

    override var health: Float
        get() = super.health
        set(value) {
            super.health = value
            if (!isLoaded) return
            session.send(PacketOutUpdateHealth(health, foodLevel, foodSaturationLevel))
        }

    // Sources for vanilla hunger system values:
    //      -> Minecraft Wiki https://minecraft.fandom.com/wiki/Hunger
    // 20 is the default vanilla food level
    override var foodLevel: Int = 20
        set(value) {
            field = value
            if (!isLoaded) return
            session.send(PacketOutUpdateHealth(health, foodLevel, foodSaturationLevel))
        }

    var foodTickTimer: Int = 0

    // 0 is the default vanilla food exhaustion level
    override var foodExhaustionLevel: Float = 0f

    // 5 is the default vanilla food saturation level
    override var foodSaturationLevel: Float = 5f
        set(value) {
            field = value
            if (!isLoaded) return
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
        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.ATTACK_DAMAGE, 1.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.1)
            .add(AttributeTypes.ATTACK_SPEED)
            .add(AttributeTypes.LUCK)
            .build()
    }
}
