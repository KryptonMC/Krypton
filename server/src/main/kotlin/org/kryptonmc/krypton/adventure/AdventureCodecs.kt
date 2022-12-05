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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.gettingNullable
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
            TEXT_COLOR.optionalFieldOf("color").gettingNullable { it.color() },
            decorationStateCodec("bold").getting { it.decoration(TextDecoration.BOLD) },
            decorationStateCodec("italic").getting { it.decoration(TextDecoration.ITALIC) },
            decorationStateCodec("underlined").getting { it.decoration(TextDecoration.UNDERLINED) },
            decorationStateCodec("strikethrough").getting { it.decoration(TextDecoration.STRIKETHROUGH) },
            decorationStateCodec("obfuscated").getting { it.decoration(TextDecoration.OBFUSCATED) },
            Codec.STRING.optionalFieldOf("insertion").gettingNullable { it.insertion() },
            Codecs.KEY.optionalFieldOf("font").gettingNullable { it.font() }
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
    private fun decorationStateCodec(name: String): MapCodec<TextDecoration.State> = Codec.BOOLEAN.optionalFieldOf(name)
        .xmap({ value -> value.map { TextDecoration.State.byBoolean(it) }.orElse(TextDecoration.State.NOT_SET) }, { it.asBoolean() })
}

private fun TextDecoration.State.asBoolean(): Optional<Boolean> = when (this) {
    TextDecoration.State.NOT_SET -> Optional.empty()
    TextDecoration.State.FALSE -> Optional.of(false)
    TextDecoration.State.TRUE -> Optional.of(true)
}
