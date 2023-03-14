/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
