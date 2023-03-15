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
package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.StringArgumentType
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter

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

    override fun read(reader: BinaryReader): StringArgumentType = when (val type = reader.readVarInt()) {
        SINGLE_WORD_TYPE -> StringArgumentType.word()
        QUOTABLE_PHRASE_TYPE -> StringArgumentType.string()
        GREEDY_PHRASE_TYPE -> StringArgumentType.greedyString()
        else -> throw IllegalArgumentException("Cannot get type with ID $type!")
    }

    override fun write(writer: BinaryWriter, value: StringArgumentType) {
        writer.writeEnum(value.type)
    }
}
