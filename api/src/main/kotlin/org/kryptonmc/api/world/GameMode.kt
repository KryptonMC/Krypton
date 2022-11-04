/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.world.damage.type.DamageType

/**
 * A game mode that a player may be in that determines specific things to do
 * with what the player is able to do, such as being able to build, fly, not
 * take damage, or fly through walls.
 *
 * @param abbreviation the shortened name of this game mode, such as 's' for
 * [SURVIVAL]
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public enum class GameMode(public val abbreviation: String) : Translatable {

    /**
     * Survival mode is the default game mode. In it, you can access most
     * gameplay features, but you will take damage, and cannot fly.
     */
    SURVIVAL("s"),

    /**
     * Creative mode grants you access to spawn in any block in the game. It
     * also grants you the ability to fly around the world freely, break
     * blocks instantly, and you can only take damage from types that
     * [bypass invulnerability][DamageType.bypassesInvulnerability].
     */
    CREATIVE("c"),

    /**
     * Adventure mode is designed for custom maps. In it, your block breaking
     * and placing are restricted, you still take damage like normal, and you
     * cannot fly.
     */
    ADVENTURE("a"),

    /**
     * Spectator mode is designed for spectating things. In it, you will take
     * no damage from anything, not even types that bypass invulnerability, and
     * you can fly through walls. You cannot interact with anything in the
     * world, including breaking and placing blocks, attacking entities, and
     * opening containers.
     */
    SPECTATOR("sp");

    override fun translationKey(): String = "gameMode.${name.lowercase()}"
}
