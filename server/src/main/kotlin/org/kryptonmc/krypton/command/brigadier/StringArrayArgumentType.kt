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
