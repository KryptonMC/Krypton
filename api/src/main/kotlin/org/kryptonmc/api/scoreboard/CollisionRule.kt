/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

/**
 * A rule for collision between members of a team.
 */
public enum class CollisionRule {

    ALWAYS,
    NEVER,
    PUSH_OTHER_TEAMS,
    PUSH_OWN_TEAM
}
