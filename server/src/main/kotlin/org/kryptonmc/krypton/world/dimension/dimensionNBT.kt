/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.nbt.compound

fun DimensionType.toNBT() = compound {
    boolean("piglin_safe", isPiglinSafe)
    boolean("natural", isNatural)
    boolean("ultrawarm", isUltrawarm)
    boolean("has_skylight", hasSkylight)
    boolean("has_ceiling", hasCeiling)
    boolean("has_raids", hasRaids)
    boolean("bed_works", bedWorks)
    boolean("respawn_anchor_works", respawnAnchorWorks)
    float("ambient_light", ambientLight)
    fixedTime?.let { long("fixed_time", it) }
    string("infiniburn", infiniburn.asString())
    string("effects", effects.asString())
    int("min_y", minimumY)
    int("height", height)
    int("logical_height", logicalHeight)
    double("coordinate_scale", coordinateScale)
}
