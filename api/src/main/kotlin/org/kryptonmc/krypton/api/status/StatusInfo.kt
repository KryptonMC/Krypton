package org.kryptonmc.krypton.api.status

import net.kyori.adventure.text.Component
import java.awt.image.BufferedImage

/**
 * Holder for status information
 *
 * @author Callum Seabrook
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