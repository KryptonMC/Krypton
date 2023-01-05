/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.config

import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.ServerConfig
import org.kryptonmc.krypton.config.category.AdvancedCategory
import org.kryptonmc.krypton.config.category.ModulesCategory
import org.kryptonmc.krypton.config.category.ProxyCategory
import org.kryptonmc.krypton.config.category.ServerCategory
import org.kryptonmc.krypton.config.category.StatusCategory
import org.kryptonmc.krypton.config.category.WorldCategory
import org.kryptonmc.krypton.config.serializer.EnumTypeSerializer
import org.kryptonmc.krypton.config.serializer.LocaleTypeSerializer
import org.kryptonmc.krypton.util.enumhelper.Difficulties
import org.kryptonmc.krypton.util.enumhelper.GameModes
import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import java.io.IOException
import java.nio.file.Path

@ConfigSerializable
@JvmRecord
data class KryptonConfig(
    @Comment("The main server settings.")
    val server: ServerCategory = ServerCategory(),
    @Comment("Status configuration")
    val status: StatusCategory = StatusCategory(),
    @Comment("Global world configuration options")
    val world: WorldCategory = WorldCategory(),
    @Comment("Advanced settings. Don't touch these unless you know what you're doing.")
    val advanced: AdvancedCategory = AdvancedCategory(),
    @Comment("Proxy IP forwarding settings.")
    val proxy: ProxyCategory = ProxyCategory(),
    @Comment("Module settings.")
    val modules: ModulesCategory = ModulesCategory()
) : ServerConfig {

    override val isOnline: Boolean
        get() = server.onlineMode
    override val ip: String
        get() = server.ip
    override val port: Int
        get() = server.port
    override val motd: Component
        get() = status.motd
    override val maxPlayers: Int
        get() = status.maxPlayers

    companion object {

        private val HEADER = """
            This is the main Krypton configuration file. All settings in this file apply globally
            across the entire server, regardless of what they are.

            If you need any help with any of the settings in this file, you can join us on Discord
            at https://discord.gg/4QuwYACDRX
        """.trimIndent()

        private val OPTIONS = ConfigurationOptions.defaults()
            .header(HEADER)
            .serializers { builder ->
                builder.registerAll(ConfigurateComponentSerializer.builder()
                    .scalarSerializer(LegacyComponentSerializer.builder()
                        .character(LegacyComponentSerializer.AMPERSAND_CHAR)
                        .extractUrls()
                        .hexColors()
                        .build())
                    .outputStringComponents(true)
                    .build()
                    .serializers())
                builder.register(EnumTypeSerializer.of("difficulty", Difficulties::fromId, Difficulties::fromName))
                builder.register(EnumTypeSerializer.of("game mode", GameModes::fromId, GameModes::fromName))
                builder.register(LocaleTypeSerializer)
                builder.registerAnnotatedObjects(objectMapperFactory())
            }

        @JvmStatic
        fun load(path: Path): KryptonConfig {
            val loader = HoconConfigurationLoader.builder().path(path).defaultOptions(OPTIONS).build()
            val node = loader.load()
            val config = node.get<KryptonConfig>() ?: throw IOException("Unable to load configuration!")
            loader.save(node)
            return config
        }
    }
}
