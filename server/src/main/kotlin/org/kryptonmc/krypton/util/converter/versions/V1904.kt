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
import org.kryptonmc.krypton.util.converter.walkers.ItemListsDataWalker

object V1904 {

    private const val VERSION = MCVersions.V18W43C + 1

    fun register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:ocelot", VERSION) { data, _, _ ->
            val catType = data.getInt("CatType")
            if (catType == 0) {
                val owner = data.getString("Owner")
                val ownerUUID = data.getString("OwnerUUID")
                if ((owner != null && owner.isNotEmpty()) || (ownerUUID != null && ownerUUID.isNotEmpty())) {
                    data.setBoolean("Trusting", true)
                }
            } else if (catType in 1..3) {
                data.setString("id", "minecraft:cat")
                data.setString("OwnerUUID", data.getString("OwnerUUID", "")!!)
            }
            null
        }
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:cat", ItemListsDataWalker("ArmorItems", "HandItems"))
    }
}
