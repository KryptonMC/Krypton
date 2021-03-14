package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event
import java.util.*

/**
 * Called when the client sends its settings information
 *
 * @author Callum Seabrook
 */
data class ClientSettingsEvent(
    val player: Player,
    val locale: Locale,
    val viewDistance: Int,
    val hasColorsEnabled: Boolean,
    val skinSettings: SkinSettings
) : Event

data class SkinSettings(
    val cape: Boolean,
    val jacket: Boolean,
    val leftSleeve: Boolean,
    val rightSleeve: Boolean,
    val leftPants: Boolean,
    val rightPants: Boolean,
    val hat: Boolean
)