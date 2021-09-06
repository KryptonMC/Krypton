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
import com.google.gson.JsonParseException
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V165 {

    private const val VERSION = MCVersions.V1_9_PRE2

    fun register() = MCTypeRegistry.ITEM_STACK.addStructureConverter(VERSION) { data, _, _ ->
        val tag = data.getMap<String>("tag") ?: return@addStructureConverter null
        val pages = tag.getList("pages", ObjectType.STRING) ?: return@addStructureConverter null

        for (i in 0 until pages.size()) {
            val page = pages.getString(i)
            val component = if (page != "null" && page.isNotEmpty()) {
                if (page.isSurroundedBy('"') || page.isSurroundedBy('{', '}')) {
                    try {
                        V101.GSON.fromJson<Component>(page)
                    } catch (exception: JsonParseException) {
                        Component.empty()
                    }
                } else {
                    Component.empty()
                }
            } else {
                Component.empty()
            }
            pages.setString(i, GsonComponentSerializer.gson().serialize(component))
        }
        null
    }

    private fun String.isSurroundedBy(
        start: Char,
        end: Char = start
    ): Boolean = first() == start && last() == end
}
