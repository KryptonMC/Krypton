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

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converters.helpers.SpawnEggNamingHelper
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil

object V105 {

    private const val VERSION = MCVersions.V15W32C + 1

    fun register() = MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:spawn_egg", VERSION) { data, _, _ ->
        val tag = data.getMap<String>("tag") ?: NBTTypeUtil.createEmptyMap()
        val damage = data.getShort("Damage")
        if (damage != 0.toShort()) data.setShort("Damage", 0)
        val entityTag = tag.getMap<String>("EntityTag") ?: NBTTypeUtil.createEmptyMap()

        if (!entityTag.hasKey("id", ObjectType.STRING)) {
            val converted = SpawnEggNamingHelper.getSpawnNameFromId(damage)
            if (converted != null) {
                entityTag.setString("id", converted)
                tag.setMap("EntityTag", entityTag)
                data.setMap("tag", tag)
            }
        }
        null
    }
}
