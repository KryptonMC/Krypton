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
package org.kryptonmc.api.tags

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.internal.annotations.ImmutableType
import java.util.stream.Stream

/**
 * A set of tags from a specific registry for a specific tag key.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface TagSet<T> : Iterable<T> {

    /**
     * The key that this tag set is mapped to.
     */
    @get:JvmName("key")
    public val key: TagKey<T>

    /**
     * The registry that contains this tag set.
     */
    @get:JvmName("registry")
    public val registry: Registry<T>

    /**
     * Gets the size of this tag set.
     *
     * @return the size of this tag set
     */
    public fun size(): Int

    /**
     * Checks if this tag set contains the given [value].
     *
     * @param value the value
     * @return true if this tag set contains the value, false otherwise
     */
    public fun contains(value: T): Boolean

    /**
     * Gets the value at the given [index] in this tag set.
     *
     * @param index the index
     * @return the value
     */
    public fun get(index: Int): T

    /**
     * Creates a new stream of the values in this tag set.
     *
     * @return a new stream of this tag set
     */
    public fun stream(): Stream<T>
}
