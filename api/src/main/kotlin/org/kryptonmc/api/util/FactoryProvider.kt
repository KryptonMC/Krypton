/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

/**
 * Used to provide various factories from the backend for static factory functions.
 */
interface FactoryProvider {

    /**
     * Provides the factory with the given type [type], or throws a
     * [FactoryNotFoundException] if there is no factory registered for the given
     * type.
     */
    @Throws(TypeNotPresentException::class)
    fun <T> provide(type: Class<T>): T

    companion object {

        /**
         * The singleton instance.
         */
        val INSTANCE = FACTORY_PROVIDER
    }
}

/**
 * Provides the factory with the given type [T], or throws a
 * [FactoryNotFoundException] if there is no factory registered for the given
 * type.
 */
@JvmSynthetic
inline fun <reified T> FactoryProvider.provide(): T = provide(T::class.java)

/**
 * The singleton factory provider instance.
 */
val FACTORY_PROVIDER = serviceOrError<FactoryProvider>("factory provider")
