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
package org.kryptonmc.krypton.registry.biomes

import com.google.gson.annotations.SerializedName
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound

data class BiomeRegistry(
    val type: String,
    @SerializedName("value") val values: List<BiomeEntry>
) {

    fun toNBT() = compound {
        string("type", type)
        put("value", ListTag(values.mapTo(mutableListOf()) { it.toNBT() }, CompoundTag.ID))
    }
}

data class BiomeEntry(
    val name: String,
    val id: Int,
    @SerializedName("element") val settings: BiomeEntrySettings
) {

    fun toNBT() = compound {
        string("name", name)
        int("id", id)
        put("element", settings.toNBT())
    }
}

data class BiomeEntrySettings(
    val precipitation: String,
    val effects: BiomeEntryEffects,
    val depth: Float,
    val temperature: Float,
    val scale: Float,
    val downfall: Float,
    val category: String,
    @SerializedName("temperature_modifier") val temperatureModifier: String? = null
) {

    fun toNBT() = compound {
        string("precipitation", precipitation)
        put("effects", effects.toNBT())
        float("depth", depth)
        float("temperature", temperature)
        float("scale", scale)
        float("downfall", downfall)
        string("category", category)
        temperatureModifier?.let { string("temperature_modifier", it) }
    }
}

data class BiomeEntryEffects(
    val music: BiomeEntryMusic? = null,
    @SerializedName("grass_color_modifier") val grassColorModifier: String? = null,
    @SerializedName("sky_color") val skyColor: Int,
    @SerializedName("grass_color") val grassColor: Int? = null,
    @SerializedName("ambient_sound") val ambientSound: String? = null,
    @SerializedName("additions_sound") val additionsSound: BiomeEntrySound? = null,
    @SerializedName("foliage_color") val foliageColor: Int? = null,
    @SerializedName("particle") val particle: BiomeEntryParticle? = null,
    @SerializedName("water_fog_color") val waterFogColor: Int,
    @SerializedName("fog_color") val fogColor: Int,
    @SerializedName("water_color") val waterColor: Int,
    @SerializedName("mood_sound") val moodSound: BiomeEffectSound
) {

    fun toNBT() = compound {
        music?.let { put("music", it.toNBT()) }
        grassColorModifier?.let { string("grass_color_modifier", it) }
        grassColor?.let { int("grass_color", it) }
        ambientSound?.let { string("ambient_sound", it) }
        additionsSound?.let { put("additions_sound", it.toNBT()) }
        foliageColor?.let { int("foliage_color", it) }
        particle?.let { put("particle", it.toNBT()) }
        int("sky_color", skyColor)
        int("water_fog_color", waterFogColor)
        int("fog_color", fogColor)
        int("water_color", waterColor)
        put("mood_sound", moodSound.toNBT())
    }
}

data class BiomeEffectSound(
    @SerializedName("tick_delay") val tickDelay: Int,
    val offset: Double,
    val sound: String,
    @SerializedName("block_search_extent") val blockSearchExtent: Int
) {

    fun toNBT() = compound {
        int("tick_delay", tickDelay)
        double("offset", offset)
        string("sound", sound)
        int("block_search_extent", blockSearchExtent)
    }
}

data class BiomeEntryMusic(
    @SerializedName("replace_current_music") val replaceCurrentMusic: Boolean,
    @SerializedName("max_delay") val maxDelay: Int,
    val sound: String,
    @SerializedName("min_delay") val minDelay: Int
) {

    fun toNBT() = compound {
        boolean("replace_current_music", replaceCurrentMusic)
        int("max_delay", maxDelay)
        string("sound", sound)
        int("min_delay", minDelay)
    }
}

data class BiomeEntrySound(
    val sound: String,
    @SerializedName("tick_chance") val tickChance: Double
) {

    fun toNBT() = compound {
        string("sound", sound)
        double("tick_chance", tickChance)
    }
}

data class BiomeEntryParticle(
    val probability: Float,
    val options: BiomeEntryParticleOptions
) {

    fun toNBT() = compound {
        float("probability", probability)
        put("options", options.toNBT())
    }
}

data class BiomeEntryParticleOptions(val type: String) {

    fun toNBT() = compound { string("type", type) }
}
