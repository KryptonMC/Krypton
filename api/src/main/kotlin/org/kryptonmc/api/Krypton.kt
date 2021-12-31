/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.tags.TagManager
import org.kryptonmc.api.util.FactoryProvider

/**
 * The static singleton accessor for various managers in Krypton that need to
 * be statically accessible.
 *
 * Not recommended for usage. Please prefer the dependency injection methods
 * or the various static accessors to work with these types.
 */
public object Krypton {

    /**
     * The registry manager for the server.
     */
    @JvmStatic
    public val registryManager: RegistryManager
        @JvmName("registryManager") get() = internalRegistryManager!!

    /**
     * The tag manager for the server.
     */
    @JvmStatic
    public val tagManager: TagManager
        @JvmName("tagManager") get() = internalTagManager!!

    /**
     * The factory provider for the server.
     */
    @JvmStatic
    public val factoryProvider: FactoryProvider
        @JvmName("factoryProvider") get() = internalFactoryProvider!!

    @JvmStatic
    private var internalFactoryProvider: FactoryProvider? = null
    @JvmStatic
    private var internalRegistryManager: RegistryManager? = null
    @JvmStatic
    private var internalTagManager: TagManager? = null
}
