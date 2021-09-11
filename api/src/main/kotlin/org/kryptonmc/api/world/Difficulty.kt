/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

/**
 * Represents the difficulty of a world. That being a measure of how difficult
 * it is to play the game (though this is not always accurate).
 */
public enum class Difficulty {

    /**
     * In peaceful mode, no hostile monsters will spawn in the world.
     *
     * Players will also regain health over time, and the hunger bar
     * does not deplete.
     */
    PEACEFUL,

    /**
     * In easy mode, hostile mobs spawn at normal rates, but they deal less
     * damage than on normal difficulty. The hunger bar does deplete, and
     * starving can deal up to 5 hears of damage.
     */
    EASY,

    /**
     * In normal mode, hostile mobs spawn at normal rates, and they deal normal
     * amounts of damage. The hunger bar does deplete, and starving can deal up
     * to 9.5 hearts of damage.
     */
    NORMAL,

    /**
     * In hard mode, hostile mobs spawn at normal rates, but they deal much
     * greater damage than on normal difficulty. The hunger bar does deplete,
     * and starving can kill players.
     */
    HARD;

    public companion object {

        /**
         * Retrieves a difficulty from its legacy ID. Should only need to be
         * used internally.
         */
        @JvmStatic
        public fun fromId(id: Int): Difficulty = values()[id % values().size]
    }
}
