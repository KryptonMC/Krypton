/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.entity.entities

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.entity.MainHand
import org.kryptonmc.krypton.api.inventory.InventoryHolder
import org.kryptonmc.krypton.api.inventory.PlayerInventory
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import java.net.InetSocketAddress
import java.util.Locale
import java.util.UUID

/**
 * Represents a player
 */
interface Player : Sender, InventoryHolder, HoverEventSource<HoverEvent.ShowEntity> {

    /**
     * The player's UUID
     */
    val uuid: UUID

    /**
     * The player's display name
     */
    var displayName: Component

    /**
     * The address that the player is currently connected from
     */
    val address: InetSocketAddress

    /**
     * The player's abilities
     */
    val abilities: Abilities

    /**
     * The world this player is currently in
     */
    val world: World

    /**
     * The dimension the player is currently in
     */
    val dimension: NamespacedKey

    /**
     * The current location of the player
     */
    val location: Location

    /**
     * The velocity of the player
     */
    val velocity: Vector

    /**
     * If this player is on terra firma (solid earth)
     */
    val isOnGround: Boolean

    /**
     * If this player is crouching
     */
    val isCrouching: Boolean

    /**
     * If this player is sprinting
     */
    val isSprinting: Boolean

    /**
     * The view distance of this player
     */
    val viewDistance: Int

    /**
     * The current time of this player
     *
     * Will always be the time of the server
     */
    val time: Long

    /**
     * The scoreboard currently being shown to this player
     */
    var scoreboard: Scoreboard?

    /**
     * This player's main hand
     */
    val mainHand: MainHand

    /**
     * The player's inventory
     *
     * This can and should never be changed
     */
    override val inventory: PlayerInventory

    /**
     * This player's locale
     */
    val locale: Locale?

    /**
     * Spawns particles for this player relative to a location.
     *
     * @param particleEffect the [ParticleEffect] used to describe the particles' appearance
     * @param location the central location to spawn the particles at
     */
    fun spawnParticles(particleEffect: ParticleEffect, location: Location)
}
