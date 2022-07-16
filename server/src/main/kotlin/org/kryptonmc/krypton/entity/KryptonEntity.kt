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
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutRemoveEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.tags.KryptonTagTypes
import org.kryptonmc.krypton.util.ceil
import org.kryptonmc.krypton.util.floor
import org.kryptonmc.krypton.util.nextUUID
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.kryptonmc.krypton.world.fluid.handler
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
//  first, the loading and saving mechanism can probably be extracted to a separate system, to isolate entities.
//  second, all of the entity processing logic can also be moved elsewhere
//  third, there is a lot of vanilla stuff in here that is clearly just taken, which isn't allowed
@Suppress("LeakingThis")
abstract class KryptonEntity(override var world: KryptonWorld, override val type: EntityType<out Entity>) : Entity {

    final override val id: Int = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid: UUID = Random.nextUUID()
    override val teamRepresentation: Component
        get() = Component.text(uuid.toString())
    override val name: Component
        get() = customName ?: type.translation
    override val displayName: Component
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
    override var isInvulnerable: Boolean = false
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
        get() = world.getBlock(location.floorX(), location.floorY(), location.floorZ()) === Blocks.BUBBLE_COLUMN

    open val maxAirTicks: Int
        get() = 300
    open val isAlive: Boolean
        get() = !isRemoved
    open val isSpectator: Boolean
        get() = false
    protected open val pushedByFluid: Boolean
        get() = true
    override val isRideable: Boolean
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
    private var fluidOnEyes: Tag<Fluid>? = null
    var isRemoved: Boolean = false
        private set
    private var wasDamaged = false

    final override var isOnFire: Boolean
        get() = getSharedFlag(0)
        set(value) = setSharedFlag(0, value)
    final override var isSneaking: Boolean
        get() = getSharedFlag(1)
        set(value) = setSharedFlag(1, value)
    final override var isSprinting: Boolean
        get() = getSharedFlag(3)
        set(value) = setSharedFlag(3, value)
    final override var isSwimming: Boolean
        get() = getSharedFlag(4)
        set(value) = setSharedFlag(4, value)
    final override var isInvisible: Boolean
        get() = getSharedFlag(5)
        set(value) = setSharedFlag(5, value)
    final override var isGlowing: Boolean
        get() = getSharedFlag(6)
        set(value) = setSharedFlag(6, value)
    final override var air: Int
        get() = data[MetadataKeys.AIR_TICKS]
        set(value) = data.set(MetadataKeys.AIR_TICKS, value)
    final override var customName: Component?
        get() = data[MetadataKeys.CUSTOM_NAME]
        set(value) = data.set(MetadataKeys.CUSTOM_NAME, value)
    final override var isCustomNameVisible: Boolean
        get() = data[MetadataKeys.CUSTOM_NAME_VISIBILITY]
        set(value) = data.set(MetadataKeys.CUSTOM_NAME_VISIBILITY, value)
    final override var isSilent: Boolean
        get() = data[MetadataKeys.SILENT]
        set(value) = data.set(MetadataKeys.SILENT, value)
    final override var hasGravity: Boolean
        get() = !data[MetadataKeys.NO_GRAVITY]
        set(value) = data.set(MetadataKeys.NO_GRAVITY, !value)
    var pose: Pose
        get() = data[MetadataKeys.POSE]
        set(value) = data.set(MetadataKeys.POSE, value)
    final override var frozenTicks: Int
        get() = data[MetadataKeys.FROZEN_TICKS]
        set(value) = data.set(MetadataKeys.FROZEN_TICKS, value)

    init {
        data.add(MetadataKeys.FLAGS, 0)
        data.add(MetadataKeys.AIR_TICKS, maxAirTicks)
        data.add(MetadataKeys.CUSTOM_NAME, null)
        data.add(MetadataKeys.CUSTOM_NAME_VISIBILITY, false)
        data.add(MetadataKeys.SILENT, false)
        data.add(MetadataKeys.NO_GRAVITY, false)
        data.add(MetadataKeys.POSE, Pose.STANDING)
        data.add(MetadataKeys.FROZEN_TICKS, 0)
    }

