package org.kryptonmc.krypton.world.data

import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.serialization.Dynamic

class WorldVersion(
    val storageVersion: Int,
    val lastPlayed: Long,
    val minecraftVersionName: String,
    val minecraftVersion: DataVersion,
    val snapshot: Boolean
) {

    companion object {

        @JvmStatic
        fun parse(data: Dynamic<*>): WorldVersion {
            val storageVersion = data.get("version").asInt(0)
            val lastPlayed = data.get("LastPlayed").asLong(0L)
            val version = data.get("Version")
            if (!version.result().isPresent) return WorldVersion(storageVersion, lastPlayed, "", DataVersion(0), false)
            return WorldVersion(
                storageVersion,
                lastPlayed,
                version.get("Name").asString(KryptonPlatform.minecraftVersion),
                DataVersion(version.get("Id").asInt(KryptonPlatform.worldVersion), version.get("Series").asString(DataVersion.MAIN_SERIES)),
                version.get("Snapshot").asBoolean(!KryptonPlatform.isStableMinecraft)
            )
        }
    }
}
