/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.inventory.InventoryHolder
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.plugin.PluginMessageRecipient
import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.space.Position
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.Location
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.scoreboard.Scoreboard
import java.net.InetSocketAddress
import java.util.Locale

/**
 * Represents a player.
 */
interface Player : LivingEntity, Sender, InventoryHolder, PluginMessageRecipient, HoverEventSource<HoverEvent.ShowEntity> {

    /**
     * The address that the player is currently connected from.
     */
    val address: InetSocketAddress

    /**
     * The player's abilities.
     */
    val abilities: Abilities

    /**
     * The dimension the player is currently in.
     */
    val dimensionType: DimensionType

    /**
     * The view distance of this player.
     */
    val viewDistance: Int

    /**
     * The current time of this player.
     *
     * Will always be the time of the server.
     */
    val time: Long

    /**
     * This player's current gamemode.
     */
    val gamemode: Gamemode

    /**
     * The direction this player is currently facing.
     */
    val direction: Direction

    /**
     * The scoreboard currently being shown to this player.
     */
    var scoreboard: Scoreboard?

    /**
     * This player's main hand.
     */
    val mainHand: MainHand

    /**
     * The player's inventory.
     *
     * This can and should never be changed.
     */
    override val inventory: PlayerInventory

    /**
     * This player's locale.
     */
    val locale: Locale?

    /**
     * Spawns particles for this player relative to a location.
     *
     * @param particleEffect the [ParticleEffect] used to describe the particles' appearance
     * @param location the central location to spawn the particles at
     */
    fun spawnParticles(particleEffect: ParticleEffect, location: Location)

    /**
     * Teleport this player to the specified position.
     */
    fun teleport(position: Position)

    /**
     * Teleport this player to the specified other [player].
     */
    fun teleport(player: Player)

    /**
     * Returns true if this player has the correct tool to be able to break
     * the given [block].
     *
     * @param block the block to check
     * @return true if the player can break it, false otherwise
     */
    fun hasCorrectTool(block: Block): Boolean

    /**
     * Gets the speed at which this player can destroy the given
     * [block].
     *
     * @param block the block the player is destroying
     * @return the destroy speed
     */
    fun getDestroySpeed(block: Block): Float

    /**
     * Kicks the player with the given text shown
     */
    fun disconnect(text: Component)
}
