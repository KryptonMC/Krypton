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
package org.kryptonmc.krypton.entity.player

import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongArraySet
import it.unimi.dsi.fastutil.longs.LongSet
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.player.ChangeGameModeEvent
import org.kryptonmc.api.event.player.PerformActionEvent
import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.service.AFKService
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.provide
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.adventure.BossBarManager
import org.kryptonmc.krypton.adventure.toItemStack
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.EquipmentSlots
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEquipable
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.GameState
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutActionBar
import org.kryptonmc.krypton.packet.out.play.PacketOutCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutChangeGameState
import org.kryptonmc.krypton.packet.out.play.PacketOutChat
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
import org.kryptonmc.krypton.statistic.KryptonStatisticsTracker
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.util.chunkX
import org.kryptonmc.krypton.util.chunkZ
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.chunk.ChunkPosition
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.spongepowered.math.vector.Vector3d
import org.spongepowered.math.vector.Vector3i
import java.net.InetSocketAddress
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

    var permissionFunction: PermissionFunction = DEFAULT_PERMISSION_FUNCTION

    override val name: Component = Component.text(profile.name)
    override var uuid: UUID
        get() = profile.uuid
        set(_) = Unit // Player UUIDs are read only.

    // This is a bit hacky, but ensures that data will never be sent in the wrong order for players.
    @Volatile
    var isLoaded: Boolean = false

    override var canFly: Boolean = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var canBuild: Boolean = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var canInstantlyBuild: Boolean = false
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var walkingSpeed: Float = 0.1F
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var flyingSpeed: Float = 0.05F
        set(value) {
            field = value
            if (!isLoaded) return
            onAbilitiesUpdate()
        }
    override var isFlying: Boolean = false
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
                if (chestplate.type === ItemTypes.ELYTRA && chestplate.meta.damage < chestplate.type.durability - 1) super.isGliding = true
            }
        }
    override val inventory: KryptonPlayerInventory = KryptonPlayerInventory(this)
    override var openInventory: Inventory? = null
    override val handSlots: Iterable<KryptonItemStack>
        get() = sequenceOf(inventory.mainHand, inventory.offHand).asIterable()
    override val armorSlots: Iterable<KryptonItemStack>
        get() = inventory.armor

    override val scoreboard: KryptonScoreboard = world.scoreboard
    override var locale: Locale? = null
    override val statistics: KryptonStatisticsTracker = KryptonStatisticsTracker(this, server.worldManager.statsFolder.resolve("$uuid.json"))
    override val cooldowns: KryptonCooldownTracker = KryptonCooldownTracker(this)
    override val teamRepresentation: Component = name
    override val pushedByFluid: Boolean
        get() = !isFlying

    // TODO: Per-player view distance, see issue #49
    override val viewDistance: Int = server.config.world.viewDistance
    override var chatVisibility: ChatVisibility = ChatVisibility.FULL
    override var filterText: Boolean = false
    override var allowsListing: Boolean = true
    override var time: Long = 0L

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
    // Hacks to get around Kotlin not letting us set the value of the property without calling the setter.
    private var internalGameMode: GameMode = GameMode.SURVIVAL
    override var gameMode: GameMode
        get() = internalGameMode
        set(value) = updateGameMode(value, ChangeGameModeEvent.Cause.API)
    override val isSpectator: Boolean
        get() = gameMode == GameMode.SPECTATOR
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

    private val afkService = server.servicesManager.provide<AFKService>()!!
    override var isAfk: Boolean
        get() = afkService.isAfk(this)
        set(value) = afkService.setAfk(this, value)

    val blockHandler: PlayerBlockHandler = PlayerBlockHandler(this)

    internal var respawnPosition: Vector3i? = null
    internal var respawnForced = false
    internal var respawnAngle = 0F
    internal var respawnDimension = World.OVERWORLD

    private var previousCentralX = 0
    private var previousCentralZ = 0
    private val visibleChunks = LongArraySet()

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

    internal var foodTickTimer = 0

    // 0 is the default vanilla food exhaustion level
    override var foodExhaustionLevel: Float = 0f

    // 5 is the default vanilla food saturation level
    override var foodSaturationLevel: Float = 5f
        set(value) {
            field = value
            if (!isLoaded) return
            session.send(PacketOutUpdateHealth(health, foodLevel, foodSaturationLevel))
        }

    override var absorption: Float
        get() = data[MetadataKeys.PLAYER.ADDITIONAL_HEARTS]
        set(value) = data.set(MetadataKeys.PLAYER.ADDITIONAL_HEARTS, value)
    var skinSettings: Byte
        get() = data[MetadataKeys.PLAYER.SKIN_FLAGS]
        set(value) = data.set(MetadataKeys.PLAYER.SKIN_FLAGS, value)
    override var mainHand: MainHand
        get() = if (data[MetadataKeys.PLAYER.MAIN_HAND] == 0.toByte()) MainHand.LEFT else MainHand.RIGHT
        set(value) = data.set(MetadataKeys.PLAYER.MAIN_HAND, if (value == MainHand.LEFT) 0 else 1)

    init {
        data.add(MetadataKeys.PLAYER.ADDITIONAL_HEARTS)
        data.add(MetadataKeys.PLAYER.SCORE)
        data.add(MetadataKeys.PLAYER.SKIN_FLAGS)
        data.add(MetadataKeys.PLAYER.MAIN_HAND)
        data.add(MetadataKeys.PLAYER.LEFT_SHOULDER)
        data.add(MetadataKeys.PLAYER.RIGHT_SHOULDER)
    }

    fun updateGameMode(mode: GameMode, cause: ChangeGameModeEvent.Cause) {
        if (mode === gameMode) return
        val result = server.eventManager.fireSync(ChangeGameModeEvent(this, gameMode, mode, cause)).result
        if (!result.isAllowed) return

        oldGameMode = gameMode
        internalGameMode = result.newGameMode ?: mode
        if (!isLoaded) return
        updateAbilities()
        onAbilitiesUpdate()
        server.sessionManager.sendGrouped(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.UPDATE_GAMEMODE, this))
        session.send(PacketOutChangeGameState(GameState.CHANGE_GAMEMODE, mode.ordinal.toFloat()))
        if (mode != GameMode.SPECTATOR) camera = this
    }

    override fun equipment(slot: EquipmentSlot): KryptonItemStack = when {
        slot == EquipmentSlot.MAIN_HAND -> inventory.mainHand
        slot == EquipmentSlot.OFF_HAND -> inventory.offHand
        slot.type == EquipmentSlot.Type.ARMOR -> inventory.armor[EquipmentSlots.index(slot)]
        else -> KryptonItemStack.EMPTY
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) {
        when {
            slot == EquipmentSlot.MAIN_HAND -> inventory.setHeldItem(Hand.MAIN, item)
            slot == EquipmentSlot.OFF_HAND -> inventory.setHeldItem(Hand.OFF, item)
            slot.type == EquipmentSlot.Type.ARMOR -> inventory.armor[EquipmentSlots.index(slot)] = item
        }
    }

    override fun tick() {
        super.tick()
        blockHandler.tick()
        hungerMechanic()
        cooldowns.tick()
        if (data.isDirty) session.send(PacketOutMetadata(id, data.dirty))
    }

    private fun hungerMechanic() {
        // TODO: More actions for exhaustion, add constants?
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) return
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
            Difficulty.NORMAL -> if (health > 1 && foodLevel == 0 && foodTickTimer == 80) health--
            Difficulty.HARD -> if (foodLevel == 0 && foodTickTimer == 80) health--
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

    fun isBlockActionRestricted(x: Int, y: Int, z: Int): Boolean {
        if (gameMode != GameMode.ADVENTURE && gameMode != GameMode.SPECTATOR) return false
        if (gameMode == GameMode.SPECTATOR) return true
        if (canBuild) return false
        val mainHand = inventory.mainHand
        return mainHand.isEmpty() // TODO: Check Adventure CanDestroy
    }

    fun hasCorrectTool(block: KryptonBlock): Boolean = !block.requiresCorrectTool ||
            inventory.heldItem(Hand.MAIN).type.handler().isCorrectTool(block)

    fun interactOn(entity: KryptonEntity, hand: Hand): InteractionResult {
        if (isSpectator) {
            // TODO: Open spectator menu
            return InteractionResult.PASS
        }
        // FIXME
        /*
        var heldItem = heldItem(hand)
        val result = entity.interact(this, hand)
        if (result.consumesAction) {
            if (canInstantlyBuild && heldItem === heldItem(hand)) {
                setHeldItem(hand, heldItem.withAmount())
                heldItem.amount = heldCopy.amount
            }
            return result
        }
        if (heldItem.isEmpty() || entity !is KryptonLivingEntity) return InteractionResult.PASS
        val interactResult = heldItem.type.handler().interactEntity(heldItem, this, entity, hand)
        if (interactResult.consumesAction) {
            if (heldItem.isEmpty() && !canInstantlyBuild) setHeldItem(hand, KryptonItemStack.EMPTY)
            return interactResult
        }
         */
        return InteractionResult.PASS
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
            is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> repeat(effect.quantity) { session.send(packet) }
            // Send particles to player at location
            else -> session.send(packet)
        }
    }

    override fun teleport(position: Vector3d) {
        val oldLocation = location
        location = position

        if (Positioning.deltaInMoveRange(oldLocation, location)) {
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
        if (title.times() != null) session.send(PacketOutTitleTimes(title.times()!!))
        session.send(PacketOutSubTitle(title.subtitle()))
        session.send(PacketOutTitle(title.title()))
    }

    override fun <T : Any> sendTitlePart(part: TitlePart<T>, value: T) {
        val packet = when (part) {
            TitlePart.TITLE -> PacketOutTitle(value as Component)
            TitlePart.SUBTITLE -> PacketOutSubTitle(value as Component)
            TitlePart.TIMES -> PacketOutTitleTimes(value as Title.Times)
            else -> throw IllegalArgumentException("Unknown title part $part!")
        }
        session.send(packet)
    }

    fun sendTitle(title: Component) {
        session.send(PacketOutTitle(title))
    }

    fun sendSubtitle(subtitle: Component) {
        session.send(PacketOutSubTitle(subtitle))
    }

    fun sendTitleTimes(fadeInTicks: Int, stayTicks: Int, fadeOutTicks: Int) {
        session.send(PacketOutTitleTimes(fadeInTicks, stayTicks, fadeOutTicks))
    }

    override fun clearTitle() {
        session.send(PacketOutClearTitles(false))
    }

    override fun resetTitle() {
        session.send(PacketOutClearTitles(true))
    }

    override fun showBossBar(bar: BossBar) {
        BossBarManager.addBar(bar, this)
    }

    override fun hideBossBar(bar: BossBar) {
        BossBarManager.removeBar(bar, this)
    }

    override fun playSound(sound: Sound) {
        playSound(sound, location.x(), location.y(), location.z())
    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val type = Registries.SOUND_EVENT[sound.name()]
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
        val x = entity.location.x()
        val y = entity.location.y()
        val z = entity.location.z()
        session.send(PacketOutNamedSoundEffect(sound.name(), sound.source(), x, y, z, sound.volume(), sound.pitch()))
    }

    override fun stopSound(stop: SoundStop) {
        session.send(PacketOutStopSound(stop))
    }

    override fun openBook(book: Book) {
        openBook(book.toItemStack())
    }

    fun openBook(item: KryptonItemStack) {
        val slot = inventory.items.size + inventory.heldSlot
        val stateId = inventory.stateId
        session.send(PacketOutSetSlot(0, stateId, slot, item))
        session.send(PacketOutOpenBook(hand))
        session.send(PacketOutSetSlot(0, stateId, slot, inventory.mainHand))
    }

    private fun updateAbilities() {
        when (gameMode) {
            GameMode.CREATIVE -> {
                isInvulnerable = true
                canFly = true
                canInstantlyBuild = true
            }
            GameMode.SPECTATOR -> {
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
        val centralX = location.chunkX()
        val centralZ = location.chunkZ()
        val radius = server.config.world.viewDistance

        if (firstLoad) {
            for (x in centralX - radius..centralX + radius) {
                for (z in centralZ - radius..centralZ + radius) {
                    newChunks.add(ChunkPosition.toLong(x, z))
                }
            }
        } else if (abs(centralX - previousCentralX) > radius || abs(centralZ - previousCentralZ) > radius) {
            visibleChunks.clear()
            for (x in centralX - radius..centralX + radius) {
                for (z in centralZ - radius..centralZ + radius) {
                    newChunks.add(ChunkPosition.toLong(x, z))
                }
            }
        } else if (previousCentralX != centralX || previousCentralZ != centralZ) {
            previousChunks = LongArraySet(visibleChunks)
            for (x in centralX - radius..centralX + radius) {
                for (z in centralZ - radius..centralZ + radius) {
                    val pos = ChunkPosition.toLong(x, z)
                    if (visibleChunks.contains(pos)) previousChunks.remove(pos) else newChunks.add(pos)
                }
            }
        } else {
            return
        }

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

        visibleChunks.addAll(newChunks)
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
                session.write(chunk.cachedPacket)
            }

            if (previousChunks == null) return@thenRun
            previousChunks.forEach {
                session.send(PacketOutUnloadChunk(it.toInt(), (it shr 32).toInt()))
                visibleChunks.remove(it)
            }
            previousChunks.clear()
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
        isInvisible = gameMode == GameMode.SPECTATOR
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

    companion object {

        private const val FLYING_ACHIEVEMENT_MINIMUM_SPEED = 25
        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.ATTACK_DAMAGE, 1.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.1)
            .add(AttributeTypes.ATTACK_SPEED)
            .add(AttributeTypes.LUCK)
            .build()

        private val DEFAULT_PERMISSION_FUNCTION = PermissionFunction.ALWAYS_NOT_SET
        @JvmField
        val DEFAULT_PERMISSIONS: PermissionProvider = PermissionProvider { DEFAULT_PERMISSION_FUNCTION }
    }
}
