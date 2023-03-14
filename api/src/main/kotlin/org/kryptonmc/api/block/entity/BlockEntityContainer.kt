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
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.util.Vec3i

/**
 * Something that contains block entities.
 */
public interface BlockEntityContainer {

    /**
     * Gets the block entity at the given [x], [y], and [z] coordinates, or
     * returns null if there is no block entity at the given [x], [y], and [z]
     * coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the block entity at the coordinates, or null if not present
     */
    public fun getBlockEntity(x: Int, y: Int, z: Int): BlockEntity?

    /**
     * Gets the block entity at the given [position], or returns null if there
     * is no block entity at the given [position].
     *
     * @param position the position of the block entity
     * @return the block entity at the position, or null if not present
     */
    public fun getBlockEntity(position: Vec3i): BlockEntity? = getBlockEntity(position.x, position.y, position.z)
}
