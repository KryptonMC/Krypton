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
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.world.Location
import org.kryptonmc.api.world.World
import java.util.UUID

/**
 * Represents an entity in a world.
 */
interface Entity : Sender, Identified, HoverEventSource<HoverEvent.ShowEntity>, Sound.Emitter {

    /**
     * The world this entity is currently in.
     */
    val world: World

    /**
     * The unique ID of this entity.
     */
    val uuid: UUID

    /**
     * The custom display name of this entity, as a component.
     *
     * May be [empty][Component.empty], indicating this entity doesn't
     * have a custom display name set.
     */
    var displayName: Component

    /**
     * If the current [displayName] is visible or not.
     */
    val isDisplayNameVisible: Boolean

    /**
     * The location of this entity.
     */
    val location: Location

    /**
     * This entity's velocity.
     */
    val velocity: Vector

    /**
     * If this entity is currently on fire.
     */
    val isOnFire: Boolean

    /**
     * If this entity is on terra firma.
     */
    val isOnGround: Boolean

    /**
     * If this entity is crouching/sneaking.
     */
    val isCrouching: Boolean

    /**
     * If this entity is sprinting.
     */
    val isSprinting: Boolean

    /**
     * If this entity is swimming.
     */
    val isSwimming: Boolean

    /**
     * If this entity is invisible.
     */
    val isInvisible: Boolean

    /**
     * If this entity is glowing.
     */
    val isGlowing: Boolean

    /**
     * If this entity is currently flying with elytra.
     */
    val isFlying: Boolean

    /**
     * If this entity is dead or not
     */
    val isDead: Boolean

    /**
     * If this entity is persistent (stored when the world is unloaded).
     */
    val isPersistent: Boolean

    /**
     * If this entity is silent.
     */
    val isSilent: Boolean

    /**
     * If this entity has gravity.
     */
    val hasGravity: Boolean

    /**
     * The amount of ticks this entity has lived for.
     */
    val ticksLived: Int

    /**
     * The current air supply of this entity.
     */
    val airTicks: Int

    /**
     * The type of this entity.
     */
    val type: EntityType<out Entity>

    /**
     * Marks this entity to be removed in the very near future, preferably within
     * one game tick.
     */
    fun remove()
}
