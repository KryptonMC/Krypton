/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.Contract

/**
 * An object that can be built.
 *
 * @param B the builder type
 * @param T the type of the built object
 */
public interface Buildable<B : AbstractBuilder<T>, T> {

    /**
     * Creates a new builder from this object.
     *
     * The created builder should have all of the existing properties of this
     * object already set, to allow for easy bulk editing of the object.
     *
     * @return a new builder
     */
    @Contract("-> new", pure = true)
    public fun toBuilder(): B
}
