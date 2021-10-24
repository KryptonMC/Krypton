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
import org.kryptonmc.api.util.CataloguedBy

/**
 * A type of a game tag.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(TagTypes::class)
public interface TagType<T : Any> : Keyed {

    /**
     * The registry for the entries that the tag uses.
     *
     * For example, for the blocks tag type, this will be the block registry.
     */
    @get:JvmName("registry")
    public val registry: Registry<T>
}
