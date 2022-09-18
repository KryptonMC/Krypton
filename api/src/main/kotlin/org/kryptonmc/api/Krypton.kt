/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.tags.Tag
import org.kryptonmc.api.tags.TagManager
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.api.util.FactoryProvider

internal object Krypton {

    @JvmStatic
    internal val registryManager: RegistryManager
        @JvmSynthetic get() = internalRegistryManager!!

    // Implementation note: All three of these need to be set reflectively.
    @JvmStatic
    private var factoryProvider: FactoryProvider? = null
    @JvmStatic
    private var internalRegistryManager: RegistryManager? = null
    @JvmStatic
    private var tagManager: TagManager? = null

    @JvmStatic
    @JvmSynthetic
    internal inline fun <reified T> factory(): T = factoryProvider!!.provide(T::class.java)

    @JvmStatic
    @JvmSynthetic
    internal fun <T : Any> tag(type: TagType<T>, name: String): Tag<T>? = tagManager!!.get(type, name)
}
