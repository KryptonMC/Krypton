/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.registry.dimensions

import com.google.gson.annotations.SerializedName
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound

data class DimensionRegistry(
    val type: String,
    @SerializedName("value") val values: List<DimensionEntry>
) {

    fun toNBT() = compound {
        string("type", type)
        put("value", ListTag(values.mapTo(mutableListOf()) { it.toNBT() }, CompoundTag.ID))
    }
}

data class DimensionEntry(
    val name: String,
    val id: Int,
    @SerializedName("element") val settings: DimensionEntrySettings
) {

    fun toNBT() = compound {
        string("name", name)
        int("id", id)
        put("element", settings.toNBT())
    }
}

data class DimensionEntrySettings(
    @SerializedName("piglin_safe") val piglinSafe: Boolean = false,
    val natural: Boolean = true,
    @SerializedName("ambient_light") val ambientLight: Float = 0.0f,
    val infiniburn: String,
    @SerializedName("respawn_anchor_works") val respawnAnchorWorks: Boolean = false,
    @SerializedName("has_skylight") val hasSkylight: Boolean = true,
    @SerializedName("bed_works") val bedWorks: Boolean = true,
    val effects: String,
    @SerializedName("fixed_time") val fixedTime: Long = -1L,
    @SerializedName("has_raids") val hasRaids: Boolean = true,
    @SerializedName("logical_height") val logicalHeight: Int = 256,
    @SerializedName("coordinate_scale") val coordinateScale: Double = 0.0,
    val ultrawarm: Boolean = false,
    @SerializedName("has_ceiling") val hasCeiling: Boolean = false,
    @SerializedName("min_y") val minY: Int,
    val height: Int
) {

    fun toNBT() = compound {
        boolean("piglin_safe", piglinSafe)
        boolean("natural", natural)
        float("ambient_light", ambientLight)
        string("infiniburn", infiniburn)
        boolean("respawn_anchor_works", respawnAnchorWorks)
        boolean("has_skylight", hasSkylight)
        boolean("bed_works", bedWorks)
        string("effects", effects)
        if (fixedTime != -1L) long("fixed_time", fixedTime)
        boolean("has_raids", hasRaids)
        int("logical_height", logicalHeight)
        double("coordinate_scale", coordinateScale)
        boolean("ultrawarm", ultrawarm)
        boolean("has_ceiling", hasCeiling)
        int("min_y", minY)
        int("height", height)
    }
}
