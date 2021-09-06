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

import ca.spottedleaf.dataconverter.types.MapType
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V1458 {

    private const val VERSION = MCVersions.V17W50A + 1

    fun register() {
        // From CB
        MCTypeRegistry.PLAYER.addStructureConverter(VERSION) { data, _, _ -> data.updateCustomName() }

        MCTypeRegistry.ENTITY.addStructureConverter(VERSION) { data, _, _ ->
            if (data.getString("id") == "minecraft:commandblock_minecart") null else data.updateCustomName()
        }

        MCTypeRegistry.ITEM_STACK.addStructureConverter(VERSION) { data, _, _ ->
            val tag = data.getMap<String>("tag") ?: return@addStructureConverter null
            val display = tag.getMap<String>("display") ?: return@addStructureConverter null
            val name = display.getString("Name")

            if (name != null) {
                display.setString("Name", GsonComponentSerializer.gson().serialize(text(name)))
            } else {
                val localisedName = display.getString("LocName")
                if (localisedName != null) {
                    display.setString("Name", GsonComponentSerializer.gson().serialize(translatable(localisedName)))
                    display.remove("LocName")
                }
            }
            null
        }

        MCTypeRegistry.TILE_ENTITY.addStructureConverter(VERSION) { data, _, _ ->
            if (data.getString("id") == "minecraft:command_block") null else data.updateCustomName()
        }
    }

    fun MapType<String>.updateCustomName(): MapType<String>? {
        val customName = getString("CustomName", "")!!
        if (customName.isEmpty()) {
            remove("CustomName")
        } else {
            setString("CustomName", GsonComponentSerializer.gson().serialize(text(customName)))
        }
        return null
    }
}
