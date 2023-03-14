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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.registry.KryptonRegistries

/**
 * An argument type that parses entity types that are summonable, in the form
 * of namespaced keys.
 *
 * The default namespace accepted here is "minecraft", just like with
 * everywhere else that keys are used, and that is also valid for entity type
 * keys here, so "pig" and "minecraft:pig" are equivalent.
 */
object SummonEntityArgument : ArgumentType<Key> {

    private val EXAMPLES = setOf("minecraft:pig", "cow")
    private val ERROR_UNKNOWN_ENTITY = CommandExceptions.dynamic("entity.notFound")
    private val ERROR_INVALID = CommandExceptions.simple("argument.id.invalid")

    /**
     * Ensures the given [key] is both a valid entity type **and** the entity
     * type that mapped to the key is summonable.
     *
     * If the entity type is not summonable, [ERROR_UNKNOWN_ENTITY] will be
     * thrown, despite the entity actually being known.
     */
    @JvmStatic
    fun ensureSummonable(key: Key): Key {
        val type = KryptonRegistries.ENTITY_TYPE.get(key)
        if (!type.isSummonable) throw ERROR_UNKNOWN_ENTITY.create(key)
        return type.key()
    }

    @JvmStatic
    fun get(context: CommandContext<CommandSourceStack>, name: String): Key = ensureSummonable(context.getArgument(name, Key::class.java))

    override fun parse(reader: StringReader): Key = ensureSummonable(readKey(reader))

    override fun getExamples(): Collection<String> = EXAMPLES

    @JvmStatic
    private fun readKey(reader: StringReader): Key {
        val cursor = reader.cursor
        try {
            return Key.key(StringReading.readKeyString(reader))
        } catch (_: InvalidKeyException) {
            CommandExceptions.resetAndThrow(reader, cursor, ERROR_INVALID.createWithContext(reader))
        }
    }
}
