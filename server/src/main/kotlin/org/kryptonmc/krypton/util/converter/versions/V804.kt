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

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V804 {

    private const val VERSION = MCVersions.V16W35A + 1

    fun register() = MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:banner", VERSION) { data, _, _ ->
        val tag = data.getMap<String>("tag") ?: return@addConverterForId null
        val blockEntity = tag.getMap<String>("BlockEntityTag") ?: return@addConverterForId null
        if (!blockEntity.hasKey("Base", ObjectType.NUMBER)) return@addConverterForId null
        data.setShort("Damage", (blockEntity.getShort("Base").toInt() and 15).toShort())

        tag.getMap<String>("display")?.let { display ->
            display.getList("Lore", ObjectType.STRING)?.let {
                if (it.size() == 1 && it.getString(0) == "(+NBT)") return@addConverterForId null
            }
        }

        blockEntity.remove("Base")
        if (blockEntity.isEmpty) tag.remove("BlockEntityTag")
        if (tag.isEmpty) data.remove("tag")
        null
    }
}
