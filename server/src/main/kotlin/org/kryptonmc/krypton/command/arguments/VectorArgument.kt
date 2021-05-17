package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.toMessage
import org.kryptonmc.api.entity.entities.Player
import org.kryptonmc.krypton.command.arguments.coordinates.Coordinates
import org.kryptonmc.krypton.command.arguments.coordinates.LocalCoordinates
import org.kryptonmc.krypton.command.arguments.coordinates.WorldCoordinates
import java.util.concurrent.CompletableFuture

class VectorArgument(private val correctCenter: Boolean) : ArgumentType<Coordinates> {

    override fun parse(reader: StringReader) =
        if (reader.canRead() && reader.peek() == '^') LocalCoordinates.parse(reader) else WorldCoordinates.parseDouble(reader, correctCenter)

    override fun <S> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        if (context.source !is Player) return Suggestions.empty()
        val remaining = builder.remaining
        val suggestions = listOf(if (remaining.isNotEmpty() && remaining[0] == '^') {
            TextCoordinates.CENTER_LOCAL
        } else {
            TextCoordinates.CENTER_GLOBAL
        })
        return builder.suggestCoordinates(remaining, suggestions) {
            try {
                parse(StringReader(it))
                true
            } catch (exception: CommandSyntaxException) {
                false
            }
        }
    }

    override fun getExamples() = EXAMPLES

    companion object {

        private val EXAMPLES = listOf("0 0 0", "0.1 -0.5 .9")
    }
}

val ERROR_NOT_COMPLETE = SimpleCommandExceptionType(Component.translatable("argument.pos3d.incomplete").toMessage())
val ERROR_MIXED_TYPE = SimpleCommandExceptionType(Component.translatable("argument.pos.mixed").toMessage())

class TextCoordinates(val x: String, val y: String, val z: String) {

    companion object {

        val CENTER_LOCAL = TextCoordinates("^", "^", "^")
        val CENTER_GLOBAL = TextCoordinates("~", "~", "~")
    }
}

fun SuggestionsBuilder.suggestCoordinates(
    text: String,
    coordinates: Collection<TextCoordinates>,
    predicate: (String) -> Boolean
): CompletableFuture<Suggestions> {
    if (text.isEmpty()) {
        val results = mutableListOf<String>()
        coordinates.forEach {
            val suggestion = "${it.x} ${it.y} ${it.z}"
            if (!predicate(suggestion)) return@forEach
            results += it.x
            results += "${it.x} ${it.y}"
            results += suggestion
        }
        return suggest(results)
    }
    val components = text.split(" ")
    if (components.size == 1) {
        val results = mutableListOf<String>()
        coordinates.forEach {
            val suggestion = "${components[0]} ${it.y} ${it.z}"
            if (!predicate(suggestion)) return@forEach
            results += "${components[0]} ${it.y}"
            results += suggestion
        }
        return suggest(results)
    }
    if (components.size == 2) {
        val results = mutableListOf<String>()
        coordinates.forEach {
            val suggestion = "${components[0]} ${components[1]} ${it.z}"
            if (!predicate(suggestion)) return@forEach
            results += suggestion
        }
        return suggest(results)
    }
    return suggest(emptyList())
}

fun SuggestionsBuilder.suggest(suggestions: Iterable<String>): CompletableFuture<Suggestions> {
    val remaining = remaining.lowercase()
    suggestions.forEach {
        if (!remaining.matchesSubString(it.lowercase())) return@forEach
        suggest(it)
    }
    return buildFuture()
}

fun String.matchesSubString(other: String): Boolean {
    var i = 0
    while (!other.startsWith(this, i)) {
        i = other.indexOf(Char(95), i)
        if (i < 0) return false
        i++
    }
    return true
}
