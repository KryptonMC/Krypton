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
package org.kryptonmc.krypton.adventure.network

import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.TextDecoration.State as DecorationState
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.OptionalBoolean
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.Optional

object AdventureCodecs {

    @JvmField
    val TEXT_COLOR: Codec<TextColor> = Codec.STRING.comapFlatMap({ TextColorSerialization.decodeResult(it) }, { TextColorSerialization.encode(it) })
    @JvmField
    val STYLE_FORMATTING: Codec<Style> = RecordCodecBuilder.create { instance ->
        instance.group(
            TEXT_COLOR.optionalFieldOf("color").getting { Optional.ofNullable(it.color()) },
            decorationStateCodec("bold").getting { it.decoration(TextDecoration.BOLD) },
            decorationStateCodec("italic").getting { it.decoration(TextDecoration.ITALIC) },
            decorationStateCodec("underlined").getting { it.decoration(TextDecoration.UNDERLINED) },
            decorationStateCodec("strikethrough").getting { it.decoration(TextDecoration.STRIKETHROUGH) },
            decorationStateCodec("obfuscated").getting { it.decoration(TextDecoration.OBFUSCATED) },
            Codec.STRING.optionalFieldOf("insertion").getting { Optional.ofNullable(it.insertion()) },
            Keys.CODEC.optionalFieldOf("font").getting { Optional.ofNullable(it.font()) }
        ).apply(instance) { color, bold, italic, underlined, strikethrough, obfuscated, insertion, font ->
            Style.style()
                .color(color.orElse(null))
                .decoration(TextDecoration.BOLD, bold)
                .decoration(TextDecoration.ITALIC, italic)
                .decoration(TextDecoration.UNDERLINED, underlined)
                .decoration(TextDecoration.STRIKETHROUGH, strikethrough)
                .decoration(TextDecoration.OBFUSCATED, obfuscated)
                .insertion(insertion.orElse(null))
                .font(font.orElse(null))
                .build()
        }
    }

    @JvmStatic
    private fun decorationStateCodec(name: String): MapCodec<DecorationState> = Codec.BOOLEAN.optionalFieldOf(name)
        .xmap({ it.map(DecorationState::byBoolean).orElse(DecorationState.NOT_SET) }, ::stateAsBoolean)

    @JvmStatic
    private fun stateAsBoolean(state: DecorationState): Optional<Boolean> = when (state) {
        DecorationState.NOT_SET -> Optional.empty()
        DecorationState.TRUE -> OptionalBoolean.TRUE
        DecorationState.FALSE -> OptionalBoolean.FALSE
    }
}
