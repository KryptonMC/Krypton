/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Keyed
import javax.annotation.concurrent.Immutable

/**
 * A game tag that may be applied to certain objects to denote specific
 * features or categories that something may have or be placed in to.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface Tag<T : Any> : Keyed {

    /**
     * The type of tag this tag is.
     */
    @get:JvmName("type")
    public val type: TagType<T>

    /**
     * All of the values that are under this tag.
     */
    @get:JvmName("values")
    public val values: Collection<T>

    /**
     * Checks if the given [value] is in this tag.
     *
     * @param value the value
     * @return true if the value is in this tag, false otherwise
     */
    public fun contains(value: T): Boolean = values.contains(value)
}
