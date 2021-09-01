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
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil

object V806 {

    private const val VERSION = MCVersions.V16W36A + 1

    fun register() {
        val potionWaterUpdater = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val tag = data.getMap("tag") ?: NBTTypeUtil.createEmptyMap<String>().apply { data.setMap("tag", this) }
                if (!tag.hasKey("Potion", ObjectType.STRING)) tag.setString("Potion", "minecraft:water")
                return null
            }
        }

        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:potion", potionWaterUpdater)
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:splash_potion", potionWaterUpdater)
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:lingering_potion", potionWaterUpdater)
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:tipped_arrow", potionWaterUpdater)
    }
}
