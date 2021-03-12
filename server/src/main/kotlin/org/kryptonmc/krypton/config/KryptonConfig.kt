package org.kryptonmc.krypton.config

import net.kyori.adventure.text.TextComponent
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode

data class KryptonConfig(
    val server: ServerConfig,
    val status: StatusConfig,
    val world: WorldConfig
)

data class ServerConfig(
    val ip: String,
    val port: Int,
    val onlineMode: Boolean,
    val compressionThreshold: Int
)

data class StatusConfig(
    val motd: TextComponent,
    val maxPlayers: Int
)

data class WorldConfig(
    val name: String,
    val gamemode: Gamemode,
    val difficulty: Difficulty,
    val hardcore: Boolean,
    val viewDistance: Int
)

const val RGB_MAX_VALUE = 16777215