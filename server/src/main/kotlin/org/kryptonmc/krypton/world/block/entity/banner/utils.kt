/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.DyeColors
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.downcast

private val BANNER_COLOR_BY_BANNER_BLOCK = Int2ObjectOpenHashMap<DyeColor>().apply {
    put(Blocks.WHITE_BANNER.downcast().id, DyeColors.WHITE)
    put(Blocks.ORANGE_BANNER.downcast().id, DyeColors.ORANGE)
    put(Blocks.MAGENTA_BANNER.downcast().id, DyeColors.MAGENTA)
    put(Blocks.LIGHT_BLUE_BANNER.downcast().id, DyeColors.LIGHT_BLUE)
    put(Blocks.YELLOW_BANNER.downcast().id, DyeColors.YELLOW)
    put(Blocks.LIME_BANNER.downcast().id, DyeColors.LIME)
    put(Blocks.PINK_BANNER.downcast().id, DyeColors.PINK)
    put(Blocks.GRAY_BANNER.downcast().id, DyeColors.GRAY)
    put(Blocks.LIGHT_GRAY_BANNER.downcast().id, DyeColors.LIGHT_GRAY)
    put(Blocks.CYAN_BANNER.downcast().id, DyeColors.CYAN)
    put(Blocks.PURPLE_BANNER.downcast().id, DyeColors.PURPLE)
    put(Blocks.BLUE_BANNER.downcast().id, DyeColors.BLUE)
    put(Blocks.BROWN_BANNER.downcast().id, DyeColors.BROWN)
    put(Blocks.GREEN_BANNER.downcast().id, DyeColors.GREEN)
    put(Blocks.RED_BANNER.downcast().id, DyeColors.RED)
    put(Blocks.BLACK_BANNER.downcast().id, DyeColors.BLACK)
    put(Blocks.WHITE_WALL_BANNER.downcast().id, DyeColors.WHITE)
    put(Blocks.ORANGE_WALL_BANNER.downcast().id, DyeColors.ORANGE)
    put(Blocks.MAGENTA_WALL_BANNER.downcast().id, DyeColors.MAGENTA)
    put(Blocks.LIGHT_BLUE_WALL_BANNER.downcast().id, DyeColors.LIGHT_BLUE)
    put(Blocks.YELLOW_WALL_BANNER.downcast().id, DyeColors.YELLOW)
    put(Blocks.LIME_WALL_BANNER.downcast().id, DyeColors.LIME)
    put(Blocks.PINK_WALL_BANNER.downcast().id, DyeColors.PINK)
    put(Blocks.GRAY_WALL_BANNER.downcast().id, DyeColors.GRAY)
    put(Blocks.LIGHT_GRAY_WALL_BANNER.downcast().id, DyeColors.LIGHT_GRAY)
    put(Blocks.CYAN_WALL_BANNER.downcast().id, DyeColors.CYAN)
    put(Blocks.PURPLE_WALL_BANNER.downcast().id, DyeColors.PURPLE)
    put(Blocks.BLUE_WALL_BANNER.downcast().id, DyeColors.BLUE)
    put(Blocks.BROWN_WALL_BANNER.downcast().id, DyeColors.BROWN)
    put(Blocks.GREEN_WALL_BANNER.downcast().id, DyeColors.GREEN)
    put(Blocks.RED_WALL_BANNER.downcast().id, DyeColors.RED)
    put(Blocks.BLACK_WALL_BANNER.downcast().id, DyeColors.BLACK)
}

fun KryptonBlock.bannerColor(): DyeColor = BANNER_COLOR_BY_BANNER_BLOCK.getOrDefault(id, DyeColors.WHITE)
