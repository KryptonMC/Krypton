/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.util.FactoryProvider

internal object Krypton {

    // Implementation note: These need to be set reflectively.
    @JvmStatic
    private var factoryProvider: FactoryProvider? = null
    @JvmStatic
    private var staticRegistryHolder: RegistryHolder? = null

    @JvmStatic
    @JvmSynthetic
    internal fun staticRegistryHolder(): RegistryHolder = staticRegistryHolder!!

    @JvmStatic
    @JvmSynthetic
    internal inline fun <reified T> factory(): T = factoryProvider!!.provide(T::class.java)
}
