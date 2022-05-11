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
 * Indicates the type of slab that a block this property is applied to
 * represents.
 *
 * Slabs are different from a lot of other blocks, in that two of them can be
 * placed in the same block space. However, for uniformity, this is all an
 * illusion, and this property is used to differentiate "half slabs", those
 * that occupy half of a block from "full slabs", those that occupy a full
 * block, when two slabs are placed in the same block.
 */
public enum class SlabType {

    /**
     * The slab occupies the top half of a block.
     */
    TOP,

    /**
     * The slab occupies the bottom half of a block.
     */
    BOTTOM,

    /**
     * The slab occupies both halves of a block. This happens when two slabs
     * are placed in the same block.
     */
    DOUBLE
}
