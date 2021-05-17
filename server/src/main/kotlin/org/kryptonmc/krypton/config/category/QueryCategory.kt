package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class QueryCategory(
    @Comment("Whether to enable the query protocol.")
    val enabled: Boolean = false,
    @Comment("The port the query listener should listen on.")
    val port: Int = 25566
)
