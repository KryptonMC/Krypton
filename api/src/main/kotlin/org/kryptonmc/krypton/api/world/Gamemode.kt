package org.kryptonmc.krypton.api.world

import kotlinx.serialization.Serializable
import org.kryptonmc.krypton.api.world.Gamemode.ADVENTURE
import org.kryptonmc.krypton.api.world.Gamemode.CREATIVE
import org.kryptonmc.krypton.api.world.Gamemode.SPECTATOR
import org.kryptonmc.krypton.api.world.Gamemode.SURVIVAL
import java.util.Locale

/**
 * Represents a game mode, those being [SURVIVAL], [CREATIVE], [ADVENTURE]
 * and [SPECTATOR].
 */
@Serializable
@Suppress("MemberVisibilityCanBePrivate")
enum class Gamemode {

    /**
     * Plain old survival mode. In this mode, you have a finite amount of health,
     * and you can take damage.
     */
    SURVIVAL,

    /**
     * In creative this mode, you are completely invulnerable, you can fly,
     * and you can spawn and use any item you wish.
     */
    CREATIVE,

    /**
     * Adventure mode is designed for map creators, in that blocks require specific
     * tools to break, and you cannot break them without those tools.
     */
    ADVENTURE,

    /**
     * In spectator mode, you are also completely invulnerable, but you can
     * also fly through blocks, as the entire world is essentially non existant
     * to your client (you can see things, but you will never collide with them)
     */
    SPECTATOR;

    override fun toString() = name.toLowerCase(Locale.ROOT)

    companion object {

        /**
         * Retrieves a game mode from its legacy ID. Should only need to be used internally.
         */
        @JvmStatic
        fun fromId(id: Int): Gamemode? {
            if (id !in 0 until values().size) return null
            return values()[id]
        }
    }
}
