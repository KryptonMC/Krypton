/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.inventory.InventoryHolder
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.plugin.PluginMessageRecipient
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.scoreboard.Scoreboard
import org.spongepowered.math.vector.Vector3d
import java.net.InetSocketAddress
import java.util.Locale

/**
 * A player that is connected to the server and playing the game.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Player : LivingEntity, Sender, InventoryHolder, PluginMessageRecipient, HoverEventSource<HoverEvent.ShowEntity> {

    /**
     * The address that the player is currently connected from.
     */
    @get:JvmName("address")
    public val address: InetSocketAddress

    /**
     * The game profile for this player.
     *
     * Will contain offline mode details for offline mode players.
     */
    @get:JvmName("profile")
    public val profile: GameProfile

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
    @get:JvmName("walkingSpeed")
    public var walkingSpeed: Float

    /**
     * The current speed at which this player can fly at.
     */
    @get:JvmName("flyingSpeed")
    public var flyingSpeed: Float

    /**
     * The dimension resource key for the world the player is currently in.
     */
    @get:JvmName("dimension")
    public val dimension: ResourceKey<World>

    /**
     * The dimension the player is currently in.
     */
    @get:JvmName("dimensionType")
    public val dimensionType: DimensionType

    /**
     * The view distance of this player.
     */
    @get:JvmName("viewDistance")
    public val viewDistance: Int

    /**
     * The current time of this player.
     *
     * Will always be the time of the server.
     */
    @get:JvmName("time")
    public val time: Long

    /**
     * This player's current game mode.
     */
    @get:JvmName("gameMode")
    public val gameMode: GameMode

    /**
     * The direction this player is currently facing.
     */
    @get:JvmName("direction")
    public val direction: Direction

    /**
     * The scoreboard currently being shown to this player.
     */
    @get:JvmName("scoreboard")
    public var scoreboard: Scoreboard?

    /**
     * This player's main hand.
     */
    @get:JvmName("mainHand")
    public val mainHand: MainHand

    /**
     * The inventory of this player.
     *
     * This holds information on all of the items that are currently held by
     * this player.
     */
    @get:JvmName("inventory")
    override val inventory: PlayerInventory

    /**
     * The locale of this player.
     *
     * This may be null if the client has not yet indicated what locale they
     * want to use, in which case a sensible default should be assumed, such
     * as [Locale.US].
     */
    @get:JvmName("locale")
    public val locale: Locale?

    /**
     * The statistics tracker for this player.
     */
    @get:JvmName("statistics")
    public val statistics: StatisticsTracker

    /**
     * Increments the given [statistic] by 1.
     *
     * Note: This will increase the current value by 1, it will not set it.
     * To set statistics, use [StatisticsTracker.set]
     *
     * @param statistic the statistic
     */
    public fun incrementStatistic(statistic: Statistic<*>): Unit = incrementStatistic(statistic, 1)

    /**
     * Increments the given [statistic] by the given [amount].
     *
     * Note: This will increase the current value by the amount, it will not
     * set it. To set statistics, use [StatisticsTracker.set]
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    public fun incrementStatistic(statistic: Statistic<*>, amount: Int)

    /**
     * Increments the given custom statistic [key] by 1.
     *
     * @param key the custom statistic key
     */
    public fun incrementStatistic(key: Key): Unit = incrementStatistic(key, 1)

    /**
     * Increments the given custom statistic [key] by the given [amount].
     *
     * @param key the custom statistic key
     */
    public fun incrementStatistic(key: Key, amount: Int)

    /**
     * Decrements the given [statistic] by 1.
     *
     * Note: This will decrease the current value by 1, it will not set it.
     * To set statistics, use [StatisticsTracker.set]
     *
     * @param statistic the statistic
     */
    public fun decrementStatistic(statistic: Statistic<*>): Unit = decrementStatistic(statistic, 1)

    /**
     * Decrements the given [statistic] by the given [amount].
     *
     * Note: This will decrease the current value by the amount, it will not
     * set it. To set statistics, use [StatisticsTracker.set]
     *
     * @param statistic the statistic
     * @param amount the amount
     */
    public fun decrementStatistic(statistic: Statistic<*>, amount: Int)

    /**
     * Resets the given statistic back to a value of 0.
     *
     * @param statistic the statistic
     */
    public fun resetStatistic(statistic: Statistic<*>)

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
     * Teleport this player to the specified position.
     */
    public fun teleport(position: Vector3d)

    /**
     * Teleport this player to the specified other [player].
     */
    public fun teleport(player: Player)

    /**
     * Returns true if this player has the correct tool to be able to break
     * the given [block].
     *
     * @param block the block to check
     * @return true if the player can break it, false otherwise
     */
    public fun hasCorrectTool(block: Block): Boolean

    /**
     * Gets the speed at which this player can destroy the given
     * [block].
     *
     * @param block the block the player is destroying
     * @return the destroy speed
     */
    public fun destroySpeed(block: Block): Float

    /**
     * Kicks the player with the given text shown
     */
    public fun disconnect(text: Component)
}
