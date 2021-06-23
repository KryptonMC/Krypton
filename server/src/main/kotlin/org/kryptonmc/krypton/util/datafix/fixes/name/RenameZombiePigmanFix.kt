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

class RenameZombiePigmanFix(outputSchema: Schema) : RenameEntityFix("RenameZombiePigmanFix", outputSchema, true) {

    override fun rename(name: String) = if (name == "minecraft:zombie_pigman") "minecraft:zombified_piglin" else name

    companion object {

        val REMAPPED_IDS = mapOf("minecraft:zombie_pigman_spawn_egg" to "minecraft:zombified_piglin_spawn_egg")
    }
}
