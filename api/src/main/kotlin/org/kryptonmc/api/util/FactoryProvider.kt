/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.util

/**
 * Used to provide various factories from the backend for static factory
 * functions.
 */
public interface FactoryProvider {

    /**
     * Provides the factory with the given type [type], or throws a
     * [TypeNotFoundException] if there is no factory registered for the
     * given type.
     *
     * @param T the factory type
     * @param type the class of the type
     * @return the factory
     */
    public fun <T> provide(type: Class<T>): T

    /**
     * Registers the given [factory] of the given [type] to this factory
     * provider.
     *
     * @param T the factory type
     * @param type the class of the type
     * @param factory the factory to register
     * @throws IllegalStateException if the factory is already registered
     */
    public fun <T> register(type: Class<T>, factory: T)
}

/**
 * Provides the factory with the given type [T], or throws a
 * [TypeNotFoundException] if there is no factory registered for the given
 * type.
 *
 * @param T the factory type
 */
@JvmSynthetic
public inline fun <reified T> FactoryProvider.provide(): T = provide(T::class.java)

/**
 * Registers the given [factory] of the given type [T] to this factory
 * provider.
 *
 * @param T the factory type
 * @param factory the factory to register
 * @throws IllegalStateException if the factory is already registered
 */
@JvmSynthetic
public inline fun <reified T> FactoryProvider.register(factory: T) {
    register(T::class.java, factory)
}
