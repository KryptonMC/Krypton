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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.property.Properties
import org.kryptonmc.api.tags.BlockTags

private val PLANTS = intArrayOf(
    Blocks.OAK_SAPLING.id,
    Blocks.SPRUCE_SAPLING.id,
    Blocks.BIRCH_SAPLING.id,
    Blocks.JUNGLE_SAPLING.id,
    Blocks.ACACIA_SAPLING.id,
    Blocks.DARK_OAK_SAPLING.id,
    Blocks.DANDELION.id,
    Blocks.POPPY.id,
    Blocks.BLUE_ORCHID.id,
    Blocks.ALLIUM.id,
    Blocks.AZURE_BLUET.id,
    Blocks.RED_TULIP.id,
    Blocks.ORANGE_TULIP.id,
    Blocks.WHITE_TULIP.id,
    Blocks.PINK_TULIP.id,
    Blocks.OXEYE_DAISY.id,
    Blocks.CORNFLOWER.id,
    Blocks.WITHER_ROSE.id,
    Blocks.LILY_OF_THE_VALLEY.id,
    Blocks.BROWN_MUSHROOM.id,
    Blocks.RED_MUSHROOM.id,
    Blocks.WHEAT.id,
    Blocks.SUGAR_CANE.id,
    Blocks.ATTACHED_PUMPKIN_STEM.id,
    Blocks.ATTACHED_MELON_STEM.id,
    Blocks.PUMPKIN_STEM.id,
    Blocks.MELON_STEM.id,
    Blocks.LILY_PAD.id,
    Blocks.NETHER_WART.id,
    Blocks.COCOA.id,
    Blocks.CARROTS.id,
    Blocks.POTATOES.id,
    Blocks.CHORUS_PLANT.id,
    Blocks.CHORUS_FLOWER.id,
    Blocks.BEETROOTS.id,
    Blocks.SWEET_BERRY_BUSH.id,
    Blocks.WARPED_FUNGUS.id,
    Blocks.CRIMSON_FUNGUS.id,
    Blocks.WEEPING_VINES.id,
    Blocks.WEEPING_VINES_PLANT.id,
    Blocks.TWISTING_VINES.id,
    Blocks.TWISTING_VINES_PLANT.id,
    Blocks.CAVE_VINES.id,
    Blocks.CAVE_VINES_PLANT.id,
    Blocks.SPORE_BLOSSOM.id,
    Blocks.AZALEA.id,
    Blocks.FLOWERING_AZALEA.id,
    Blocks.MOSS_CARPET.id,
    Blocks.BIG_DRIPLEAF.id,
    Blocks.BIG_DRIPLEAF_STEM.id,
    Blocks.SMALL_DRIPLEAF.id
)

private val REPLACEABLE_PLANTS = intArrayOf(
    Blocks.GRASS.id,
    Blocks.FERN.id,
    Blocks.DEAD_BUSH.id,
    Blocks.VINE.id,
    Blocks.GLOW_LICHEN.id,
    Blocks.SUNFLOWER.id,
    Blocks.LILAC.id,
    Blocks.ROSE_BUSH.id,
    Blocks.PEONY.id,
    Blocks.TALL_GRASS.id,
    Blocks.LARGE_FERN.id
)

private val VEGETABLES = intArrayOf(
    Blocks.PUMPKIN.id,
    Blocks.CARVED_PUMPKIN.id,
    Blocks.JACK_O_LANTERN.id,
    Blocks.MELON.id
)

fun Block.isPlant(): Boolean = PLANTS.contains(id)

fun Block.isReplaceablePlant() = REPLACEABLE_PLANTS.contains(id)

fun Block.isVegetable() = VEGETABLES.contains(id)

fun Block.isBurning(): Boolean = BlockTags.FIRE.contains(this) ||
        this === Blocks.LAVA ||
        this === Blocks.MAGMA_BLOCK ||
        isLitCampfire() ||
        this === Blocks.LAVA_CAULDRON

fun Block.isLitCampfire(): Boolean = contains(Properties.LIT) && BlockTags.CAMPFIRES.contains(this) && get(Properties.LIT)!!
