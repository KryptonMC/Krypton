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
import java.util.Optional
import kotlin.random.Random

/**
 * A registry with a default key and value.
 */
class DefaultedRegistry<T>(defaultKey: String, key: RegistryKey<out Registry<T>>) : MappedRegistry<T>(key) {

    /**
     * The default key for this defaulted registry
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val defaultKey = Key.key(defaultKey)

    private var defaultValue: T? = null

    override fun <V : T> registerMapping(id: Int, key: RegistryKey<T>, value: V): V {
        if (defaultKey == key.location) defaultValue = value
        return super.registerMapping(id, key, value)
    }

    override fun idOf(value: T) = super.idOf(value).takeIf { it != -1 } ?: super.idOf(defaultValue!!)

    override fun getKey(value: T) = super.getKey(value) ?: defaultKey

    override fun get(key: Key) = super.get(key) ?: defaultValue!!

    override fun getOptional(key: Key) = Optional.ofNullable(super.get(key))

    override fun get(id: Int) = super.get(id) ?: defaultValue!!

    override fun getRandom(random: Random) = super.getRandom(random) ?: defaultValue!!
}
