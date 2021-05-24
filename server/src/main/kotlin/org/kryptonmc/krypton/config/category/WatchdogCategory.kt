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
package org.kryptonmc.krypton.config.category

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class WatchdogCategory(
    @Setting("timeout-time")
    @Comment("The time (in seconds) that the server must not respond for before watchdog considers it dead.")
    val timeoutTime: Int = 60,
    @Setting("restart-on-crash")
    @Comment("Whether we should attempt to restart the server if it crashes")
    val restartOnCrash: Boolean = true,
    @Setting("restart-script")
    @Comment("The script we should use to attempt to restart the server.")
    val restartScript: String = "./start.sh",
    @Setting("restart-message")
    @Comment("The message to send to players when we restart the server. Supports legacy and hex codes (&# format).")
    val restartMessage: Component = Component.text("Server closed.", NamedTextColor.RED),
    @Setting("early-warning-interval")
    @Comment("How often (in milliseconds) watchdog should warn you in advanced that the server isn't responding")
    val earlyWarningInterval: Long = 5000L,
    @Setting("early-warning-delay")
    @Comment("The time (in milliseconds) before watchdog first warns you about the server not responding")
    val earlyWarningDelay: Long = 10000L
)
