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
package org.kryptonmc.krypton.world.block.tile

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.world.block.blocks.BannerEntity

abstract class BlockEntity(val id: String) {

    abstract val position: Vector

    abstract val keepPacked: Boolean
}

fun CompoundBinaryTag.toBlockEntity(): BlockEntity {
    val position = Vector(getInt("x").toDouble(), getInt("y").toDouble(), getInt("z").toDouble())
    val keepPacked = getBoolean("keepPacked")
    when (val id = getString("id")) {
        "banner" -> return BannerEntity.fromNBT(position, keepPacked, this)
        else -> throw IllegalArgumentException("Unknown block entity with id $id")
    }
}
