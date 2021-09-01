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
import org.kryptonmc.krypton.util.converter.walkers.convert
import org.kryptonmc.krypton.util.converter.walkers.convertList

object V1022 {

    private const val VERSION = MCVersions.V17W06A

    fun register() {
        MCTypeRegistry.PLAYER.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.getMap<String>("RootVehicle")?.convert(MCTypeRegistry.ENTITY, "Entity", fromVersion, toVersion)

            data.convertList(MCTypeRegistry.ITEM_STACK, "Inventory", fromVersion, toVersion)
            data.convertList(MCTypeRegistry.ITEM_NAME, "EnderItems", fromVersion, toVersion)

            data.convert(MCTypeRegistry.ENTITY, "ShoulderEntityLeft", fromVersion, toVersion)
            data.convert(MCTypeRegistry.ENTITY, "ShoulderEntityRight", fromVersion, toVersion)

            data.getMap<String>("recipeBook")?.let {
                it.convert(MCTypeRegistry.RECIPE, "recipes", fromVersion, toVersion)
                it.convert(MCTypeRegistry.RECIPE, "toBeDisplayed", fromVersion, toVersion)
            }
            null
        }

        MCTypeRegistry.HOTBAR.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            data.keys().forEach { data.convertList(MCTypeRegistry.ITEM_STACK, it, fromVersion, toVersion) }
            null
        }
    }
}
