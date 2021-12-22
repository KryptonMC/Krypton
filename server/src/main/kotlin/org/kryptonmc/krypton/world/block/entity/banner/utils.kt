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
package org.kryptonmc.krypton.world.block.entity.banner

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.DyeColors

private val BANNER_COLOR_BY_BANNER_BLOCK = Int2ObjectOpenHashMap<DyeColor>().apply {
    put(Blocks.WHITE_BANNER.id, DyeColors.WHITE)
    put(Blocks.ORANGE_BANNER.id, DyeColors.ORANGE)
    put(Blocks.MAGENTA_BANNER.id, DyeColors.MAGENTA)
    put(Blocks.LIGHT_BLUE_BANNER.id, DyeColors.LIGHT_BLUE)
    put(Blocks.YELLOW_BANNER.id, DyeColors.YELLOW)
    put(Blocks.LIME_BANNER.id, DyeColors.LIME)
    put(Blocks.PINK_BANNER.id, DyeColors.PINK)
    put(Blocks.GRAY_BANNER.id, DyeColors.GRAY)
    put(Blocks.LIGHT_GRAY_BANNER.id, DyeColors.LIGHT_GRAY)
    put(Blocks.CYAN_BANNER.id, DyeColors.CYAN)
    put(Blocks.PURPLE_BANNER.id, DyeColors.PURPLE)
    put(Blocks.BLUE_BANNER.id, DyeColors.BLUE)
    put(Blocks.BROWN_BANNER.id, DyeColors.BROWN)
    put(Blocks.GREEN_BANNER.id, DyeColors.GREEN)
    put(Blocks.RED_BANNER.id, DyeColors.RED)
    put(Blocks.BLACK_BANNER.id, DyeColors.BLACK)
    put(Blocks.WHITE_WALL_BANNER.id, DyeColors.WHITE)
    put(Blocks.ORANGE_WALL_BANNER.id, DyeColors.ORANGE)
    put(Blocks.MAGENTA_WALL_BANNER.id, DyeColors.MAGENTA)
    put(Blocks.LIGHT_BLUE_WALL_BANNER.id, DyeColors.LIGHT_BLUE)
    put(Blocks.YELLOW_WALL_BANNER.id, DyeColors.YELLOW)
    put(Blocks.LIME_WALL_BANNER.id, DyeColors.LIME)
    put(Blocks.PINK_WALL_BANNER.id, DyeColors.PINK)
    put(Blocks.GRAY_WALL_BANNER.id, DyeColors.GRAY)
    put(Blocks.LIGHT_GRAY_WALL_BANNER.id, DyeColors.LIGHT_GRAY)
    put(Blocks.CYAN_WALL_BANNER.id, DyeColors.CYAN)
    put(Blocks.PURPLE_WALL_BANNER.id, DyeColors.PURPLE)
    put(Blocks.BLUE_WALL_BANNER.id, DyeColors.BLUE)
    put(Blocks.BROWN_WALL_BANNER.id, DyeColors.BROWN)
    put(Blocks.GREEN_WALL_BANNER.id, DyeColors.GREEN)
    put(Blocks.RED_WALL_BANNER.id, DyeColors.RED)
    put(Blocks.BLACK_WALL_BANNER.id, DyeColors.BLACK)
}

fun Block.bannerColor(): DyeColor = BANNER_COLOR_BY_BANNER_BLOCK.getOrDefault(id, DyeColors.WHITE)
