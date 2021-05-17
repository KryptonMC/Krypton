package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class ServerCategory(
    @Comment("The IP used by players to connect. 0.0.0.0 means listen on all interfaces.")
    val ip: String = "0.0.0.0",
    @Comment("The port used by players to connect.")
    val port: Int = 25565,
    @Setting("online-mode")
    @Comment("Whether the server authenticates users with Mojang.")
    val onlineMode: Boolean = true,
    @Setting("compression-threshold")
    @Comment("The threshold at which packets larger will be compressed. Set to -1 to disable.")
    val compressionThreshold: Int = 256
)
