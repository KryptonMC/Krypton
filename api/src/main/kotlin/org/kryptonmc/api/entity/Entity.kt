/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.jetbrains.annotations.Range
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.scoreboard.TeamMember
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.damage.DamageSource
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d

/**
 * An entity somewhere in a world.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Entity : Sender, HoverEventSource<HoverEvent.ShowEntity>, Sound.Emitter, TeamMember {

    /**
     * The ID of this entity.
     *
     * This should be unique whilst the server is running, however it should
     * not be persisted in any way, shape, or form, as it is retrieved when the
     * entity object is first created, and discarded when the entity is
     * removed.
     */
    @get:JvmName("id")
    public val id: Int

    /**
     * The world this entity is currently in.
     */
    @get:JvmName("world")
    public val world: World

    /**
     * The type of this entity.
     */
    @get:JvmName("type")
    public val type: EntityType<out Entity>

    /**
     * The custom name of the entity. May be null if the custom name has not
     * been set.
     */
    @get:JvmName("customName")
    public var customName: Component?

    /**
     * If the current [customName] is visible or not.
     */
    public var isCustomNameVisible: Boolean

    /**
     * The display name of this entity.
     *
     * In vanilla, this is dependent on the name of the team the entity is in.
     */
    @get:JvmName("displayName")
    public val displayName: Component

    /**
     * The current position of this entity.
     */
    @get:JvmName("location")
    public var location: Vector3d

    /**
     * The current rotation of this entity.
     *
     * The format of this rotation is:
     * - x -> yaw
     * - y -> pitch
     */
    @get:JvmName("rotation")
    public var rotation: Vector2f

    /**
     * The current delta X, Y, and Z values of this entity, in metres per tick.
     */
    @get:JvmName("velocity")
    public var velocity: Vector3d

    /**
     * The current bounding box of this entity. This is used to determine the
     * area in which an entity can be interacted with, also known as a hitbox.
     */
    @get:JvmName("boundingBox")
    public var boundingBox: BoundingBox

    /**
     * If this entity is a passenger of another entity.
     */
    public val isPassenger: Boolean

    /**
     * The passengers this entity currently has.
     *
     * Will be empty if the entity has no passengers.
     */
    @get:JvmName("passengers")
    public val passengers: List<Entity>

    /**
     * If this entity is a vehicle for another entity.
     */
    public val isVehicle: Boolean

    /**
     * The entity that this entity is a passenger of.
     */
    @get:JvmName("vehicle")
    public var vehicle: Entity?

    /**
     * If this entity should not take damage from any source.
     *
     * When this is true, the following statements are also true:
     * - If this entity is living, it cannot be moved by fishing rods, attacks,
     * explosions, or projectiles.
     * - Objects such as vehicles and item frames cannot be destroyed unless
     * their support is also removed.
     * - If this entity is a player, it will also be ignored by any hostile
     * mobs.
     *
     * Setting this to true, however, will not prevent this entity from being
     * damaged by a player in creative mode.
     */
    public var isInvulnerable: Boolean

    /**
     * If this entity is currently on fire.
     */
    public var isOnFire: Boolean

    /**
     * If this entity is on terra firma.
     */
    public var isOnGround: Boolean

    /**
     * If this entity is sneaking.
     */
    public var isSneaking: Boolean

    /**
     * If this entity is sprinting.
     */
    public var isSprinting: Boolean

    /**
     * If this entity is swimming.
     */
    public var isSwimming: Boolean

    /**
     * If this entity is invisible.
     */
    public var isInvisible: Boolean

    /**
     * If this entity has a glowing outline.
     */
    public var isGlowing: Boolean

    /**
     * If this entity is silenced, meaning it does not produce any sounds.
     *
     * This also means that any attempts to play a sound with this entity as
     * its emitter will also fail.
     */
    public var isSilent: Boolean

    /**
     * If this entity is rideable or not.
     */
    public val isRideable: Boolean

    /**
     * If this entity is affected by gravity.
     *
     * When this value is false, this entity will not naturally fall when it
     * has no blocks under its feet to support it.
     */
    @get:JvmName("hasGravity")
    public var hasGravity: Boolean

    /**
     * The amount of ticks this entity has existed for.
     *
     * This will be increased by 1 for every tick this entity exists for.
     */
    @get:JvmName("ticksExisted")
    public val ticksExisted: @Range(from = 0L, to = Int.MAX_VALUE.toLong()) Int

    /**
     * How much air this entity has, in ticks.
     *
     * This will fill to a maximum of 300 in air, giving the entity a total of
     * 15 seconds underwater before the entity begins to drown and die.
     *
     * If this value reaches 0, and this entity is underwater, it will
     * take 1 health point (half a heart) of damage for every second
     * it remains underwater.
     */
    @get:JvmName("air")
    public val air: @Range(from = 0L, to = Int.MAX_VALUE.toLong()) Int

    /**
     * This value can mean one of two things, depending on if the value is
     * positive or negative.
     *
     * When the value is positive, this represents the number of ticks until
     * the entity is no longer [on fire][isOnFire].
     *
     * When the value is negative, this represents the number of ticks this
     * entity can survive in fire for before burning.
     */
    @get:JvmName("fireTicks")
    public val fireTicks: Short

    /**
     * The amount of ticks this entity has been freezing for.
     *
     * Whilst this is available for all entities, it will only affect entities
     * that are not in the ["freeze_immune_entity_types" entity type tag][https://minecraft.fandom.com/wiki/Tag#Entity_types].
     *
     * This value will increase by 1 for every tick this entity is in powder
     * snow, to a maximum of 300, and decrease by 2 for every tick this entity
     * is not in powder snow.
     */
    @get:JvmName("frozenTicks")
    public val frozenTicks: Int

    /**
     * The distance this entity has fallen.
     *
     * The larger the value of this, the more damage the entity will take when
     * it lands.
     */
    @get:JvmName("fallDistance")
    public val fallDistance: Float

    /**
     * If this entity is currently touching water.
     */
    @get:JvmName("inWater")
    public val inWater: Boolean

    /**
     * If this entity is currently touching lava.
     */
    @get:JvmName("inLava")
    public val inLava: Boolean

    /**
     * If this entity is currently fully submerged under water, meaning its
     * entire hitbox must be under water.
     */
    @get:JvmName("underwater")
    public val underwater: Boolean

    /**
     * Moves this entity by the specified x, y, z, yaw, and pitch amounts.
     *
     * The values provided to this function are **relative**, meaning they
     * will be added to the current values.
     *
     * @param x the X amount
     * @param y the Y amount
     * @param z the Z amount
     * @param yaw the yaw amount
     * @param pitch the pitch amount
     */
    public fun move(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, yaw: Float = 0F, pitch: Float = 0F)

    /**
     * Moves this entity to the specified x, y, and z coordinates.
     *
     * The values provided to this function are **absolute**, meaning they
     * will replace the existing x, y, and z coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public fun moveTo(x: Double = location.x(), y: Double = location.y(), z: Double = location.z())

    /**
     * Makes this entity look to the specified yaw and pitch.
     *
     * The values provided to this function are **absolute**, meaning they
     * will replace the existing yaw and pitch values.
     */
    public fun look(yaw: Float = rotation.x(), pitch: Float = rotation.y())

    /**
     * Repositions this entity to be at the specified x, y, and z coordinates,
     * and looking at the specified yaw and pitch amounts.
     *
     * The values provided to this function are **absolute**, meaning they
     * will replace the existing x, y, z, yaw, and pitch values.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param yaw the yaw value
     * @param pitch the pitch value
     */
    public fun reposition(
        x: Double = location.x(),
        y: Double = location.y(),
        z: Double = location.z(),
        yaw: Float = rotation.x(),
        pitch: Float = rotation.y()
    )

    /**
     * Attempts to make the given [entity] ride this entity.
     *
     * @param entity the entity
     */
    public fun tryRide(entity: Entity)

    /**
     * Adds the given [entity] as a passenger of this entity.
     *
     * @param entity the entity to be added
     */
    public fun addPassenger(entity: Entity)

    /**
     * Removes the given [entity] as a passenger of this entity.
     *
     * @param entity the entity to be removed
     */
    public fun removePassenger(entity: Entity)

    /**
     * Ejects all passengers from this entity.
     */
    public fun ejectPassengers()

    /**
     * Ejects this entity from it's vehicle.
     */
    public fun ejectVehicle()

    /**
     * Damages this entity with the given [source].
     *
     * @param source the source of the damage
     * @param damage the damage amount
     * @return true if damaging this entity was successful, false otherwise
     */
    public fun damage(source: DamageSource, damage: Float): Boolean

    /**
     * Removes this entity from the world it is currently in.
     *
     * This removal may happen immediately, or be queued up and happen in the
     * near future, depending on how it is implemented.
     */
    public fun remove()
}
