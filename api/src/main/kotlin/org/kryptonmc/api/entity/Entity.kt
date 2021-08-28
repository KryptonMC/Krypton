/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.identity.Identified
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.space.BoundingBox
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.space.Location
import org.kryptonmc.api.world.World
import java.util.UUID

/**
 * Represents an entity in a world.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Entity : Sender, Identified, HoverEventSource<HoverEvent.ShowEntity>, Sound.Emitter {

    /**
     * The world this entity is currently in.
     */
    public val world: World

    /**
     * The type of this entity.
     */
    public val type: EntityType<out Entity>

    /**
     * The unique ID of this entity.
     */
    public val uuid: UUID

    /**
     * The custom display name of this entity, as a component.
     *
     * May be [empty][Component.empty], indicating this entity does not
     * have a custom display name set.
     */
    public var displayName: Component

    /**
     * If the current [displayName] is visible or not.
     */
    public var isDisplayNameVisible: Boolean

    /**
     * The current location of this entity.
     */
    public var location: Location

    /**
     * The current delta X, Y, and Z values of this entity,
     * in metres per tick.
     */
    public var velocity: Vector

    /**
     * The current bounding box of this entity.
     */
    public var boundingBox: BoundingBox

    /**
     * The current dimensions of this entity.
     */
    public var dimensions: EntityDimensions

    /**
     * The passengers this entity currently has.
     *
     * Will be empty if the entity has no passengers.
     */
    public val passengers: List<Entity>

    /**
     * If this entity should not take damage from any source.
     *
     * When this is true, the following statements are also true:
     * - If this entity is living, it cannot be moved by fishing rods,
     *   attacks, explosions, or projectiles.
     * - Objects such as vehicles and item frames cannot be destroyed
     *   unless their support is also removed.
     * - If this entity is a player, it will also be ignored by any
     *   hostile mobs.
     *
     * Setting this to true, however, will not prevent this entity from
     * being damaged by a player in creative mode.
     */
    public val isInvulnerable: Boolean

    /**
     * If this entity is currently on fire.
     */
    public val isOnFire: Boolean

    /**
     * If this entity is on terra firma.
     */
    public val isOnGround: Boolean

    /**
     * If this entity is crouching/sneaking.
     */
    public val isCrouching: Boolean

    /**
     * If this entity is sprinting.
     */
    public val isSprinting: Boolean

    /**
     * If this entity is swimming.
     */
    public val isSwimming: Boolean

    /**
     * If this entity is invisible.
     */
    public val isInvisible: Boolean

    /**
     * If this entity has a glowing outline.
     */
    public val isGlowing: Boolean

    /**
     * If this entity is currently flying with an
     * [elytra][org.kryptonmc.api.item.ItemTypes.ELYTRA].
     */
    public val isFlying: Boolean

    /**
     * If this entity is silenced, meaning it does not produce any sounds.
     */
    public val isSilent: Boolean

    /**
     * If this entity is affected by gravity.
     *
     * When this value is false, this entity will not naturally fall when it
     * has no blocks under its feet to support it.
     */
    @get:JvmName("hasGravity")
    public val hasGravity: Boolean

    /**
     * The amount of ticks this entity has existed for.
     *
     * This will be increased by 1 for every tick this entity exists for.
     */
    public val ticksExisted: Int

    /**
     * How much air this entity has, in ticks.
     *
     * This will fill to a maximum of 300 in air, giving a total of 15
     * seconds underwater before the entity begins to drown and die.
     *
     * If this value reaches 0, and this entity is underwater, it will
     * take 1 health point (half a heart) of damage for every second
     * it remains underwater.
     */
    public val air: Int

    /**
     * This value can mean one of two things, depending on if the value is positive
     * or negative.
     *
     * When the value is positive, this represents the number of ticks until the entity
     * is no longer [on fire][isOnFire].
     *
     * When the value is negative, this represents the number of ticks this entity can
     * survive in fire for before burning.
     */
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
    public val frozenTicks: Int

    /**
     * The distance this entity has fallen.
     *
     * The larger the value of this, the more damage the entity will take when
     * it lands.
     */
    public val fallDistance: Float

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
    public fun moveTo(x: Double = location.x, y: Double = location.y, z: Double = location.z)

    /**
     * Makes this entity look to the specified yaw and pitch.
     *
     * The values provided to this function are **absolute**, meaning they
     * will replace the existing yaw and pitch values.
     */
    public fun look(yaw: Float = location.yaw, pitch: Float = location.pitch)

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
        x: Double = location.x,
        y: Double = location.y,
        z: Double = location.z,
        yaw: Float = location.yaw,
        pitch: Float = location.pitch
    )

    /**
     * Gets the distance to the specified [entity] squared.
     *
     * @param entity the entity to calculate the distance to
     * @return the distance to the other entity squared
     */
    public fun distanceToSquared(entity: Entity): Double = location.distanceSquared(entity.location)

    /**
     * Marks this entity to be removed in the very near future, preferably within
     * one game tick.
     */
    public fun remove()
}
