/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.entity.Equipable
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.event.player.PerformActionEvent
import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.plugin.PluginMessageRecipient
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.user.BaseUser
import org.kryptonmc.api.world.GameMode
import org.spongepowered.math.vector.Vector3d
import java.net.InetSocketAddress

/**
 * A player that is connected to the server and playing the game.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Player : LivingEntity, Equipable, PluginMessageRecipient, BaseUser {

    /**
     * The address that the player is currently connected from.
     */
    public val address: InetSocketAddress

    /**
     * If this player can fly.
     */
    @get:JvmName("canFly")
    public var canFly: Boolean

    /**
     * If this player can build (place/break blocks).
     */
    @get:JvmName("canBuild")
    public var canBuild: Boolean

    /**
     * If this player can place and break blocks with no delay.
     */
    @get:JvmName("canInstantlyBuild")
    public var canInstantlyBuild: Boolean

    /**
     * The current speed at which this player can walk at.
     */
    public var walkingSpeed: Float

    /**
     * The current speed at which this player can fly at.
     */
    public var flyingSpeed: Float

    /**
     * If this player is currently flying.
     */
    public var isFlying: Boolean

    /**
     * The dimension resource key for the world the player is currently in.
     */
    public val dimension: ResourceKey<World>

    /**
     * The dimension the player is currently in.
     */
    public val dimensionType: DimensionType

    /**
     * The settings for the player.
     */
    public val settings: PlayerSettings

    /**
     * If this player is currently AFK, meaning they are not doing anything.
     */
    public var isAfk: Boolean

    /**
     * This player's current game mode.
     */
    public var gameMode: GameMode

    /**
     * The direction this player is currently facing.
     */
    public val facing: Direction

    /**
     * The scoreboard currently being shown to this player.
     */
    public val scoreboard: Scoreboard

    /**
     * The inventory of this player.
     *
     * This holds information on all of the items that are currently held by
     * this player.
     */
    public val inventory: PlayerInventory

    /**
     * The inventory that this player currently has open.
     */
    public var openInventory: Inventory?

    /**
     * The food level of this player.
     */
    public var foodLevel: Int

    /**
     * The food exhaustion level of this player.
     */
    public var foodExhaustionLevel: Float

    /**
     * The food saturation level of this player.
     */
    public var foodSaturationLevel: Float

    /**
     * The statistics tracker for this player.
     */
    public val statistics: StatisticsTracker

    /**
     * The cooldown tracker for this player.
     */
    public val cooldowns: CooldownTracker

    /**
     * Spawns particles for this player relative to a location.
     *
     * @param effect the particle effect used to describe the particles'
     * appearance
     * @param location the central location to spawn the particles at
     */
    public fun spawnParticles(effect: ParticleEffect, location: Vector3d)

    /**
     * Sends the given resource [pack] to this player.
     *
     * @param pack the resource pack
     */
    public fun sendResourcePack(pack: ResourcePack)

    /**
     * Teleports this player to the given [position].
     */
    public fun teleport(position: Vector3d)

    /**
     * Teleports this player to the given other [player].
     */
    public fun teleport(player: Player)

    /**
     * Hides this player from all other players on the server.
     */
    public fun vanish()

    /**
     * Shows this player to all other players on the server.
     */
    public fun unvanish()

    /**
     * Shows the given [player] to this player if they are hidden, or does
     * nothing if they aren't.
     *
     * @param player the player to show
     */
    public fun show(player: Player)

    /**
     * Hides the given [player] from this player if they are shown, or does
     * nothing if they aren't.
     *
     * @param player the player to hide
     */
    public fun hide(player: Player)

    /**
     * Checks if this player can see the given other [player].
     *
     * @param player the player to check
     * @return true if this player can see the other player, false otherwise
     */
    public fun canSee(player: Player): Boolean

    /**
     * Makes this player start gliding (elytra flying).
     *
     * This should call the [PerformActionEvent], which may cause this action
     * to get cancelled, meaning the player will keep its previous gliding
     * status.
     *
     * @return whether the request to start gliding was approved, or it was
     * cancelled by the result of calling the event
     */
    public fun startGliding(): Boolean

    /**
     * Makes this player stop gliding (elytra flying).
     *
     * This should call the [PerformActionEvent], which may cause this action
     * to get cancelled, meaning the player will keep their previous gliding
     * status.
     *
     * @return whether the request to stop gliding was approved, or it was
     * cancelled by the result of calling the event
     */
    public fun stopGliding(): Boolean

    /**
     * Kicks the player with the given [text] shown.
     */
    public fun disconnect(text: Component)
}
