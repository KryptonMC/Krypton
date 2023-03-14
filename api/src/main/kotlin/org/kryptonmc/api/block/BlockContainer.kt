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
package org.kryptonmc.api.block

import org.kryptonmc.api.util.Vec3i

/**
 * Something that contains blocks.
 *
 * The default value that will be returned instead of null if no block is
 * found is the block state representing air.
 */
public interface BlockContainer {

    /**
     * Gets the block at the given [x], [y], and [z] coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the block at the given coordinates
     */
    public fun getBlock(x: Int, y: Int, z: Int): BlockState

    /**
     * Gets the block at the given [position].
     *
     * @param position the position
     * @return the block at the given position
     */
    public fun getBlock(position: Vec3i): BlockState
}
