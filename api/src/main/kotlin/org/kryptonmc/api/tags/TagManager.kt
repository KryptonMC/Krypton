/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key

/**
 * The manager of tags.
 */
interface TagManager {

    /**
     * The tags this manager is currently managing.
     */
    val tags: Map<Key, List<Tag<out Any>>>

    /**
     * Gets the tag for the given [key].
     *
     * @param key the key
     * @return the tags for the given key
     */
    operator fun <T : Any> get(key: Key): List<Tag<T>>
}
