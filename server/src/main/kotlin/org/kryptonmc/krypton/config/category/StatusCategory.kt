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
