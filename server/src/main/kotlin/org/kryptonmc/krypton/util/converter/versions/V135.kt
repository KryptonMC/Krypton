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
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList

object V135 {

    private const val VERSION = MCVersions.V15W40B + 1

    fun register() {
        MCTypeRegistry.ENTITY.addStructureConverter(VERSION) { data, _, _ ->
            var temp = data
            var ret: MapType<String>? = null

            while (temp.hasKey("Riding", ObjectType.MAP)) {
                val riding = temp.getMap<String>("Riding")!!
                temp.remove("Riding")

                val passengers = NBTTypeUtil.createEmptyList()
                riding.setList("Passengers", passengers)
                passengers.addMap(temp)

                temp = riding
                ret = temp
            }
            ret
        }

        MCTypeRegistry.PLAYER.addStructureWalker(VERSION, ItemListsDataWalker("Inventory", "EnderItems"))
        MCTypeRegistry.PLAYER.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.getMap<String>("RootVehicle")?.convert(MCTypeRegistry.ENTITY, "Entity", fromVersion, toVersion)
            null
        }

        MCTypeRegistry.ENTITY.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.convertList(MCTypeRegistry.ENTITY, "Passengers", fromVersion, toVersion)
            null
        }
    }
}
