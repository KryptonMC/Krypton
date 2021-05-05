package org.kryptonmc.krypton.server

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.krypton.api.world.Difficulty
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.serializers.ComponentSerializer

private val DEFAULT_MOTD = Component.text("Krypton is a Minecraft server written in Kotlin!")
    .color(TextColor.color(128, 0, 255))

/**
 * Represents the main config.conf file, in a [Serializable] object.
 *
 * Notice all of these have default values. This is to avoid errors on upgrade when we add
 * new options
 */
@Serializable
data class KryptonConfig(
    val server: ServerConfig = ServerConfig(),
    val status: StatusConfig = StatusConfig(),
    val world: WorldConfig = WorldConfig(),
    val advanced: AdvancedConfig = AdvancedConfig(),
    val query: QueryConfig = QueryConfig(),
    val other: OtherConfig = OtherConfig()
)

/**
 * The server config section within the main configuration file.
 */
@Serializable
data class ServerConfig(
    val ip: String = "0.0.0.0",
    val port: Int = 25565,
    @SerialName("online-mode") val onlineMode: Boolean = true,
    @SerialName("compression-threshold") val compressionThreshold: Int = 256
)

/**
 * The server config section within the main configuration file.
 */
@Serializable
data class StatusConfig(
    @Serializable(with = TextComponentSerializer::class) val motd: TextComponent = DEFAULT_MOTD,
    @SerialName("max-players") val maxPlayers: Int = 20
)

/**
 * The world config section within the main configuration file.
 */
@Serializable
data class WorldConfig(
    val name: String = "world",
    @Serializable(with = GamemodeSerializer::class) val gamemode: Gamemode = Gamemode.SURVIVAL,
    @SerialName("force-default-gamemode") val forceDefaultGamemode: Boolean = false,
    @Serializable(with = DifficultySerializer::class) val difficulty: Difficulty = Difficulty.NORMAL,
    val hardcore: Boolean = false,
    @SerialName("view-distance") val viewDistance: Int = 10,
    @SerialName("autosave-interval") val autosaveInterval: Int = 6000
)

/**
 * The advanced config section within the main configuration file.
 */
@Serializable
data class AdvancedConfig(
    @SerialName("synchronize-chunk-writes") val synchronizeChunkWrites: Boolean = true,
    @SerialName("enable-jmx-monitoring") val enableJmxMonitoring: Boolean = true
)

/**
 * The query config section within the main configuration file.
 */
@Serializable
data class QueryConfig(
    val enabled: Boolean = false,
    val port: Int = 25566
)

/**
 * The section for other settings that don't belong anywhere else
 */
@Serializable
data class OtherConfig(
    val bungeecord: Boolean = false,
    val metrics: Boolean = true,
    @SerialName("timeout-time") val timeoutTime: Int = 60,
    @SerialName("restart-on-crash") val restartOnCrash: Boolean = true,
    @SerialName("restart-script") val restartScript: String = "./start.sh",
    @SerialName("early-warning-interval") val earlyWarningInterval: Long = 5000,
    @SerialName("early-warning-delay") val earlyWarningDelay: Long = 10000,
    @SerialName("save-threshold") val saveThreshold: Int = 5
)

internal object GamemodeSerializer : KSerializer<Gamemode> by Gamemode.serializer() {

    override fun deserialize(decoder: Decoder): Gamemode {
        val value = decoder.decodeString()
        if (value.toIntOrNull() != null) return requireNotNull(Gamemode.fromId(value.toInt()))
        return Gamemode.valueOf(value.uppercase())
    }
}

internal object DifficultySerializer : KSerializer<Difficulty> by Difficulty.serializer() {

    override fun deserialize(decoder: Decoder): Difficulty {
        val value = decoder.decodeString()
        if (value.toIntOrNull() != null) return requireNotNull(Difficulty.fromId(value.toInt()))
        return Difficulty.valueOf(value.uppercase())
    }
}

internal object TextComponentSerializer : KSerializer<TextComponent> {

    override val descriptor = PrimitiveSerialDescriptor("TextComponent", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TextComponent) =
        encoder.encodeString(LegacyComponentSerializer.legacyAmpersand().serialize(value))

    override fun deserialize(decoder: Decoder) =
        LegacyComponentSerializer.legacyAmpersand().deserialize(decoder.decodeString())
}
