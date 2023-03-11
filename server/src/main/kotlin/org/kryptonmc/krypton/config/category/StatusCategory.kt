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
package org.kryptonmc.krypton.config.category

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
@JvmRecord
data class StatusCategory(
    @Comment("If this server responds to status requests from clients.")
    val enabled: Boolean = true,
    @Comment("The message of the day. Supports legacy and hex codes (using &#).")
    val motd: TextComponent = DEFAULT_MOTD,
    @Comment("The upper limit of the player count. Any players that try to join when this is reached will be kicked.")
    val maxPlayers: Int = DEFAULT_MAX_PLAYERS
) {

    companion object {

        const val DEFAULT_MAX_PLAYERS: Int = 20
        private val DEFAULT_MOTD = Component.text("Krypton is a Minecraft server written in Kotlin!", TextColor.color(128, 0, 255))
    }
}
