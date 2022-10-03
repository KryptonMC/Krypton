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

import com.mojang.brigadier.Message
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.arguments.coordinates.TextCoordinates
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate

fun SuggestionsBuilder.suggestCoordinates(text: String, coordinates: TextCoordinates, predicate: Predicate<String>): CompletableFuture<Suggestions> {
    val results = ArrayList<String>()
    val (x, y, z) = coordinates
    if (text.isEmpty()) {
        results.addIf("$x $y $z", predicate) {
            add(x.toString())
            add("$x $y")
            add(it)
        }
    } else {
        val components = text.split(" ")
        if (components.size == 1) {
            results.addIf("${components[0]} $y $z", predicate) {
                add("${components[0]} $y")
                add(it)
            }
        } else if (components.size == 2) {
            results.addIf("${components[0]} ${components[1]} $z", predicate, MutableList<String>::add)
        }
    }
    return suggest(results)
}

private inline fun <T> MutableList<T>.addIf(input: String, predicate: Predicate<String>, action: MutableList<T>.(String) -> Unit) {
    if (predicate.test(input)) action(input)
}

fun <T> Sequence<T>.suggestResource(
    builder: SuggestionsBuilder,
    provider: Function<T, Key>,
    messageProvider: Function<T, Message>
): CompletableFuture<Suggestions> {
    filterResources(builder.remainingLowerCase, provider) { builder.suggest(provider.apply(it).toString(), messageProvider.apply(it)) }
    return builder.buildFuture()
}

fun <T> Sequence<T>.filterResources(text: String, provider: Function<T, Key>, consumer: Consumer<T>) {
    val hasColon = text.indexOf(':') > -1
    forEach {
        val key = provider.apply(it)
        if (hasColon && text.matchesSubString(key.asString())) {
            consumer.accept(it)
        } else if (text.matchesSubString(key.namespace()) || key.namespace() == Key.MINECRAFT_NAMESPACE && text.matchesSubString(key.value())) {
            consumer.accept(it)
        }
    }
}

private fun SuggestionsBuilder.suggest(suggestions: Iterable<String>): CompletableFuture<Suggestions> {
    suggestions.forEach { if (remainingLowerCase.matchesSubString(it.lowercase())) suggest(it) }
    return buildFuture()
}

fun String.matchesSubString(other: String): Boolean {
    var i = 0
    while (!other.startsWith(this, i)) {
        i = other.indexOf('_', i)
        if (i < 0) return false
        i++
    }
    return true
}
