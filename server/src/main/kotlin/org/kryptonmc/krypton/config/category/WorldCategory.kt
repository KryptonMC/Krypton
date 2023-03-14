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
    @Comment("The game mode for this world. Valid values are: 0-3 (legacy), survival, creative, adventure and spectator (case insensitive).")
    val defaultGameMode: GameMode = GameMode.SURVIVAL,
    @Comment("Whether the game mode should be forced for all players in the world.")
    val forceDefaultGameMode: Boolean = false,
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
