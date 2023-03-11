/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.network.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.krypton.adventure.network.AdventureCodecs
import org.kryptonmc.krypton.util.ImmutableLists
import org.kryptonmc.krypton.util.serialization.EnumCodecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class ChatTypeDecoration(private val translationKey: String, val parameters: List<Parameter>, val style: Style) : Translatable {

    override fun translationKey(): String = translationKey

    fun decorate(message: Component, type: RichChatType.Bound): Component =
        Component.translatable(translationKey, style, resolveParameters(message, type))

    private fun resolveParameters(message: Component, type: RichChatType.Bound): List<Component> =
        Array(parameters.size) { parameters.get(it).select(message, type) }.asList()

    enum class Parameter(private val selector: Selector) {

        SENDER({ _, type -> type.name }),
        TARGET({ _, type -> type.targetName }),
        CONTENT({ message, _ -> message });

        fun select(message: Component, type: RichChatType.Bound): Component = selector.select(message, type) ?: Component.empty()

        private fun interface Selector {

            fun select(message: Component, type: RichChatType.Bound): Component?
        }

        companion object {

            @JvmField
            val CODEC: Codec<Parameter> = EnumCodecs.of { Parameter.values() }
        }
    }

    companion object {

        @JvmField
        val CODEC: Codec<ChatTypeDecoration> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("translation_key").getting { it.translationKey() },
                Parameter.CODEC.listOf().fieldOf("parameters").getting { it.parameters },
                AdventureCodecs.STYLE_FORMATTING.optionalFieldOf("style", Style.empty()).getting { it.style }
            ).apply(instance, ::ChatTypeDecoration)
        }

        @JvmStatic
        fun withSender(key: String): ChatTypeDecoration =
            ChatTypeDecoration(key, ImmutableLists.of(Parameter.SENDER, Parameter.CONTENT), Style.empty())

        @JvmStatic
        fun incomingDirectMessage(key: String): ChatTypeDecoration {
            val style = Style.style().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC).build()
            return ChatTypeDecoration(key, ImmutableLists.of(Parameter.SENDER, Parameter.CONTENT), style)
        }

        @JvmStatic
        fun outgoingDirectMessage(key: String): ChatTypeDecoration {
            val style = Style.style().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC).build()
            return ChatTypeDecoration(key, ImmutableLists.of(Parameter.TARGET, Parameter.CONTENT), style)
        }

        @JvmStatic
        fun teamMessage(key: String): ChatTypeDecoration =
            ChatTypeDecoration(key, ImmutableLists.of(Parameter.TARGET, Parameter.SENDER, Parameter.CONTENT), Style.empty())
    }
}
