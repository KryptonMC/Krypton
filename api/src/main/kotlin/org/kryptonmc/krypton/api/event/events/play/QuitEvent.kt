package org.kryptonmc.krypton.api.event.events.play

import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.event.Event

/**
 * Called when the connection to a player in the PLAY state is lost.
 *
 * @param player the player who quit
 * @author Callum Seabrook
 */
data class QuitEvent(val player: Player) : Event {

    @Volatile var message = translatable {
        key("multiplayer.player.left")
        color(NamedTextColor.YELLOW)
        args(text(player.name))
    }
}
