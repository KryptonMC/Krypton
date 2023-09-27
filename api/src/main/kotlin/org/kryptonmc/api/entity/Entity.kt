/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.identity.Identified
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.scoreboard.TeamMember
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.damage.DamageSource
import java.util.UUID

/**
 * An entity somewhere in a world.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Entity : Sender, HoverEventSource<HoverEvent.ShowEntity>, Sound.Emitter, Sound.Source.Provider, TeamMember, Identified {

    /**
     * The ID of this entity.
     *
     * This should be unique whilst the server is running, however it should
     * not be persisted in any way, shape, or form, as it is retrieved when the
     * entity object is first created, and discarded when the entity is
     * removed.
     */
    public val id: Int

    /**
     * The UUID of this entity.
     *
     * This will always be unique, and exists specifically to provide
     * uniqueness across server runs (persistent uniqueness).
     */
    public val uuid: UUID

    /**
     * The world this entity is currently in.
     */
    public val world: World

    /**
     * The type of this entity.
     */
    public val type: EntityType<Entity>

    /**
     * The custom name of the entity. May be null if the custom name has not
     * been set.
     */
    public var customName: Component?

    /**
     * If the current custom name is visible or not.
     */
    public var isCustomNameVisible: Boolean

    /**
     * The display name of this entity in their current team, if they are in
     * a team.
     *
     * If this entity is not in a team, this will return the name of the
     * entity.
     */
    public val displayName: Component

    /**
     * The current position of this entity.
     *
     * This can be updated using [teleport].
     */
    public val position: Position

    /**
     * The current delta X, Y, and Z values of this entity, in metres per tick.
     */
    public var velocity: Vec3d

    /**
     * The current bounding box of this entity. This is used to determine the
     * area in which an entity can be interacted with, also known as a hitbox.
     */
    public val boundingBox: BoundingBox

    /**
     * The passengers this entity currently has.
     *
     * Will be empty if the entity has no passengers.
     */
    public val passengers: List<Entity>

    /**
     * The entity that this entity is a passenger of.
     */
    public val vehicle: Entity?

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
    public val isOnGround: Boolean

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
    public val ticksExisted: Int

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
    public val airSupply: Int

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
    public val remainingFireTicks: Int

    /**
     * The amount of ticks this entity has been freezing for.
     *
     * Whilst this is available for all entities, it will only affect entities
     * that are not in the ["freeze_immune_entity_types" entity type tag][https://minecraft.wiki/w/Tag#Entity_types].
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
     * The scheduler for this entity.
     *
     * Useful for scheduling tasks that should only exist for the lifetime of
     * the entity, as all tasks that are scheduler with this scheduler will
     * stop running after the entity is removed.
     */
    public val scheduler: Scheduler

    /**
     * If this entity is a passenger of another entity.
     */
    public fun isPassenger(): Boolean

    /**
     * If this entity is a vehicle for another entity.
     */
    public fun isVehicle(): Boolean

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
     * If this entity is currently touching water.
     */
    public fun isInWater(): Boolean

    /**
     * If this entity is currently touching lava.
     */
    public fun isInLava(): Boolean

    /**
     * If this entity is currently fully submerged under water, meaning its
     * entire hitbox must be under water.
     */
    public fun isUnderwater(): Boolean

    /**
     * Teleports this entity to the given [position].
     *
     * @param position the position to teleport to
     */
    public fun teleport(position: Position)

    /**
     * Teleports this entity to the given other [entity].
     *
     * @param entity the entity to teleport to
     */
    public fun teleport(entity: Entity) {
        teleport(entity.position)
    }

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
