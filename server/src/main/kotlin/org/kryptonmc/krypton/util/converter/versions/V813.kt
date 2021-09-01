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
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V813 {

    private const val VERSION = MCVersions.V16W40A
    val SHULKER_ID_BY_COLOUR = arrayOf(
        "minecraft:white_shulker_box",
        "minecraft:orange_shulker_box",
        "minecraft:magenta_shulker_box",
        "minecraft:light_blue_shulker_box",
        "minecraft:yellow_shulker_box",
        "minecraft:lime_shulker_box",
        "minecraft:pink_shulker_box",
        "minecraft:gray_shulker_box",
        "minecraft:silver_shulker_box",
        "minecraft:cyan_shulker_box",
        "minecraft:purple_shulker_box",
        "minecraft:blue_shulker_box",
        "minecraft:brown_shulker_box",
        "minecraft:green_shulker_box",
        "minecraft:red_shulker_box",
        "minecraft:black_shulker_box"
    )

    fun register() {
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:shulker_box", VERSION) { data, _, _ ->
            val tag = data.getMap<String>("tag") ?: return@addConverterForId null
            val blockEntity = tag.getMap<String>("BlockEntityTag") ?: return@addConverterForId null

            val color = blockEntity.getInt("Color")
            blockEntity.remove("Color")
            data.setString("id", SHULKER_ID_BY_COLOUR[color % SHULKER_ID_BY_COLOUR.size])
            null
        }

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:shulker_box", VERSION) { data, _, _ ->
            data.remove("Color")
            null
        }
    }
}
