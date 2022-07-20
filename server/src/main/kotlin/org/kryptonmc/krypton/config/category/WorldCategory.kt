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
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.GameMode
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
@JvmRecord
data class WorldCategory(
    @Comment("The name of the folder with the world to load in it.")
    val name: String = "world",
    @Comment("Settings for world generation")
    val generator: GeneratorCategory = GeneratorCategory(),
    @Comment("The gamemode for this world. Valid values are: 0-3 (legacy), survival, creative, adventure and spectator (case insensitive).")
    val gameMode: GameMode = GameMode.SURVIVAL,
    @Comment("The default difficulty. Valid values are: 0-3 (legacy), peaceful, easy, normal and hard (case insensitive).")
    val difficulty: Difficulty = Difficulty.NORMAL,
    @Comment("If this server is in hardcore mode. Currently does nothing.")
    val hardcore: Boolean = false,
    @Setting("allow-command-blocks")
    @Comment("If command blocks are allowed to be used by players.")
    val allowCommandBlocks: Boolean = false,
    @Setting("view-distance")
    @Comment("The render distance of the server. This is how many chunks you can see in front of you, excluding the one you are in.")
    val viewDistance: Int = 10,
    @Setting("simulation-distance")
    @Comment("The distance, in chunks, that the client will simulate things, like entities.")
    val simulationDistance: Int = 10,
    @Setting("autosave-interval")
    @Comment("The amount of time (in ticks) between automatic world saves.")
    val autosaveInterval: Int = 6000,
    @Setting("spawn-protection-radius")
    @Comment("The radius from spawn in which players cannot break blocks.")
    val spawnProtectionRadius: Int = 16,
    @Setting("send-spawn-protection-message")
    @Comment("If the spawn protection message should be sent to players in protection.")
    val sendSpawnProtectionMessage: Boolean = true,
    @Setting("spawn-protection-message")
    @Comment("The message sent to players that try to break blocks in spawn.")
    val spawnProtectionMessage: Component = DEFAULT_PROTECTION_MESSAGE
) {

    companion object {

        private val DEFAULT_PROTECTION_MESSAGE = Component.text()
            .content("You are currently in a protected area. You may not break blocks here.")
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .build()
    }
}
