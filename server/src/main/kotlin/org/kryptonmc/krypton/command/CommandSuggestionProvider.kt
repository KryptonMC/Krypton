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

import com.google.common.base.Strings
import com.mojang.brigadier.Message
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.arguments.coordinates.TextCoordinates
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream

interface CommandSuggestionProvider {

    companion object {

        @JvmStatic
        fun <T> filterResources(resources: Iterable<T>, input: String, toKey: Function<T, Key>, action: Consumer<T>) {
            val hasColon = input.indexOf(':') > -1
            resources.forEach {
                val key = toKey.apply(it)
                if (hasColon && matchesSubString(input, key.asString())) {
                    action.accept(it)
                } else if (
                    matchesSubString(input, key.namespace()) ||
                    key.namespace() == Key.MINECRAFT_NAMESPACE &&
                    matchesSubString(input, key.value())
                ) {
                    action.accept(it)
                }
            }
        }

        @JvmStatic
        fun <T> filterResources(resources: Iterable<T>, remaining: String, prefix: String, toKey: Function<T, Key>, action: Consumer<T>) {
            if (remaining.isEmpty()) {
                resources.forEach(action)
                return
            }
            val commonPrefix = Strings.commonPrefix(remaining, prefix)
            if (commonPrefix.isEmpty()) return
            filterResources(resources, remaining.substring(commonPrefix.length), toKey, action)
        }

        @JvmStatic
        fun suggestResource(resources: Iterable<Key>, builder: SuggestionsBuilder, prefix: String): CompletableFuture<Suggestions> {
            filterResources(resources, builder.remainingLowerCase, prefix, { it }, { builder.suggest(prefix + it) })
            return builder.buildFuture()
        }

        @JvmStatic
        fun suggestResource(resources: Stream<Key>, builder: SuggestionsBuilder, prefix: String): CompletableFuture<Suggestions> =
            suggestResource(Iterable { resources.iterator() }, builder, prefix)

        @JvmStatic
        fun suggestResource(resources: Iterable<Key>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            filterResources(resources, builder.remainingLowerCase, { it }, { builder.suggest(it.toString()) })
            return builder.buildFuture()
        }

        @JvmStatic
        fun suggestResource(resources: Stream<Key>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> =
            suggestResource(Iterable { resources.iterator() }, builder)

        @JvmStatic
        fun <T> suggestResource(resources: Stream<T>, builder: SuggestionsBuilder, toKey: Function<T, Key>,
                                suggester: Function<T, Message>): CompletableFuture<Suggestions> {
            filterResources(Iterable { resources.iterator() }, builder.remainingLowerCase, toKey) {
                builder.suggest(toKey.apply(it).asString(), suggester.apply(it))
            }
            return builder.buildFuture()
        }

        @JvmStatic
        fun suggestCoordinates(remaining: String, coordinates: TextCoordinates, builder: SuggestionsBuilder,
                               validator: Predicate<String>): CompletableFuture<Suggestions> {
            val results = ArrayList<String>()
            val (x, y, z) = coordinates
            if (remaining.isEmpty()) {
                tryAddSuggestions(results, "$x $y $z", validator) {
                    it.add(x.toString())
                    it.add("$x $y")
                }
            } else {
                val components = remaining.split(" ")
                if (components.size == 1) {
                    tryAddSuggestions(results, "${components[0]} $y $z", validator) { it.add("${components[0]} $y") }
                } else if (components.size == 2) {
                    tryAddSuggestions(results, "${components[0]} ${components[1]} $z", validator)
                }
            }
            return suggest(results, builder)
        }

        @JvmStatic
        fun suggest2dCoordinates(remaining: String, coordinates: TextCoordinates, builder: SuggestionsBuilder,
                                 validator: Predicate<String>): CompletableFuture<Suggestions> {
            val results = ArrayList<String>()
            val (x, _, z) = coordinates
            if (remaining.isEmpty()) {
                tryAddSuggestions(results, "$x $z", validator) { it.add(x.toString()) }
            } else {
                val components = remaining.split(" ")
                if (components.size == 1) tryAddSuggestions(results, "${components[0]} $z", validator)
            }
            return suggest(results, builder)
        }

        @JvmStatic
        fun suggest(values: Iterable<String>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            values.forEach { if (matchesSubString(builder.remainingLowerCase, it.lowercase())) builder.suggest(it) }
            return builder.buildFuture()
        }

        @JvmStatic
        fun suggest(values: Stream<String>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            values.filter { matchesSubString(builder.remainingLowerCase, it.lowercase()) }.forEach { builder.suggest(it) }
            return builder.buildFuture()
        }

        @JvmStatic
        fun suggest(values: Array<String>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            values.forEach { if (matchesSubString(builder.remainingLowerCase, it.lowercase())) builder.suggest(it) }
            return builder.buildFuture()
        }

        private fun matchesSubString(input: String, substring: String): Boolean {
            var index = 0
            while (!substring.startsWith(input, index)) {
                index = substring.indexOf(':', index)
                if (index < 0) return false
                ++index
            }
            return true
        }

        private inline fun <T> tryAddSuggestions(list: MutableList<T>, suggestion: T, predicate: Predicate<T>,
                                                 action: (MutableList<T>) -> Unit = {}) {
            if (predicate.test(suggestion)) {
                action(list)
                list.add(suggestion)
            }
        }
    }
}
