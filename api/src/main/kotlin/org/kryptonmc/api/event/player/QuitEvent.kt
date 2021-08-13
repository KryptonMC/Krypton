/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.extra.kotlin.translatable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.entity.player.Player

/**
 * Called when the connection to a player in the PLAY state is lost.
 *
 * @param player the player who quit
 */
class QuitEvent(val player: Player) {

    /**
     * The message to send to the player when they quit.
     */
    @Volatile
    var message: Component = translatable {
        key("multiplayer.player.left")
        color(NamedTextColor.YELLOW)
        args(text(player.name))
    }
}
