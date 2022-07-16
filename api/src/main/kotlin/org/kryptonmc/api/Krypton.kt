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
import org.kryptonmc.api.tags.TagManager
import org.kryptonmc.api.util.FactoryProvider

internal object Krypton {

    @JvmStatic
    @get:JvmSynthetic
    internal val registryManager: RegistryManager
        get() = internalRegistryManager!!

    @JvmStatic
    @get:JvmSynthetic
    internal val tagManager: TagManager
        get() = internalTagManager!!

    @JvmStatic
    @get:JvmSynthetic
    internal val factoryProvider: FactoryProvider
        get() = internalFactoryProvider!!

    // Implementation note: All three of these need to be set reflectively.
    @JvmStatic
    private var internalFactoryProvider: FactoryProvider? = null
    @JvmStatic
    private var internalRegistryManager: RegistryManager? = null
    @JvmStatic
    private var internalTagManager: TagManager? = null
}
