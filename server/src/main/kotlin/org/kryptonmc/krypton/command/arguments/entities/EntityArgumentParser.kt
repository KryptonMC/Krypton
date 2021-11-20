/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.command.arguments.entities

import com.mojang.brigadier.StringReader

object EntityArgumentParser {

    @JvmStatic
    fun parse(
        reader: StringReader,
        operation: Char,
        position: Int,
        onlyPlayers: Boolean,
        singleTarget: Boolean,
    ): EntityQuery = when (operation) {
        'p' -> EntityQuery(emptyList(), EntityQuery.Selector.NEAREST_PLAYER)
        'e' -> {
            if (singleTarget) {
                reader.cursor = 0
                throw EntityArgumentExceptions.TOO_MANY_ENTITIES.createWithContext(reader)
            } else if (onlyPlayers) {
                reader.cursor = 0
                throw EntityArgumentExceptions.ONLY_FOR_PLAYERS.createWithContext(reader)
            }

            if (reader.canRead() && reader.peek() == '[') {
                reader.skip()
                EntityQuery(parseArguments(reader), EntityQuery.Selector.ALL_ENTITIES)
            } else {
                EntityQuery(emptyList(), EntityQuery.Selector.ALL_ENTITIES)
            }
        }
        'r' -> EntityQuery(emptyList(), EntityQuery.Selector.RANDOM_PLAYER)
        'a' -> {
            if (singleTarget) {
                reader.cursor = 0
                throw EntityArgumentExceptions.TOO_MANY_PLAYERS.createWithContext(reader)
            }

            if (reader.canRead() && reader.peek() == '[') {
                reader.skip()
                EntityQuery(parseArguments(reader), EntityQuery.Selector.ALL_PLAYERS)
            } else {
                EntityQuery(emptyList(), EntityQuery.Selector.ALL_PLAYERS)
            }
        }
        's' -> EntityQuery(emptyList(), EntityQuery.Selector.EXECUTOR)
        else -> {
            reader.cursor = position
            throw EntityArgumentExceptions.UNKNOWN_SELECTOR.createWithContext(reader, "@$operation")
        }
    }

    @JvmStatic
    private fun parseArguments(reader: StringReader): List<EntityArgument.EntityArg> {
        reader.skipWhitespace()
        val args = mutableListOf<EntityArgument.EntityArg>()
        while (reader.canRead() && reader.peek() != ']') {
            reader.skipWhitespace()
            val position = reader.cursor
            val option = reader.readString()
            if (option !in EntityArguments.ARGUMENTS) {
                throw EntityArgumentExceptions.INVALID_OPTION.createWithContext(reader, option)
            }

            reader.skipWhitespace()
            if (reader.canRead() && reader.peek() == '=') {
                reader.skip()
                reader.skipWhitespace()

                val exclude = reader.peek() == '!'
                if (exclude) reader.skip()

                val value = reader.readString()
                args.add(EntityArgument.EntityArg(option, value, exclude))

                reader.skipWhitespace()
                if (!reader.canRead()) continue

                if (reader.peek() == ',') {
                    reader.skip()
                    continue
                }

                if (reader.peek() != ']') throw EntityArgumentExceptions.UNTERMINATED.createWithContext(reader)
                break
            }

            reader.cursor = position
            throw EntityArgumentExceptions.VALUELESS.createWithContext(reader, option)
        }
        if (reader.canRead()) reader.skip() else throw EntityArgumentExceptions.UNTERMINATED.createWithContext(reader)
        return args.toList()
    }
}
