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
package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.StringArgumentType
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeEnum

/**
 * String argument types only serialize the type of string argument they are.
 * The possible values are:
 * * SINGLE_WORD - reads until it finds a space
 * * QUOTABLE_PHRASE - if the string starts with ", keeps reading until another
 *   " appears closing it off (can escape with \), otherwise behaves the same
 *   as SINGLE_WORD
 * * GREEDY_STRING - reads until there is nothing more to be read
 *
 * See [here](https://wiki.vg/Command_Data#brigadier:string)
 */
object StringArgumentSerializer : ArgumentSerializer<StringArgumentType> {

    private const val SINGLE_WORD_TYPE = 0
    private const val QUOTABLE_PHRASE_TYPE = 1
    private const val GREEDY_PHRASE_TYPE = 2

    override fun read(buf: ByteBuf): StringArgumentType = when (val type = buf.readVarInt()) {
        SINGLE_WORD_TYPE -> StringArgumentType.word()
        QUOTABLE_PHRASE_TYPE -> StringArgumentType.string()
        GREEDY_PHRASE_TYPE -> StringArgumentType.greedyString()
        else -> throw IllegalArgumentException("Cannot get type with ID $type!")
    }

    override fun write(buf: ByteBuf, value: StringArgumentType) {
        buf.writeEnum(value.type)
    }
}
