/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.util.StringSerializable

/**
 * The shape of a minecart rail.
 */
public enum class RailShape(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west"),
    ASCENDING_NORTH("ascending_north"),
    ASCENDING_SOUTH("ascending_south"),
    ASCENDING_EAST("ascending_east"),
    ASCENDING_WEST("ascending_west"),
    NORTH_EAST("north_east"),
    NORTH_WEST("north_west"),
    SOUTH_EAST("south_east"),
    SOUTH_WEST("south_west");

    /**
     * If this rail shape is ascending.
     */
    public val isAscending: Boolean
        get() = this == ASCENDING_NORTH || this == ASCENDING_SOUTH || this == ASCENDING_EAST || this == ASCENDING_WEST
}
