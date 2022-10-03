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
package org.kryptonmc.krypton.entity

import com.google.common.collect.Iterables
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutRemoveEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.tags.KryptonTagTypes
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.UnaryOperator
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

// TODO: If we keep doing this the vanilla way, this is going to end up with way too much in it.
//  First, the loading and saving mechanism can probably be extracted to a separate system, to isolate entities.
//  Second, all of the entity processing logic can also be moved elsewhere
//  Third, there is a lot of vanilla stuff in here that is clearly just taken, which isn't allowed
@Suppress("LeakingThis")
abstract class KryptonEntity(final override var world: KryptonWorld, final override val type: EntityType<out Entity>) : Entity {

    final override val id: Int = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid: UUID = Maths.createInsecureUUID(Random)
    override val teamRepresentation: Component
        get() = Component.text(uuid.toString())
    override val name: Component
        get() = customName ?: type.translation
    final override val displayName: Component
        get() {
            val team = team ?: return name
            return team.formatName(name).style {
                it.hoverEvent(asHoverEvent())
                it.insertion(uuid.toString())
            }
        }
    protected var cachedPointers: Pointers? = null
    val data: MetadataHolder = MetadataHolder(this)

    final override val server: KryptonServer = world.server

    override val team: Team?
        get() = world.scoreboard.memberTeam(teamRepresentation)
    final override var location: Vector3d = Vector3d.ZERO
    final override var rotation: Vector2f = Vector2f.ZERO
    final override var velocity: Vector3d = Vector3d.ZERO
    final override var boundingBox: BoundingBox = BoundingBox.zero()
    final override var isOnGround: Boolean = true
    final override var ticksExisted: Int = 0
    final override var fireTicks: Short = 0
    final override var isInvulnerable: Boolean = false
    final override var fallDistance: Float = 0F

    final override val isPassenger: Boolean
        get() = vehicle != null
    final override val isVehicle: Boolean
        get() = passengers.isNotEmpty()
    final override val passengers: MutableList<Entity> = mutableListOf()
    final override var vehicle: Entity? = null
        set(value) {
            if (value !is KryptonEntity) return
            field = value
        }
    val rootVehicle: KryptonEntity
        get() {
            var root: KryptonEntity = this
            while (root.isPassenger) {
                root = root.vehicle!! as KryptonEntity
            }
            return root
        }
    val hasExactlyOnePlayerPassenger: Boolean
        get() = indirectPassengersSequence.count { it is Player } == 1
    private val selfAndPassengers: Sequence<Entity>
        get() = sequenceOf(this).plus(indirectPassengersSequence)
    private val indirectPassengersSequence: Sequence<Entity>
        get() = passengers.asSequence().flatMap { (it as KryptonEntity).selfAndPassengers }

    final override var inWater: Boolean = false
    final override var inLava: Boolean = false
    final override var underwater: Boolean = false
    val inBubbleColumn: Boolean
        get() = world.getBlock(location.floorX(), location.floorY(), location.floorZ()).eq(Blocks.BUBBLE_COLUMN)

    open val maxAirTicks: Int
        get() = DEFAULT_MAX_AIR
    open val isAlive: Boolean
        get() = !isRemoved
    open val isSpectator: Boolean
        get() = false
    protected open val pushedByFluid: Boolean
        get() = true
    final override val isRideable: Boolean
        get() = type.isRideable

    open val soundSource: Sound.Source
        get() = Sound.Source.NEUTRAL
    protected open val swimSound: SoundEvent
        get() = SoundEvents.GENERIC_SWIM
    protected open val splashSound: SoundEvent
        get() = SoundEvents.GENERIC_SPLASH
    protected open val highSpeedSplashSound: SoundEvent
        get() = SoundEvents.GENERIC_SPLASH

    open val handSlots: Iterable<KryptonItemStack>
        get() = emptyList()
    open val armorSlots: Iterable<KryptonItemStack>
        get() = emptyList()
    open val allSlots: Iterable<KryptonItemStack>
        get() = Iterables.concat(handSlots, armorSlots)

    val viewers: MutableSet<KryptonPlayer> = ConcurrentHashMap.newKeySet()
    private var eyeHeight = 0F
    private val fluidHeights = Object2DoubleArrayMap<Tag<Fluid>>(2)
    private val fluidOnEyes = HashSet<Tag<Fluid>>()
    var isRemoved: Boolean = false
        private set
    private var wasDamaged = false

