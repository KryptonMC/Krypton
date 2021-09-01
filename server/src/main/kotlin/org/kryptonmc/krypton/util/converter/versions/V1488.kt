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
import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.versions.V1458.updateCustomName
import org.kryptonmc.krypton.util.converters.RenameBlocksConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V1488 {

    private const val VERSION = MCVersions.V18W19B + 3

    fun register() {
        RenameBlocksConverter.register(VERSION, mapOf(
            "minecraft:kelp_top" to "minecraft:kelp",
            "minecraft:kelp" to "minecraft:kelp_plant"
        )::get)
        RenameItemsConverter.register(VERSION, mapOf("minecraft:kelp_top" to "minecraft:kelp")::get)

        // Don't ask me why in V1458 they wrote the converter to NOT do command blocks and THEN in THIS version
        // to ONLY do command blocks. I don't know.

        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:command_block", VERSION) { data, _, _ -> data.updateCustomName() }
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:commandblock_minecart", VERSION) { data, _, _ -> data.updateCustomName() }

        MCTypeRegistry.STRUCTURE_FEATURE.addStructureConverter(VERSION) { data, _, _ ->
            val children = data.getList("Children", ObjectType.MAP)
            var isIgloo = false
            if (children != null) {
                isIgloo = true
                for (i in 0 until children.size()) {
                    if (!children.getMap<String>(i).isIglooPiece()) {
                        isIgloo = false
                        break
                    }
                }
            } else {
                isIgloo = false
            }

            if (isIgloo) {
                data.remove("Children")
                data.setString("id", "Igloo")
                return@addStructureConverter null
            }
            children?.let {
                var i = 0
                while (i < it.size()) {
                    val child = it.getMap<String>(i)
                    if (child.isIglooPiece()) {
                        children.remove(i)
                        continue
                    }
                    ++i
                }
            }
            null
        }
    }

    private fun MapType<String>.isIglooPiece() = getString("id") == "Iglu"
}
