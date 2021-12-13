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

/**
 * Parses entity selectors.
 */
object EntityArgumentParser {

    const val SELECTOR_CHAR = '@'
    private const val OPENING_BRACKET = '['
    private const val CLOSING_BRACKET = ']'
    private const val SELECTOR_SEPARATOR = ','
    private const val EXCLUSION = '!'
    private const val KEY_VALUE_SEPARATOR = '='

    @JvmStatic
    fun parse(
        reader: StringReader,
        operation: Char,
        position: Int,
        onlyPlayers: Boolean,
        singleTarget: Boolean,
    ): EntityQuery = when (operation) {
        EntityQuery.Selector.NEAREST_PLAYER_CHAR -> EntityQuery(emptyList(), EntityQuery.Selector.NEAREST_PLAYER)
        EntityQuery.Selector.ALL_ENTITIES_CHAR -> {
            if (singleTarget) {
                reader.cursor = 0
                throw EntityArgumentExceptions.TOO_MANY_ENTITIES.createWithContext(reader)
            } else if (onlyPlayers) {
                reader.cursor = 0
                throw EntityArgumentExceptions.ONLY_FOR_PLAYERS.createWithContext(reader)
            }

            // If we have a [, we know that we have filters to parse
            if (reader.canRead() && reader.peek() == OPENING_BRACKET) {
                reader.skip()
                EntityQuery(parseArguments(reader), EntityQuery.Selector.ALL_ENTITIES)
            } else {
                EntityQuery(emptyList(), EntityQuery.Selector.ALL_ENTITIES)
            }
        }
        EntityQuery.Selector.RANDOM_PLAYER_CHAR -> EntityQuery(emptyList(), EntityQuery.Selector.RANDOM_PLAYER)
        EntityQuery.Selector.ALL_PLAYERS_CHAR -> {
            if (singleTarget) {
                reader.cursor = 0
                throw EntityArgumentExceptions.TOO_MANY_PLAYERS.createWithContext(reader)
            }

            // If we have a [, we know that we have filters to parse
            if (reader.canRead() && reader.peek() == OPENING_BRACKET) {
                reader.skip()
                EntityQuery(parseArguments(reader), EntityQuery.Selector.ALL_PLAYERS)
            } else {
                EntityQuery(emptyList(), EntityQuery.Selector.ALL_PLAYERS)
            }
        }
        EntityQuery.Selector.EXECUTOR_CHAR -> EntityQuery(emptyList(), EntityQuery.Selector.EXECUTOR)
        else -> {
            reader.cursor = position
            throw EntityArgumentExceptions.UNKNOWN_SELECTOR.createWithContext(reader, "$SELECTOR_CHAR$operation")
        }
    }

    @JvmStatic
    private fun parseArguments(reader: StringReader): List<EntityArgument.EntityArg> {
        reader.skipWhitespace()
        val args = mutableListOf<EntityArgument.EntityArg>()
        while (reader.canRead() && reader.peek() != CLOSING_BRACKET) {
            reader.skipWhitespace()
            val position = reader.cursor
            val option = reader.readString()
            if (!EntityArguments.VALID.contains(option)) throw EntityArgumentExceptions.INVALID_OPTION.createWithContext(reader, option)

            reader.skipWhitespace()
            if (reader.canRead() && reader.peek() == KEY_VALUE_SEPARATOR) {
                reader.skip()
                reader.skipWhitespace()

                val exclude = reader.peek() == EXCLUSION
                if (exclude) reader.skip()

                val value = reader.readString()
                args.add(EntityArgument.EntityArg(option, value, exclude))

                reader.skipWhitespace()
                if (!reader.canRead()) continue

                if (reader.peek() == SELECTOR_SEPARATOR) {
                    reader.skip()
                    continue
                }

                if (reader.peek() != CLOSING_BRACKET) throw EntityArgumentExceptions.UNTERMINATED.createWithContext(reader)
                break
            }

            reader.cursor = position
            throw EntityArgumentExceptions.VALUELESS.createWithContext(reader, option)
        }
        if (reader.canRead()) reader.skip() else throw EntityArgumentExceptions.UNTERMINATED.createWithContext(reader)
        return args.toList()
    }
}
