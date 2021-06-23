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
package org.kryptonmc.krypton.util.datafix.fixes.name

import com.mojang.datafixers.schemas.Schema

class RenameCodSalmonFix(outputSchema: Schema, changesType: Boolean) : RenameEntityFix("RenameSalmonFix", outputSchema, changesType) {

    override fun rename(name: String) = RENAMED_IDS.getOrDefault(name, name)

    companion object {

        val RENAMED_IDS = mapOf(
            "minecraft:salmon_mob" to "minecraft:salmon",
            "minecraft:cod_mob" to "minecraft:cod"
        )
        val RENAMED_EGG_IDS = mapOf(
            "minecraft:salmon_mob_spawn_egg" to "minecraft:salmon_spawn_egg",
            "minecraft:cod_mob_spawn_egg" to "minecraft:cod_spawn_egg"
        )
    }
}
