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
