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

import com.google.common.collect.Iterators
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.tags.TagSet
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.holder.HolderSet
import java.util.stream.Stream

class KryptonTagSet<T>(override val registry: KryptonRegistry<T>, private val delegate: HolderSet.Named<T>) : TagSet<T> {

    override val key: TagKey<T>
        get() = delegate.key

    override fun size(): Int = delegate.size()

    override fun contains(value: T): Boolean = delegate.contains(registry.wrapAsHolder(value!!))

    override fun get(index: Int): T = delegate.get(index).value()

    override fun iterator(): Iterator<T> = Iterators.transform(delegate.iterator()) { it.value() }

    override fun stream(): Stream<T> = delegate.stream().map { it.value() }
}
