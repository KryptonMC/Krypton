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
import com.google.gson.Gson
import com.google.gson.JsonParseException
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V101 {

    private const val VERSION = MCVersions.V15W32A + 1
    val GSON: Gson = GsonComponentSerializer.gson().serializer().newBuilder().setLenient().create()

    fun register() = MCTypeRegistry.TILE_ENTITY.addConverterForId("Sign", VERSION) { data, _, _ ->
        data.updateLine("Text1")
        data.updateLine("Text2")
        data.updateLine("Text3")
        data.updateLine("Text4")
        null
    }

    private fun MapType<String>.updateLine(path: String) {
        val textString = getString(path)
        if (textString.isNullOrEmpty() || textString == "null") {
            setString(path, GsonComponentSerializer.gson().serialize(Component.empty()))
            return
        }

        val component = if (textString != "null" && textString.isNotEmpty()) {
            if (textString.first() == '"' && textString.last() == '"' || textString.first() == '{' && textString.last() == '}') {
                try {
                    GSON.fromJson<Component>(textString)
                } catch (exception: JsonParseException) {
                    Component.empty()
                }
            } else {
                Component.empty()
            }
        } else {
            Component.empty()
        }
        setString(path, GSON.toJson(component))
    }
}
