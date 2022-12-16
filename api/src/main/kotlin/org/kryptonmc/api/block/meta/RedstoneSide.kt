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
 * Indicates the way in which a redstone wire (placed redstone dust) block
 * this property is applied to connects to an adjacent block on a specific
 * face.
 */
public enum class RedstoneSide {

    /**
     * The wire travels vertically up the side of the adjacent block.
     *
     * This is always the case when there is another wire placed on the
     * adjacent block's upper face.
     */
    UP,

    /**
     * The wire travels horizontally along the floor, connecting in to the
     * side of the adjacent block.
     */
    SIDE,

    /**
     * The wire has no connection to the adjacent block.
     */
    NONE;

    /**
     * Gets whether this side of the redstone wire is connected in some way, to
     * either another wire or another block, rather than being not connected,
     * or appearing as a single dot, meaning no connections on any faces.
     *
     * @return true if connected, false otherwise
     */
    public fun isConnected(): Boolean = this != NONE
}
