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
package org.kryptonmc.krypton.command.brigadier

import com.google.common.base.Splitter
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType

object StringArrayArgumentType : ArgumentType<Array<String>> {

    @JvmField
    val EMPTY: Array<String> = emptyArray()
    private val WORD_SPLITTER = Splitter.on(CommandDispatcher.ARGUMENT_SEPARATOR_CHAR)
    private val EXAMPLES = listOf("word", "some words")

    override fun parse(reader: StringReader): Array<String> {
        val text = reader.remaining
        reader.cursor = reader.totalLength
        if (text.isEmpty()) return EMPTY
        return WORD_SPLITTER.splitToList(text).toTypedArray()
    }

    override fun getExamples(): Collection<String> = EXAMPLES

    override fun toString(): String = "stringArray()"
}
