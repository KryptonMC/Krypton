package org.kryptonmc.krypton.api.world

import kotlinx.serialization.Serializable

/**
 * Represents the difficulty of a world. That being a measure of how difficult
 * it is to play the game (though this is not always accurate).
 *
 * @author Callum Seabrook
 */
@Serializable
enum class Difficulty {

    /**
     * In peaceful mode, no hostile monsters will spawn in the world.
     * Players will also regain health over time, and the hunger bar
     * does not deplete.
     */
    PEACEFUL,

    /**
     * In easy mode, hostile mobs spawn at normal rates, but they deal
     * less damage than on normal difficulty. The hunger bar does
     * deplete, and starving can deal up to 5 hears of damage.
     */
    EASY,

    /**
     * In normal mode, hostile mobs spawn at normal rates, and they
     * deal normal amounts of damage. The hunger bar does deplete,
     * and starving can deal up to 9.5 hearts of damage.
     */
    NORMAL,

    /**
     * In hard mode, hostile mobs spawn at normal rates, but they deal
     * much greater damage than on normal difficulty. The hunger bar does
     * deplete, and starving can kill players.
     */
    HARD;

    companion object {

        /**
         * Retrieves a difficulty from its legacy ID. Should only need to be used internally.
         */
        fun fromId(id: Int) = values()[id]
    }
}