/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key

/**
 * A registry with a default key-value pair.
 */
interface DefaultedRegistry<T : Any> : Registry<T> {

    /**
     * The default key for this defaulted registry.
     */
    val defaultKey: Key

    /**
     * The default value for this defaulted registry.
     */
    val defaultValue: T

    override fun get(key: Key): T

    override fun get(value: T): Key

    override fun get(key: RegistryKey<T>): T
}
