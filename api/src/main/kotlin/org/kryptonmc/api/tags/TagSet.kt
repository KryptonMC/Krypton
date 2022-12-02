/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
     * The size of this tag set.
     */
    @get:JvmName("size")
    public val size: Int

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