    final override var isOnFire: Boolean
        get() = getFlag(MetadataKeys.Entity.FLAGS, FLAG_ON_FIRE)
        set(value) = setFlag(MetadataKeys.Entity.FLAGS, FLAG_ON_FIRE, value)
    final override var isSneaking: Boolean
        get() = getFlag(MetadataKeys.Entity.FLAGS, FLAG_SNEAKING)
        set(value) = setFlag(MetadataKeys.Entity.FLAGS, FLAG_SNEAKING, value)
    final override var isSprinting: Boolean
        get() = getFlag(MetadataKeys.Entity.FLAGS, FLAG_SPRINTING)
        set(value) = setFlag(MetadataKeys.Entity.FLAGS, FLAG_SPRINTING, value)
    final override var isSwimming: Boolean
        get() = getFlag(MetadataKeys.Entity.FLAGS, FLAG_SWIMMING)
        set(value) = setFlag(MetadataKeys.Entity.FLAGS, FLAG_SWIMMING, value)
    final override var isInvisible: Boolean
        get() = getFlag(MetadataKeys.Entity.FLAGS, FLAG_INVISIBLE)
        set(value) = setFlag(MetadataKeys.Entity.FLAGS, FLAG_INVISIBLE, value)
    final override var isGlowing: Boolean
        get() = getFlag(MetadataKeys.Entity.FLAGS, FLAG_GLOWING)
        set(value) = setFlag(MetadataKeys.Entity.FLAGS, FLAG_GLOWING, value)
    final override var air: Int
        get() = data.get(MetadataKeys.Entity.AIR_TICKS)
        set(value) = data.set(MetadataKeys.Entity.AIR_TICKS, value)
    final override var customName: Component?
        get() = data.get(MetadataKeys.Entity.CUSTOM_NAME)
        set(value) = data.set(MetadataKeys.Entity.CUSTOM_NAME, value)
    final override var isCustomNameVisible: Boolean
        get() = data.get(MetadataKeys.Entity.CUSTOM_NAME_VISIBILITY)
        set(value) = data.set(MetadataKeys.Entity.CUSTOM_NAME_VISIBILITY, value)
    final override var isSilent: Boolean
        get() = data.get(MetadataKeys.Entity.SILENT)
        set(value) = data.set(MetadataKeys.Entity.SILENT, value)
    final override var hasGravity: Boolean
        get() = !data.get(MetadataKeys.Entity.NO_GRAVITY)
        set(value) = data.set(MetadataKeys.Entity.NO_GRAVITY, !value)
    var pose: Pose
        get() = data.get(MetadataKeys.Entity.POSE)
        set(value) = data.set(MetadataKeys.Entity.POSE, value)
    final override var frozenTicks: Int
        get() = data.get(MetadataKeys.Entity.FROZEN_TICKS)
        set(value) = data.set(MetadataKeys.Entity.FROZEN_TICKS, value)

    init {
        data.add(MetadataKeys.Entity.FLAGS, 0)
        data.add(MetadataKeys.Entity.AIR_TICKS, maxAirTicks)
        data.add(MetadataKeys.Entity.CUSTOM_NAME, null)
        data.add(MetadataKeys.Entity.CUSTOM_NAME_VISIBILITY, false)
        data.add(MetadataKeys.Entity.SILENT, false)
        data.add(MetadataKeys.Entity.NO_GRAVITY, false)
        data.add(MetadataKeys.Entity.POSE, Pose.STANDING)
        data.add(MetadataKeys.Entity.FROZEN_TICKS, 0)
    }

    open fun onDataUpdate(key: MetadataKey<*>) {
        // TODO: Update dimensions when key is pose
    }

    open fun addViewer(player: KryptonPlayer): Boolean {
        if (!viewers.add(player)) return false
        player.session.send(getSpawnPacket())
        player.session.send(PacketOutSetEntityMetadata(id, data.collectAll()))
        player.session.send(PacketOutSetHeadRotation(id, rotation.x()))
        return true
    }

    open fun removeViewer(player: KryptonPlayer): Boolean {
        if (!viewers.remove(player)) return false
        player.session.send(PacketOutRemoveEntities(id))
        return true
    }

    fun playSound(event: SoundEvent, volume: Float, pitch: Float) {
        if (isSilent) return
        world.playSound(location.x(), location.y(), location.z(), event, soundSource, volume, pitch)
    }

