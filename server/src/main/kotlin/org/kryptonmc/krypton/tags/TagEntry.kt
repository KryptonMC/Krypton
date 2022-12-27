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
package org.kryptonmc.krypton.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.util.Either
import java.util.function.Consumer
import java.util.function.Predicate

class TagEntry private constructor(private val id: Key, private val tag: Boolean, private val required: Boolean) {

    private fun elementOrTag(): Codecs.TagOrElementLocation = Codecs.TagOrElementLocation(id, tag)

    fun <T> build(lookup: Lookup<T>, action: Consumer<T>): Boolean {
        if (tag) {
            val tags = lookup.tag(id) ?: return !required
            tags.forEach(action)
        } else {
            val element = lookup.element(id) ?: return !required
            action.accept(element)
        }
        return true
    }

    fun visitRequiredDependencies(action: Consumer<Key>) {
        if (tag && required) action.accept(id)
    }

    fun visitOptionalDependencies(action: Consumer<Key>) {
        if (tag && !required) action.accept(id)
    }

    fun verifyIfPresent(elementPredicate: Predicate<Key>, tagPredicate: Predicate<Key>): Boolean {
        val predicate = if (tag) elementPredicate else tagPredicate
        return !required || predicate.test(id)
    }

    override fun toString(): String = buildString {
        if (tag) append('#')
        append(id)
        if (!required) append('?')
    }

    interface Lookup<T> {

        fun element(key: Key): T?

        fun tag(key: Key): Collection<T>?
    }

    companion object {

        private val FULL_CODEC = RecordCodecBuilder.create<TagEntry> { instance ->
            instance.group(
                Codecs.TAG_OR_ELEMENT_ID.fieldOf("id").getting { it.elementOrTag() },
                Codec.BOOLEAN.optionalFieldOf("required", true).getting { it.required }
            ).apply(instance) { idOrTag, required -> TagEntry(idOrTag.id, idOrTag.tag, required) }
        }
        @JvmField
        val CODEC: Codec<TagEntry> = Codec.either(Codecs.TAG_OR_ELEMENT_ID, FULL_CODEC).xmap(
            { either -> either.map({ TagEntry(it.id, it.tag, true) }, { it }) },
            { if (it.required) Either.left(it.elementOrTag()) else Either.right(it) }
        )

        @JvmStatic
        fun element(key: Key): TagEntry = TagEntry(key, false, true)

        @JvmStatic
        fun optionalElement(key: Key): TagEntry = TagEntry(key, false, false)

        @JvmStatic
        fun tag(key: Key): TagEntry = TagEntry(key, true, true)

        @JvmStatic
        fun optionalTag(key: Key): TagEntry = TagEntry(key, true, false)
    }
}
