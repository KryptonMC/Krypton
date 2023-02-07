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
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ColorParticleData
import org.kryptonmc.api.effect.particle.data.DirectionalParticleData
import org.kryptonmc.api.effect.particle.data.NoteParticleData
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.ChatVisibility
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.player.ChangeGameModeEvent
import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.commands.KryptonPermission
import org.kryptonmc.krypton.entity.util.EquipmentSlots
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
import org.kryptonmc.krypton.inventory.KryptonPlayerInventory
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.handler
import org.kryptonmc.krypton.network.NettyConnection
import org.kryptonmc.krypton.packet.out.play.GameEventTypes
import org.kryptonmc.krypton.network.PacketSendListener
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.network.chat.OutgoingChatMessage
import org.kryptonmc.krypton.network.chat.RemoteChatSession
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities
import org.kryptonmc.krypton.packet.out.play.PacketOutGameEvent
import org.kryptonmc.krypton.packet.out.play.PacketOutOpenBook
import org.kryptonmc.krypton.packet.out.play.PacketOutParticle
import org.kryptonmc.krypton.packet.out.play.PacketOutPluginMessage
import org.kryptonmc.krypton.packet.out.play.PacketOutResourcePack
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCamera
import org.kryptonmc.krypton.packet.out.play.PacketOutSetContainerSlot
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHealth
import org.kryptonmc.krypton.packet.out.play.PacketOutSystemChat
import org.kryptonmc.krypton.packet.out.play.PacketOutTeleportEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.statistic.KryptonStatisticsTracker
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.nbt.CompoundTag
import java.net.InetSocketAddress
import java.time.Instant
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.math.sqrt

