package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class OtherCategory(
    @Comment("If we should enable support for BungeeCord's IP forwarding mechanism. Requires all connections to originate from BungeeCord.")
    val bungeecord: Boolean = false,
    @Comment("If we should enable bStats metrics for the server")
    val metrics: Boolean = true,
    @Setting("save-threshold")
    @Comment("The duration (in seconds) a single tick must take before the single tick profiler reports it.")
    val saveThreshold: Int = 5
)
