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
import org.kryptonmc.krypton.util.converters.RenameEntitiesConverter
import org.kryptonmc.krypton.util.converters.RenameItemsConverter

object V1486 {

    private const val VERSION = MCVersions.V18W19B + 1
    val RENAMED_ENTITY_IDS = mapOf(
        "minecraft:salmon_mob" to "minecraft:salmon",
        "minecraft:cod_mob" to "minecraft:cod"
    )
    val RENAMED_ITEM_IDS = mapOf(
        "minecraft:salmon_mob_spawn_egg" to "minecraft:salmon_spawn_egg",
        "minecraft:cod_mob_spawn_egg" to "minecraft:cod_spawn_egg"
    )

    fun register() {
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:cod_mob", "minecraft:cod")
        MCTypeRegistry.ENTITY.copyWalkers(VERSION, "minecraft:salmon_mob", "minecraft:salmon")

        RenameEntitiesConverter.register(VERSION, RENAMED_ENTITY_IDS::get)
        RenameItemsConverter.register(VERSION, RENAMED_ITEM_IDS::get)
    }
}
