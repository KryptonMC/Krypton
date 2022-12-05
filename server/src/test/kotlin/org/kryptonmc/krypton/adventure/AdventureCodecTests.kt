package org.kryptonmc.krypton.adventure

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.commands.krypton.KryptonColors
import org.kryptonmc.krypton.testutil.Bootstrapping
import org.kryptonmc.serialization.gson.GsonOps
import java.util.Locale
import kotlin.test.assertEquals

class AdventureCodecTests {

    @Test
    fun `test text colour serialization`() {
        assertEquals(NamedTextColor.WHITE.toString(), TextColorSerialization.encode(NamedTextColor.WHITE))
        assertEquals(asHexString(KryptonColors.STANDARD_PURPLE), TextColorSerialization.encode(KryptonColors.STANDARD_PURPLE))
    }

    @Test
    fun `test text colour deserialization`() {
        assertEquals(NamedTextColor.WHITE, TextColorSerialization.decode(NamedTextColor.WHITE.toString()))
        assertEquals(KryptonColors.STANDARD_PURPLE, TextColorSerialization.decode(asHexString(KryptonColors.STANDARD_PURPLE)))
    }

    @Test
    fun `test style serialization`() {
        // We use Adventure's internal Style serializer here because we assume that always produces a correct result for this test,
        // and so we can use the result as a reference for testing our own serializer.
        val jsonSerializer = GsonComponentSerializer.gson().serializer()
        val codec = AdventureCodecs.STYLE_FORMATTING

        val simple = Style.style(NamedTextColor.WHITE)
        val complex = Style.style()
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.ITALIC)
            .insertion("Hello World!")
            .font(Key.key("krypton", "do_things"))
            .build()
        val simpleJson = jsonSerializer.toJsonTree(simple, Style::class.java)
        val complexJson = jsonSerializer.toJsonTree(complex, Style::class.java)
        assertEquals(simple, codec.read(simpleJson, GsonOps.INSTANCE).result().orElseThrow())
        assertEquals(complex, codec.read(complexJson, GsonOps.INSTANCE).result().orElseThrow())
    }

    @Test
    fun `test style deserialization`() {
        // We use Adventure's internal Style serializer here because we assume that always produces a correct result for this test,
        // and so we can use the result as a reference for testing our own serializer.
        val jsonSerializer = GsonComponentSerializer.gson().serializer()
        val codec = AdventureCodecs.STYLE_FORMATTING

        val simple = Style.style(NamedTextColor.WHITE)
        val complex = Style.style()
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.BOLD)
            .decorate(TextDecoration.UNDERLINED)
            .decorate(TextDecoration.ITALIC)
            .insertion("Hello World!")
            .font(Key.key("krypton", "do_things"))
            .build()
        val simpleJson = codec.encodeStart(simple, GsonOps.INSTANCE).result().orElseThrow()
        val complexJson = codec.encodeStart(complex, GsonOps.INSTANCE).result().orElseThrow()
        assertEquals(simple, jsonSerializer.fromJson(simpleJson, Style::class.java))
        assertEquals(complex, jsonSerializer.fromJson(complexJson, Style::class.java))
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load factories for color`() {
            Bootstrapping.loadFactories()
        }

        // TODO: Remove when Adventure is updated to 4.12.0
        @JvmStatic
        private fun asHexString(color: TextColor): String = String.format(Locale.ROOT, "#%06X", color.value())
    }
}
