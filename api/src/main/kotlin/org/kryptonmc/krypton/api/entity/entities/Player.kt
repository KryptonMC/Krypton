package org.kryptonmc.krypton.api.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.effect.particle.ParticleEffect
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
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
    val locale: Locale

    fun spawnParticles(particleEffect: ParticleEffect, location: Location)
}
