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
