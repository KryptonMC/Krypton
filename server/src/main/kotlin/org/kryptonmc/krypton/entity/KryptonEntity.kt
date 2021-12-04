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
package org.kryptonmc.krypton.entity

import com.google.common.collect.Iterables
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutDestroyEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetPassengers
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.tags.KryptonTagTypes
import org.kryptonmc.krypton.util.ceil
import org.kryptonmc.krypton.util.floor
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nextUUID
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.UnaryOperator
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

@Suppress("LeakingThis")
abstract class KryptonEntity(
    override var world: KryptonWorld,
    override val type: EntityType<out Entity>
) : Entity {

    final override val id = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid = Random.nextUUID()
        set(value) {
            field = value
            uuidComponent = Component.text(value.toString())
            identity = Identity.identity(value)
        }
    private var uuidComponent: Component? = null
        get() {
            if (field == null) field = Component.text(uuid.toString())
            return field
        }
    private var identity: Identity? = null
        get() {
            if (field == null) field = Identity.identity(uuid)
            return field
        }
    override val teamRepresentation: Component
        get() = uuidComponent!!
    override val name: Component
        get() = customName ?: type.translation
    override val displayName: Component
        get() = team?.formatName(name)?.style {
            it.hoverEvent(asHoverEvent())
            it.insertion(uuid.toString())
        } ?: name
    val data = MetadataHolder(this)

    final override val server = world.server

    override val team: Team?
        get() = world.scoreboard.memberTeam(teamRepresentation)
    final override var location: Vector3d = Vector3d.ZERO
    final override var rotation: Vector2f = Vector2f.ZERO
    final override var velocity: Vector3d = Vector3d.ZERO
    final override var boundingBox = BoundingBox.zero()
    final override var dimensions = EntityDimensions.fixed(type.dimensions.width, type.dimensions.height)
    final override var isOnGround = true
    final override var ticksExisted = 0
    final override var fireTicks: Short = 0
    override var isInvulnerable = false
    final override var fallDistance = 0F

    final override val isPassenger: Boolean
        get() = vehicle != null
    final override val isVehicle: Boolean
        get() = passengers.isNotEmpty()
    final override val passengers = mutableListOf<Entity>()
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
    val selfAndPassengers: Sequence<Entity>
        get() = sequenceOf(this).plus(indirectPassengersSequence)
    val passengersAndSelf: Sequence<Entity>
        get() = indirectPassengersSequence.plus(this)
    val indirectPassengers: Iterable<Entity>
        get() = Iterable { indirectPassengersSequence.iterator() }
    private val indirectPassengersSequence: Sequence<Entity>
        get() = passengers.asSequence().flatMap { (it as KryptonEntity).selfAndPassengers }

    final override var inWater = false
    final override var inLava = false
    final override var underwater = false

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
    var noPhysics = false
    private var oldVelocity = Vector3d.ZERO
    private var eyeHeight = 0F
    private val fluidHeights = Object2DoubleArrayMap<Tag<Fluid>>(2)
    private var fluidOnEyes: Tag<Fluid>? = null
    private var portalCooldown = 0
    var isRemoved = false
        private set
    private var wasDamaged = false
    private var tickCount = 0
    private var gravityTickCount = 0

    init {
        data.add(MetadataKeys.FLAGS)
        data.add(MetadataKeys.AIR_TICKS, maxAirTicks)
        data.add(MetadataKeys.CUSTOM_NAME)
        data.add(MetadataKeys.CUSTOM_NAME_VISIBILITY)
        data.add(MetadataKeys.SILENT)
        data.add(MetadataKeys.NO_GRAVITY)
        data.add(MetadataKeys.POSE)
        data.add(MetadataKeys.FROZEN_TICKS)
    }

    open fun onDataUpdate(key: MetadataKey<*>) {
        // TODO: Data updating
    }

    open fun load(tag: CompoundTag) {
        air = tag.getShort("Air").toInt()
        if (tag.contains("CustomName", StringTag.ID)) {
            customName = GsonComponentSerializer.gson().deserialize(tag.getString("CustomName"))
        }
        isCustomNameVisible = tag.getBoolean("CustomNameVisible")
        fallDistance = tag.getFloat("FallDistance")
        fireTicks = tag.getShort("Fire")
        isGlowing = tag.getBoolean("Glowing")
        isInvulnerable = tag.getBoolean("Invulnerable")

        val motion = tag.getList("Motion", DoubleTag.ID)
        val motionX = motion.getDouble(0).takeIf { abs(it) <= 10.0 } ?: 0.0
        val motionY = motion.getDouble(1).takeIf { abs(it) <= 10.0 } ?: 0.0
        val motionZ = motion.getDouble(2).takeIf { abs(it) <= 10.0 } ?: 0.0
        velocity = Vector3d(motionX, motionY, motionZ)

        hasGravity = !tag.getBoolean("NoGravity")
        isOnGround = tag.getBoolean("OnGround")
        portalCooldown = tag.getInt("PortalCooldown")

        val position = tag.getList("Pos", DoubleTag.ID)
        val rotation = tag.getList("Rotation", FloatTag.ID)
        velocity = Vector3d(motionX, motionY, motionZ)
        this.location = Vector3d(position.getDouble(0), position.getDouble(1), position.getDouble(2))
        this.rotation = Vector2f(rotation.getFloat(0), rotation.getFloat(1))

        isSilent = tag.getBoolean("Silent")
        frozenTicks = tag.getInt("TicksFrozen")
        if (tag.hasUUID("UUID")) uuid = tag.getUUID("UUID")!!
    }

    open fun save() = buildCompound {
        // Display name
        if (isCustomNameVisible) boolean("CustomNameVisible", true)
        customName?.let { string("CustomName", it.toJsonString()) }

        // Flags
        if (isGlowing) boolean("Glowing", true)
        if (isInvulnerable) boolean("Invulnerable", true)
        if (!hasGravity) boolean("NoGravity", true)
        boolean("OnGround", isOnGround)
        if (isSilent) boolean("Silent", true)

        // Positioning
        list("Motion", DoubleTag.ID, DoubleTag.of(velocity.x()), DoubleTag.of(velocity.y()), DoubleTag.of(velocity.z()))
        list("Pos", DoubleTag.ID, DoubleTag.of(location.x()), DoubleTag.of(location.y()), DoubleTag.of(location.z()))
        list("Rotation", FloatTag.ID, FloatTag.of(rotation.x()), FloatTag.of(rotation.y()))

        // Type & UUID
        if (this@KryptonEntity !is KryptonPlayer) string("id", this@KryptonEntity.type.key().asString())
        uuid("UUID", uuid)

        // Misc values
        short("Air", air.toShort())
        short("Fire", fireTicks)
        int("TicksFrozen", frozenTicks)
        float("FallDistance", fallDistance)
    }

    fun saveWithPassengers(): CompoundTag.Builder = save().apply {
        if (isVehicle) {
            val passengerList = MutableListTag()
            passengers.forEach {
                if (it !is KryptonEntity) return@forEach
                passengerList.add(it.saveWithPassengers().build())
            }
            if (passengerList.isNotEmpty()) put("Passengers", passengerList)
        }
    }

    open fun addViewer(player: KryptonPlayer): Boolean {
        if (!viewers.add(player)) return false
        player.session.send(getSpawnPacket())
        player.session.send(PacketOutMetadata(id, data.all))
        player.session.send(PacketOutHeadLook(id, rotation.x()))
        return true
    }

    open fun removeViewer(player: KryptonPlayer): Boolean {
        if (!viewers.remove(player)) return false
        player.session.send(PacketOutDestroyEntities(id))
        return true
    }

    private fun playSound(event: SoundEvent, volume: Float, pitch: Float) {
        if (isSilent) return
        world.playSound(location.x(), location.y(), location.z(), event, soundSource, volume, pitch)
    }

    open fun tick() {
        updateWater()
        updateUnderFluid()
        updateSwimming()
        if (data.isDirty) viewers.forEach { it.session.send(PacketOutMetadata(id, data.dirty)) }
        if (wasDamaged) {
            viewers.forEach { it.session.send(PacketOutEntityVelocity(this)) }
            wasDamaged = false
        }
    }

    protected open fun getSpawnPacket(): Packet = PacketOutSpawnEntity(this)

    private fun getSharedFlag(flag: Int): Boolean = data[MetadataKeys.FLAGS].toInt() and (1 shl flag) != 0

    private fun setSharedFlag(flag: Int, state: Boolean) {
        val flags = data[MetadataKeys.FLAGS].toInt()
        data[MetadataKeys.FLAGS] = (if (state) flags or (1 shl flag) else flags and (1 shl flag).inv()).toByte()
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
                    val height = y.toDouble() + fluid.handler.height(fluid, x, y, z, world)
                    if (height < minY) continue
                    shouldPush = true
                    amount = max(height - minY, amount)
                    if (!pushed) continue
                    var flow = fluid.handler.flow(fluid, x, y, z, world)
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
            viewers.forEach { it.session.send(PacketOutEntityVelocity(this)) }
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
            val height = y + fluid.handler.height(fluid, x, y.floor(), z, world)
            if (height > y) fluidOnEyes = it
            return
        }
    }

    private fun underFluid(fluid: Tag<Fluid>): Boolean = fluidOnEyes === fluid

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

    override fun identity(): Identity = identity!!

    override fun asHoverEvent(op: UnaryOperator<HoverEvent.ShowEntity>): HoverEvent<HoverEvent.ShowEntity> = HoverEvent.showEntity(
        op.apply(HoverEvent.ShowEntity.of(type.key(), uuid, displayName))
    )

    override fun addPassenger(entity: Entity) {
        if (passengers.contains(entity) || entity.passengers.contains(this)) return
        entity.vehicle = this
        passengers.add(entity)
        world.playerManager.sendToAll(PacketOutSetPassengers(this))
    }

    override fun removePassenger(entity: Entity) {
        if (entity.vehicle === this) return
        if (!passengers.contains(entity)) return
        entity.vehicle = null
        passengers.remove(entity)
        world.playerManager.sendToAll(PacketOutSetPassengers(this))
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
    override var isGliding: Boolean
        get() = getSharedFlag(7)
        set(value) = setSharedFlag(7, value)
    final override var air: Int
        get() = data[MetadataKeys.AIR_TICKS]
        set(value) = data.set(MetadataKeys.AIR_TICKS, value)
    final override var customName: Component?
        get() = data[MetadataKeys.CUSTOM_NAME].orElse(null)
        set(value) = data.set(MetadataKeys.CUSTOM_NAME, Optional.ofNullable(value))
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
    val hasVelocity: Boolean
        get() = velocity.x() != 0.0 && velocity.y() != 0.0 && velocity.z() != 0.0

    companion object {

        private val NEXT_ENTITY_ID = AtomicInteger(0)
        @JvmStatic
        protected val LOGGER = logger("Entity")

        private const val FLUID_VECTOR_EPSILON = 0.003
        private const val FLUID_VECTOR_MAGIC = 0.0045000000000000005
        private const val WATER_FLOW_SCALE = 0.014
        private const val FAST_LAVA_FLOW_SCALE = 0.007
        private const val SLOW_LAVA_FLOW_SCALE = 0.0023333333333333335
        private const val BREATHING_DISTANCE_BELOW_EYES = 0.11111111F
    }
}
