/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

/**
 * Something that can be represented as a [Fluid].
 */
public fun interface FluidLike {

    /**
     * Converts this fluid like object to its fluid representation.
     */
    public fun asFluid(): Fluid
}
