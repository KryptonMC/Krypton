/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.login

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.ComponentResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a player logs in and a player object has been
 * constructed for them (just before the state is switched to PLAY).
 *
 * @param player the player who joined
 */
class JoinEvent(val player: Player) : ResultedEvent<ComponentResult> {

    override var result = ComponentResult.allowed(translatable("multiplayer.player.joined", NamedTextColor.YELLOW, listOf(text(player.name))))
}