class KryptonPlayer(
    override val connection: NettyConnection,
    override val profile: GameProfile,
    world: KryptonWorld,
    override val address: InetSocketAddress
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
    override val gameModeSystem: PlayerGameModeSystem = PlayerGameModeSystem(this)
    val chunkViewingSystem: PlayerChunkViewingSystem = PlayerChunkViewingSystem(this)

    override val abilities: Abilities = Abilities()
    override val inventory: KryptonPlayerInventory = KryptonPlayerInventory(this)
    override var openInventory: Inventory? = null

    override val statisticsTracker: KryptonStatisticsTracker =
        KryptonStatisticsTracker(this, server.worldManager.statsFolder().resolve("$uuid.json"))
    override val itemCooldownTracker: KryptonCooldownTracker = KryptonCooldownTracker(this)

    override var settings: KryptonPlayerSettings = KryptonPlayerSettings.DEFAULT
    private var lastActionTime = System.currentTimeMillis()
    private var chatSession: RemoteChatSession? = null

    private var camera: KryptonEntity = this
        set(value) {
            val old = field
            field = value
            if (old != field) {
                connection.send(PacketOutSetCamera(field.id))
                teleport(field.position)
            }
        }

    override val isOnline: Boolean
        get() = server.getPlayer(uuid) === this
    override var hasJoinedBefore: Boolean = false
    override var firstJoined: Instant = Instant.EPOCH
    override var lastJoined: Instant = Instant.now()

    internal var respawnData: RespawnData? = null

    override var health: Float
        get() = super.health
        set(value) {
            super.health = value
            connection.send(PacketOutSetHealth(health, foodLevel, foodSaturationLevel))
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

    override fun updateGameMode(mode: GameMode, cause: ChangeGameModeEvent.Cause): ChangeGameModeEvent? {
        val event = gameModeSystem.changeGameMode(mode, cause)
        if (event == null || !event.isAllowed()) return null

        connection.send(PacketOutGameEvent(GameEventTypes.CHANGE_GAMEMODE, mode.ordinal.toFloat()))
        if (mode == GameMode.SPECTATOR) {
            stopRiding()
        } else {
            camera = this
        }
        onAbilitiesUpdate()
        return event
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
        itemCooldownTracker.tick()
        if (data.isDirty()) connection.send(PacketOutSetEntityMetadata(id, data.collectDirty()))
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
        val packet = PacketOutParticle.fromEffect(effect, location)
        when (effect.data) {
            // Send multiple packets based on the quantity
            is DirectionalParticleData, is ColorParticleData, is NoteParticleData -> repeat(effect.quantity) { connection.send(packet) }
            // Send particles to player at location
            else -> connection.send(packet)
        }
    }

    override fun teleport(position: Vec3d) {
        val oldLocation = this.position
        this.position = position

        val packet = if (Positioning.isDeltaInMoveRange(oldLocation, this.position)) {
            PacketOutTeleportEntity.create(this)
        } else {
            PacketOutUpdateEntityPosition(
                id,
                Positioning.calculateDelta(this.position.x, oldLocation.x),
                Positioning.calculateDelta(this.position.y, oldLocation.y),
                Positioning.calculateDelta(this.position.z, oldLocation.z),
                isOnGround
            )
        }
        connection.send(packet)
        viewingSystem.sendToViewers(packet)
        chunkViewingSystem.updateChunks()
    }

    override fun teleport(player: Player) {
        teleport(player.position)
    }

    override fun sendPluginMessage(channel: Key, message: ByteArray) {
        connection.send(PacketOutPluginMessage(channel, message))
    }

    override fun sendResourcePack(pack: ResourcePack) {
        connection.send(PacketOutResourcePack(pack.uri.toString(), pack.hash, pack.isForced, pack.promptMessage))
    }

    override fun openBook(item: KryptonItemStack) {
        val slot = inventory.items.size + inventory.heldSlot
        val stateId = inventory.stateId()
        connection.send(PacketOutSetContainerSlot(0, stateId, slot, item))
        connection.send(PacketOutOpenBook(hand))
        connection.send(PacketOutSetContainerSlot(0, stateId, slot, inventory.mainHand))
    }

    override fun pointers(): Pointers {
        if (cachedPointers == null) {
            cachedPointers = Pointers.builder()
                .withDynamic(Identity.DISPLAY_NAME) { displayName }
                .withDynamic(Identity.NAME) { profile.name }
                .withDynamic(Identity.UUID) { profile.uuid }
                .withStatic(PermissionChecker.POINTER, PermissionChecker { getPermissionValue(it) })
                .withDynamic(Identity.LOCALE) { settings.locale }
                .build()
        }
        return cachedPointers!!
    }

    override fun disconnect(text: Component) {
        // We always use the play disconnect here because we should be in the play state by the time the player is constructed, and certainly
        // by the time any plugin is able to access it.
        connection.playHandler().disconnect(text)
    }

    fun disconnect() {
        vehicleSystem.ejectPassengers()
        // TODO: Stop sleeping if sleeping
    }

    override fun onAbilitiesUpdate() {
        if (connection.inPlayState()) connection.send(PacketOutAbilities.create(abilities))
        removeEffectParticles()
        isInvisible = gameMode == GameMode.SPECTATOR
    }

    fun updateMovementStatistics(deltaX: Double, deltaY: Double, deltaZ: Double) {
        // TODO: Walking underwater, walking on water, climbing
        if (isSwimming) {
            val value = (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F).roundToInt()
            if (value > 0) statisticsTracker.incrementStatistic(CustomStatistics.SWIM_ONE_CM.get(), value)
            return
        }
        if (isOnGround) {
            val value = (sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F).roundToInt()
            if (value > 0) {
                when {
                    isSprinting -> statisticsTracker.incrementStatistic(CustomStatistics.SPRINT_ONE_CM.get(), value)
                    isSneaking -> statisticsTracker.incrementStatistic(CustomStatistics.CROUCH_ONE_CM.get(), value)
                    else -> statisticsTracker.incrementStatistic(CustomStatistics.WALK_ONE_CM.get(), value)
                }
            }
            return
        }
        if (isGliding) {
            val value = (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 100F).roundToInt()
            statisticsTracker.incrementStatistic(CustomStatistics.AVIATE_ONE_CM.get(), value)
            return
        }
        val value = (sqrt(deltaX * deltaX + deltaZ * deltaZ) * 100F).roundToInt()
        if (value > FLYING_ACHIEVEMENT_MINIMUM_SPEED) statisticsTracker.incrementStatistic(CustomStatistics.FLY_ONE_CM.get(), value)
    }

    fun resetLastActionTime() {
        lastActionTime = System.currentTimeMillis()
    }

    override fun sendSystemMessage(message: Component) {
        sendSystemMessage(message, false)
    }

    override fun isPushedByFluid(): Boolean = !isFlying

    override fun canBeSeenByAnyone(): Boolean = gameModeSystem.gameMode() != GameMode.SPECTATOR && super.canBeSeenByAnyone()

    fun canUseGameMasterBlocks(): Boolean = abilities.canInstantlyBuild && hasPermission(KryptonPermission.USE_GAME_MASTER_BLOCKS.node)

    fun sendSystemMessage(message: Component, overlay: Boolean) {
        if (!acceptsSystemMessages(overlay)) return
        connection.send(PacketOutSystemChat(message, overlay), PacketSendListener.sendOnFailure {
            if (acceptsSystemMessages(false)) {
                val notDelivered = Component.text(KryptonAdventure.toPlainText(message, 256), NamedTextColor.YELLOW)
                PacketOutSystemChat(Component.translatable("multiplayer.message_not_delivered", NamedTextColor.RED, notDelivered), false)
            } else {
                null
            }
        })
    }

    fun sendChatMessage(message: OutgoingChatMessage, filter: Boolean, type: RichChatType.Bound) {
        if (acceptsChatMessages()) message.sendToPlayer(this, filter, type)
    }

    fun shouldFilterMessageTo(target: KryptonPlayer): Boolean {
        if (target === this) return false
        return settings.filterText || target.settings.filterText
    }

    private fun acceptsSystemMessages(overlay: Boolean): Boolean = if (settings.chatVisibility == ChatVisibility.HIDDEN) overlay else true

    fun acceptsChatMessages(): Boolean = settings.chatVisibility == ChatVisibility.FULL

    fun chatSession(): RemoteChatSession? = chatSession

    fun setChatSession(session: RemoteChatSession) {
        chatSession = session
    }

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
