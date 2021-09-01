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
package org.kryptonmc.krypton.util.converters

import org.kryptonmc.krypton.util.converters.helpers.RenameStringValueTypeHelper
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import kotlin.math.min

object RenameBlocksConverter {

    fun register(version: Int, renamer: (String) -> String?) = register(version, 0, renamer)

    fun register(version: Int, subVersion: Int = 0, renamer: (String) -> String?) {
        RenameStringValueTypeHelper.register(version, subVersion, MCTypeRegistry.BLOCK_NAME, renamer)
        MCTypeRegistry.BLOCK_STATE.addStructureConverter(version, subVersion) { data, _, _ ->
            val name = data.getString("Name") ?: return@addStructureConverter null
            val converted = renamer(name)
            if (converted != null) data.setString("Name", converted)
            null
        }
    }

    fun registerAndFixJigsaw(version: Int, renamer: (String) -> String?) = registerAndFixJigsaw(version, 0, renamer)

    fun registerAndFixJigsaw(version: Int, subVersion: Int, renamer: (String) -> String?) {
        register(version, subVersion, renamer)
        // TODO: Check on update. "minecraft:jigsaw" could change.
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:jigsaw", version, subVersion) { data, _, _ ->
            val finalState = data.getString("final_state")
            if (finalState.isNullOrEmpty()) return@addConverterForId null

            val nbtStart1 = finalState.indexOf('[')
            val nbtStart2 = finalState.indexOf('{')
            var stateNameEnd = finalState.length
            if (nbtStart1 > 0) stateNameEnd = min(stateNameEnd, nbtStart1)
            if (nbtStart2 > 0) stateNameEnd = min(stateNameEnd, nbtStart2)

            val blockStateName = finalState.substring(0, stateNameEnd)
            val converted = renamer(blockStateName) ?: return@addConverterForId null

            val convertedState = converted + finalState.substring(stateNameEnd)
            data.setString("final_state", convertedState)
            null
        }
    }
}
