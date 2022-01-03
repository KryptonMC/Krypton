/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.Contract

/**
 * A builder for keyed objects.
 */
public interface KeyedBuilder<R : Keyed, B : KeyedBuilder<R, B>> : Buildable.Builder<R> {

    /**
     * Sets the key for the object that is being built to the given [key].
     *
     * @param key the key
     * @return this builder
     */
    @Contract("_ -> this", mutates = "this")
    public fun key(key: Key): B
}
