/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey

/**
 * A registry with a default key-value pair.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DefaultedRegistry<T> : Registry<T> {

    /**
     * The default key for this defaulted registry.
     */
    @get:JvmName("defaultKey")
    public val defaultKey: Key

    /**
     * The default value for this defaulted registry.
     */
    @get:JvmName("defaultValue")
    public val defaultValue: T

    override fun get(key: Key): T

    override fun get(value: T): Key

    override fun get(key: ResourceKey<T>): T
}
