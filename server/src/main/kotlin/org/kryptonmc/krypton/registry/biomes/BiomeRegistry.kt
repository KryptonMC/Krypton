package org.kryptonmc.krypton.registry.biomes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag

@Serializable
data class BiomeRegistry(
    val type: String,
    @SerialName("value") val values: List<BiomeEntry>
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", type)
        .put("value", ListBinaryTag.builder().add(values.map { it.toNBT() }).build())
        .build()
}

@Serializable
data class BiomeEntry(
    val name: String,
    val id: Int,
    @SerialName("element") val settings: BiomeEntrySettings
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("name", name)
        .putInt("id", id)
        .put("element", settings.toNBT())
        .build()
}

@Serializable
data class BiomeEntrySettings(
    val precipitation: String,
    val effects: BiomeEntryEffects,
    val depth: Float,
    val temperature: Float,
    val scale: Float,
    val downfall: Float,
    val category: String,
    @SerialName("temperature_modifier") val temperatureModifier: String? = null
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

@Serializable
data class BiomeEntryEffects(
    val music: BiomeEntryMusic? = null,
    @SerialName("grass_color_modifier") val grassColorModifier: String? = null,
    @SerialName("sky_color") val skyColor: Int,
    @SerialName("grass_color") val grassColor: Int? = null,
    @SerialName("ambient_sound") val ambientSound: String? = null,
    @SerialName("additions_sound") val additionsSound: BiomeEntrySound? = null,
    @SerialName("foliage_color") val foliageColor: Int? = null,
    @SerialName("particle") val particle: BiomeEntryParticle? = null,
    @SerialName("water_fog_color") val waterFogColor: Int,
    @SerialName("fog_color") val fogColor: Int,
    @SerialName("water_color") val waterColor: Int,
    @SerialName("mood_sound") val moodSound: BiomeEffectSound
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

@Serializable
data class BiomeEffectSound(
    @SerialName("tick_delay") val tickDelay: Int,
    val offset: Double,
    val sound: String,
    @SerialName("block_search_extent") val blockSearchExtent: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putInt("tick_delay", tickDelay)
        .putDouble("offset", offset)
        .putString("sound", sound)
        .putInt("block_search_extent", blockSearchExtent)
        .build()
}

@Serializable
data class BiomeEntryMusic(
    @SerialName("replace_current_music") val replaceCurrentMusic: Boolean,
    @SerialName("max_delay") val maxDelay: Int,
    val sound: String,
    @SerialName("min_delay") val minDelay: Int
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putBoolean("replace_current_music", replaceCurrentMusic)
        .putInt("max_delay", maxDelay)
        .putString("sound", sound)
        .putInt("min_delay", minDelay)
        .build()
}

@Serializable
data class BiomeEntrySound(
    val sound: String,
    @SerialName("tick_chance") val tickChance: Double
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("sound", sound)
        .putDouble("tick_chance", tickChance)
        .build()
}

@Serializable
data class BiomeEntryParticle(
    val probability: Float,
    val options: BiomeEntryParticleOptions
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putFloat("probability", probability)
        .put("options", options.toNBT())
        .build()
}

@Serializable
data class BiomeEntryParticleOptions(
    val type: String
) {

    fun toNBT() = CompoundBinaryTag.builder()
        .putString("type", type)
        .build()
}