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

import ca.spottedleaf.dataconverter.types.MapType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converters.RenameOptionsConverter

object V2558 {

    private const val VERSION = MCVersions.V1_16_PRE2 + 1

    fun register() {
        RenameOptionsConverter.register(VERSION, mapOf("key_key.swapHands" to "key_key.swapOffhand")::get)
        MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureConverter(VERSION) { data, _, _ ->
            val dimensions = data.getMap("dimensions")
                ?: NBTTypeUtil.createEmptyMap<String>().apply { data.setMap("dimensions", this) }
            if (dimensions.isEmpty) data.setMap("dimensions", data.recreateSettings())
            null
        }
    }

    private fun MapType<String>.recreateSettings(): MapType<String> {
        val seed = getLong("seed")
        return V2550.vanillaLevels(seed, V2550.defaultOverworld(seed), false)
    }
}
