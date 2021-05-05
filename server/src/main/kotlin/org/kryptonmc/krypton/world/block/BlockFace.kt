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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.util.Index

enum class BlockFace(val id: Int) {

    TOP(1),
    BOTTOM(0),
    NORTH(2),
    SOUTH(3),
    EAST(5),
    WEST(4);

    companion object {

        private val KEYS = Index.create(BlockFace::class.java) { it.id }

        fun fromId(id: Int) = requireNotNull(KEYS.value(id))
    }
}
