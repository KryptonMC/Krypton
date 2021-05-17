package org.kryptonmc.krypton.config.category

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class AdvancedCategory(
    @Setting("synchronize-chunk-writes")
    @Comment("Whether we should use the DSYNC option for saving region files to disk.")
    val synchronizeChunkWrites: Boolean = true,
    @Setting("enable-jmx-monitoring")
    @Comment("Whether to attempt to register the server as a JMX bean.")
    val enableJmxMonitoring: Boolean = true
)
