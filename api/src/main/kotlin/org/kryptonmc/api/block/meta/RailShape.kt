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
     * If this rail shape ascends the Minecart by one Y level.
     */
    public val isAscending: Boolean
        get() = this == ASCENDING_NORTH || this == ASCENDING_SOUTH || this == ASCENDING_EAST || this == ASCENDING_WEST
}
