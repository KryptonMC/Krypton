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
import org.kryptonmc.krypton.util.converters.RenameEntitiesConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V1928 {

    private const val VERSION = MCVersions.V19W04B + 1

    fun register() {
        RenameEntitiesConverter.register(VERSION, mapOf("minecraft:illager_beast" to "minecraft:ravager")::get)
        RenameItemsConverter.register(VERSION, mapOf("minecraft:illager_beast_spawn_egg" to "minecraft:ravager_spawn_egg")::get)
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:ravager", ItemListsDataWalker("ArmorItems", "HandItems"))
    }
}
