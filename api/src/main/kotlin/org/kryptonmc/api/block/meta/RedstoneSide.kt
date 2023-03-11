/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
