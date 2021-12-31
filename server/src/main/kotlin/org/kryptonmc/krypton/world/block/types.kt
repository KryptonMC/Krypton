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

import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.property.Properties
import org.kryptonmc.api.tags.BlockTags

private val PLANTS = persistentListOf(
    Blocks.OAK_SAPLING,
    Blocks.SPRUCE_SAPLING,
    Blocks.BIRCH_SAPLING,
    Blocks.JUNGLE_SAPLING,
    Blocks.ACACIA_SAPLING,
    Blocks.DARK_OAK_SAPLING,
    Blocks.DANDELION,
    Blocks.POPPY,
    Blocks.BLUE_ORCHID,
    Blocks.ALLIUM,
    Blocks.AZURE_BLUET,
    Blocks.RED_TULIP,
    Blocks.ORANGE_TULIP,
    Blocks.WHITE_TULIP,
    Blocks.PINK_TULIP,
    Blocks.OXEYE_DAISY,
    Blocks.CORNFLOWER,
    Blocks.WITHER_ROSE,
    Blocks.LILY_OF_THE_VALLEY,
    Blocks.BROWN_MUSHROOM,
    Blocks.RED_MUSHROOM,
    Blocks.WHEAT,
    Blocks.SUGAR_CANE,
    Blocks.ATTACHED_PUMPKIN_STEM,
    Blocks.ATTACHED_MELON_STEM,
    Blocks.PUMPKIN_STEM,
    Blocks.MELON_STEM,
    Blocks.LILY_PAD,
    Blocks.NETHER_WART,
    Blocks.COCOA,
    Blocks.CARROTS,
    Blocks.POTATOES,
    Blocks.CHORUS_PLANT,
    Blocks.CHORUS_FLOWER,
    Blocks.BEETROOTS,
    Blocks.SWEET_BERRY_BUSH,
    Blocks.WARPED_FUNGUS,
    Blocks.CRIMSON_FUNGUS,
    Blocks.WEEPING_VINES,
    Blocks.WEEPING_VINES_PLANT,
    Blocks.TWISTING_VINES,
    Blocks.TWISTING_VINES_PLANT,
    Blocks.CAVE_VINES,
    Blocks.CAVE_VINES_PLANT,
    Blocks.SPORE_BLOSSOM,
    Blocks.AZALEA,
    Blocks.FLOWERING_AZALEA,
    Blocks.MOSS_CARPET,
    Blocks.BIG_DRIPLEAF,
    Blocks.BIG_DRIPLEAF_STEM,
    Blocks.SMALL_DRIPLEAF
)

private val REPLACEABLE_PLANTS = persistentListOf(
    Blocks.GRASS,
    Blocks.FERN,
    Blocks.DEAD_BUSH,
    Blocks.VINE,
    Blocks.GLOW_LICHEN,
    Blocks.SUNFLOWER,
    Blocks.LILAC,
    Blocks.ROSE_BUSH,
    Blocks.PEONY,
    Blocks.TALL_GRASS,
    Blocks.LARGE_FERN
)

private val VEGETABLES = persistentListOf(
    Blocks.PUMPKIN,
    Blocks.CARVED_PUMPKIN,
    Blocks.JACK_O_LANTERN,
    Blocks.MELON
)

private val GAME_MASTER_BLOCKS = persistentListOf(
    Blocks.COMMAND_BLOCK,
    Blocks.CHAIN_COMMAND_BLOCK,
    Blocks.REPEATING_COMMAND_BLOCK,
    Blocks.JIGSAW,
    Blocks.STRUCTURE_BLOCK
)

fun Block.isPlant(): Boolean = PLANTS.contains(this)

fun Block.isReplaceablePlant(): Boolean = REPLACEABLE_PLANTS.contains(this)

fun Block.isVegetable(): Boolean = VEGETABLES.contains(this)

fun Block.isBurning(): Boolean = BlockTags.FIRE.contains(this) ||
        this === Blocks.LAVA ||
        this === Blocks.MAGMA_BLOCK ||
        isLitCampfire() ||
        this === Blocks.LAVA_CAULDRON

fun Block.isLitCampfire(): Boolean = contains(Properties.LIT) && BlockTags.CAMPFIRES.contains(this) && get(Properties.LIT)!!

fun Block.isGameMasterBlock(): Boolean = GAME_MASTER_BLOCKS.contains(this)
