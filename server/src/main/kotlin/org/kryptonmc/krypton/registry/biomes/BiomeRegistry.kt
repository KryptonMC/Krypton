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
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag

data class BiomeRegistry(
    val type: String,
    @SerializedName("value") val values: List<BiomeEntry>
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", type)
        .put("value", ListBinaryTag.builder().add(values.map { it.toNBT() }).build())
        .build()
}

data class BiomeEntry(
    val name: String,
    val id: Int,
    @SerializedName("element") val settings: BiomeEntrySettings
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("name", name)
        .putInt("id", id)
        .put("element", settings.toNBT())
        .build()
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

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("precipitation", precipitation)
        .put("effects", effects.toNBT())
        .putFloat("depth", depth)
        .putFloat("temperature", temperature)
        .putFloat("scale", scale)
        .putFloat("downfall", downfall)
        .putString("category", category)
        .apply { if (temperatureModifier != null) putString("temperature_modifier", temperatureModifier) }
        .build()
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

    fun toNBT() = CompoundBinaryTag.builder()
        .apply {
            if (music != null) put("music", music.toNBT())
            if (grassColorModifier != null) putString("grass_color_modifier", grassColorModifier)
        }
        .putInt("sky_color", skyColor)
        .apply {
            if (grassColor != null) putInt("grass_color", grassColor)
            if (ambientSound != null) putString("ambient_sound", ambientSound)
            if (additionsSound != null) put("additions_sound", additionsSound.toNBT())
            if (foliageColor != null) putInt("foliage_color", foliageColor)
            if (particle != null) put("particle", particle.toNBT())
        }
        .putInt("water_fog_color", waterFogColor)
        .putInt("fog_color", fogColor)
        .putInt("water_color", waterColor)
        .put("mood_sound", moodSound.toNBT())
        .build()
}

data class BiomeEffectSound(
    @SerializedName("tick_delay") val tickDelay: Int,
    val offset: Double,
    val sound: String,
    @SerializedName("block_search_extent") val blockSearchExtent: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("tick_delay", tickDelay)
        .putDouble("offset", offset)
        .putString("sound", sound)
        .putInt("block_search_extent", blockSearchExtent)
        .build()
}

data class BiomeEntryMusic(
    @SerializedName("replace_current_music") val replaceCurrentMusic: Boolean,
    @SerializedName("max_delay") val maxDelay: Int,
    val sound: String,
    @SerializedName("min_delay") val minDelay: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putBoolean("replace_current_music", replaceCurrentMusic)
        .putInt("max_delay", maxDelay)
        .putString("sound", sound)
        .putInt("min_delay", minDelay)
        .build()
}

data class BiomeEntrySound(
    val sound: String,
    @SerializedName("tick_chance") val tickChance: Double
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("sound", sound)
        .putDouble("tick_chance", tickChance)
        .build()
}

data class BiomeEntryParticle(
    val probability: Float,
    val options: BiomeEntryParticleOptions
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putFloat("probability", probability)
        .put("options", options.toNBT())
        .build()
}

data class BiomeEntryParticleOptions(val type: String) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", type)
        .build()
}
