/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

/**
 * A goat.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Goat : Animal {

    /**
     * If this goat is a screaming goat, meaning it can scream.
     */
    @get:JvmName("canScream")
    public var canScream: Boolean
}
