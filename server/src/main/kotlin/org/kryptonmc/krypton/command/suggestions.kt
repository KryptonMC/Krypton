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
package org.kryptonmc.krypton.command

import com.mojang.brigadier.Message
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.arguments.coordinates.TextCoordinates
import java.util.concurrent.CompletableFuture

fun SuggestionsBuilder.suggestCoordinates(
    text: String,
    coordinates: Sequence<TextCoordinates>,
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

fun <T> Iterable<T>.suggestKey(
    builder: SuggestionsBuilder,
    provider: (T) -> Key,
    messageProvider: (T) -> Message
): CompletableFuture<Suggestions> {
    val remaining = builder.remaining.lowercase()
    filterResources(remaining, provider) { builder.suggest(provider(it).toString(), messageProvider(it)) }
    return builder.buildFuture()
}

fun <T> Iterable<T>.filterResources(text: String, provider: (T) -> Key, consumer: (T) -> Unit) {
    val hasColon = text.indexOf(':') > -1
    forEach {
        val key = provider(it)
        if (hasColon && text.matchesSubString(key.asString())) {
            consumer(it)
        } else if (text.matchesSubString(key.namespace()) || key.namespace() == "minecraft" && text.matchesSubString(key.value())) {
            consumer(it)
        }
    }
}

private fun SuggestionsBuilder.suggest(suggestions: Iterable<String>): CompletableFuture<Suggestions> {
    suggestions.forEach {
        if (!remainingLowerCase.matchesSubString(it.lowercase())) return@forEach
        suggest(it)
    }
    return buildFuture()
}

fun String.matchesSubString(other: String): Boolean {
    var i = 0
    while (!other.startsWith(this, i)) {
        i = other.indexOf(95.toChar(), i)
        if (i < 0) return false
        i++
    }
    return true
}
