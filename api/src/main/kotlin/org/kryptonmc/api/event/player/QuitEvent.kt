/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.ComponentResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when the connection to a player in the PLAY state is lost.
 *
 * @param player the player who quit
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public data class QuitEvent(@get:JvmName("player") public val player: Player) : ResultedEvent<ComponentResult> {

    @get:JvmName("result")
    override var result: ComponentResult = ComponentResult.allowed(Component.translatable(
        "multiplayer.player.left",
        NamedTextColor.YELLOW,
        player.displayName
    ))
}
