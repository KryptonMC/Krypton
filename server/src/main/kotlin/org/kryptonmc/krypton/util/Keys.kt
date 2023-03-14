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

import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult

object Keys {

    @JvmField
    val CODEC: Codec<Key> = Codec.STRING.comapFlatMap({ read(it) }, { it.asString() }).stable()

    @JvmStatic
    fun isValidCharacter(char: Char): Boolean = Key.allowedInValue(char) || char == ':'

    @JvmStatic
    fun create(namespace: String, path: String): Key? {
        return try {
            Key.key(namespace, path)
        } catch (_: InvalidKeyException) {
            null
        }
    }

    @JvmStatic
    fun create(input: String): Key? {
        return try {
            Key.key(input)
        } catch (_: InvalidKeyException) {
            null
        }
    }

    @JvmStatic
    fun read(value: String): DataResult<Key> {
        return try {
            DataResult.success(Key.key(value))
        } catch (exception: InvalidKeyException) {
            DataResult.error("$value is not a valid resource location! ${exception.message}")
        }
    }

    @JvmStatic
    fun translation(type: String, key: Key?): String {
        if (key == null) return "$type.unregistered_sadface"
        return "$type.${key.namespace()}.${key.value().replace('/', '.')}"
    }
}
