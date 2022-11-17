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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
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

    override fun parse(reader: StringReader): Key = ensureSummonable(readKey(reader))

    override fun getExamples(): Collection<String> = EXAMPLES

    @JvmStatic
    private fun readKey(reader: StringReader): Key {
        val cursor = reader.cursor
        while (reader.canRead() && isAllowedInKey(reader.peek())) {
            reader.skip()
        }
        try {
            return Key.key(reader.string.substring(cursor, reader.cursor))
        } catch (_: InvalidKeyException) {
            reader.cursor = cursor
            throw ERROR_INVALID.createWithContext(reader)
        }
    }

    @JvmStatic
    private fun isAllowedInKey(char: Char): Boolean = char >= '0' && char <= '9' ||
            char >= 'a' && char <= 'z' ||
            char == '_' ||
            char == ':' ||
            char == '/' ||
            char == '.' ||
            char == '-'
}
