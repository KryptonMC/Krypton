/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.util.StringSerializable
import org.kryptonmc.api.util.TranslationHolder

/**
 * Represents the difficulty of a world. That being a measure of how difficult
 * it is to play the game (though this is not always accurate).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public enum class Difficulty(@get:JvmName("serialized") override val serialized: String) : StringSerializable, TranslationHolder {

    /**
     * In peaceful mode, no hostile monsters will spawn in the world.
     *
     * Players will also regain health over time, and the hunger bar
     * does not deplete.
     */
    PEACEFUL("peaceful"),

    /**
     * In easy mode, hostile mobs spawn at normal rates, but they deal less
     * damage than on normal difficulty. The hunger bar does deplete, and
     * starving can deal up to 5 hears of damage.
     */
    EASY("easy"),

    /**
     * In normal mode, hostile mobs spawn at normal rates, and they deal normal
     * amounts of damage. The hunger bar does deplete, and starving can deal up
     * to 9.5 hearts of damage.
     */
    NORMAL("normal"),

    /**
     * In hard mode, hostile mobs spawn at normal rates, but they deal much
     * greater damage than on normal difficulty. The hunger bar does deplete,
     * and starving can kill players.
     */
    HARD("hard");

    @get:JvmName("translation")
    override val translation: TranslatableComponent = Component.translatable("options.difficulty.$serialized")

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the difficulty with the given [name], or returns null if there
         * is no difficulty with the given [name].
         *
         * @param name the name
         * @return the difficulty with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): Difficulty? = BY_NAME[name]

        /**
         * Gets the difficulty with the given [id], or returns null if there is
         * no difficulty with the given [id].
         *
         * @param id the ID
         * @return the difficulty with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): Difficulty? = VALUES.getOrNull(id)
    }
}
