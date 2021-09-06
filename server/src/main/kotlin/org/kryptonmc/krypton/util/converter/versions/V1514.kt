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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.world.scoreboard.RenderType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V1514 {

    private const val VERSION = MCVersions.V1_13_PRE7 + 1

    fun register() {
        MCTypeRegistry.OBJECTIVE.addStructureConverter(VERSION) { data, _, _ -> updateDisplayName(data) }
        MCTypeRegistry.TEAM.addStructureConverter(VERSION) { data, _, _ -> updateDisplayName(data) }
        MCTypeRegistry.OBJECTIVE.addStructureConverter(VERSION) { data, _, _ ->
            data.getString("RenderType")?.let { return@addStructureConverter null }
            val criteriaName = data.getString("CriteriaName", "")!!
            data.setString(
                "RenderType",
                if (criteriaName == "health") RenderType.HEARTS.serialized else RenderType.INTEGER.serialized
            )
            null
        }
    }

    private fun updateDisplayName(data: MapType<String>): MapType<String>? {
        val displayName = data.getString("DisplayName") ?: return null
        val update = GsonComponentSerializer.gson().serialize(Component.text(displayName))
        data.setString("DisplayName", update)
        return null
    }
}
