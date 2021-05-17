/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard.criteria

/**
 * Represents a single criteria, also known as a [Criterion]
 *
 * See [here](https://minecraft.gamepedia.com/Scoreboard#Single_criteria) for details
 */
enum class Criterion(override val isMutable: Boolean) : Criteria {

    DUMMY(true),
    TRIGGER(true),
    DEATH_COUNT(true),
    PLAYER_KILL_COUNT(true),
    TOTAL_KILL_COUNT(true),
    HEALTH(false),
    XP(false),
    LEVEL(false),
    FOOD(false),
    AIR(false),
    ARMOR(false)
}
