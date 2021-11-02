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
 * All of the possible vanilla visibilities.
 */
@Catalogue(Visibility::class)
public object Visibilities {

    // @formatter:off
    @JvmField public val ALWAYS: Visibility = get("always")
    @JvmField public val NEVER: Visibility = get("never")
    @JvmField public val HIDE_FOR_OTHER_TEAMS: Visibility = get("hide_for_other_teams")
    @JvmField public val HIDE_FOR_OWN_TEAM: Visibility = get("hide_for_own_team")

    // @formatter:on
    @JvmStatic
    private fun get(name: String): Visibility = Registries.VISIBILITIES[Key.key(name)]!!
}
