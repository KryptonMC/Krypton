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

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.kryptonmc.krypton.util.serialization.Codec
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.serialization.CompoundCodec
import org.kryptonmc.krypton.util.serialization.decode
import org.kryptonmc.krypton.util.serialization.encode
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound

object AdventureCodecs {

    @JvmField
    val STYLE_FORMATTING: CompoundCodec<Style> = CompoundCodec.of(
        {
            compound {
                encode(TEXT_COLOR, "color", it.color())
                decoration("bold", it.decoration(TextDecoration.BOLD))
                decoration("italic", it.decoration(TextDecoration.ITALIC))
                decoration("underlined", it.decoration(TextDecoration.UNDERLINED))
                decoration("strikethrough", it.decoration(TextDecoration.STRIKETHROUGH))
                decoration("obfuscated", it.decoration(TextDecoration.OBFUSCATED))
                encode(Codecs.STRING, "insertion", it.insertion())
                encode(Codecs.KEY, "font", it.font())
            }
        },
        {
            Style.style()
                .color(it.decode(TEXT_COLOR, "color"))
                .decoration(TextDecoration.BOLD, it.getDecorationState("bold"))
                .decoration(TextDecoration.BOLD, it.getDecorationState("italic"))
                .decoration(TextDecoration.BOLD, it.getDecorationState("underlined"))
                .decoration(TextDecoration.BOLD, it.getDecorationState("strikethrough"))
                .decoration(TextDecoration.BOLD, it.getDecorationState("obfuscated"))
                .insertion(it.decode(Codecs.STRING, "insertion"))
                .font(it.decode(Codecs.KEY, "font"))
                .build()
        }
    )
    @JvmField
    val TEXT_COLOR: Codec<StringTag, TextColor> = Codecs.STRING.transform(TextColor::serialize) {
        val value = if (it.startsWith("#")) TextColor.fromHexString(it) else NamedTextColor.NAMES.value(it)
        requireNotNull(value) { "No text colour could be found for string $it!" }
    }
}

private fun CompoundTag.getDecorationState(name: String): TextDecoration.State {
    if (!contains(name, ByteTag.ID)) return TextDecoration.State.NOT_SET
    return TextDecoration.State.byBoolean(getBoolean(name))
}

private fun CompoundTag.Builder.decoration(name: String, state: TextDecoration.State) {
    if (state == TextDecoration.State.NOT_SET) return
    boolean(name, state == TextDecoration.State.TRUE)
}
