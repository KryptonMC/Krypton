/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.play

import org.kryptonmc.api.entity.player.Player
import java.util.Locale

/**
 * Called when the client sends its settings information.
 *
 * @param player the player who sent their settings
 * @param locale the player's locale
 * @param viewDistance the player's view distance
 * @param hasColorsEnabled if the player has chat colours enabled
 * @param skinSettings the player's skin settings
 */
class ClientSettingsEvent(
    val player: Player,
    val locale: Locale,
    val viewDistance: Int,
    val hasColorsEnabled: Boolean,
    val skinSettings: SkinSettings
)

/**
 * Holder for all the settings for a player's skin.
 *
 * @param cape whether the player has their cape shown
 * @param jacket whether the player has their jacket shown
 * @param leftSleeve whether the player has their left sleeve shown
 * @param rightSleeve whether the player has their right sleeve shown
 * @param leftPants whether the player has the left part of their trousers shown
 * @param rightPants whether the player has the right part of their trousers shown
 * @param hat whether the player has their hat shown
 */
data class SkinSettings(
    val cape: Boolean,
    val jacket: Boolean,
    val leftSleeve: Boolean,
    val rightSleeve: Boolean,
    val leftPants: Boolean,
    val rightPants: Boolean,
    val hat: Boolean
)
