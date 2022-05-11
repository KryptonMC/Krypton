/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

/**
 * Indicates which variant of a block that can attach to either the block
 * above it or the block below it, such as stairs or trapdoors, a block this
 * property is applied to represents.
 */
public enum class Half {

    /**
     * The block is attached to the block above it.
     */
    TOP,

    /**
     * The block is attached to the block below it.
     */
    BOTTOM
}
