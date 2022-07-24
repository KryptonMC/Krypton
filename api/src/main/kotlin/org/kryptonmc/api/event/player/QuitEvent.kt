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

/**
 * Called when the connection to a player is lost.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface QuitEvent : PlayerEvent {

    /**
     * The message that will be sent to all other players on the server when
     * the player quits.
     *
     * Set this to null to send no message when the player quits.
     */
    @get:JvmName("quitMessage")
    public var quitMessage: Component?
}
