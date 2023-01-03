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

class CoordinateSuggestionTest {

    @Test
    fun `ensure false predicate never adds with no input`() {
        val predicate = Predicate<String> { false }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertTrue(result.list.isEmpty())
    }

    @Test
    fun `ensure false predicate never adds with x input`() {
        val predicate = Predicate<String> { false }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertTrue(result.list.isEmpty())
    }

    @Test
    fun `ensure false predicate never adds with x and y input`() {
        val predicate = Predicate<String> { false }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("42 42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertTrue(result.list.isEmpty())
    }

    @Test
    fun `ensure true predicate always adds x relative with no input`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertEquals("~", result.list.get(0).text)
    }

    @Test
    fun `ensure true predicate always adds x y relative with no input`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertEquals("~ ~", result.list.get(1).text)
    }

    @Test
    fun `ensure true predicate always adds x y z relative with no input`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertEquals("~ ~ ~", result.list.get(2).text)
    }

    @Test
    fun `ensure true predicate always adds x value and y relative with x input`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertEquals("42 ~", result.list.get(0).text)
    }

    @Test
    fun `ensure true predicate always adds x value and y and z relative with x input`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertEquals("42 ~ ~", result.list.get(1).text)
    }

    @Test
    fun `ensure true predicate always adds x and y value and z relative with x and y input`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        val result = CommandSuggestionProvider.suggestCoordinates("42 42", TextCoordinates.CENTER_GLOBAL, builder, predicate).join()
        assertEquals("42 42 ~", result.list.get(0).text)
    }
}
