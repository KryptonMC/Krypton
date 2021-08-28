/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.key.Key
import net.kyori.adventure.pointer.Pointer

/**
 * A key used for retrieving item metadata.
 *
 * @param key the namespaced key for this key
 * @param type the type of this key
 */
@JvmRecord
public data class MetaKey<V : Any>(
    @get:JvmName("_get-key") @JvmSynthetic public val key: Key,
    @get:JvmName("_get-type") @JvmSynthetic public val type: Class<V>
) : Pointer<V> {

    override fun key(): Key = key

    override fun type(): Class<V> = type
}
