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
package org.kryptonmc.krypton.util

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.Optional
import java.util.function.Predicate
import java.util.regex.Pattern

class KeyPattern(private val namespacePattern: Pattern?, private val valuePattern: Pattern?) {

    private val namespacePredicate = namespacePattern?.asPredicate() ?: Predicate { true }
    private val valuePredicate = valuePattern?.asPredicate() ?: Predicate { true }
    private val locationPredicate = Predicate<Key> { namespacePredicate.test(it.namespace()) && valuePredicate.test(it.value()) }

    fun namespacePredicate(): Predicate<String> = namespacePredicate

    fun valuePredicate(): Predicate<String> = valuePredicate

    fun locationPredicate(): Predicate<Key> = locationPredicate

    companion object {

        @JvmField
        val CODEC: Codec<KeyPattern> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codecs.PATTERN.optionalFieldOf("namespace").getting { Optional.ofNullable(it.namespacePattern) },
                Codecs.PATTERN.optionalFieldOf("path").getting { Optional.ofNullable(it.valuePattern) }
            ).apply(instance) { namespace, value -> KeyPattern(namespace.orElse(null), value.orElse(null)) }
        }
    }
}
