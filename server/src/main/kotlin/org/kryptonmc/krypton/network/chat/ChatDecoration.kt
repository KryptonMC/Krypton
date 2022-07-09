/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.network.chat

import kotlinx.collections.immutable.persistentSetOf
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.krypton.adventure.AdventureCodecs
import org.kryptonmc.krypton.util.serialization.CompoundEncoder
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.compound

@JvmRecord
data class ChatDecoration(private val translationKey: String, val parameters: Set<Parameter>, val style: Style) : Translatable {

    override fun translationKey(): String = translationKey

    enum class Parameter {

        SENDER,
        TEAM_NAME,
        CONTENT
    }

    companion object {

        @JvmField
        val ENCODER: CompoundEncoder<ChatDecoration> = CompoundEncoder {
            compound {
                string("translationKey", it.translationKey())
                list("parameters") { it.parameters.forEach { addString(it.name.lowercase()) } }
                encode(AdventureCodecs.STYLE_FORMATTING, "style", it.style)
            }
        }

        @JvmStatic
        fun withSender(key: String): ChatDecoration = ChatDecoration(key, persistentSetOf(Parameter.SENDER, Parameter.CONTENT), Style.empty())

        @JvmStatic
        fun directMessage(key: String): ChatDecoration =
            ChatDecoration(key, persistentSetOf(Parameter.SENDER, Parameter.CONTENT), Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC))

        @JvmStatic
        fun teamMessage(key: String): ChatDecoration =
            ChatDecoration(key, persistentSetOf(Parameter.SENDER, Parameter.TEAM_NAME, Parameter.CONTENT), Style.empty())
    }
}
