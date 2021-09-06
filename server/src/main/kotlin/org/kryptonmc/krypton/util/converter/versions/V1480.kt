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
package org.kryptonmc.krypton.util.converter.versions

import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converters.RenameBlocksConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V1480 {

    private const val VERSION = MCVersions.V18W14A + 1
    private val RENAMED_IDS = mapOf(
        "minecraft:blue_coral" to "minecraft:tube_coral_block",
        "minecraft:pink_coral" to "minecraft:brain_coral_block",
        "minecraft:purple_coral" to "minecraft:bubble_coral_block",
        "minecraft:red_coral" to "minecraft:fire_coral_block",
        "minecraft:yellow_coral" to "minecraft:horn_coral_block",
        "minecraft:blue_coral_plant" to "minecraft:tube_coral",
        "minecraft:pink_coral_plant" to "minecraft:brain_coral",
        "minecraft:purple_coral_plant" to "minecraft:bubble_coral",
        "minecraft:red_coral_plant" to "minecraft:fire_coral",
        "minecraft:yellow_coral_plant" to "minecraft:horn_coral",
        "minecraft:blue_coral_fan" to "minecraft:tube_coral_fan",
        "minecraft:pink_coral_fan" to "minecraft:brain_coral_fan",
        "minecraft:purple_coral_fan" to "minecraft:bubble_coral_fan",
        "minecraft:red_coral_fan" to "minecraft:fire_coral_fan",
        "minecraft:yellow_coral_fan" to "minecraft:horn_coral_fan",
        "minecraft:blue_dead_coral" to "minecraft:dead_tube_coral",
        "minecraft:pink_dead_coral" to "minecraft:dead_brain_coral",
        "minecraft:purple_dead_coral" to "minecraft:dead_bubble_coral",
        "minecraft:red_dead_coral" to "minecraft:dead_fire_coral",
        "minecraft:yellow_dead_coral" to "minecraft:dead_horn_coral",
    )

    fun register() {
        RenameBlocksConverter.register(VERSION, RENAMED_IDS::get)
        RenameItemsConverter.register(VERSION, RENAMED_IDS::get)
    }
}
