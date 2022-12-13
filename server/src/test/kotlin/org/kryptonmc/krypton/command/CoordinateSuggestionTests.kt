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
package org.kryptonmc.krypton.command

import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.command.arguments.coordinates.TextCoordinates
import java.util.function.Predicate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoordinateSuggestionTests {

    @Test
    fun `test false predicate never adds`() {
        val predicate = Predicate<String> { false }
        val builder = SuggestionsBuilder("", "", 0)
        val result1 = CommandSuggestionProvider.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, builder, predicate).join().list
        assertTrue(result1.isEmpty())
        val result2 = CommandSuggestionProvider.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join().list
        assertTrue(result2.isEmpty())
        val result3 = CommandSuggestionProvider.suggestCoordinates("42 42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join().list
        assertTrue(result3.isEmpty())
    }

    @Test
    fun `test true predicate always adds`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result1 = CommandSuggestionProvider.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, builder, predicate).join().list
        assertEquals("~", result1.get(0).text)
        assertEquals("~ ~", result1.get(1).text)
        assertEquals("~ ~ ~", result1.get(2).text)
        val result2 = CommandSuggestionProvider.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join().list
        assertEquals("42 ~", result2.get(0).text)
        assertEquals("42 ~ ~", result2.get(1).text)
        val result3 = CommandSuggestionProvider.suggestCoordinates("42 42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join().list
        assertEquals("42 42 ~", result3.get(0).text)
    }
}
