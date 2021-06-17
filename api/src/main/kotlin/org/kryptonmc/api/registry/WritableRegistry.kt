/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import java.util.OptionalInt

/**
 * This is not internal as it is not restricted to usage within the API only, though it
 * is not recommended to use this class, as it is not designed for use outside of the
 * API
 */
@Suppress("UndocumentedPublicFunction")
sealed class WritableRegistry<T>(key: RegistryKey<out Registry<T>>) : Registry<T>(key) {

    abstract fun <V : T> register(key: RegistryKey<T>, value: V): V

    abstract fun <V : T> registerMapping(id: Int, key: RegistryKey<T>, value: V): V

    abstract fun <V : T> registerOrOverride(id: OptionalInt, key: RegistryKey<T>, value: V): V
}
