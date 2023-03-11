/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.world.material

object Materials {

    @JvmField
    val AIR: Material = Material.builder(MaterialColors.NONE).noCollision().notSolidBlocking().notSolid().replaceable().build()
    @JvmField
    val STRUCTURAL_AIR: Material = Material.builder(MaterialColors.NONE).noCollision().notSolidBlocking().notSolid().replaceable().build()
    @JvmField
    val PORTAL: Material = Material.builder(MaterialColors.NONE).noCollision().notSolidBlocking().notSolid().notPushable().build()
    @JvmField
    val CLOTH_DECORATION: Material = Material.builder(MaterialColors.WOOL).noCollision().notSolidBlocking().notSolid().flammable().build()
    @JvmField
    val PLANT: Material = Material.builder(MaterialColors.PLANT).noCollision().notSolidBlocking().notSolid().destroyOnPush().build()
    @JvmField
    val WATER_PLANT: Material = Material.builder(MaterialColors.WATER).noCollision().notSolidBlocking().notSolid().destroyOnPush().build()
    @JvmField
    val REPLACEABLE_PLANT: Material =
        Material.builder(MaterialColors.PLANT).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().flammable().build()
    @JvmField
    val REPLACEABLE_FIREPROOF_PLANT: Material =
        Material.builder(MaterialColors.PLANT).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().build()
    @JvmField
    val REPLACEABLE_WATER_PLANT: Material =
        Material.builder(MaterialColors.WATER).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().build()
    @JvmField
    val WATER: Material =
        Material.builder(MaterialColors.WATER).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().liquid().build()
    @JvmField
    val BUBBLE_COLUMN: Material =
        Material.builder(MaterialColors.WATER).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().liquid().build()
    @JvmField
    val LAVA: Material =
        Material.builder(MaterialColors.FIRE).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().liquid().build()
    @JvmField
    val TOP_SNOW: Material = Material.builder(MaterialColors.SNOW).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().build()
    @JvmField
    val FIRE: Material = Material.builder(MaterialColors.NONE).noCollision().notSolidBlocking().notSolid().destroyOnPush().replaceable().build()
    @JvmField
    val DECORATION: Material = Material.builder(MaterialColors.NONE).noCollision().notSolidBlocking().notSolid().destroyOnPush().build()
    @JvmField
    val WEB: Material = Material.builder(MaterialColors.WOOL).noCollision().notSolidBlocking().destroyOnPush().build()
    @JvmField
    val SCULK: Material = Material.builder(MaterialColors.COLOR_BLACK).build()
    @JvmField
    val BUILDABLE_GLASS: Material = Material.builder(MaterialColors.NONE).build()
    @JvmField
    val CLAY: Material = Material.builder(MaterialColors.CLAY).build()
    @JvmField
    val DIRT: Material = Material.builder(MaterialColors.DIRT).build()
    @JvmField
    val GRASS: Material = Material.builder(MaterialColors.GRASS).build()
    @JvmField
    val ICE_SOLID: Material = Material.builder(MaterialColors.ICE).build()
    @JvmField
    val SAND: Material = Material.builder(MaterialColors.SAND).build()
    @JvmField
    val SPONGE: Material = Material.builder(MaterialColors.COLOR_YELLOW).build()
    @JvmField
    val SHULKER_SHELL: Material = Material.builder(MaterialColors.COLOR_PURPLE).build()
    @JvmField
    val WOOD: Material = Material.builder(MaterialColors.WOOD).flammable().build()
    @JvmField
    val NETHER_WOOD: Material = Material.builder(MaterialColors.WOOD).build()
    @JvmField
    val BAMBOO_SAPLING: Material = Material.builder(MaterialColors.WOOD).flammable().destroyOnPush().noCollision().build()
    @JvmField
    val BAMBOO: Material = Material.builder(MaterialColors.WOOD).flammable().destroyOnPush().build()
    @JvmField
    val WOOL: Material = Material.builder(MaterialColors.WOOL).flammable().build()
    @JvmField
    val EXPLOSIVE: Material = Material.builder(MaterialColors.FIRE).flammable().notSolidBlocking().build()
    @JvmField
    val LEAVES: Material = Material.builder(MaterialColors.PLANT).flammable().notSolidBlocking().destroyOnPush().build()
    @JvmField
    val GLASS: Material = Material.builder(MaterialColors.NONE).notSolidBlocking().build()
    @JvmField
    val ICE: Material = Material.builder(MaterialColors.ICE).notSolidBlocking().build()
    @JvmField
    val CACTUS: Material = Material.builder(MaterialColors.PLANT).notSolidBlocking().destroyOnPush().build()
    @JvmField
    val STONE: Material = Material.builder(MaterialColors.STONE).build()
    @JvmField
    val METAL: Material = Material.builder(MaterialColors.METAL).build()
    @JvmField
    val SNOW: Material = Material.builder(MaterialColors.SNOW).build()
    @JvmField
    val HEAVY_METAL: Material = Material.builder(MaterialColors.METAL).notPushable().build()
    @JvmField
    val BARRIER: Material = Material.builder(MaterialColors.NONE).notPushable().build()
    @JvmField
    val PISTON: Material = Material.builder(MaterialColors.STONE).notPushable().build()
    @JvmField
    val MOSS: Material = Material.builder(MaterialColors.PLANT).destroyOnPush().build()
    @JvmField
    val VEGETABLE: Material = Material.builder(MaterialColors.PLANT).destroyOnPush().build()
    @JvmField
    val EGG: Material = Material.builder(MaterialColors.PLANT).destroyOnPush().build()
    @JvmField
    val CAKE: Material = Material.builder(MaterialColors.NONE).destroyOnPush().build()
    @JvmField
    val AMETHYST: Material = Material.builder(MaterialColors.COLOR_PURPLE).build()
    @JvmField
    val POWDER_SNOW: Material = Material.builder(MaterialColors.SNOW).notSolid().noCollision().build()
    @JvmField
    val FROGSPAWN: Material = Material.builder(MaterialColors.WATER).noCollision().notSolidBlocking().notSolid().destroyOnPush().build()
    @JvmField
    val FROGLIGHT: Material = Material.builder(MaterialColors.NONE).build()
}