    open fun onDataUpdate(key: MetadataKey<*>) {
        // TODO: Data updating
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

    private fun getSharedFlag(flag: Int): Boolean = getFlag(MetadataKeys.FLAGS, flag)

    private fun setSharedFlag(flag: Int, state: Boolean) {
        setFlag(MetadataKeys.FLAGS, flag, state)
    }

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
        val minX = (boundingBox.minimumX - 0.001).floor()
        val minY = (boundingBox.minimumY - 0.001).floor()
        val minZ = (boundingBox.minimumZ - 0.001).floor()
        val maxX = (boundingBox.maximumX + 0.001).ceil()
        val maxY = (boundingBox.maximumY + 0.001).ceil()
        val maxZ = (boundingBox.maximumZ + 0.001).ceil()
        var amount = 0.0
        val pushed = pushedByFluid
        var shouldPush = false
        var offset = Vector3d.ZERO
        var pushes = 0

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val fluid = world.getFluid(x, y, z)
                    if (!tag.contains(fluid)) continue
                    val height = y.toDouble() + fluid.handler().height(fluid, x, y, z, world)
                    if (height < minY) continue
                    shouldPush = true
                    amount = max(height - minY, amount)
                    if (!pushed) continue
                    var flow = fluid.handler().flow(fluid, x, y, z, world)
                    if (amount < 0.4) flow = flow.mul(amount)
                    offset = offset.add(flow)
                    ++pushes
                }
            }
        }

        if (offset.length() > 0.0) {
            if (pushes > 0) offset = offset.mul(1.0 / pushes)
            var wasNormalized = false
            if (this !is KryptonPlayer) {
                offset = offset.normalize()
                wasNormalized = true
            }

            val velocity = velocity
            offset = offset.mul(flowScale * 1.0)
            if (abs(velocity.x()) < FLUID_VECTOR_EPSILON && abs(velocity.z()) < FLUID_VECTOR_EPSILON && offset.length() < FLUID_VECTOR_MAGIC) {
                if (!wasNormalized) offset = offset.normalize()
                offset = offset.mul(FLUID_VECTOR_MAGIC)
            }
            this.velocity = this.velocity.add(offset)
            server.sessionManager.sendGrouped(viewers, PacketOutSetEntityVelocity(this))
        }

        fluidHeights[tag] = amount
        return shouldPush
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateUnderFluid() {
        underwater = underFluid(FluidTags.WATER)
        fluidOnEyes = null

        val x = location.floorX()
        val y = (location.y() + eyeHeight) - BREATHING_DISTANCE_BELOW_EYES
        val z = location.floorZ()
        val fluid = world.getFluid(x, y.floor(), z)

        KryptonTagManager.tags[KryptonTagTypes.FLUIDS]!!.forEach {
            it as Tag<Fluid>
            if (!it.contains(fluid)) return@forEach
            val height = y + fluid.handler().height(fluid, x, y.floor(), z, world)
            if (height > y) fluidOnEyes = it
            return
        }
    }

    fun underFluid(fluid: Tag<Fluid>): Boolean = fluidOnEyes === fluid

    // TODO: Separate interaction logic
    //open fun interact(player: KryptonPlayer, hand: Hand): InteractionResult = InteractionResult.PASS

    private fun updateSwimming() {
        isSwimming = if (isSwimming) {
            isSprinting && inWater
        } else {
            isSprinting && underwater && FluidTags.WATER.contains(world.getFluid(location.floorX(), location.floorY(), location.floorZ()))
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

    open fun isInvulnerableTo(source: KryptonDamageSource): Boolean = isRemoved ||
            isInvulnerable &&
            source.type !== DamageTypes.VOID &&
            !source.isCreativePlayer

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

    protected fun getFlag(key: MetadataKey<Byte>, flag: Int): Boolean = data[key].toInt() and (1 shl flag) != 0

    protected fun setFlag(key: MetadataKey<Byte>, flag: Int, state: Boolean) {
        val flags = data[key].toInt()
        val value = if (state) flags or (1 shl flag) else flags and (1 shl flag).inv()
        data[key] = value.toByte()
    }

    companion object {

        private val NEXT_ENTITY_ID = AtomicInteger(0)

        private const val FLUID_VECTOR_EPSILON = 0.003
        private const val FLUID_VECTOR_MAGIC = 0.0045000000000000005
        private const val WATER_FLOW_SCALE = 0.014
        private const val FAST_LAVA_FLOW_SCALE = 0.007
        private const val SLOW_LAVA_FLOW_SCALE = 0.0023333333333333335
        private const val BREATHING_DISTANCE_BELOW_EYES = 0.11111111F
    }
}
