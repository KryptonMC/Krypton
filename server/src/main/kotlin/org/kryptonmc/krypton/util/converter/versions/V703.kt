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
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker

object V703 {

    private const val VERSION = MCVersions.V1_10_2 + 191

    fun register() {
        MCTypeRegistry.ENTITY.addConverterForId("EntityHorse", VERSION) { data, _, _ ->
            val type = data.getInt("Type")
            data.remove("Type")
            data.setString("id", when (type) {
                0 -> "Horse"
                1 -> "Donkey"
                2 -> "Mule"
                3 -> "ZombieHorse"
                4 -> "SkeletonHorse"
                else -> "Horse"
            })
            null
        }

        MCTypeRegistry.ENTITY.addWalker(VERSION, "Horse", ItemsDataWalker("ArmorItem", "SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Horse", ItemListsDataWalker("ArmorItems", "HandItems"))

        MCTypeRegistry.ENTITY.addWalker(VERSION, "Donkey", ItemsDataWalker("SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Donkey", ItemListsDataWalker("Items", "ArmorItems", "HandItems"))

        MCTypeRegistry.ENTITY.addWalker(VERSION, "Mule", ItemsDataWalker("SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Mule", ItemListsDataWalker("Items", "ArmorItems", "HandItems"))

        MCTypeRegistry.ENTITY.addWalker(VERSION, "ZombieHorse", ItemsDataWalker("SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ZombieHorse", ItemListsDataWalker("ArmorItems", "HandItems"))

        MCTypeRegistry.ENTITY.addWalker(VERSION, "SkeletonHorse", ItemsDataWalker("SaddleItem"))
        MCTypeRegistry.ENTITY.addWalker(VERSION, "SkeletonHorse", ItemListsDataWalker("ArmorItems", "HandItems"))
    }
}
