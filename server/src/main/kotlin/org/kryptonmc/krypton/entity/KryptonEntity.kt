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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent.ShowEntity
import net.kyori.adventure.text.event.HoverEvent.showEntity
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutDestroyEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutHeadLook
import org.kryptonmc.krypton.packet.out.play.PacketOutMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nextUUID
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.buildCompound
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.UnaryOperator
import kotlin.math.abs
import kotlin.random.Random

@Suppress("LeakingThis")
abstract class KryptonEntity(
    override var world: KryptonWorld,
    override val type: EntityType<out Entity>
) : Entity {

    val id = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid = Random.nextUUID()
        set(value) {
            field = value
            uuidComponent = Component.text(value.toString())
            identity = Identity.identity(value)
        }
    private var uuidComponent: Component = Component.text(uuid.toString())
    private var identity: Identity = Identity.identity(uuid)
    override val teamRepresentation: Component
        get() = uuidComponent
    override val name: Component
        get() = customName ?: type.translation
    override val displayName: Component
        get() = team?.formatName(name)?.style {
            it.hoverEvent(asHoverEvent())
            it.insertion(uuid.toString())
        } ?: name
    val data = MetadataHolder(this)

    final override val server = world.server
    override val permissionLevel = 0

    override val team: Team?
        get() = world.scoreboard.memberTeam(teamRepresentation)
    final override var location: Vector3d = Vector3d.ZERO
    final override var rotation: Vector2f = Vector2f.ZERO
    final override var velocity: Vector3d = Vector3d.ZERO
    final override var boundingBox = BoundingBox.zero()
    final override var dimensions = EntityDimensions.fixed(1, 1) // TODO: Use type dimensions
    final override var isOnGround = true
    final override var ticksExisted = 0
    final override var fireTicks: Short = 0
    final override var isInvulnerable = false
    final override var fallDistance = 0F
    final override val passengers = emptyList<Entity>()

    open val maxAirTicks: Int
        get() = 300
    open val isAlive: Boolean
        get() = !isRemoved
    val viewers: MutableSet<KryptonPlayer> = ConcurrentHashMap.newKeySet()
    var noPhysics = false
    private var portalCooldown = 0
    private var isRemoved = false

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

    open fun addViewer(player: KryptonPlayer): Boolean {
        if (!viewers.add(player)) return false
        player.viewableEntities.add(this)
        player.session.send(getSpawnPacket())
        player.session.send(PacketOutMetadata(id, data.all))
        player.session.send(PacketOutHeadLook(id, rotation.x()))
        return true
    }

    open fun removeViewer(player: KryptonPlayer): Boolean {
        if (!viewers.remove(player)) return false
        player.session.send(PacketOutDestroyEntities(id))
        player.viewableEntities.remove(this)
        return true
    }

    protected open fun getSpawnPacket(): Packet = PacketOutSpawnEntity(this)

    private fun getSharedFlag(flag: Int) = data[MetadataKeys.FLAGS].toInt() and (1 shl flag) != 0

    private fun setSharedFlag(flag: Int, state: Boolean) {
        val flags = data[MetadataKeys.FLAGS].toInt()
        data[MetadataKeys.FLAGS] = (if (state) flags or (1 shl flag) else flags and (1 shl flag).inv()).toByte()
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

    override fun getPermissionValue(permission: String) = TriState.FALSE

    override fun remove() {
        if (isRemoved) return
        isRemoved = true
        world.removeEntity(this)
    }

    override fun identity() = identity

    override fun asHoverEvent(op: UnaryOperator<ShowEntity>) = showEntity(op.apply(ShowEntity.of(type.key(), uuid, displayName)))

    final override var isOnFire: Boolean
        get() = getSharedFlag(0)
        set(value) = setSharedFlag(0, value)
    final override var isCrouching: Boolean
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
    final override var isFlying: Boolean
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
    }
}
