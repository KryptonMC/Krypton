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

import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class WorldCategory(
    @Comment("The name of the folder with the world to load in it.")
    val name: String = "world",
    @Comment("Settings for world generation")
    val generator: GeneratorCategory = GeneratorCategory(),
    @Comment("The gamemode for this world. " +
            "Valid values are: 0-3 (legacy), survival, creative, adventure and spectator (case insensitive).")
    val gamemode: Gamemode = Gamemode.SURVIVAL,
    @Setting("force-default-gamemode")
    @Comment("Forces the above gamemode for all players in all worlds.")
    val forceDefaultGamemode: Boolean = false,
    @Comment("The default difficulty. Valid values are: 0-3 (legacy), peaceful, easy, normal and hard (case insensitive).")
    val difficulty: Difficulty = Difficulty.NORMAL,
    @Comment("If this server is in hardcore mode. Currently does nothing.")
    val hardcore: Boolean = false,
    @Setting("view-distance")
    @Comment("The render distance of the server. " +
            "This is how many chunks you can see in front of you, excluding the one you are in.")
    val viewDistance: Int = 10,
    @Setting("autosave-interval")
    @Comment("The amount of time (in ticks) between automatic world saves.")
    val autosaveInterval: Int = 6000
)
