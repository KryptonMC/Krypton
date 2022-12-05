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
        builder.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, predicate)
        assertTrue(builder.build().list.isEmpty())
        builder.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, predicate)
        assertTrue(builder.build().list.isEmpty())
        builder.suggestCoordinates("42 42", TextCoordinates.CENTER_GLOBAL, predicate)
        assertTrue(builder.build().list.isEmpty())
    }

    @Test
    fun `test true predicate always adds`() {
        val predicate = Predicate<String> { true }
        val builder = SuggestionsBuilder("", "", 0)
        builder.suggestCoordinates("", TextCoordinates.CENTER_GLOBAL, predicate)
        val result1 = builder.build().list
        assertEquals("~", result1.get(0).text)
        assertEquals("~ ~", result1.get(1).text)
        assertEquals("~ ~ ~", result1.get(2).text)
        builder.suggestCoordinates("42", TextCoordinates.CENTER_GLOBAL, predicate)
        val result2 = builder.build().list
        assertEquals("42 ~", result2.get(0).text)
        assertEquals("42 ~ ~", result2.get(1).text)
        builder.suggestCoordinates("42 42", TextCoordinates.CENTER_GLOBAL, predicate)
        val result3 = builder.build().list
        assertEquals("42 42 ~", result3.get(0).text)
    }
}
