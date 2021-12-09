/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.util.TranslationHolder
import org.kryptonmc.api.world.damage.type.DamageType

/**
 * A game mode that a player may be in that determines specific things to do
 * with what the player is able to do, such as being able to build, fly, not
 * take damage, or fly through walls.
 *
 * @param displayName the name that this game mode will display as, such as
 * 'survival' for [SURVIVAL]
 * @param abbreviation the shortened name of this game mode, such as 's' for
 * [SURVIVAL]
 * @param canBuild whether players in this game mode can build
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public enum class GameMode(
    public val displayName: String,
    public val abbreviation: String,
    public val canBuild: Boolean
) : TranslationHolder {

    /**
     * Survival mode is the default game mode. In it, you can access most
     * gameplay features, but you will take damage, and cannot fly.
     */
    SURVIVAL("survival", "s", true),

    /**
     * Creative mode grants you access to spawn in any block in the game. It
     * also grants you the ability to fly around the world freely, break
     * blocks instantly, and you can only take damage from types that
     * [bypass invulnerability][DamageType.bypassesInvulnerability].
     */
    CREATIVE("creative", "c", true),

    /**
     * Adventure mode is designed for custom maps. In it, your block breaking
     * and placing are restricted, you still take damage like normal, and you
     * cannot fly.
     */
    ADVENTURE("adventure", "a", false),

    /**
     * Spectator mode is designed for spectating things. In it, you will take
     * no damage from anything, not even types that bypass invulnerability, and
     * you can fly through walls. You cannot interact with anything in the
     * world, including breaking and placing blocks, attacking entities, and
     * opening containers.
     */
    SPECTATOR("spectator", "sp", false);

    @get:JvmName("translation")
    override val translation: TranslatableComponent = Component.translatable("gameMode.$displayName")

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.displayName }
        private val BY_ABBREVIATION = VALUES.associateBy { it.abbreviation }

        /**
         * Gets the game mode with the given [name], or returns null if there
         * is no game mode with the given [name].
         *
         * @param name the name
         * @return the game mode with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): GameMode? = BY_NAME[name]

        /**
         * Gets the game mode with the given [abbreviation], or returns null
         * if there is no game mode with the given [abbreviation].
         *
         * @param abbreviation the abbreviation
         * @return the game mode with the abbreviation, or null if not present
         */
        @JvmStatic
        public fun fromAbbreviation(abbreviation: String): GameMode? = BY_ABBREVIATION[abbreviation]

        /**
         * Gets the game mode with the given [id], or returns null if there is
         * no game mode with the given [id].
         *
         * @param id the ID
         * @return the game mode with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): GameMode? = VALUES.getOrNull(id)
    }
}
