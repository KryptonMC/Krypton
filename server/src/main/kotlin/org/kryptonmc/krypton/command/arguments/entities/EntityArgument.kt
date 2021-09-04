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
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import org.kryptonmc.api.command.Sender
import org.kryptonmc.krypton.command.argument.argument

class EntityArgument private constructor(
    val onlyPlayers: Boolean,
    val singleTarget: Boolean,
) : ArgumentType<EntityQuery> {

    override fun parse(reader: StringReader): EntityQuery {
        if (reader.canRead() && reader.peek() == '@') {
            reader.skip()
            if (!reader.canRead()) throw EntityArgumentExceptions.MISSING_SELECTOR.createWithContext(reader)
            val position = reader.cursor
            return EntityArgumentParser.parse(reader, reader.read(), position, onlyPlayers, singleTarget)
        }
        val input = reader.readString()
        if (input matches PLAYER_NAME_REGEX) return EntityQuery(emptyList(), EntityQuery.Selector.PLAYER, input)
        return EntityQuery(emptyList(), EntityQuery.Selector.UNKNOWN)
    }

    override fun getExamples() = EXAMPLES

    companion object {

        private val EXAMPLES = listOf("Player1", "Player2", "@a", "@e", "@r", "@a[gamemode=adventure]")
        private val PLAYER_NAME_REGEX = Regex("[a-zA-Z0-9_]{1,16}")

        /**
         * @return An argument which can only accept one player
         */
        fun player() = EntityArgument(true, true)

        /**
         * @return An argument which can only accept players
         */
        fun players() = EntityArgument(true, false)

        /**
         * @return An argument which can accept one entity
         */
        fun entity() = EntityArgument(false, true)

        /**
         * @return An argument which can accept entities
         */
        fun entities() = EntityArgument(false, false)
    }

    data class EntityArg(val name: String, val value: Any, val exclude: Boolean)
}

fun CommandContext<Sender>.entityArgument(name: String) = argument<EntityQuery>(name)
