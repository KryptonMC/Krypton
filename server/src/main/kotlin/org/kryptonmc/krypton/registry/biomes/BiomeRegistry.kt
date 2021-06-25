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
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.krypton.util.nbt.setBoolean

data class BiomeRegistry(
    val type: String,
    @SerializedName("value") val values: List<BiomeEntry>
) {

    fun toNBT() = NBTCompound()
        .setString("type", type)
        .set("value", NBTList<NBTCompound>(NBTTypes.TAG_Compound).apply { values.forEach { add(it.toNBT()) } })
}

data class BiomeEntry(
    val name: String,
    val id: Int,
    @SerializedName("element") val settings: BiomeEntrySettings
) {

    fun toNBT() = NBTCompound()
        .setString("name", name)
        .setInt("id", id)
        .set("element", settings.toNBT())
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

    fun toNBT() = NBTCompound()
        .setString("precipitation", precipitation)
        .set("effects", effects.toNBT())
        .setFloat("depth", depth)
        .setFloat("temperature", temperature)
        .setFloat("scale", scale)
        .setFloat("downfall", downfall)
        .setString("category", category)
        .apply { temperatureModifier?.let { setString("temperature_modifier", it) } }
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

    fun toNBT() = NBTCompound()
        .apply {
            music?.let { set("music", it.toNBT()) }
            grassColorModifier?.let { setString("grass_color_modifier", it) }
            grassColor?.let { setInt("grass_color", it) }
            ambientSound?.let { setString("ambient_sound", it) }
            additionsSound?.let { set("additions_sound", it.toNBT()) }
            foliageColor?.let { setInt("foliage_color", it) }
            particle?.let { set("particle", it.toNBT()) }
        }
        .setInt("sky_color", skyColor)
        .setInt("water_fog_color", waterFogColor)
        .setInt("fog_color", fogColor)
        .setInt("water_color", waterColor)
        .set("mood_sound", moodSound.toNBT())
}

data class BiomeEffectSound(
    @SerializedName("tick_delay") val tickDelay: Int,
    val offset: Double,
    val sound: String,
    @SerializedName("block_search_extent") val blockSearchExtent: Int
) {

    fun toNBT() = NBTCompound()
        .setInt("tick_delay", tickDelay)
        .setDouble("offset", offset)
        .setString("sound", sound)
        .setInt("block_search_extent", blockSearchExtent)
}

data class BiomeEntryMusic(
    @SerializedName("replace_current_music") val replaceCurrentMusic: Boolean,
    @SerializedName("max_delay") val maxDelay: Int,
    val sound: String,
    @SerializedName("min_delay") val minDelay: Int
) {

    fun toNBT() = NBTCompound()
        .setBoolean("replace_current_music", replaceCurrentMusic)
        .setInt("max_delay", maxDelay)
        .setString("sound", sound)
        .setInt("min_delay", minDelay)
}

data class BiomeEntrySound(
    val sound: String,
    @SerializedName("tick_chance") val tickChance: Double
) {

    fun toNBT() = NBTCompound()
        .setString("sound", sound)
        .setDouble("tick_chance", tickChance)
}

data class BiomeEntryParticle(
    val probability: Float,
    val options: BiomeEntryParticleOptions
) {

    fun toNBT() = NBTCompound()
        .setFloat("probability", probability)
        .set("options", options.toNBT())
}

data class BiomeEntryParticleOptions(val type: String) {

    fun toNBT() = NBTCompound().setString("type", type)
}
