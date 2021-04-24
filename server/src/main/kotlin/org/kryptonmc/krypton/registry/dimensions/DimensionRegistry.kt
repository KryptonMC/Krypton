package org.kryptonmc.krypton.registry.dimensions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag

@Serializable
data class DimensionRegistry(
    val type: String,
    @SerialName("value") val values: List<DimensionEntry>
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", type)
        .put("value", ListBinaryTag.builder().add(values.map { it.toNBT() }).build())
        .build()
}

@Serializable
data class DimensionEntry(
    val name: String,
    val id: Int,
    @SerialName("element") val settings: DimensionEntrySettings
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("name", name)
        .putInt("id", id)
        .put("element", settings.toNBT())
        .build()
}

@Serializable
data class DimensionEntrySettings(
    @SerialName("piglin_safe") val piglinSafe: Boolean = false,
    val natural: Boolean = true,
    @SerialName("ambient_light") val ambientLight: Float = 0.0f,
    val infiniburn: String,
    @SerialName("respawn_anchor_works") val respawnAnchorWorks: Boolean = false,
    @SerialName("has_skylight") val hasSkylight: Boolean = true,
    @SerialName("bed_works") val bedWorks: Boolean = true,
    val effects: String,
    @SerialName("fixed_time") val fixedTime: Long = -1L,
    @SerialName("has_raids") val hasRaids: Boolean = true,
    @SerialName("logical_height") val logicalHeight: Int = 256,
    @SerialName("coordinate_scale") val coordinateScale: Double = 0.0,
    val ultrawarm: Boolean = false,
    @SerialName("has_ceiling") val hasCeiling: Boolean = false
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putBoolean("piglin_safe", piglinSafe)
        .putBoolean("natural", natural)
        .putFloat("ambient_light", ambientLight)
        .putString("infiniburn", infiniburn)
        .putBoolean("respawn_anchor_works", respawnAnchorWorks)
        .putBoolean("has_skylight", hasSkylight)
        .putBoolean("bed_works", bedWorks)
        .putString("effects", effects)
        .apply { if (fixedTime != -1L) putLong("fixed_time", fixedTime) }
        .putBoolean("has_raids", hasRaids)
        .putInt("logical_height", logicalHeight)
        .putDouble("coordinate_scale", coordinateScale)
        .putBoolean("ultrawarm", ultrawarm)
        .putBoolean("has_ceiling", hasCeiling)
        .build()
}
