package org.kryptonmc.krypton.config

import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.krypton.config.category.AdvancedCategory
import org.kryptonmc.krypton.config.category.OtherCategory
import org.kryptonmc.krypton.config.category.QueryCategory
import org.kryptonmc.krypton.config.category.ServerCategory
import org.kryptonmc.krypton.config.category.StatusCategory
import org.kryptonmc.krypton.config.category.WatchdogCategory
import org.kryptonmc.krypton.config.category.WorldCategory
import org.kryptonmc.krypton.config.serializer.DifficultyTypeSerializer
import org.kryptonmc.krypton.config.serializer.GamemodeTypeSerializer
import org.kryptonmc.krypton.config.serializer.LocaleTypeSerializer
import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class KryptonConfig(
    @Comment("\n\nThe main server settings.")
    val server: ServerCategory = ServerCategory(),
    @Comment("\n\nStatus configuration")
    val status: StatusCategory = StatusCategory(),
    @Comment("\n\nGlobal world configuration options")
    val world: WorldCategory = WorldCategory(),
    @Comment("\n\nAdvanced settings. Don't touch these unless you know what you're doing.")
    val advanced: AdvancedCategory = AdvancedCategory(),
    @Comment("\n\nSettings for the GS4 query protocol listener. If you don't know what that means, don't touch these :)")
    val query: QueryCategory = QueryCategory(),
    @Comment("\n\nWatchdog settings. The watchdog monitors the server for freezes.")
    val watchdog: WatchdogCategory = WatchdogCategory(),
    @Comment("\n\nOther settings that don't quite fit in anywhere else.")
    val other: OtherCategory = OtherCategory()
) {

    companion object {

        const val FILE_NAME = "config.conf"
        private val HEADER = """
            This is the main Krypton configuration file. All settings in this file apply globally
            across the entire server, regardless of what they are.

            If you need any help with any of the settings in this file, you can join us on Discord
            at https://discord.gg/4QuwYACDRX
        """.trimIndent()

        val OPTIONS: ConfigurationOptions = ConfigurationOptions.defaults()
            .header(HEADER)
            .serializers {
                it.registerAll(ConfigurateComponentSerializer.builder()
                    .scalarSerializer(LegacyComponentSerializer.builder()
                        .character('&')
                        .extractUrls()
                        .hexColors()
                        .build())
                    .outputStringComponents(true)
                    .build()
                    .serializers()
                ).register(DifficultyTypeSerializer)
                    .register(GamemodeTypeSerializer)
                    .register(LocaleTypeSerializer)
                    .registerAnnotatedObjects(objectMapperFactory())
            }
    }
}