    open fun tick() {
        updateWater()
        updateUnderFluid()
        updateSwimming()
        if (data.isDirty) server.sessionManager.sendGrouped(viewers, PacketOutSetEntityMetadata(id, data.collectDirty()))
        if (wasDamaged) {
            server.sessionManager.sendGrouped(viewers, PacketOutSetEntityVelocity(this))
            wasDamaged = false
        }
    }

    protected open fun getSpawnPacket(): Packet = PacketOutSpawnEntity(this)

    private fun updateWater(): Boolean {
        fluidHeights.clear()
        updateWaterCurrent()
        val lavaScale = if (world.dimensionType.isUltrawarm) FAST_LAVA_FLOW_SCALE else SLOW_LAVA_FLOW_SCALE
        val pushedFromLava = updateFluidHeight(FluidTags.LAVA, lavaScale)
        return inWater || pushedFromLava
    }

    private fun updateWaterCurrent() {
        if (updateFluidHeight(FluidTags.WATER, WATER_FLOW_SCALE)) {
            fallDistance = 0F
            inWater = true
            fireTicks = 0
        } else {
            inWater = false
        }
    }

    private fun updateFluidHeight(tag: Tag<Fluid>, flowScale: Double): Boolean {
        val minX = Maths.floor(boundingBox.minimumX - 0.001)
        val minY = Maths.floor(boundingBox.minimumY - 0.001)
        val minZ = Maths.floor(boundingBox.minimumZ - 0.001)
        val maxX = Maths.ceil(boundingBox.maximumX + 0.001)
        val maxY = Maths.ceil(boundingBox.maximumY + 0.001)
        val maxZ = Maths.ceil(boundingBox.maximumZ + 0.001)
        var amount = 0.0
        val pushed = pushedByFluid
        var shouldPush = false
        var offset = Vector3d.ZERO
        var pushes = 0

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val fluid = world.getFluid(x, y, z)
                    if (!fluid.eq(tag)) continue
                    val height = (y.toFloat() + fluid.getHeight(world, x, y, z)).toDouble()
                    if (height < minY) continue
                    shouldPush = true
                    amount = max(height - minY, amount)
                    if (!pushed) continue
                    var flow = fluid.getFlow(world, x, y, z)
                    if (amount < 0.4) flow = flow.mul(amount)
                    offset = offset.add(flow)
                    ++pushes
                }
            }
        }

        if (offset.length() > 0.0) {
            if (pushes > 0) offset = offset.mul(1.0 / pushes)
            if (this !is KryptonPlayer) offset = offset.normalize()

            val velocity = velocity
            offset = offset.mul(flowScale * 1.0)
            if (abs(velocity.x()) < FLUID_VECTOR_EPSILON && abs(velocity.z()) < FLUID_VECTOR_EPSILON && offset.length() < FLUID_VECTOR_MAGIC) {
                offset = offset.normalize().mul(FLUID_VECTOR_MAGIC)
            }
            this.velocity = this.velocity.add(offset)
            server.sessionManager.sendGrouped(viewers, PacketOutSetEntityVelocity(this))
        }

        fluidHeights.put(tag, amount)
        return shouldPush
    }

    private fun updateUnderFluid() {
        underwater = underFluid(FluidTags.WATER)
        fluidOnEyes.clear()

        val y = (location.y() + eyeHeight) - BREATHING_DISTANCE_BELOW_EYES
        val vehicle: KryptonEntity? = vehicle?.downcast()
        if (vehicle is KryptonBoat && !vehicle.underwater && vehicle.boundingBox.maximumY >= y && vehicle.boundingBox.minimumY <= y) return

        val x = location.floorX()
        val blockY = Maths.floor(y)
        val z = location.floorZ()
        val fluid = world.getFluid(x, blockY, z)

        val height = (blockY.toFloat() + fluid.getHeight(world, x, blockY, z))
        if (height <= y) return
        KryptonTagManager.get(KryptonTagTypes.FLUIDS).forEach(fluidOnEyes::add)
    }

    fun underFluid(fluid: Tag<Fluid>): Boolean = fluidOnEyes.contains(fluid)

    // TODO: Separate interaction logic
    //open fun interact(player: KryptonPlayer, hand: Hand): InteractionResult = InteractionResult.PASS

    private fun updateSwimming() {
        isSwimming = if (isSwimming) {
            isSprinting && inWater && !isPassenger
        } else {
            isSprinting && underwater && !isPassenger && world.getFluid(location.floorX(), location.floorY(), location.floorZ()).eq(FluidTags.WATER)
        }
    }

    final override fun move(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
        location = location.add(x, y, z)
        rotation = rotation.add(yaw, pitch)
    }

    final override fun moveTo(x: Double, y: Double, z: Double) {
        location = Vector3d(x, y, z)
    }

    final override fun look(yaw: Float, pitch: Float) {
        rotation = Vector2f(yaw, pitch)
    }

    final override fun reposition(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
        moveTo(x, y, z)
        look(yaw, pitch)
    }

    open fun isInvulnerableTo(source: KryptonDamageSource): Boolean =
        isRemoved || isInvulnerable && source.type !== DamageTypes.VOID && !source.isCreativePlayer

    open fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        markDamaged()
        return false
    }

    protected fun markDamaged() {
        wasDamaged = true
    }

    final override fun damage(source: DamageSource, damage: Float): Boolean {
        if (source !is KryptonDamageSource) return false
        return damage(source, damage)
    }

    override fun getPermissionValue(permission: String): TriState = TriState.FALSE

    override fun remove() {
        if (isRemoved) return
        isRemoved = true
        ejectVehicle()
        ejectPassengers()
        world.removeEntity(this)
    }

    override fun identity(): Identity = Identity.identity(uuid)

    override fun pointers(): Pointers {
        if (cachedPointers == null) {
            cachedPointers = Pointers.builder()
                .withDynamic(Identity.DISPLAY_NAME, ::name)
                .withDynamic(Identity.UUID, ::uuid)
                .withStatic(PermissionChecker.POINTER, PermissionChecker(::getPermissionValue))
                .build()
        }
        return cachedPointers!!
    }

    override fun asHoverEvent(op: UnaryOperator<HoverEvent.ShowEntity>): HoverEvent<HoverEvent.ShowEntity> =
        HoverEvent.showEntity(op.apply(HoverEvent.ShowEntity.of(type.key(), uuid, displayName)))

    open fun asChatSender(): ChatSender = ChatSender.SYSTEM

    override fun addPassenger(entity: Entity) {
        if (passengers.contains(entity) || entity.passengers.contains(this)) return
        entity.vehicle = this
        passengers.add(entity)
        server.sessionManager.sendGrouped(PacketOutSetPassengers(this))
    }

    override fun removePassenger(entity: Entity) {
        if (entity.vehicle === this) return
        if (!passengers.contains(entity)) return
        entity.vehicle = null
        passengers.remove(entity)
        server.sessionManager.sendGrouped(PacketOutSetPassengers(this))
    }

    override fun ejectPassengers() {
        passengers.forEach(Entity::ejectVehicle)
    }

    override fun ejectVehicle() {
        vehicle?.removePassenger(this)
        vehicle = null
    }

    override fun tryRide(entity: Entity) {
        if (isRideable) addPassenger(entity)
    }

    protected fun getFlag(key: MetadataKey<Byte>, flag: Int): Boolean = data.get(key).toInt() and (1 shl flag) != 0

    protected fun setFlag(key: MetadataKey<Byte>, flag: Int, state: Boolean) {
        val flags = data.get(key).toInt()
        val value = if (state) flags or (1 shl flag) else flags and (1 shl flag).inv()
        data.set(key, value.toByte())
    }

    companion object {

        private val NEXT_ENTITY_ID = AtomicInteger(0)

        private const val FLAG_ON_FIRE = 0
        private const val FLAG_SNEAKING = 1
        private const val FLAG_SPRINTING = 3
        private const val FLAG_SWIMMING = 4
        private const val FLAG_INVISIBLE = 5
        private const val FLAG_GLOWING = 6
        private const val DEFAULT_MAX_AIR = 15 * 20 // 15 seconds in ticks

        private const val FLUID_VECTOR_EPSILON = 0.003
        private const val FLUID_VECTOR_MAGIC = 0.0045000000000000005
        private const val WATER_FLOW_SCALE = 0.014
        private const val FAST_LAVA_FLOW_SCALE = 0.007
        private const val SLOW_LAVA_FLOW_SCALE = 0.0023333333333333335
        const val BREATHING_DISTANCE_BELOW_EYES: Float = 0.11111111F
    }
}
