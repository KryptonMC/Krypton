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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.entity.player.PlayerSettings
import org.kryptonmc.api.event.player.ChangeGameModeEvent
import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.service.AFKService
import org.kryptonmc.api.service.provide
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.EquipmentSlots
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.components.BasePlayer
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.player.PlayerSerializer
import org.kryptonmc.krypton.entity.system.PlayerChunkViewingSystem
import org.kryptonmc.krypton.entity.system.PlayerGameModeSystem
import org.kryptonmc.krypton.entity.system.PlayerHungerSystem
import org.kryptonmc.krypton.event.player.KryptonChangeGameModeEvent
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.network.SessionHandler
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.packet.out.play.GameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfo
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerSlot
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHealth
import org.kryptonmc.krypton.packet.out.play.PacketOutTeleportEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.statistic.KryptonStatisticsTracker
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.GameModes
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.util.Positioning
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.nbt.CompoundTag
import java.net.InetSocketAddress
import java.time.Instant
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Suppress("INAPPLICABLE_JVM_NAME")
class KryptonPlayer(
    override val session: SessionHandler,
    override val profile: GameProfile,
    world: KryptonWorld,
    override val address: InetSocketAddress,
    override val publicKey: PlayerPublicKey?
) : KryptonLivingEntity(world), BasePlayer {

    override val type: KryptonEntityType<KryptonPlayer>
        get() = KryptonEntityTypes.PLAYER
    override val serializer: EntitySerializer<KryptonPlayer>
        get() = PlayerSerializer
    override var permissionFunction: PermissionFunction = DEFAULT_PERMISSION_FUNCTION

    override val name: Component = Component.text(profile.name)
    override var uuid: UUID
        get() = profile.uuid
        set(_) = Unit // Player UUIDs are read only.

    override val hungerSystem: PlayerHungerSystem = PlayerHungerSystem(this)
    val gameModeSystem: PlayerGameModeSystem = PlayerGameModeSystem(this)
    val chunkViewingSystem: PlayerChunkViewingSystem = PlayerChunkViewingSystem(this)

    override val abilities: Abilities = Abilities()
    override val inventory: KryptonPlayerInventory = KryptonPlayerInventory(this)
    override var openInventory: Inventory? = null

    override val statistics: KryptonStatisticsTracker = KryptonStatisticsTracker(this, server.worldManager.statsFolder.resolve("$uuid.json"))
    override val cooldowns: KryptonCooldownTracker = KryptonCooldownTracker(this)

    override var settings: PlayerSettings = KryptonPlayerSettings.DEFAULT
    private val chatSender = ChatSender(uuid, publicKey)
    private var lastActionTime = System.currentTimeMillis()

    private var camera: KryptonEntity = this
        set(value) {
            val old = field
            field = value
            if (old != field) {
                session.send(PacketOutSetCamera(field.id))
                teleport(field.location)
            }
        }

    var oldGameMode: GameMode? = null
    // Hacks to get around Kotlin not letting us set the value of the property without calling the setter.
    private var internalGameMode = GameMode.SURVIVAL
    override var gameMode: GameMode
        get() = internalGameMode
        set(value) = updateGameMode(value, ChangeGameModeEvent.Cause.API)
    val canUseGameMasterBlocks: Boolean
        get() = abilities.canInstantlyBuild && hasPermission(KryptonPermission.USE_GAME_MASTER_BLOCKS.node)
    override val canBeSeenByAnyone: Boolean
        get() = gameMode != GameMode.SPECTATOR && super.canBeSeenByAnyone
    override val isPushedByFluid: Boolean
        get() = !isFlying

    override val isOnline: Boolean
        get() = server.getPlayer(uuid) === this
    override var hasJoinedBefore: Boolean = false
    override var firstJoined: Instant = Instant.EPOCH
    override var lastJoined: Instant = Instant.now()

    override var isAfk: Boolean
        get() = server.servicesManager.provide<AFKService>()!!.isAfk(this)
        set(value) = server.servicesManager.provide<AFKService>()!!.setAfk(this, value)

    internal var respawnData: RespawnData? = null

    override var health: Float
        get() = super.health
        set(value) {
            super.health = value
            session.send(PacketOutSetHealth(health, foodLevel, foodSaturationLevel))
        }
    override var absorption: Float
        get() = data.get(MetadataKeys.Player.ADDITIONAL_HEARTS)
        set(value) = data.set(MetadataKeys.Player.ADDITIONAL_HEARTS, value)

    override fun defineData() {
        super<KryptonLivingEntity>.defineData()
        data.define(MetadataKeys.Player.ADDITIONAL_HEARTS, 0F)
        data.define(MetadataKeys.Player.SCORE, 0)
        data.define(MetadataKeys.Player.SKIN_FLAGS, 0)
        data.define(MetadataKeys.Player.MAIN_HAND, 1)
        data.define(MetadataKeys.Player.LEFT_SHOULDER, CompoundTag.EMPTY)
        data.define(MetadataKeys.Player.RIGHT_SHOULDER, CompoundTag.EMPTY)
    }

    fun updateGameMode(mode: GameMode, cause: ChangeGameModeEvent.Cause) {
        if (mode === gameMode) return
        val result = server.eventManager.fireSync(KryptonChangeGameModeEvent(this, gameMode, mode, cause)).result
        if (!result.isAllowed) return

        oldGameMode = gameMode
        internalGameMode = result.newGameMode ?: mode
        GameModes.updatePlayerAbilities(mode, abilities)
        onAbilitiesUpdate()
        server.sessionManager.sendGrouped(PacketOutPlayerInfo(PacketOutPlayerInfo.Action.UPDATE_GAMEMODE, this))
        session.send(PacketOutGameEvent(GameEvent.CHANGE_GAMEMODE, mode.ordinal.toFloat()))
        if (mode != GameMode.SPECTATOR) camera = this
    }

    override fun getEquipment(slot: EquipmentSlot): KryptonItemStack = when {
        slot == EquipmentSlot.MAIN_HAND -> inventory.mainHand
        slot == EquipmentSlot.OFF_HAND -> inventory.offHand
        slot.type == EquipmentSlot.Type.ARMOR -> inventory.armor.get(EquipmentSlots.index(slot))
        else -> KryptonItemStack.EMPTY
    }

    override fun setEquipment(slot: EquipmentSlot, item: KryptonItemStack) {
        when {
            slot == EquipmentSlot.MAIN_HAND -> inventory.setHeldItem(Hand.MAIN, item)
            slot == EquipmentSlot.OFF_HAND -> inventory.setHeldItem(Hand.OFF, item)
            slot.type == EquipmentSlot.Type.ARMOR -> inventory.armor.set(EquipmentSlots.index(slot), item)
        }
    }

    override fun tick() {
        super.tick()
        gameModeSystem.tick()
        hungerSystem.tick()
        cooldowns.tick()
        if (data.isDirty) session.send(PacketOutSetEntityMetadata(id, data.collectDirty()))
    }

    @Suppress("UnusedPrivateMember") // We will use the position later.
    fun isBlockActionRestricted(position: BlockPos): Boolean {
        if (gameMode != GameMode.ADVENTURE && gameMode != GameMode.SPECTATOR) return false
        if (gameMode == GameMode.SPECTATOR) return true
        if (canBuild) return false
        val mainHand = inventory.mainHand
        return mainHand.isEmpty() // TODO: Check Adventure CanDestroy
    }

    fun hasCorrectTool(state: KryptonBlockState): Boolean = !state.requiresCorrectTool ||
            inventory.mainHand.type.handler().isCorrectTool(state)

    fun getDestroySpeed(state: KryptonBlockState): Float {
        var speed = inventory.getDestroySpeed(state)
        // TODO: Add enchantment and effect checking here
        // TODO: Check aqua affinity when implemented
        if (waterPhysicsSystem.isUnderFluid(FluidTags.WATER)) speed /= WATER_FLYING_DESTROY_SPEED_FACTOR
        if (!isOnGround) speed /= WATER_FLYING_DESTROY_SPEED_FACTOR
        return speed
    }

    @Suppress("UnusedPrivateMember")
    fun interactOn(entity: KryptonEntity, hand: Hand): InteractionResult {
        if (gameMode == GameMode.SPECTATOR) {
            // TODO: Open spectator menu
            return InteractionResult.PASS
        }
        // FIXME
        /*
        val heldItem = getHeldItem(hand)
        val result = entity.interact(this, hand)
        if (result.consumesAction()) {
            val currentHeldItem = getHeldItem(hand)
            if (abilities.canInstantlyBuild && heldItem !== currentHeldItem && currentHeldItem.amount < heldItem.amount) {
                setHeldItem(hand, heldItem.withAmount(currentHeldItem.amount))
            }
            return result
        }
        if (heldItem.isEmpty() || entity !is KryptonLivingEntity) return InteractionResult.PASS
        val interactResult = heldItem.type.handler().interactEntity(heldItem, this, entity, hand)
        if (interactResult.consumesAction()) {
            if (heldItem.isEmpty() && !canInstantlyBuild) setHeldItem(hand, KryptonItemStack.EMPTY)
            return interactResult
        }
        */
        return InteractionResult.PASS
    }

    override fun spawnParticles(effect: ParticleEffect, location: Vec3d) {
        val packet = PacketOutParticle.from(effect, location)
        when (effect.data) {
            // Send multiple packets based on the quantity
            is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> repeat(effect.quantity) { session.send(packet) }
            // Send particles to player at location
            else -> session.send(packet)
        }
    }

    override fun teleport(position: Vec3d) {
        val oldLocation = location
        location = position

        if (Positioning.deltaInMoveRange(oldLocation, location)) {
            session.send(PacketOutTeleportEntity(id, location, yaw, pitch, isOnGround))
        } else {
            session.send(PacketOutUpdateEntityPosition(
                id,
                Positioning.delta(location.x, oldLocation.x),
                Positioning.delta(location.y, oldLocation.y),
                Positioning.delta(location.z, oldLocation.z),
                isOnGround
            ))
        }
        chunkViewingSystem.updateChunks()
    }

    override fun teleport(player: Player) {
        teleport(player.location)
    }

    override fun sendPluginMessage(channel: Key, message: ByteArray) {
        session.send(PacketOutPluginMessage(channel, message))
    }

    override fun sendResourcePack(pack: ResourcePack) {
        session.send(PacketOutResourcePack(pack))
    }

    override fun openBook(item: KryptonItemStack) {
        val slot = inventory.items.size + inventory.heldSlot
        val stateId = inventory.stateId
        session.send(PacketOutSetContainerSlot(0, stateId, slot, item))
        session.send(PacketOutOpenBook(hand))
        session.send(PacketOutSetContainerSlot(0, stateId, slot, inventory.mainHand))
    }

    override fun pointers(): Pointers {
        if (cachedPointers == null) {
            cachedPointers = Pointers.builder()
                .withDynamic(Identity.DISPLAY_NAME, ::displayName)
                .withDynamic(Identity.NAME) { profile.name }
                .withDynamic(Identity.UUID) { profile.uuid }
                .withStatic(PermissionChecker.POINTER, PermissionChecker(::getPermissionValue))
                .withDynamic(Identity.LOCALE) { settings.locale }
                .build()
        }
        return cachedPointers!!
    }

    override fun disconnect(text: Component) {
        session.disconnect(text)
    }

    override fun onAbilitiesUpdate() {
        session.send(PacketOutAbilities(abilities))
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
            if (value > 0) {
                when {
                    isSprinting -> statistics.increment(CustomStatistics.SPRINT_ONE_CM, value)
                    isSneaking -> statistics.increment(CustomStatistics.CROUCH_ONE_CM, value)
                    else -> statistics.increment(CustomStatistics.WALK_ONE_CM, value)
                }
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

    fun resetLastActionTime() {
        lastActionTime = System.currentTimeMillis()
    }

    override fun asChatSender(): ChatSender = chatSender

    companion object {

        private const val FLYING_ACHIEVEMENT_MINIMUM_SPEED = 25
        private const val WATER_FLYING_DESTROY_SPEED_FACTOR = 5F

        private val DEFAULT_PERMISSION_FUNCTION = PermissionFunction.ALWAYS_NOT_SET
        @JvmField
        val DEFAULT_PERMISSIONS: PermissionProvider = PermissionProvider { DEFAULT_PERMISSION_FUNCTION }

        private const val DEFAULT_ATTACK_DAMAGE = 1.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.1

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonLivingEntity.attributes()
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_SPEED)
            .add(KryptonAttributeTypes.LUCK)
    }
}
