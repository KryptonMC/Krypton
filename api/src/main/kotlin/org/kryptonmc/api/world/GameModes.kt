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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla game modes.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(GameMode::class)
public object GameModes {

    // @formatter:off
    @JvmField public val SURVIVAL: GameMode = register("survival", "s", true)
    @JvmField public val CREATIVE: GameMode = register("creative", "c", true)
    @JvmField public val ADVENTURE: GameMode = register("adventure", "a", false)
    @JvmField public val SPECTATOR: GameMode = register("spectator", "sp", false)

    // @formatter:on
    private fun register(name: String, abbreviation: String, canBuild: Boolean): GameMode {
        val key = Key.key("krypton", "game_modes/$name")
        val mode = Registries.register(Registries.GAME_MODES, key, GameMode.of(name, abbreviation, canBuild))
        return mode
    }
}
