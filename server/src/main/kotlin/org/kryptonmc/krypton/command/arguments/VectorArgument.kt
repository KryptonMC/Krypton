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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.coordinates.LocalCoordinates
import org.kryptonmc.krypton.command.arguments.coordinates.TextCoordinates
import org.kryptonmc.krypton.command.arguments.coordinates.WorldCoordinates
import org.kryptonmc.krypton.command.suggestCoordinates
import java.util.concurrent.CompletableFuture

/**
 * An argument type that will parse all 3 types of coordinates.
 *
 * When [correctCenter] is set to true, 0.5 will be added to the X, Y, and Z
 * values to correct coordinates in to the centre of a block.
 */
class VectorArgument private constructor(private val correctCenter: Boolean = true) : ArgumentType<Coordinates> {

    override fun parse(reader: StringReader): Coordinates {
        if (reader.canRead() && reader.peek() == '^') return LocalCoordinates.parse(reader)
        return WorldCoordinates.parse(reader, correctCenter)
    }

    override fun <S> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        if (context.source !is Player) return Suggestions.empty()
        val remaining = builder.remaining
        val suggestion = if (remaining.isNotEmpty() && remaining[0] == '^') TextCoordinates.CENTER_LOCAL else TextCoordinates.CENTER_GLOBAL
        return builder.suggestCoordinates(remaining, suggestion) {
            try {
                parse(StringReader(it))
                true
            } catch (_: CommandSyntaxException) {
                false
            }
        }
    }

    override fun getExamples(): Collection<String> = EXAMPLES

    companion object {

        private val EXAMPLES = listOf("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5")
        private val NORMAL = VectorArgument(true)

        /**
         * A vector argument that will correct coordinates to the centre of a
         * block by adding 0.5 to them.
         */
        @JvmStatic
        fun normal(): VectorArgument = NORMAL
    }
}
