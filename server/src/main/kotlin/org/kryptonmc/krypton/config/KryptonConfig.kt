package org.kryptonmc.krypton.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode

@Serializable
data class KryptonConfig(
    val server: ServerConfig,
    val status: StatusConfig,
    val world: WorldConfig,
    val advanced: AdvancedConfig
)

@Serializable
data class ServerConfig(
    val ip: String,
    val port: Int,
    @SerialName("online-mode") val onlineMode: Boolean,
    @SerialName("compression-threshold") val compressionThreshold: Int,
    @SerialName("tick-threshold") val tickThreshold: Int
)

@Serializable
data class StatusConfig(
    @Serializable(with = TextComponentSerializer::class) val motd: TextComponent,
    @SerialName("max-players") val maxPlayers: Int
)

@Serializable
data class WorldConfig(
    val name: String,
    @Serializable(with = GamemodeSerializer::class) val gamemode: Gamemode,
    @SerialName("force-default-gamemode") val forceDefaultGamemode: Boolean,
    @Serializable(with = DifficultySerializer::class) val difficulty: Difficulty,
    val hardcore: Boolean,
    @SerialName("view-distance") val viewDistance: Int,
    @SerialName("autosave-interval") val autosaveInterval: Int
)

@Serializable
data class AdvancedConfig(
    @SerialName("synchronize-chunk-writes") val synchronizeChunkWrites: Boolean = true
)

internal object GamemodeSerializer : KSerializer<Gamemode> by Gamemode.serializer() {

    override fun deserialize(decoder: Decoder): Gamemode {
        val value = decoder.decodeString()
        if (value.toIntOrNull() != null) return requireNotNull(Gamemode.fromId(value.toInt()))
        return Gamemode.valueOf(value.toUpperCase())
    }
}

internal object DifficultySerializer : KSerializer<Difficulty> by Difficulty.serializer() {

    override fun deserialize(decoder: Decoder): Difficulty {
        val value = decoder.decodeString()
        if (value.toIntOrNull() != null) return requireNotNull(Difficulty.fromId(value.toInt()))
        return Difficulty.valueOf(value.toUpperCase())
    }
}

internal object TextComponentSerializer : KSerializer<TextComponent> {

    override val descriptor = PrimitiveSerialDescriptor("TextComponent", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TextComponent) =
        encoder.encodeString(LegacyComponentSerializer.legacyAmpersand().serialize(value))

    override fun deserialize(decoder: Decoder) =
        LegacyComponentSerializer.legacyAmpersand().deserialize(decoder.decodeString())
}