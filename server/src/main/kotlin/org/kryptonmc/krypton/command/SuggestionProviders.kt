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

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.registry.Registries
import java.util.concurrent.CompletableFuture

/**
 * Holds built-in suggestion providers that are used client-side to make
 * suggestions without bothering the server.
 *
 * All retrievals in here will default to returning information about the
 * special "ask_server" suggestion provider name, which instructs the client
 * that they should bother the server for custom suggestions.
 */
object SuggestionProviders {

    private val PROVIDERS_BY_NAME = mutableMapOf<Key, SuggestionProvider<Sender>>()
    private val DEFAULT_NAME = Key.key("ask_server")

    /**
     * Suggestion provider that suggests all entity types that can be summoned
     * through some means, such as with the `/summon` command.
     */
    @JvmField
    val SUMMONABLE_ENTITIES: SuggestionProvider<Sender> = register(Key.key("summonable_entities")) { _, builder ->
        Registries.ENTITY_TYPE.values.asSequence()
            .filter { it.isSummonable }
            .suggestKey(builder, EntityType<*>::key) {
                val key = Registries.ENTITY_TYPE[it]
                Component.translatable("entity.${key.namespace()}.${key.value().replace("/", ".")}").toMessage()
            }
    }

    @JvmStatic
    fun name(provider: SuggestionProvider<Sender>): Key {
        if (provider is Wrapper) return provider.name
        return DEFAULT_NAME
    }

    @JvmStatic
    private fun register(key: Key, provider: SuggestionProvider<Sender>): SuggestionProvider<Sender> {
        require(key !in PROVIDERS_BY_NAME) { "A command suggestion provider is already registered with the given key $key!" }
        PROVIDERS_BY_NAME[key] = provider
        return Wrapper(key, provider)
    }

    @JvmRecord
    private data class Wrapper(val name: Key, private val delegate: SuggestionProvider<Sender>) : SuggestionProvider<Sender> {

        override fun getSuggestions(
            context: CommandContext<Sender>?,
            builder: SuggestionsBuilder?
        ): CompletableFuture<Suggestions> = delegate.getSuggestions(context, builder)
    }
}
