/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.registry.Registry

/**
 * The type of a [Tag].
 *
 * @param registry the registry the tag uses to lookup IDs
 * @param name the name of the tags directory on disk
 */
data class TagType<T : Any>(
    val registry: Registry<T>,
    val name: String
) : Keyed {

    /**
     * The key of this tag type.
     */
    val key = registry.key.location

    override fun key() = key
}
