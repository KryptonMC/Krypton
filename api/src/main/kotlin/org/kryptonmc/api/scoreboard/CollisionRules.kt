/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the possible vanilla collision rules.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(CollisionRule::class)
public object CollisionRules {

    // @formatter:off
    @JvmField public val ALWAYS: CollisionRule = get("always")
    @JvmField public val NEVER: CollisionRule = get("never")
    @JvmField public val PUSH_OTHER_TEAMS: CollisionRule = get("push_other_teams")
    @JvmField public val PUSH_OWN_TEAM: CollisionRule = get("push_own_team")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): CollisionRule = Registries.COLLISION_RULES[Key.key(name)]!!
}
