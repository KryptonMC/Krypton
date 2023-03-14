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
 * Indicates the shape of a minecart rail this property is applied to
 * represents.
 *
 * Minecart rails can be in one of three layouts:
 * * Straight flat, which will keep the Minecart on the same Y level and
 *   direction of travel. These rails can either be north to south or east to
 *   west.
 * * Straight ascending, which will ascend the Minecart up by one Y level, and
 *   maintain the same direction of travel.
 * * Corner, which will change the Minecart's orientation by 90 degrees,
 *   either clockwise or anticlockwise, depending on the direction change.
 */
public enum class RailShape {

    NORTH_SOUTH,
    EAST_WEST,
    ASCENDING_NORTH,
    ASCENDING_SOUTH,
    ASCENDING_EAST,
    ASCENDING_WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;

    /**
     * Gets whether this rail shape ascends the Minecart by one Y level.
     *
     * @return true if ascending, false otherwise
     */
    public fun isAscending(): Boolean = this == ASCENDING_NORTH || this == ASCENDING_SOUTH || this == ASCENDING_EAST || this == ASCENDING_WEST
}
