/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.world.Gamemode.ADVENTURE
import org.kryptonmc.api.world.Gamemode.CREATIVE
import org.kryptonmc.api.world.Gamemode.SPECTATOR
import org.kryptonmc.api.world.Gamemode.SURVIVAL

/**
 * Represents a game mode, those being [SURVIVAL], [CREATIVE], [ADVENTURE]
 * and [SPECTATOR].
 * @param shortName Represents the short name of the given gamemode
 */
@Suppress("MemberVisibilityCanBePrivate")
public enum class Gamemode(public val shortName: String) {

    /**
     * Plain old survival mode. In this mode, you have a finite amount of
     * health, and you can take damage.
     */
    SURVIVAL("s"),

    /**
     * In creative this mode, you are completely invulnerable, you can fly,
     * and you can spawn and use any item you wish.
     */
    CREATIVE("c"),

    /**
     * Adventure mode is designed for map creators, in that blocks require
     * specific tools to break, and you cannot break them without those tools.
     */
    ADVENTURE("a"),

    /**
     * In spectator mode, you are also completely invulnerable, but you can
     * also fly through blocks, as the entire world is essentially non existent
     * to your client (you can see things, but you will never collide with
     * them).
     */
    SPECTATOR("sp");

    /**
     * If this gamemode can build.
     */
    public val canBuild: Boolean
        get() = this == SURVIVAL || this == CREATIVE

    /**
     *  Represents the key of the gamemode
     */
    public val key: Key by lazy { key("gameMode.${name.lowercase()}") }

    override fun toString(): String = name.lowercase()

    public companion object {

        /**
         * Retrieves a game mode from its legacy ID. Should only need to be
         * used internally.
         */
        @JvmStatic
        public fun fromId(id: Int): Gamemode? {
            if (id !in 0 until values().size) return null
            return values()[id]
        }

        /**
         * Retrieves a game mode from its name.
         */
        @JvmStatic
        public fun fromName(name: String): Gamemode? = try {
            valueOf(name.uppercase())
        } catch (_: IllegalArgumentException) {
            null
        }

        /**
         * Retrieves a game mode from its short name
         */
        @JvmStatic
        public fun fromShortName(shortName: String): Gamemode? = values().firstOrNull { it.shortName == shortName }
    }
}
