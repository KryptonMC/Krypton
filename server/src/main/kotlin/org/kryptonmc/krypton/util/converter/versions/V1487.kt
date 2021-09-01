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
import org.kryptonmc.krypton.util.converters.RenameBlocksConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V1487 {

    private const val VERSION = MCVersions.V18W19B + 2

    fun register() {
        val remap = mapOf(
            "minecraft:prismarine_bricks_slab" to "minecraft:prismarine_brick_slab",
            "minecraft:prismarine_bricks_stairs" to "minecraft:prismarine_brick_stairs"
        )

        RenameItemsConverter.register(VERSION, remap::get)
        RenameBlocksConverter.register(VERSION, remap::get)
    }
}
