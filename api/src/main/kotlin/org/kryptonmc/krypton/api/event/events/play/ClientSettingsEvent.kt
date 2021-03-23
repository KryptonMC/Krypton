package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event
import java.util.*

/**
 * Called when the client sends its settings information
 *
 * @param player the player who sent their settings
 * @param locale the player's locale
 * @param viewDistance the player's view distance
 * @param hasColorsEnabled if the player has chat colours enabled
 * @param skinSettings the player's skin settings
 * @author Callum Seabrook
 */
data class ClientSettingsEvent(
    val player: Player,
    val locale: Locale,
    val viewDistance: Int,
    val hasColorsEnabled: Boolean,
    val skinSettings: SkinSettings
) : Event

/**
 * Holder for all the settings for a player's skin
 *
 * @param cape whether the player has their cape shown
 * @param jacket whether the player has their jacket shown
 * @param leftSleeve whether the player has their left sleeve shown
 * @param rightSleeve whether the player has their right sleeve shown
 * @param leftPants whether the player has the left part of their trousers shown
 * @param rightPants whether the player has the right part of their trousers shown
 * @param hat whether the player has their hat shown
 * @author Callum Seabrook
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