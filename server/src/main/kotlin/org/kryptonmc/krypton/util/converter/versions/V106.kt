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
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.convert

object V106 {

    private const val VERSION = MCVersions.V15W32C + 2

    fun register() {
        MCTypeRegistry.UNTAGGED_SPAWNER.addStructureConverter(VERSION) { data, _, _ ->
            val entityId = data.getString("EntityId")
            if (entityId != null) {
                data.remove("EntityId")
                val spawnData = data.getMap("SpawnData")
                    ?: NBTTypeUtil.createEmptyMap<String>().apply { data.setMap("SpawnData", this) }
                spawnData.setString("id", entityId.ifEmpty { "Pig" })
            }

            val spawnPotentials = data.getList("SpawnPotentials", ObjectType.MAP)
            if (spawnPotentials != null) {
                for (i in 0 until spawnPotentials.size()) {
                    val spawn = spawnPotentials.getMap<String>(i)
                    val spawnType = spawn.getString("Type") ?: continue
                    spawn.remove("Type")

                    var properties = spawn.getMap<String>("Properties")
                    if (properties == null) properties = NBTTypeUtil.createEmptyMap() else spawn.remove("Properties")

                    properties.setString("id", spawnType)
                    spawn.setMap("Entity", properties)
                }
            }
            null
        }

        MCTypeRegistry.UNTAGGED_SPAWNER.addStructureWalker(VERSION) { data, fromVersion, toVersion ->
            val spawnPotentials = data.getList("SpawnPotentials", ObjectType.MAP)
            if (spawnPotentials != null) {
                for (i in 0 until spawnPotentials.size()) {
                    val spawnPotential = spawnPotentials.getMap<String>(i)
                    spawnPotential.convert(MCTypeRegistry.ENTITY, "Entity", fromVersion, toVersion)
                }
            }

            data.convert(MCTypeRegistry.ENTITY, "SpawnData", fromVersion, toVersion)
            null
        }
    }
}
