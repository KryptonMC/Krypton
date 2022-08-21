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

import kotlinx.collections.immutable.persistentListOf
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.krypton.adventure.AdventureCodecs
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class ChatTypeDecoration(private val translationKey: String, val parameters: List<Parameter>, val style: Style) : Translatable {

    override fun translationKey(): String = translationKey

    enum class Parameter {

        SENDER,
        TARGET,
        CONTENT;

        companion object {

            @JvmField
            val CODEC: Codec<Parameter> = EnumCodecs.of(Parameter::values)
        }
    }

    companion object {

        @JvmField
        val CODEC: Codec<ChatTypeDecoration> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("translation_key").getting { it.translationKey() },
                Parameter.CODEC.listOf().fieldOf("parameters").getting(ChatTypeDecoration::parameters),
                AdventureCodecs.STYLE_FORMATTING.optionalFieldOf("style", Style.empty()).getting(ChatTypeDecoration::style)
            ).apply(instance, ::ChatTypeDecoration)
        }

        @JvmStatic
        fun withSender(key: String): ChatTypeDecoration =
            ChatTypeDecoration(key, persistentListOf(Parameter.SENDER, Parameter.CONTENT), Style.empty())

        @JvmStatic
        fun incomingDirectMessage(key: String): ChatTypeDecoration {
            val style = Style.style().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC).build()
            return ChatTypeDecoration(key, persistentListOf(Parameter.SENDER, Parameter.CONTENT), style)
        }

        @JvmStatic
        fun outgoingDirectMessage(key: String): ChatTypeDecoration {
            val style = Style.style().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC).build()
            return ChatTypeDecoration(key, persistentListOf(Parameter.TARGET, Parameter.CONTENT), style)
        }

        @JvmStatic
        fun teamMessage(key: String): ChatTypeDecoration =
            ChatTypeDecoration(key, persistentListOf(Parameter.TARGET, Parameter.SENDER, Parameter.CONTENT), Style.empty())
    }
}
