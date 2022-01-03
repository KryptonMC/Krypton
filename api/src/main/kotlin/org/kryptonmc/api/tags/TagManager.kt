/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

/**
 * The manager of tags.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface TagManager {

    /**
     * All of the available tags, mapped by their type.
     */
    @get:JvmName("tags")
    public val tags: Map<TagType<*>, List<Tag<*>>>

    /**
     * Gets the list of tags for the given [type].
     *
     * @param type the type
     * @return the list of tags for the type
     */
    public operator fun <T : Any> get(type: TagType<T>): List<Tag<T>>

    /**
     * Gets the tag with the given [type] and [name], or returns null if there
     * is no tag with the given [type] and [name].
     *
     * @param type the type
     * @param name the name
     * @return the tag with the type and name, or null if not present
     */
    public operator fun <T : Any> get(type: TagType<T>, name: String): Tag<T>?
}
