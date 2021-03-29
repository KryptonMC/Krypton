package org.kryptonmc.krypton.api.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import java.net.InetSocketAddress
import java.util.*

/**
 * Represents a player
 *
 * @author Callum Seabrook
 */
interface Player : Sender {

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
     * If this player is flying
     */
    val isFlying: Boolean

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