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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import org.kryptonmc.krypton.util.Keys

/**
 * Some common utilities for StringReader.
 */
object StringReading {

    @JvmStatic
    fun readKeyString(reader: StringReader): String = readStringUntil(reader) { Keys.isValidCharacter(it) }

    @JvmStatic
    fun readNonSpaceString(reader: StringReader): String = readStringUntil(reader) { it != CommandDispatcher.ARGUMENT_SEPARATOR_CHAR }

    @JvmStatic
    private inline fun readStringUntil(reader: StringReader, condition: (Char) -> Boolean): String {
        val startPosition = reader.cursor
        while (reader.canRead() && condition(reader.peek())) {
            reader.skip()
        }
        return reader.string.substring(startPosition, reader.cursor)
    }

    @JvmStatic
    fun readOptionalDouble(reader: StringReader): Double {
        if (!reader.canRead() || reader.peek() == CommandDispatcher.ARGUMENT_SEPARATOR_CHAR) return 0.0
        return reader.readDouble()
    }
}
