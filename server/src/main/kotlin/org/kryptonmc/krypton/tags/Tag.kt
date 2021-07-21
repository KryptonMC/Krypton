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
package org.kryptonmc.krypton.tags

import com.google.common.collect.ImmutableSet
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import me.bardy.gsonkt.toBooleanStrict
import me.bardy.gsonkt.toPrimitiveOrNull
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.KEY_CODEC
import java.util.Optional
import java.util.function.Supplier
import kotlin.random.Random

interface Tag<T : Any> {

    val values: List<T>

    operator fun contains(value: T): Boolean

    fun random(random: Random) = values.run { this[random.nextInt(size)] }

    interface Named<T : Any> : Tag<T> {

        val name: Key
    }

    sealed interface Entry {

        fun <T : Any> build(getter: (Key) -> Tag<T>?, values: (Key) -> T?, consumer: (T) -> Unit): Boolean

        fun visitRequiredDependencies(consumer: (Key) -> Unit) {}

        fun visitOptionalDependencies(consumer: (Key) -> Unit) {}
    }

    class TagEntry(private val id: Key) : Entry {

        override fun <T : Any> build(
            getter: (Key) -> Tag<T>?,
            values: (Key) -> T?,
            consumer: (T) -> Unit
        ) = getter(id)?.values?.forEach(consumer) != null

        override fun visitRequiredDependencies(consumer: (Key) -> Unit) = consumer(id)

        override fun toString() = "#$id"
    }

    class ElementEntry(private val id: Key) : Entry {

        override fun <T : Any> build(
            getter: (Key) -> Tag<T>?,
            values: (Key) -> T?,
            consumer: (T) -> Unit
        ) = values(id)?.let(consumer) != null

        override fun toString() = id.asString()
    }

    class OptionalTagEntry(private val id: Key) : Entry {

        override fun <T : Any> build(getter: (Key) -> Tag<T>?, values: (Key) -> T?, consumer: (T) -> Unit): Boolean {
            getter(id)?.values?.forEach(consumer)
            return true
        }

        override fun visitOptionalDependencies(consumer: (Key) -> Unit) = consumer(id)

        override fun toString() = "#$id?"
    }

    class OptionalElementEntry(private val id: Key) : Entry {

        override fun <T : Any> build(getter: (Key) -> Tag<T>?, values: (Key) -> T?, consumer: (T) -> Unit): Boolean {
            values(id)?.let(consumer)
            return true
        }

        override fun toString() = "$id?"
    }

    class Builder {

        private val entries = mutableListOf<Entry>()

        fun add(json: JsonObject, source: String) = apply {
            val values = json["values"].asJsonArray
            val tagEntries = values.map(JsonElement::toTagEntry)
            if (json["replace"].toPrimitiveOrNull()?.toBooleanStrict() == true) entries.clear()
            tagEntries.forEach { entries.add(Entry(it, source)) }
        }

        fun visitRequiredDependencies(action: (Key) -> Unit) = entries.forEach { it.entry.visitRequiredDependencies(action) }

        fun visitOptionalDependencies(action: (Key) -> Unit) = entries.forEach { it.entry.visitOptionalDependencies(action) }

        fun <T : Any> build(getter: (Key) -> Tag<T>?, values: (Key) -> T?): Either<Collection<Entry>, Tag<T>> {
            val builder = ImmutableSet.builder<T>()
            val failed = mutableListOf<Entry>()
            entries.forEach {
                val entry = it.entry
                if (!entry.build(getter, values, builder::add)) failed.add(it)
            }
            return if (failed.isEmpty()) Either.right(fromSet(builder.build())) else Either.left(failed)
        }

        class Entry(val entry: Tag.Entry, val source: String) {

            override fun toString() = "$entry (from $source)"
        }
    }

    companion object {

        fun <T : Any> fromSet(set: Set<T>) = SetTag(set)

        fun <T : Any> codec(supplier: () -> TagCollection<T>): Codec<Tag<T>> = KEY_CODEC.flatXmap(
            { key -> Optional.ofNullable(supplier()[key]).map { DataResult.success(it) }.orElseGet { DataResult.error("Unknown key $key!") } },
            { tag -> Optional.ofNullable(supplier()[tag]).map { DataResult.success(it) }.orElseGet { DataResult.error("Unknown tag $tag!") } }
        )
    }
}

private fun JsonElement.toTagEntry(): Tag.Entry {
    val id = if (isJsonObject) asJsonObject["id"].asString else asString
    val required = if (isJsonObject) asJsonObject["required"].toPrimitiveOrNull()?.toBooleanStrict() ?: true else true
    return if (id.startsWith("#")) {
        val key = Key.key(id.substring(1))
        if (required) Tag.TagEntry(key) else Tag.OptionalTagEntry(key)
    } else {
        val key = Key.key(id)
        if (required) Tag.ElementEntry(key) else Tag.OptionalElementEntry(key)
    }
}
