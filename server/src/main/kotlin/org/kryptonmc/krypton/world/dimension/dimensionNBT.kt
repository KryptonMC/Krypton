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

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.util.nbt.setBoolean

fun DimensionType.toNBT() = NBTCompound()
    .setBoolean("piglin_safe", isPiglinSafe)
    .setBoolean("natural", isNatural)
    .setBoolean("ultrawarm", isUltrawarm)
    .setBoolean("has_skylight", hasSkylight)
    .setBoolean("has_ceiling", hasCeiling)
    .setBoolean("has_raids", hasRaids)
    .setBoolean("bed_works", bedWorks)
    .setBoolean("respawn_anchor_works", respawnAnchorWorks)
    .setFloat("ambient_light", ambientLight)
    .apply { fixedTime?.let { setLong("fixed_time", it) } }
    .setString("infiniburn", infiniburn.asString())
    .setString("effects", effects.asString())
    .setInt("min_y", minimumY)
    .setInt("height", height)
    .setInt("logical_height", logicalHeight)
    .setDouble("coordinate_scale", coordinateScale)
