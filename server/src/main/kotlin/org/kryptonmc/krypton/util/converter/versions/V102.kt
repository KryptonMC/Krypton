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
import org.kryptonmc.krypton.util.converters.helpers.ItemNameHelper
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.logger

object V102 {

    private val LOGGER = logger<V102>()
    private const val VERSION = MCVersions.V15W32A + 2

    fun register() {
        // V102 -> V15W32A + 2
        // V102 schema only modifies ITEM_STACK to have only a string ID, but our ITEM_NAME is generic (int or String) so we don't
        // actually need to update the walker

        MCTypeRegistry.ITEM_NAME.addConverter(VERSION) { data, _, _ ->
            if (data !is Number) return@addConverter null
            val id = data.toInt()
            ItemNameHelper.getNameFromId(id) ?: kotlin.run {
                LOGGER.warn("Unknown legacy integer id $id (V102)")
                ItemNameHelper.getNameFromId(0)
            }
        }

        MCTypeRegistry.ITEM_STACK.addStructureConverter(VERSION) { data, _, _ ->
            if (!data.hasKey("id", ObjectType.NUMBER)) return@addStructureConverter null
            val id = data.getInt("id")
            val remap = ItemNameHelper.getNameFromId(id) ?: kotlin.run {
                LOGGER.warn("Unknown legacy integer id $id (V102)")
                ItemNameHelper.getNameFromId(0)!!
            }
            data.setString("id", remap)
            null
        }
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:potion", VERSION) { data, _, _ ->
            val damage = data.getShort("Damage")
            if (damage != 0.toShort()) data.setShort("Damage", 0)
            val tag = data.getMap("tag") ?: NBTTypeUtil.createEmptyMap<String>().apply { data.setMap("tag", this) }
            if (!tag.hasKey("Potion", ObjectType.STRING)) {
                val converted = ItemNameHelper.getPotionNameFromId(damage)
                tag.setString("Potion", converted ?: "minecraft:water")
                if (damage.toInt() and 16384 == 16384) data.setString("id", "minecraft:splash_potion")
            }
            null
        }
    }
}
