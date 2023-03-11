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
package org.kryptonmc.krypton.util.serialization

import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataOps
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.util.Pair
import java.util.function.Function

class EnumCodec<E : Enum<E>>(resolver: Function<String, E?>) : Codec<E> {

    private val codec = Codecs.stringResolver({ it.name.lowercase() }, resolver)

    override fun <T> decode(input: T, ops: DataOps<T>): DataResult<Pair<E, T>> = codec.decode(input, ops)

    override fun <T : Any> encode(input: E, ops: DataOps<T>, prefix: T): DataResult<T> = codec.encode(input, ops, prefix)
}
