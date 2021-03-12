package org.kryptonmc.krypton.api.world

/**
 * Represents a game mode, those being [SURVIVAL], [CREATIVE], [ADVENTURE]
 * and [SPECTATOR].
 *
 * [id] is the legacy ID of the game mode. It should mostly only be used
 * internally, as the protocol still uses these legacy IDs.
 *
 * @author Callum Seabrook
 */
enum class Gamemode(val id: Int) {

    /**
     * Plain old survival mode. In this mode, you have a finite amount of health,
     * and you can take damage.
     */
    SURVIVAL(0),

    /**
     * In creative this mode, you are completely invulnerable, you can fly,
     * and you can spawn and use any item you wish.
     */
    CREATIVE(1),

    /**
     * Adventure mode is designed for map creators, in that blocks require specific
     * tools to break, and you cannot break them without those tools.
     */
    ADVENTURE(2),

    /**
     * In spectator mode, you are also completely invulnerable, but you can
     * also fly through blocks, as the entire world is essentially non existant
     * to your client (you can see things, but you will never collide with them)
     */
    SPECTATOR(3);

    companion object {

        private val VALUES = values().associateBy { it.id }

        /**
         * Retrieves a game mode from its legacy ID. Should only need to be used internally.
         */
        fun fromId(id: Int) = VALUES[id]
    }
}