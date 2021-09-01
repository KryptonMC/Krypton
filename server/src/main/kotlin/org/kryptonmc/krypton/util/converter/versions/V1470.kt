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
import org.kryptonmc.krypton.util.converter.walkers.TypePathsDataWalker

object V1470 {

    private const val VERSION = MCVersions.V18W08A

    fun register() {
        registerMob("minecraft:turtle")
        registerMob("minecraft:cod_mob")
        registerMob("minecraft:tropical_fish")
        registerMob("minecraft:salmon_mob")
        registerMob("minecraft:puffer_fish")
        registerMob("minecraft:phantom")
        registerMob("minecraft:dolphin")
        registerMob("minecraft:drowned")

        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:trident", TypePathsDataWalker(MCTypeRegistry.BLOCK_STATE, arrayOf("inBlockState")))
    }

    private fun registerMob(id: String) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, ItemListsDataWalker("ArmorItems", "HandItems"))
    }
}
