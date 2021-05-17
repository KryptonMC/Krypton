/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.status

import net.kyori.adventure.text.Component

/**
 * Holder for status information
 */
interface StatusInfo {

    /**
     * The maximum players that can join this server
     */
    val maxPlayers: Int

    /**
     * The message of the day for this server
     */
    val motd: Component
}
