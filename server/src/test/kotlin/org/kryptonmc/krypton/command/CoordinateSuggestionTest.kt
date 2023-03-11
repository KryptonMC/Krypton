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
