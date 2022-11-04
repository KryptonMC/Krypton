/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.event.ComponentResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * Called when a player sends a chat message (not a command).
 */
public interface ChatEvent : PlayerEvent, ResultedEvent<ComponentResult> {

    /**
     * The message that the player has sent.
     */
    public val message: String
}
