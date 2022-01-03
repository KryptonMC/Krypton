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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.entity.Equipable
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.MainHand
import org.kryptonmc.api.inventory.InventoryHolder
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.plugin.PluginMessageRecipient
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.world.GameMode
import org.spongepowered.math.vector.Vector3d
import java.net.InetSocketAddress
import java.time.Instant
import java.util.Locale

/**
 * A player that is connected to the server and playing the game.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Player : LivingEntity, Equipable, InventoryHolder, PluginMessageRecipient {

    /**
     * The address that the player is currently connected from.
     */
    @get:JvmName("address")
    public val address: InetSocketAddress

    /**
     * If this player is currently online.
     */
    public val isOnline: Boolean

    /**
     * If this player has joined this server before.
     */
    @get:JvmName("hasJoinedBefore")
    public val hasJoinedBefore: Boolean

    /**
     * The time that this player first joined the server.
     */
    @get:JvmName("firstJoined")
    public val firstJoined: Instant

    /**
     * The latest time when this player last joined the server.
     */
    @get:JvmName("lastJoined")
    public val lastJoined: Instant

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
     * If this player is currently flying.
     */
    public var isFlying: Boolean

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
     * The visibility settings for this player's chat.
     */
    @get:JvmName("chatVisibility")
    public val chatVisibility: ChatVisibility

    /**
     * If this player wants their text filtered before it is sent to them.
     *
     * This filtering is usually meant to be done by Mojang, and is designed to
     * protect underage players from receiving any naughty words, like swear
     * words.
     *
     * It is recommended that plugins that wish to filter text for players,
     * such as chat plugins, respect this option, and allow players to opt-out
     * of chat filtering if they wish to do so.
     */
    public val filterText: Boolean

    /**
     * If this player wants to be listed in the server status' player list.
     *
     * All plugins that override the MOTD settings with their own custom MOTDs
     * should try to respect the setting that has been indicated here, as this
     * was added to prevent cross-server player tracking.
     */
    @get:JvmName("allowsListing")
    public val allowsListing: Boolean

    /**
     * The current time of this player.
     *
     * Will always be the time of the server.
     */
    @get:JvmName("time")
    public val time: Long

    /**
     * If this player is currently vanished, meaning no other player on the
     * server can see them.
     */
    public val isVanished: Boolean

    /**
     * If this player is currently AFK, meaning they are not doing anything.
     */
    public var isAfk: Boolean

    /**
     * This player's current game mode.
     */
    @get:JvmName("gameMode")
    public var gameMode: GameMode

    /**
     * The direction this player is currently facing.
     */
    @get:JvmName("direction")
    public val direction: Direction

    /**
     * The scoreboard currently being shown to this player.
     */
    @get:JvmName("scoreboard")
    public val scoreboard: Scoreboard

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
     * The food level of this player.
     */
    @get:JvmName("foodLevel")
    public var foodLevel: Int

    /**
     * The food exhaustion level of this player.
     */
    @get:JvmName("foodExhaustionLevel")
    public var foodExhaustionLevel: Float

    /**
     * The food saturation level of this player.
     */
    @get:JvmName("foodSaturationLevel")
    public var foodSaturationLevel: Float

    /**
     * The statistics tracker for this player.
     */
    @get:JvmName("statistics")
    public val statistics: StatisticsTracker

    /**
     * The cooldown tracker for this player.
     */
    @get:JvmName("cooldowns")
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
     * Teleport this player to the specified position.
     */
    public fun teleport(position: Vector3d)

    /**
     * Teleport this player to the specified other [player].
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
     * Kicks the player with the given text shown
     */
    public fun disconnect(text: Component)
}
