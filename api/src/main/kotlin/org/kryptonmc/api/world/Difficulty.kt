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
package org.kryptonmc.api.world

import net.kyori.adventure.translation.Translatable

/**
 * Represents the difficulty of a world. That being a measure of how difficult
 * it is to play the game (though this is not always accurate).
 */
public enum class Difficulty : Translatable {

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

    override fun translationKey(): String = "options.difficulty.${name.lowercase()}"
}
