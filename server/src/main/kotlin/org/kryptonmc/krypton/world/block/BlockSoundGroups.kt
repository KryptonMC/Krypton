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
package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.BlockSoundGroup
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.internal.annotations.IgnoreNotCataloguedBy

@Catalogue(BlockSoundGroup::class)
@IgnoreNotCataloguedBy
object BlockSoundGroups {

    @JvmField
    val WOOD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.WOOD_BREAK, SoundEvents.WOOD_STEP, SoundEvents.WOOD_PLACE,
        SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL)
    @JvmField
    val GRAVEL: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.GRAVEL_BREAK, SoundEvents.GRAVEL_STEP, SoundEvents.GRAVEL_PLACE,
        SoundEvents.GRAVEL_HIT, SoundEvents.GRAVEL_FALL)
    @JvmField
    val GRASS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.GRASS_BREAK, SoundEvents.GRASS_STEP, SoundEvents.GRASS_PLACE,
        SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL)
    @JvmField
    val LILY_PAD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.GRASS_BREAK, SoundEvents.GRASS_STEP, SoundEvents.LILY_PAD_PLACE,
        SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL)
    @JvmField
    val STONE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.STONE_BREAK, SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE,
        SoundEvents.STONE_HIT, SoundEvents.STONE_FALL)
    @JvmField
    val METAL: KryptonBlockSoundGroup = create(1.0F, 1.5F, SoundEvents.METAL_BREAK, SoundEvents.METAL_STEP, SoundEvents.METAL_PLACE,
        SoundEvents.METAL_HIT, SoundEvents.METAL_FALL)
    @JvmField
    val GLASS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.GLASS_BREAK, SoundEvents.GLASS_STEP, SoundEvents.GLASS_PLACE,
        SoundEvents.GLASS_HIT, SoundEvents.GLASS_FALL)
    @JvmField
    val WOOL: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.WOOL_BREAK, SoundEvents.WOOL_STEP, SoundEvents.WOOL_PLACE,
        SoundEvents.WOOL_HIT, SoundEvents.WOOL_FALL)
    @JvmField
    val SAND: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SAND_BREAK, SoundEvents.SAND_STEP, SoundEvents.SAND_PLACE,
        SoundEvents.SAND_HIT, SoundEvents.SAND_FALL)
    @JvmField
    val SNOW: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SNOW_BREAK, SoundEvents.SNOW_STEP, SoundEvents.SNOW_PLACE,
        SoundEvents.SNOW_HIT, SoundEvents.SNOW_FALL)
    @JvmField
    val POWDER_SNOW: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.POWDER_SNOW_BREAK, SoundEvents.POWDER_SNOW_STEP,
        SoundEvents.POWDER_SNOW_PLACE, SoundEvents.POWDER_SNOW_HIT, SoundEvents.POWDER_SNOW_FALL)
    @JvmField
    val LADDER: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.LADDER_BREAK, SoundEvents.LADDER_STEP, SoundEvents.LADDER_PLACE,
        SoundEvents.LADDER_HIT, SoundEvents.LADDER_FALL)
    @JvmField
    val ANVIL: KryptonBlockSoundGroup = create(0.3F, 1.0F, SoundEvents.ANVIL_BREAK, SoundEvents.ANVIL_STEP, SoundEvents.ANVIL_PLACE,
        SoundEvents.ANVIL_HIT, SoundEvents.ANVIL_FALL)
    @JvmField
    val SLIME_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SLIME_BLOCK_BREAK, SoundEvents.SLIME_BLOCK_STEP,
        SoundEvents.SLIME_BLOCK_PLACE, SoundEvents.SLIME_BLOCK_HIT, SoundEvents.SLIME_BLOCK_FALL)
    @JvmField
    val HONEY_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.HONEY_BLOCK_BREAK, SoundEvents.HONEY_BLOCK_STEP,
        SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_HIT, SoundEvents.HONEY_BLOCK_FALL)
    @JvmField
    val WET_GRASS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.WET_GRASS_PLACE,
        SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL)
    @JvmField
    val CORAL_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.CORAL_BLOCK_BREAK, SoundEvents.CORAL_BLOCK_STEP,
        SoundEvents.CORAL_BLOCK_PLACE, SoundEvents.CORAL_BLOCK_HIT, SoundEvents.CORAL_BLOCK_FALL)
    @JvmField
    val BAMBOO: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.BAMBOO_BREAK, SoundEvents.BAMBOO_STEP, SoundEvents.BAMBOO_PLACE,
        SoundEvents.BAMBOO_HIT, SoundEvents.BAMBOO_FALL)
    @JvmField
    val BAMBOO_SAPLING: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.BAMBOO_SAPLING_BREAK, SoundEvents.BAMBOO_STEP,
        SoundEvents.BAMBOO_SAPLING_PLACE, SoundEvents.BAMBOO_SAPLING_HIT, SoundEvents.BAMBOO_FALL)
    @JvmField
    val SCAFFOLDING: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SCAFFOLDING_BREAK, SoundEvents.SCAFFOLDING_STEP,
        SoundEvents.SCAFFOLDING_PLACE, SoundEvents.SCAFFOLDING_HIT, SoundEvents.SCAFFOLDING_FALL)
    @JvmField
    val SWEET_BERRY_BUSH: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundEvents.GRASS_STEP,
        SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL)
    @JvmField
    val CROP: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.CROP_BREAK, SoundEvents.GRASS_STEP, SoundEvents.CROP_PLANTED,
        SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL)
    @JvmField
    val HARD_CROP: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.WOOD_BREAK, SoundEvents.WOOD_STEP, SoundEvents.CROP_PLANTED,
        SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL)
    @JvmField
    val VINE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.VINE_BREAK, SoundEvents.VINE_STEP, SoundEvents.VINE_PLACE,
        SoundEvents.VINE_HIT, SoundEvents.VINE_FALL)
    @JvmField
    val NETHER_WART: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHER_WART_BREAK, SoundEvents.STONE_STEP,
        SoundEvents.NETHER_WART_PLANTED, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL)
    @JvmField
    val LANTERN: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.LANTERN_BREAK, SoundEvents.LANTERN_STEP, SoundEvents.LANTERN_PLACE,
        SoundEvents.LANTERN_HIT, SoundEvents.LANTERN_FALL)
    @JvmField
    val STEM: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.STEM_BREAK, SoundEvents.STEM_STEP, SoundEvents.STEM_PLACE,
        SoundEvents.STEM_HIT, SoundEvents.STEM_FALL)
    @JvmField
    val NYLIUM: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NYLIUM_BREAK, SoundEvents.NYLIUM_STEP, SoundEvents.NYLIUM_PLACE,
        SoundEvents.NYLIUM_HIT, SoundEvents.NYLIUM_FALL)
    @JvmField
    val FUNGUS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.FUNGUS_BREAK, SoundEvents.FUNGUS_STEP, SoundEvents.FUNGUS_PLACE,
        SoundEvents.FUNGUS_HIT, SoundEvents.FUNGUS_FALL)
    @JvmField
    val ROOTS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.ROOTS_BREAK, SoundEvents.ROOTS_STEP, SoundEvents.ROOTS_PLACE,
        SoundEvents.ROOTS_HIT, SoundEvents.ROOTS_FALL)
    @JvmField
    val SHROOMLIGHT: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SHROOMLIGHT_BREAK, SoundEvents.SHROOMLIGHT_STEP,
        SoundEvents.SHROOMLIGHT_PLACE, SoundEvents.SHROOMLIGHT_HIT, SoundEvents.SHROOMLIGHT_FALL)
    @JvmField
    val WEEPING_VINES: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.WEEPING_VINES_BREAK, SoundEvents.WEEPING_VINES_STEP,
        SoundEvents.WEEPING_VINES_PLACE, SoundEvents.WEEPING_VINES_HIT, SoundEvents.WEEPING_VINES_FALL)
    @JvmField
    val TWISTING_VINES: KryptonBlockSoundGroup = create(1.0F, 0.5F, SoundEvents.WEEPING_VINES_BREAK, SoundEvents.WEEPING_VINES_STEP,
        SoundEvents.WEEPING_VINES_PLACE, SoundEvents.WEEPING_VINES_HIT, SoundEvents.WEEPING_VINES_FALL)
    @JvmField
    val SOUL_SAND: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SOUL_SAND_BREAK, SoundEvents.SOUL_SAND_STEP, SoundEvents.SOUL_SAND_PLACE,
        SoundEvents.SOUL_SAND_HIT, SoundEvents.SOUL_SAND_FALL)
    @JvmField
    val SOUL_SOIL: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SOUL_SOIL_BREAK, SoundEvents.SOUL_SOIL_STEP, SoundEvents.SOUL_SOIL_PLACE,
        SoundEvents.SOUL_SOIL_HIT, SoundEvents.SOUL_SOIL_FALL)
    @JvmField
    val BASALT: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.BASALT_BREAK, SoundEvents.BASALT_STEP, SoundEvents.BASALT_PLACE,
        SoundEvents.BASALT_HIT, SoundEvents.BASALT_FALL)
    @JvmField
    val WART_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.WART_BLOCK_BREAK, SoundEvents.WART_BLOCK_STEP,
        SoundEvents.WART_BLOCK_PLACE, SoundEvents.WART_BLOCK_HIT, SoundEvents.WART_BLOCK_FALL)
    @JvmField
    val NETHERRACK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHERRACK_BREAK, SoundEvents.NETHERRACK_STEP,
        SoundEvents.NETHERRACK_PLACE, SoundEvents.NETHERRACK_HIT, SoundEvents.NETHERRACK_FALL)
    @JvmField
    val NETHER_BRICKS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHER_BRICKS_BREAK, SoundEvents.NETHER_BRICKS_STEP,
        SoundEvents.NETHER_BRICKS_PLACE, SoundEvents.NETHER_BRICKS_HIT, SoundEvents.NETHER_BRICKS_FALL)
    @JvmField
    val NETHER_SPROUTS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHER_SPROUTS_BREAK, SoundEvents.NETHER_SPROUTS_STEP,
        SoundEvents.NETHER_SPROUTS_PLACE, SoundEvents.NETHER_SPROUTS_HIT, SoundEvents.NETHER_SPROUTS_FALL)
    @JvmField
    val NETHER_ORE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHER_ORE_BREAK, SoundEvents.NETHER_ORE_STEP,
        SoundEvents.NETHER_ORE_PLACE, SoundEvents.NETHER_ORE_HIT, SoundEvents.NETHER_ORE_FALL)
    @JvmField
    val BONE_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.BONE_BLOCK_BREAK, SoundEvents.BONE_BLOCK_STEP,
        SoundEvents.BONE_BLOCK_PLACE, SoundEvents.BONE_BLOCK_HIT, SoundEvents.BONE_BLOCK_FALL)
    @JvmField
    val NETHERITE_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHERITE_BLOCK_BREAK, SoundEvents.NETHERITE_BLOCK_STEP,
        SoundEvents.NETHERITE_BLOCK_PLACE, SoundEvents.NETHERITE_BLOCK_HIT, SoundEvents.NETHERITE_BLOCK_FALL)
    @JvmField
    val ANCIENT_DEBRIS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.ANCIENT_DEBRIS_BREAK, SoundEvents.ANCIENT_DEBRIS_STEP,
        SoundEvents.ANCIENT_DEBRIS_PLACE, SoundEvents.ANCIENT_DEBRIS_HIT, SoundEvents.ANCIENT_DEBRIS_FALL)
    @JvmField
    val LODESTONE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.LODESTONE_BREAK, SoundEvents.LODESTONE_STEP, SoundEvents.LODESTONE_PLACE,
        SoundEvents.LODESTONE_HIT, SoundEvents.LODESTONE_FALL)
    @JvmField
    val CHAIN: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.CHAIN_BREAK, SoundEvents.CHAIN_STEP, SoundEvents.CHAIN_PLACE,
        SoundEvents.CHAIN_HIT, SoundEvents.CHAIN_FALL)
    @JvmField
    val NETHER_GOLD_ORE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.NETHER_GOLD_ORE_BREAK, SoundEvents.NETHER_GOLD_ORE_STEP,
        SoundEvents.NETHER_GOLD_ORE_PLACE, SoundEvents.NETHER_GOLD_ORE_HIT, SoundEvents.NETHER_GOLD_ORE_FALL)
    @JvmField
    val GILDED_BLACKSTONE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.GILDED_BLACKSTONE_BREAK, SoundEvents.GILDED_BLACKSTONE_STEP,
        SoundEvents.GILDED_BLACKSTONE_PLACE, SoundEvents.GILDED_BLACKSTONE_HIT, SoundEvents.GILDED_BLACKSTONE_FALL)
    @JvmField
    val CANDLE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.CANDLE_BREAK, SoundEvents.CANDLE_STEP, SoundEvents.CANDLE_PLACE,
        SoundEvents.CANDLE_HIT, SoundEvents.CANDLE_FALL)
    @JvmField
    val AMETHYST: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.AMETHYST_BLOCK_BREAK, SoundEvents.AMETHYST_BLOCK_STEP,
        SoundEvents.AMETHYST_BLOCK_PLACE, SoundEvents.AMETHYST_BLOCK_HIT, SoundEvents.AMETHYST_BLOCK_FALL)
    @JvmField
    val AMETHYST_CLUSTER: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundEvents.AMETHYST_CLUSTER_STEP,
        SoundEvents.AMETHYST_CLUSTER_PLACE, SoundEvents.AMETHYST_CLUSTER_HIT, SoundEvents.AMETHYST_CLUSTER_FALL)
    @JvmField
    val SMALL_AMETHYST_BUD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SMALL_AMETHYST_BUD_BREAK, SoundEvents.AMETHYST_CLUSTER_STEP,
        SoundEvents.SMALL_AMETHYST_BUD_PLACE, SoundEvents.AMETHYST_CLUSTER_HIT, SoundEvents.AMETHYST_CLUSTER_FALL)
    @JvmField
    val MEDIUM_AMETHYST_BUD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MEDIUM_AMETHYST_BUD_BREAK, SoundEvents.AMETHYST_CLUSTER_STEP,
        SoundEvents.MEDIUM_AMETHYST_BUD_PLACE, SoundEvents.AMETHYST_CLUSTER_HIT, SoundEvents.AMETHYST_CLUSTER_FALL)
    @JvmField
    val LARGE_AMETHYST_BUD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.LARGE_AMETHYST_BUD_BREAK, SoundEvents.AMETHYST_CLUSTER_STEP,
        SoundEvents.LARGE_AMETHYST_BUD_PLACE, SoundEvents.AMETHYST_CLUSTER_HIT, SoundEvents.AMETHYST_CLUSTER_FALL)
    @JvmField
    val TUFF: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.TUFF_BREAK, SoundEvents.TUFF_STEP, SoundEvents.TUFF_PLACE,
        SoundEvents.TUFF_HIT, SoundEvents.TUFF_FALL)
    @JvmField
    val CALCITE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.CALCITE_BREAK, SoundEvents.CALCITE_STEP, SoundEvents.CALCITE_PLACE,
        SoundEvents.CALCITE_HIT, SoundEvents.CALCITE_FALL)
    @JvmField
    val DRIPSTONE_BLOCK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.DRIPSTONE_BLOCK_BREAK, SoundEvents.DRIPSTONE_BLOCK_STEP,
        SoundEvents.DRIPSTONE_BLOCK_PLACE, SoundEvents.DRIPSTONE_BLOCK_HIT, SoundEvents.DRIPSTONE_BLOCK_FALL)
    @JvmField
    val POINTED_DRIPSTONE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.POINTED_DRIPSTONE_BREAK, SoundEvents.POINTED_DRIPSTONE_STEP,
        SoundEvents.POINTED_DRIPSTONE_PLACE, SoundEvents.POINTED_DRIPSTONE_HIT, SoundEvents.POINTED_DRIPSTONE_FALL)
    @JvmField
    val COPPER: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.COPPER_BREAK, SoundEvents.COPPER_STEP, SoundEvents.COPPER_PLACE,
        SoundEvents.COPPER_HIT, SoundEvents.COPPER_FALL)
    @JvmField
    val CAVE_VINES: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.CAVE_VINES_BREAK, SoundEvents.CAVE_VINES_STEP,
        SoundEvents.CAVE_VINES_PLACE, SoundEvents.CAVE_VINES_HIT, SoundEvents.CAVE_VINES_FALL)
    @JvmField
    val SPORE_BLOSSOM: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SPORE_BLOSSOM_BREAK, SoundEvents.SPORE_BLOSSOM_STEP,
        SoundEvents.SPORE_BLOSSOM_PLACE, SoundEvents.SPORE_BLOSSOM_HIT, SoundEvents.SPORE_BLOSSOM_FALL)
    @JvmField
    val AZALEA: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.AZALEA_BREAK, SoundEvents.AZALEA_STEP, SoundEvents.AZALEA_PLACE,
        SoundEvents.AZALEA_HIT, SoundEvents.AZALEA_FALL)
    @JvmField
    val FLOWERING_AZALEA: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.FLOWERING_AZALEA_BREAK, SoundEvents.FLOWERING_AZALEA_STEP,
        SoundEvents.FLOWERING_AZALEA_PLACE, SoundEvents.FLOWERING_AZALEA_HIT, SoundEvents.FLOWERING_AZALEA_FALL)
    @JvmField
    val MOSS_CARPET: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MOSS_CARPET_BREAK, SoundEvents.MOSS_CARPET_STEP,
        SoundEvents.MOSS_CARPET_PLACE, SoundEvents.MOSS_CARPET_HIT, SoundEvents.MOSS_CARPET_FALL)
    @JvmField
    val MOSS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MOSS_BREAK, SoundEvents.MOSS_STEP, SoundEvents.MOSS_PLACE,
        SoundEvents.MOSS_HIT, SoundEvents.MOSS_FALL)
    @JvmField
    val BIG_DRIPLEAF: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.BIG_DRIPLEAF_BREAK, SoundEvents.BIG_DRIPLEAF_STEP,
        SoundEvents.BIG_DRIPLEAF_PLACE, SoundEvents.BIG_DRIPLEAF_HIT, SoundEvents.BIG_DRIPLEAF_FALL)
    @JvmField
    val SMALL_DRIPLEAF: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SMALL_DRIPLEAF_BREAK, SoundEvents.SMALL_DRIPLEAF_STEP,
        SoundEvents.SMALL_DRIPLEAF_PLACE, SoundEvents.SMALL_DRIPLEAF_HIT, SoundEvents.SMALL_DRIPLEAF_FALL)
    @JvmField
    val ROOTED_DIRT: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.ROOTED_DIRT_BREAK, SoundEvents.ROOTED_DIRT_STEP,
        SoundEvents.ROOTED_DIRT_PLACE, SoundEvents.ROOTED_DIRT_HIT, SoundEvents.ROOTED_DIRT_FALL)
    @JvmField
    val HANGING_ROOTS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.HANGING_ROOTS_BREAK, SoundEvents.HANGING_ROOTS_STEP,
        SoundEvents.HANGING_ROOTS_PLACE, SoundEvents.HANGING_ROOTS_HIT, SoundEvents.HANGING_ROOTS_FALL)
    @JvmField
    val AZALEA_LEAVES: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.AZALEA_LEAVES_BREAK, SoundEvents.AZALEA_LEAVES_STEP,
        SoundEvents.AZALEA_LEAVES_PLACE, SoundEvents.AZALEA_LEAVES_HIT, SoundEvents.AZALEA_LEAVES_FALL)
    @JvmField
    val SCULK_SENSOR: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SCULK_SENSOR_BREAK, SoundEvents.SCULK_SENSOR_STEP,
        SoundEvents.SCULK_SENSOR_PLACE, SoundEvents.SCULK_SENSOR_HIT, SoundEvents.SCULK_SENSOR_FALL)
    @JvmField
    val SCULK_CATALYST: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SCULK_CATALYST_BREAK, SoundEvents.SCULK_CATALYST_STEP,
        SoundEvents.SCULK_CATALYST_PLACE, SoundEvents.SCULK_CATALYST_HIT, SoundEvents.SCULK_CATALYST_FALL)
    @JvmField
    val SCULK: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SCULK_BLOCK_BREAK, SoundEvents.SCULK_BLOCK_STEP,
        SoundEvents.SCULK_BLOCK_PLACE, SoundEvents.SCULK_BLOCK_HIT, SoundEvents.SCULK_BLOCK_FALL)
    @JvmField
    val SCULK_VEIN: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SCULK_VEIN_BREAK, SoundEvents.SCULK_VEIN_STEP,
        SoundEvents.SCULK_VEIN_PLACE, SoundEvents.SCULK_VEIN_HIT, SoundEvents.SCULK_VEIN_FALL)
    @JvmField
    val SCULK_SHRIEKER: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.SCULK_SHRIEKER_BREAK, SoundEvents.SCULK_SHRIEKER_STEP,
        SoundEvents.SCULK_SHRIEKER_PLACE, SoundEvents.SCULK_SHRIEKER_HIT, SoundEvents.SCULK_SHRIEKER_FALL)
    @JvmField
    val GLOW_LICHEN: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.GRASS_BREAK, SoundEvents.VINE_STEP, SoundEvents.GRASS_PLACE,
        SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL)
    @JvmField
    val DEEPSLATE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.DEEPSLATE_BREAK, SoundEvents.DEEPSLATE_STEP, SoundEvents.DEEPSLATE_PLACE,
        SoundEvents.DEEPSLATE_HIT, SoundEvents.DEEPSLATE_FALL)
    @JvmField
    val DEEPSLATE_BRICKS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.DEEPSLATE_BRICKS_BREAK, SoundEvents.DEEPSLATE_BRICKS_STEP,
        SoundEvents.DEEPSLATE_BRICKS_PLACE, SoundEvents.DEEPSLATE_BRICKS_HIT, SoundEvents.DEEPSLATE_BRICKS_FALL)
    @JvmField
    val DEEPSLATE_TILES: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.DEEPSLATE_TILES_BREAK, SoundEvents.DEEPSLATE_TILES_STEP,
        SoundEvents.DEEPSLATE_TILES_PLACE, SoundEvents.DEEPSLATE_TILES_HIT, SoundEvents.DEEPSLATE_TILES_FALL)
    @JvmField
    val POLISHED_DEEPSLATE: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.POLISHED_DEEPSLATE_BREAK, SoundEvents.POLISHED_DEEPSLATE_STEP,
        SoundEvents.POLISHED_DEEPSLATE_PLACE, SoundEvents.POLISHED_DEEPSLATE_HIT, SoundEvents.POLISHED_DEEPSLATE_FALL)
    @JvmField
    val FROGLIGHT: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.FROGLIGHT_BREAK, SoundEvents.FROGLIGHT_STEP, SoundEvents.FROGLIGHT_PLACE,
        SoundEvents.FROGLIGHT_HIT, SoundEvents.FROGLIGHT_FALL)
    @JvmField
    val FROGSPAWN: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.FROGSPAWN_BREAK, SoundEvents.FROGSPAWNSTEP, SoundEvents.FROGSPAWN_PLACE,
        SoundEvents.FROGSPAWN_HIT, SoundEvents.FROGSPAWN_FALL)
    @JvmField
    val MANGROVE_ROOTS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MANGROVE_ROOTS_BREAK, SoundEvents.MANGROVE_ROOTS_STEP,
        SoundEvents.MANGROVE_ROOTS_PLACE, SoundEvents.MANGROVE_ROOTS_HIT, SoundEvents.MANGROVE_ROOTS_FALL)
    @JvmField
    val MUDDY_MANGROVE_ROOTS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MUDDY_MANGROVE_ROOTS_BREAK,
        SoundEvents.MUDDY_MANGROVE_ROOTS_STEP, SoundEvents.MUDDY_MANGROVE_ROOTS_PLACE, SoundEvents.MUDDY_MANGROVE_ROOTS_HIT,
        SoundEvents.MUDDY_MANGROVE_ROOTS_FALL)
    @JvmField
    val MUD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MUD_BREAK, SoundEvents.MUD_STEP, SoundEvents.MUD_PLACE, SoundEvents.MUD_HIT,
        SoundEvents.MUD_FALL)
    @JvmField
    val MUD_BRICKS: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.MUD_BRICKS_BREAK, SoundEvents.MUD_BRICKS_STEP,
        SoundEvents.MUD_BRICKS_PLACE, SoundEvents.MUD_BRICKS_HIT, SoundEvents.MUD_BRICKS_FALL)
    @JvmField
    val PACKED_MUD: KryptonBlockSoundGroup = create(1.0F, 1.0F, SoundEvents.PACKED_MUD_BREAK, SoundEvents.PACKED_MUD_STEP,
        SoundEvents.PACKED_MUD_PLACE, SoundEvents.PACKED_MUD_HIT, SoundEvents.PACKED_MUD_FALL)

    @JvmStatic
    private fun create(volume: Float, pitch: Float, breakSound: Event, step: Event, place: Event, hit: Event, fall: Event): KryptonBlockSoundGroup =
        KryptonBlockSoundGroup(volume, pitch, breakSound.get(), step.get(), place.get(), hit.get(), fall.get())
}

private typealias Event = RegistryReference<SoundEvent>
