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
package org.kryptonmc.api.entity.player

import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.entity.Equipable
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.event.player.action.PlayerStartGlidingEvent
import org.kryptonmc.api.event.player.action.PlayerStopGlidingEvent
import org.kryptonmc.api.inventory.Inventory
import org.kryptonmc.api.inventory.PlayerInventory
import org.kryptonmc.api.plugin.PluginMessageRecipient
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.statistic.StatisticsTracker
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.user.BaseUser
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.GameMode
import java.net.SocketAddress

/**
 * A player that is connected to the server and playing the game.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Player : LivingEntity, Equipable, PluginMessageRecipient, BaseUser {

    /**
     * The address that the player is currently connected from.
     */
    public val address: SocketAddress

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
     * The settings for the player.
     */
    public val settings: PlayerSettings

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
    public val statisticsTracker: StatisticsTracker

    /**
     * The cooldown tracker for this player.
     */
    public val itemCooldownTracker: CooldownTracker

    /**
     * The current ping of this player.
     */
    public val ping: Int

    /**
     * Spawns particles for this player relative to a location.
     *
     * @param effect the particle effect used to describe the particles'
     * appearance
     * @param location the central location to spawn the particles at
     */
    public fun spawnParticles(effect: ParticleEffect, location: Vec3d)

    /**
     * Sends the given resource [pack] to this player.
     *
     * @param pack the resource pack
     */
    public fun sendResourcePack(pack: ResourcePack)

    /**
     * Makes this player start gliding (elytra flying).
     *
     * This may fire the [PlayerStartGlidingEvent], which may cause this
     * action to get denied, meaning the player will keep their previous
     * gliding status.
     *
     * @return whether the request to start gliding was approved, or it was
     * denied by the result of calling the event
     */
    public fun startGliding(): Boolean

    /**
     * Makes this player stop gliding (elytra flying).
     *
     * This may fire the [PlayerStopGlidingEvent], which may cause this action
     * to get denied, meaning the player will keep their previous gliding
     * status.
     *
     * @return whether the request to stop gliding was approved, or it was
     * denied by the result of calling the event
     */
    public fun stopGliding(): Boolean

    /**
     * Kicks the player with the given [text] shown.
     */
    public fun disconnect(text: Component)
}
