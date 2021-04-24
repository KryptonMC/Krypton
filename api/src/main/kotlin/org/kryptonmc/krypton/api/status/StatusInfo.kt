package org.kryptonmc.krypton.api.status

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
