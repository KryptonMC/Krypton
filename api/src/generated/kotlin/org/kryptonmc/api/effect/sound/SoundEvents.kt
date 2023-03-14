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
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.key.Key
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(SoundEvent::class)
public object SoundEvents {

    // @formatter:off
    @JvmField
    public val ALLAY_AMBIENT_WITH_ITEM: RegistryReference<SoundEvent> = of("entity.allay.ambient_with_item")
    @JvmField
    public val ALLAY_AMBIENT_WITHOUT_ITEM: RegistryReference<SoundEvent> = of("entity.allay.ambient_without_item")
    @JvmField
    public val ALLAY_DEATH: RegistryReference<SoundEvent> = of("entity.allay.death")
    @JvmField
    public val ALLAY_HURT: RegistryReference<SoundEvent> = of("entity.allay.hurt")
    @JvmField
    public val ALLAY_ITEM_GIVEN: RegistryReference<SoundEvent> = of("entity.allay.item_given")
    @JvmField
    public val ALLAY_ITEM_TAKEN: RegistryReference<SoundEvent> = of("entity.allay.item_taken")
    @JvmField
    public val ALLAY_THROW: RegistryReference<SoundEvent> = of("entity.allay.item_thrown")
    @JvmField
    public val AMBIENT_CAVE: RegistryReference<SoundEvent> = of("ambient.cave")
    @JvmField
    public val AMBIENT_BASALT_DELTAS_ADDITIONS: RegistryReference<SoundEvent> = of("ambient.basalt_deltas.additions")
    @JvmField
    public val AMBIENT_BASALT_DELTAS_LOOP: RegistryReference<SoundEvent> = of("ambient.basalt_deltas.loop")
    @JvmField
    public val AMBIENT_BASALT_DELTAS_MOOD: RegistryReference<SoundEvent> = of("ambient.basalt_deltas.mood")
    @JvmField
    public val AMBIENT_CRIMSON_FOREST_ADDITIONS: RegistryReference<SoundEvent> = of("ambient.crimson_forest.additions")
    @JvmField
    public val AMBIENT_CRIMSON_FOREST_LOOP: RegistryReference<SoundEvent> = of("ambient.crimson_forest.loop")
    @JvmField
    public val AMBIENT_CRIMSON_FOREST_MOOD: RegistryReference<SoundEvent> = of("ambient.crimson_forest.mood")
    @JvmField
    public val AMBIENT_NETHER_WASTES_ADDITIONS: RegistryReference<SoundEvent> = of("ambient.nether_wastes.additions")
    @JvmField
    public val AMBIENT_NETHER_WASTES_LOOP: RegistryReference<SoundEvent> = of("ambient.nether_wastes.loop")
    @JvmField
    public val AMBIENT_NETHER_WASTES_MOOD: RegistryReference<SoundEvent> = of("ambient.nether_wastes.mood")
    @JvmField
    public val AMBIENT_SOUL_SAND_VALLEY_ADDITIONS: RegistryReference<SoundEvent> = of("ambient.soul_sand_valley.additions")
    @JvmField
    public val AMBIENT_SOUL_SAND_VALLEY_LOOP: RegistryReference<SoundEvent> = of("ambient.soul_sand_valley.loop")
    @JvmField
    public val AMBIENT_SOUL_SAND_VALLEY_MOOD: RegistryReference<SoundEvent> = of("ambient.soul_sand_valley.mood")
    @JvmField
    public val AMBIENT_WARPED_FOREST_ADDITIONS: RegistryReference<SoundEvent> = of("ambient.warped_forest.additions")
    @JvmField
    public val AMBIENT_WARPED_FOREST_LOOP: RegistryReference<SoundEvent> = of("ambient.warped_forest.loop")
    @JvmField
    public val AMBIENT_WARPED_FOREST_MOOD: RegistryReference<SoundEvent> = of("ambient.warped_forest.mood")
    @JvmField
    public val AMBIENT_UNDERWATER_ENTER: RegistryReference<SoundEvent> = of("ambient.underwater.enter")
    @JvmField
    public val AMBIENT_UNDERWATER_EXIT: RegistryReference<SoundEvent> = of("ambient.underwater.exit")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP: RegistryReference<SoundEvent> = of("ambient.underwater.loop")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP_ADDITIONS: RegistryReference<SoundEvent> = of("ambient.underwater.loop.additions")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE: RegistryReference<SoundEvent> = of("ambient.underwater.loop.additions.rare")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE: RegistryReference<SoundEvent> = of("ambient.underwater.loop.additions.ultra_rare")
    @JvmField
    public val AMETHYST_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.amethyst_block.break")
    @JvmField
    public val AMETHYST_BLOCK_CHIME: RegistryReference<SoundEvent> = of("block.amethyst_block.chime")
    @JvmField
    public val AMETHYST_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.amethyst_block.fall")
    @JvmField
    public val AMETHYST_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.amethyst_block.hit")
    @JvmField
    public val AMETHYST_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.amethyst_block.place")
    @JvmField
    public val AMETHYST_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.amethyst_block.step")
    @JvmField
    public val AMETHYST_CLUSTER_BREAK: RegistryReference<SoundEvent> = of("block.amethyst_cluster.break")
    @JvmField
    public val AMETHYST_CLUSTER_FALL: RegistryReference<SoundEvent> = of("block.amethyst_cluster.fall")
    @JvmField
    public val AMETHYST_CLUSTER_HIT: RegistryReference<SoundEvent> = of("block.amethyst_cluster.hit")
    @JvmField
    public val AMETHYST_CLUSTER_PLACE: RegistryReference<SoundEvent> = of("block.amethyst_cluster.place")
    @JvmField
    public val AMETHYST_CLUSTER_STEP: RegistryReference<SoundEvent> = of("block.amethyst_cluster.step")
    @JvmField
    public val ANCIENT_DEBRIS_BREAK: RegistryReference<SoundEvent> = of("block.ancient_debris.break")
    @JvmField
    public val ANCIENT_DEBRIS_STEP: RegistryReference<SoundEvent> = of("block.ancient_debris.step")
    @JvmField
    public val ANCIENT_DEBRIS_PLACE: RegistryReference<SoundEvent> = of("block.ancient_debris.place")
    @JvmField
    public val ANCIENT_DEBRIS_HIT: RegistryReference<SoundEvent> = of("block.ancient_debris.hit")
    @JvmField
    public val ANCIENT_DEBRIS_FALL: RegistryReference<SoundEvent> = of("block.ancient_debris.fall")
    @JvmField
    public val ANVIL_BREAK: RegistryReference<SoundEvent> = of("block.anvil.break")
    @JvmField
    public val ANVIL_DESTROY: RegistryReference<SoundEvent> = of("block.anvil.destroy")
    @JvmField
    public val ANVIL_FALL: RegistryReference<SoundEvent> = of("block.anvil.fall")
    @JvmField
    public val ANVIL_HIT: RegistryReference<SoundEvent> = of("block.anvil.hit")
    @JvmField
    public val ANVIL_LAND: RegistryReference<SoundEvent> = of("block.anvil.land")
    @JvmField
    public val ANVIL_PLACE: RegistryReference<SoundEvent> = of("block.anvil.place")
    @JvmField
    public val ANVIL_STEP: RegistryReference<SoundEvent> = of("block.anvil.step")
    @JvmField
    public val ANVIL_USE: RegistryReference<SoundEvent> = of("block.anvil.use")
    @JvmField
    public val ARMOR_EQUIP_CHAIN: RegistryReference<SoundEvent> = of("item.armor.equip_chain")
    @JvmField
    public val ARMOR_EQUIP_DIAMOND: RegistryReference<SoundEvent> = of("item.armor.equip_diamond")
    @JvmField
    public val ARMOR_EQUIP_ELYTRA: RegistryReference<SoundEvent> = of("item.armor.equip_elytra")
    @JvmField
    public val ARMOR_EQUIP_GENERIC: RegistryReference<SoundEvent> = of("item.armor.equip_generic")
    @JvmField
    public val ARMOR_EQUIP_GOLD: RegistryReference<SoundEvent> = of("item.armor.equip_gold")
    @JvmField
    public val ARMOR_EQUIP_IRON: RegistryReference<SoundEvent> = of("item.armor.equip_iron")
    @JvmField
    public val ARMOR_EQUIP_LEATHER: RegistryReference<SoundEvent> = of("item.armor.equip_leather")
    @JvmField
    public val ARMOR_EQUIP_NETHERITE: RegistryReference<SoundEvent> = of("item.armor.equip_netherite")
    @JvmField
    public val ARMOR_EQUIP_TURTLE: RegistryReference<SoundEvent> = of("item.armor.equip_turtle")
    @JvmField
    public val ARMOR_STAND_BREAK: RegistryReference<SoundEvent> = of("entity.armor_stand.break")
    @JvmField
    public val ARMOR_STAND_FALL: RegistryReference<SoundEvent> = of("entity.armor_stand.fall")
    @JvmField
    public val ARMOR_STAND_HIT: RegistryReference<SoundEvent> = of("entity.armor_stand.hit")
    @JvmField
    public val ARMOR_STAND_PLACE: RegistryReference<SoundEvent> = of("entity.armor_stand.place")
    @JvmField
    public val ARROW_HIT: RegistryReference<SoundEvent> = of("entity.arrow.hit")
    @JvmField
    public val ARROW_HIT_PLAYER: RegistryReference<SoundEvent> = of("entity.arrow.hit_player")
    @JvmField
    public val ARROW_SHOOT: RegistryReference<SoundEvent> = of("entity.arrow.shoot")
    @JvmField
    public val AXE_STRIP: RegistryReference<SoundEvent> = of("item.axe.strip")
    @JvmField
    public val AXE_SCRAPE: RegistryReference<SoundEvent> = of("item.axe.scrape")
    @JvmField
    public val AXE_WAX_OFF: RegistryReference<SoundEvent> = of("item.axe.wax_off")
    @JvmField
    public val AXOLOTL_ATTACK: RegistryReference<SoundEvent> = of("entity.axolotl.attack")
    @JvmField
    public val AXOLOTL_DEATH: RegistryReference<SoundEvent> = of("entity.axolotl.death")
    @JvmField
    public val AXOLOTL_HURT: RegistryReference<SoundEvent> = of("entity.axolotl.hurt")
    @JvmField
    public val AXOLOTL_IDLE_AIR: RegistryReference<SoundEvent> = of("entity.axolotl.idle_air")
    @JvmField
    public val AXOLOTL_IDLE_WATER: RegistryReference<SoundEvent> = of("entity.axolotl.idle_water")
    @JvmField
    public val AXOLOTL_SPLASH: RegistryReference<SoundEvent> = of("entity.axolotl.splash")
    @JvmField
    public val AXOLOTL_SWIM: RegistryReference<SoundEvent> = of("entity.axolotl.swim")
    @JvmField
    public val AZALEA_BREAK: RegistryReference<SoundEvent> = of("block.azalea.break")
    @JvmField
    public val AZALEA_FALL: RegistryReference<SoundEvent> = of("block.azalea.fall")
    @JvmField
    public val AZALEA_HIT: RegistryReference<SoundEvent> = of("block.azalea.hit")
    @JvmField
    public val AZALEA_PLACE: RegistryReference<SoundEvent> = of("block.azalea.place")
    @JvmField
    public val AZALEA_STEP: RegistryReference<SoundEvent> = of("block.azalea.step")
    @JvmField
    public val AZALEA_LEAVES_BREAK: RegistryReference<SoundEvent> = of("block.azalea_leaves.break")
    @JvmField
    public val AZALEA_LEAVES_FALL: RegistryReference<SoundEvent> = of("block.azalea_leaves.fall")
    @JvmField
    public val AZALEA_LEAVES_HIT: RegistryReference<SoundEvent> = of("block.azalea_leaves.hit")
    @JvmField
    public val AZALEA_LEAVES_PLACE: RegistryReference<SoundEvent> = of("block.azalea_leaves.place")
    @JvmField
    public val AZALEA_LEAVES_STEP: RegistryReference<SoundEvent> = of("block.azalea_leaves.step")
    @JvmField
    public val BAMBOO_BREAK: RegistryReference<SoundEvent> = of("block.bamboo.break")
    @JvmField
    public val BAMBOO_FALL: RegistryReference<SoundEvent> = of("block.bamboo.fall")
    @JvmField
    public val BAMBOO_HIT: RegistryReference<SoundEvent> = of("block.bamboo.hit")
    @JvmField
    public val BAMBOO_PLACE: RegistryReference<SoundEvent> = of("block.bamboo.place")
    @JvmField
    public val BAMBOO_STEP: RegistryReference<SoundEvent> = of("block.bamboo.step")
    @JvmField
    public val BAMBOO_SAPLING_BREAK: RegistryReference<SoundEvent> = of("block.bamboo_sapling.break")
    @JvmField
    public val BAMBOO_SAPLING_HIT: RegistryReference<SoundEvent> = of("block.bamboo_sapling.hit")
    @JvmField
    public val BAMBOO_SAPLING_PLACE: RegistryReference<SoundEvent> = of("block.bamboo_sapling.place")
    @JvmField
    public val BAMBOO_WOOD_BREAK: RegistryReference<SoundEvent> = of("block.bamboo_wood.break")
    @JvmField
    public val BAMBOO_WOOD_FALL: RegistryReference<SoundEvent> = of("block.bamboo_wood.fall")
    @JvmField
    public val BAMBOO_WOOD_HIT: RegistryReference<SoundEvent> = of("block.bamboo_wood.hit")
    @JvmField
    public val BAMBOO_WOOD_PLACE: RegistryReference<SoundEvent> = of("block.bamboo_wood.place")
    @JvmField
    public val BAMBOO_WOOD_STEP: RegistryReference<SoundEvent> = of("block.bamboo_wood.step")
    @JvmField
    public val BAMBOO_WOOD_DOOR_CLOSE: RegistryReference<SoundEvent> = of("block.bamboo_wood_door.close")
    @JvmField
    public val BAMBOO_WOOD_DOOR_OPEN: RegistryReference<SoundEvent> = of("block.bamboo_wood_door.open")
    @JvmField
    public val BAMBOO_WOOD_TRAPDOOR_CLOSE: RegistryReference<SoundEvent> = of("block.bamboo_wood_trapdoor.close")
    @JvmField
    public val BAMBOO_WOOD_TRAPDOOR_OPEN: RegistryReference<SoundEvent> = of("block.bamboo_wood_trapdoor.open")
    @JvmField
    public val BAMBOO_WOOD_BUTTON_CLICK_OFF: RegistryReference<SoundEvent> = of("block.bamboo_wood_button.click_off")
    @JvmField
    public val BAMBOO_WOOD_BUTTON_CLICK_ON: RegistryReference<SoundEvent> = of("block.bamboo_wood_button.click_on")
    @JvmField
    public val BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF: RegistryReference<SoundEvent> = of("block.bamboo_wood_pressure_plate.click_off")
    @JvmField
    public val BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON: RegistryReference<SoundEvent> = of("block.bamboo_wood_pressure_plate.click_on")
    @JvmField
    public val BAMBOO_WOOD_FENCE_GATE_CLOSE: RegistryReference<SoundEvent> = of("block.bamboo_wood_fence_gate.close")
    @JvmField
    public val BAMBOO_WOOD_FENCE_GATE_OPEN: RegistryReference<SoundEvent> = of("block.bamboo_wood_fence_gate.open")
    @JvmField
    public val BARREL_CLOSE: RegistryReference<SoundEvent> = of("block.barrel.close")
    @JvmField
    public val BARREL_OPEN: RegistryReference<SoundEvent> = of("block.barrel.open")
    @JvmField
    public val BASALT_BREAK: RegistryReference<SoundEvent> = of("block.basalt.break")
    @JvmField
    public val BASALT_STEP: RegistryReference<SoundEvent> = of("block.basalt.step")
    @JvmField
    public val BASALT_PLACE: RegistryReference<SoundEvent> = of("block.basalt.place")
    @JvmField
    public val BASALT_HIT: RegistryReference<SoundEvent> = of("block.basalt.hit")
    @JvmField
    public val BASALT_FALL: RegistryReference<SoundEvent> = of("block.basalt.fall")
    @JvmField
    public val BAT_AMBIENT: RegistryReference<SoundEvent> = of("entity.bat.ambient")
    @JvmField
    public val BAT_DEATH: RegistryReference<SoundEvent> = of("entity.bat.death")
    @JvmField
    public val BAT_HURT: RegistryReference<SoundEvent> = of("entity.bat.hurt")
    @JvmField
    public val BAT_LOOP: RegistryReference<SoundEvent> = of("entity.bat.loop")
    @JvmField
    public val BAT_TAKEOFF: RegistryReference<SoundEvent> = of("entity.bat.takeoff")
    @JvmField
    public val BEACON_ACTIVATE: RegistryReference<SoundEvent> = of("block.beacon.activate")
    @JvmField
    public val BEACON_AMBIENT: RegistryReference<SoundEvent> = of("block.beacon.ambient")
    @JvmField
    public val BEACON_DEACTIVATE: RegistryReference<SoundEvent> = of("block.beacon.deactivate")
    @JvmField
    public val BEACON_POWER_SELECT: RegistryReference<SoundEvent> = of("block.beacon.power_select")
    @JvmField
    public val BEE_DEATH: RegistryReference<SoundEvent> = of("entity.bee.death")
    @JvmField
    public val BEE_HURT: RegistryReference<SoundEvent> = of("entity.bee.hurt")
    @JvmField
    public val BEE_LOOP_AGGRESSIVE: RegistryReference<SoundEvent> = of("entity.bee.loop_aggressive")
    @JvmField
    public val BEE_LOOP: RegistryReference<SoundEvent> = of("entity.bee.loop")
    @JvmField
    public val BEE_STING: RegistryReference<SoundEvent> = of("entity.bee.sting")
    @JvmField
    public val BEE_POLLINATE: RegistryReference<SoundEvent> = of("entity.bee.pollinate")
    @JvmField
    public val BEEHIVE_DRIP: RegistryReference<SoundEvent> = of("block.beehive.drip")
    @JvmField
    public val BEEHIVE_ENTER: RegistryReference<SoundEvent> = of("block.beehive.enter")
    @JvmField
    public val BEEHIVE_EXIT: RegistryReference<SoundEvent> = of("block.beehive.exit")
    @JvmField
    public val BEEHIVE_SHEAR: RegistryReference<SoundEvent> = of("block.beehive.shear")
    @JvmField
    public val BEEHIVE_WORK: RegistryReference<SoundEvent> = of("block.beehive.work")
    @JvmField
    public val BELL_BLOCK: RegistryReference<SoundEvent> = of("block.bell.use")
    @JvmField
    public val BELL_RESONATE: RegistryReference<SoundEvent> = of("block.bell.resonate")
    @JvmField
    public val BIG_DRIPLEAF_BREAK: RegistryReference<SoundEvent> = of("block.big_dripleaf.break")
    @JvmField
    public val BIG_DRIPLEAF_FALL: RegistryReference<SoundEvent> = of("block.big_dripleaf.fall")
    @JvmField
    public val BIG_DRIPLEAF_HIT: RegistryReference<SoundEvent> = of("block.big_dripleaf.hit")
    @JvmField
    public val BIG_DRIPLEAF_PLACE: RegistryReference<SoundEvent> = of("block.big_dripleaf.place")
    @JvmField
    public val BIG_DRIPLEAF_STEP: RegistryReference<SoundEvent> = of("block.big_dripleaf.step")
    @JvmField
    public val BLAZE_AMBIENT: RegistryReference<SoundEvent> = of("entity.blaze.ambient")
    @JvmField
    public val BLAZE_BURN: RegistryReference<SoundEvent> = of("entity.blaze.burn")
    @JvmField
    public val BLAZE_DEATH: RegistryReference<SoundEvent> = of("entity.blaze.death")
    @JvmField
    public val BLAZE_HURT: RegistryReference<SoundEvent> = of("entity.blaze.hurt")
    @JvmField
    public val BLAZE_SHOOT: RegistryReference<SoundEvent> = of("entity.blaze.shoot")
    @JvmField
    public val BOAT_PADDLE_LAND: RegistryReference<SoundEvent> = of("entity.boat.paddle_land")
    @JvmField
    public val BOAT_PADDLE_WATER: RegistryReference<SoundEvent> = of("entity.boat.paddle_water")
    @JvmField
    public val BONE_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.bone_block.break")
    @JvmField
    public val BONE_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.bone_block.fall")
    @JvmField
    public val BONE_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.bone_block.hit")
    @JvmField
    public val BONE_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.bone_block.place")
    @JvmField
    public val BONE_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.bone_block.step")
    @JvmField
    public val BONE_MEAL_USE: RegistryReference<SoundEvent> = of("item.bone_meal.use")
    @JvmField
    public val BOOK_PAGE_TURN: RegistryReference<SoundEvent> = of("item.book.page_turn")
    @JvmField
    public val BOOK_PUT: RegistryReference<SoundEvent> = of("item.book.put")
    @JvmField
    public val BLASTFURNACE_FIRE_CRACKLE: RegistryReference<SoundEvent> = of("block.blastfurnace.fire_crackle")
    @JvmField
    public val BOTTLE_EMPTY: RegistryReference<SoundEvent> = of("item.bottle.empty")
    @JvmField
    public val BOTTLE_FILL: RegistryReference<SoundEvent> = of("item.bottle.fill")
    @JvmField
    public val BOTTLE_FILL_DRAGONBREATH: RegistryReference<SoundEvent> = of("item.bottle.fill_dragonbreath")
    @JvmField
    public val BREWING_STAND_BREW: RegistryReference<SoundEvent> = of("block.brewing_stand.brew")
    @JvmField
    public val BUBBLE_COLUMN_BUBBLE_POP: RegistryReference<SoundEvent> = of("block.bubble_column.bubble_pop")
    @JvmField
    public val BUBBLE_COLUMN_UPWARDS_AMBIENT: RegistryReference<SoundEvent> = of("block.bubble_column.upwards_ambient")
    @JvmField
    public val BUBBLE_COLUMN_UPWARDS_INSIDE: RegistryReference<SoundEvent> = of("block.bubble_column.upwards_inside")
    @JvmField
    public val BUBBLE_COLUMN_WHIRLPOOL_AMBIENT: RegistryReference<SoundEvent> = of("block.bubble_column.whirlpool_ambient")
    @JvmField
    public val BUBBLE_COLUMN_WHIRLPOOL_INSIDE: RegistryReference<SoundEvent> = of("block.bubble_column.whirlpool_inside")
    @JvmField
    public val BUCKET_EMPTY: RegistryReference<SoundEvent> = of("item.bucket.empty")
    @JvmField
    public val BUCKET_EMPTY_AXOLOTL: RegistryReference<SoundEvent> = of("item.bucket.empty_axolotl")
    @JvmField
    public val BUCKET_EMPTY_FISH: RegistryReference<SoundEvent> = of("item.bucket.empty_fish")
    @JvmField
    public val BUCKET_EMPTY_LAVA: RegistryReference<SoundEvent> = of("item.bucket.empty_lava")
    @JvmField
    public val BUCKET_EMPTY_POWDER_SNOW: RegistryReference<SoundEvent> = of("item.bucket.empty_powder_snow")
    @JvmField
    public val BUCKET_EMPTY_TADPOLE: RegistryReference<SoundEvent> = of("item.bucket.empty_tadpole")
    @JvmField
    public val BUCKET_FILL: RegistryReference<SoundEvent> = of("item.bucket.fill")
    @JvmField
    public val BUCKET_FILL_AXOLOTL: RegistryReference<SoundEvent> = of("item.bucket.fill_axolotl")
    @JvmField
    public val BUCKET_FILL_FISH: RegistryReference<SoundEvent> = of("item.bucket.fill_fish")
    @JvmField
    public val BUCKET_FILL_LAVA: RegistryReference<SoundEvent> = of("item.bucket.fill_lava")
    @JvmField
    public val BUCKET_FILL_POWDER_SNOW: RegistryReference<SoundEvent> = of("item.bucket.fill_powder_snow")
    @JvmField
    public val BUCKET_FILL_TADPOLE: RegistryReference<SoundEvent> = of("item.bucket.fill_tadpole")
    @JvmField
    public val BUNDLE_DROP_CONTENTS: RegistryReference<SoundEvent> = of("item.bundle.drop_contents")
    @JvmField
    public val BUNDLE_INSERT: RegistryReference<SoundEvent> = of("item.bundle.insert")
    @JvmField
    public val BUNDLE_REMOVE_ONE: RegistryReference<SoundEvent> = of("item.bundle.remove_one")
    @JvmField
    public val CAKE_ADD_CANDLE: RegistryReference<SoundEvent> = of("block.cake.add_candle")
    @JvmField
    public val CALCITE_BREAK: RegistryReference<SoundEvent> = of("block.calcite.break")
    @JvmField
    public val CALCITE_STEP: RegistryReference<SoundEvent> = of("block.calcite.step")
    @JvmField
    public val CALCITE_PLACE: RegistryReference<SoundEvent> = of("block.calcite.place")
    @JvmField
    public val CALCITE_HIT: RegistryReference<SoundEvent> = of("block.calcite.hit")
    @JvmField
    public val CALCITE_FALL: RegistryReference<SoundEvent> = of("block.calcite.fall")
    @JvmField
    public val CAMEL_AMBIENT: RegistryReference<SoundEvent> = of("entity.camel.ambient")
    @JvmField
    public val CAMEL_DASH: RegistryReference<SoundEvent> = of("entity.camel.dash")
    @JvmField
    public val CAMEL_DASH_READY: RegistryReference<SoundEvent> = of("entity.camel.dash_ready")
    @JvmField
    public val CAMEL_DEATH: RegistryReference<SoundEvent> = of("entity.camel.death")
    @JvmField
    public val CAMEL_EAT: RegistryReference<SoundEvent> = of("entity.camel.eat")
    @JvmField
    public val CAMEL_HURT: RegistryReference<SoundEvent> = of("entity.camel.hurt")
    @JvmField
    public val CAMEL_SADDLE: RegistryReference<SoundEvent> = of("entity.camel.saddle")
    @JvmField
    public val CAMEL_SIT: RegistryReference<SoundEvent> = of("entity.camel.sit")
    @JvmField
    public val CAMEL_STAND: RegistryReference<SoundEvent> = of("entity.camel.stand")
    @JvmField
    public val CAMEL_STEP: RegistryReference<SoundEvent> = of("entity.camel.step")
    @JvmField
    public val CAMEL_STEP_SAND: RegistryReference<SoundEvent> = of("entity.camel.step_sand")
    @JvmField
    public val CAMPFIRE_CRACKLE: RegistryReference<SoundEvent> = of("block.campfire.crackle")
    @JvmField
    public val CANDLE_AMBIENT: RegistryReference<SoundEvent> = of("block.candle.ambient")
    @JvmField
    public val CANDLE_BREAK: RegistryReference<SoundEvent> = of("block.candle.break")
    @JvmField
    public val CANDLE_EXTINGUISH: RegistryReference<SoundEvent> = of("block.candle.extinguish")
    @JvmField
    public val CANDLE_FALL: RegistryReference<SoundEvent> = of("block.candle.fall")
    @JvmField
    public val CANDLE_HIT: RegistryReference<SoundEvent> = of("block.candle.hit")
    @JvmField
    public val CANDLE_PLACE: RegistryReference<SoundEvent> = of("block.candle.place")
    @JvmField
    public val CANDLE_STEP: RegistryReference<SoundEvent> = of("block.candle.step")
    @JvmField
    public val CAT_AMBIENT: RegistryReference<SoundEvent> = of("entity.cat.ambient")
    @JvmField
    public val CAT_STRAY_AMBIENT: RegistryReference<SoundEvent> = of("entity.cat.stray_ambient")
    @JvmField
    public val CAT_DEATH: RegistryReference<SoundEvent> = of("entity.cat.death")
    @JvmField
    public val CAT_EAT: RegistryReference<SoundEvent> = of("entity.cat.eat")
    @JvmField
    public val CAT_HISS: RegistryReference<SoundEvent> = of("entity.cat.hiss")
    @JvmField
    public val CAT_BEG_FOR_FOOD: RegistryReference<SoundEvent> = of("entity.cat.beg_for_food")
    @JvmField
    public val CAT_HURT: RegistryReference<SoundEvent> = of("entity.cat.hurt")
    @JvmField
    public val CAT_PURR: RegistryReference<SoundEvent> = of("entity.cat.purr")
    @JvmField
    public val CAT_PURREOW: RegistryReference<SoundEvent> = of("entity.cat.purreow")
    @JvmField
    public val CAVE_VINES_BREAK: RegistryReference<SoundEvent> = of("block.cave_vines.break")
    @JvmField
    public val CAVE_VINES_FALL: RegistryReference<SoundEvent> = of("block.cave_vines.fall")
    @JvmField
    public val CAVE_VINES_HIT: RegistryReference<SoundEvent> = of("block.cave_vines.hit")
    @JvmField
    public val CAVE_VINES_PLACE: RegistryReference<SoundEvent> = of("block.cave_vines.place")
    @JvmField
    public val CAVE_VINES_STEP: RegistryReference<SoundEvent> = of("block.cave_vines.step")
    @JvmField
    public val CAVE_VINES_PICK_BERRIES: RegistryReference<SoundEvent> = of("block.cave_vines.pick_berries")
    @JvmField
    public val CHAIN_BREAK: RegistryReference<SoundEvent> = of("block.chain.break")
    @JvmField
    public val CHAIN_FALL: RegistryReference<SoundEvent> = of("block.chain.fall")
    @JvmField
    public val CHAIN_HIT: RegistryReference<SoundEvent> = of("block.chain.hit")
    @JvmField
    public val CHAIN_PLACE: RegistryReference<SoundEvent> = of("block.chain.place")
    @JvmField
    public val CHAIN_STEP: RegistryReference<SoundEvent> = of("block.chain.step")
    @JvmField
    public val CHEST_CLOSE: RegistryReference<SoundEvent> = of("block.chest.close")
    @JvmField
    public val CHEST_LOCKED: RegistryReference<SoundEvent> = of("block.chest.locked")
    @JvmField
    public val CHEST_OPEN: RegistryReference<SoundEvent> = of("block.chest.open")
    @JvmField
    public val CHICKEN_AMBIENT: RegistryReference<SoundEvent> = of("entity.chicken.ambient")
    @JvmField
    public val CHICKEN_DEATH: RegistryReference<SoundEvent> = of("entity.chicken.death")
    @JvmField
    public val CHICKEN_EGG: RegistryReference<SoundEvent> = of("entity.chicken.egg")
    @JvmField
    public val CHICKEN_HURT: RegistryReference<SoundEvent> = of("entity.chicken.hurt")
    @JvmField
    public val CHICKEN_STEP: RegistryReference<SoundEvent> = of("entity.chicken.step")
    @JvmField
    public val CHISELED_BOOKSHELF_BREAK: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.break")
    @JvmField
    public val CHISELED_BOOKSHELF_FALL: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.fall")
    @JvmField
    public val CHISELED_BOOKSHELF_HIT: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.hit")
    @JvmField
    public val CHISELED_BOOKSHELF_INSERT: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.insert")
    @JvmField
    public val CHISELED_BOOKSHELF_INSERT_ENCHANTED: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.insert.enchanted")
    @JvmField
    public val CHISELED_BOOKSHELF_STEP: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.step")
    @JvmField
    public val CHISELED_BOOKSHELF_PICKUP: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.pickup")
    @JvmField
    public val CHISELED_BOOKSHELF_PICKUP_ENCHANTED: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.pickup.enchanted")
    @JvmField
    public val CHISELED_BOOKSHELF_PLACE: RegistryReference<SoundEvent> = of("block.chiseled_bookshelf.place")
    @JvmField
    public val CHORUS_FLOWER_DEATH: RegistryReference<SoundEvent> = of("block.chorus_flower.death")
    @JvmField
    public val CHORUS_FLOWER_GROW: RegistryReference<SoundEvent> = of("block.chorus_flower.grow")
    @JvmField
    public val CHORUS_FRUIT_TELEPORT: RegistryReference<SoundEvent> = of("item.chorus_fruit.teleport")
    @JvmField
    public val COD_AMBIENT: RegistryReference<SoundEvent> = of("entity.cod.ambient")
    @JvmField
    public val COD_DEATH: RegistryReference<SoundEvent> = of("entity.cod.death")
    @JvmField
    public val COD_FLOP: RegistryReference<SoundEvent> = of("entity.cod.flop")
    @JvmField
    public val COD_HURT: RegistryReference<SoundEvent> = of("entity.cod.hurt")
    @JvmField
    public val COMPARATOR_CLICK: RegistryReference<SoundEvent> = of("block.comparator.click")
    @JvmField
    public val COMPOSTER_EMPTY: RegistryReference<SoundEvent> = of("block.composter.empty")
    @JvmField
    public val COMPOSTER_FILL: RegistryReference<SoundEvent> = of("block.composter.fill")
    @JvmField
    public val COMPOSTER_FILL_SUCCESS: RegistryReference<SoundEvent> = of("block.composter.fill_success")
    @JvmField
    public val COMPOSTER_READY: RegistryReference<SoundEvent> = of("block.composter.ready")
    @JvmField
    public val CONDUIT_ACTIVATE: RegistryReference<SoundEvent> = of("block.conduit.activate")
    @JvmField
    public val CONDUIT_AMBIENT: RegistryReference<SoundEvent> = of("block.conduit.ambient")
    @JvmField
    public val CONDUIT_AMBIENT_SHORT: RegistryReference<SoundEvent> = of("block.conduit.ambient.short")
    @JvmField
    public val CONDUIT_ATTACK_TARGET: RegistryReference<SoundEvent> = of("block.conduit.attack.target")
    @JvmField
    public val CONDUIT_DEACTIVATE: RegistryReference<SoundEvent> = of("block.conduit.deactivate")
    @JvmField
    public val COPPER_BREAK: RegistryReference<SoundEvent> = of("block.copper.break")
    @JvmField
    public val COPPER_STEP: RegistryReference<SoundEvent> = of("block.copper.step")
    @JvmField
    public val COPPER_PLACE: RegistryReference<SoundEvent> = of("block.copper.place")
    @JvmField
    public val COPPER_HIT: RegistryReference<SoundEvent> = of("block.copper.hit")
    @JvmField
    public val COPPER_FALL: RegistryReference<SoundEvent> = of("block.copper.fall")
    @JvmField
    public val CORAL_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.coral_block.break")
    @JvmField
    public val CORAL_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.coral_block.fall")
    @JvmField
    public val CORAL_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.coral_block.hit")
    @JvmField
    public val CORAL_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.coral_block.place")
    @JvmField
    public val CORAL_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.coral_block.step")
    @JvmField
    public val COW_AMBIENT: RegistryReference<SoundEvent> = of("entity.cow.ambient")
    @JvmField
    public val COW_DEATH: RegistryReference<SoundEvent> = of("entity.cow.death")
    @JvmField
    public val COW_HURT: RegistryReference<SoundEvent> = of("entity.cow.hurt")
    @JvmField
    public val COW_MILK: RegistryReference<SoundEvent> = of("entity.cow.milk")
    @JvmField
    public val COW_STEP: RegistryReference<SoundEvent> = of("entity.cow.step")
    @JvmField
    public val CREEPER_DEATH: RegistryReference<SoundEvent> = of("entity.creeper.death")
    @JvmField
    public val CREEPER_HURT: RegistryReference<SoundEvent> = of("entity.creeper.hurt")
    @JvmField
    public val CREEPER_PRIMED: RegistryReference<SoundEvent> = of("entity.creeper.primed")
    @JvmField
    public val CROP_BREAK: RegistryReference<SoundEvent> = of("block.crop.break")
    @JvmField
    public val CROP_PLANTED: RegistryReference<SoundEvent> = of("item.crop.plant")
    @JvmField
    public val CROSSBOW_HIT: RegistryReference<SoundEvent> = of("item.crossbow.hit")
    @JvmField
    public val CROSSBOW_LOADING_END: RegistryReference<SoundEvent> = of("item.crossbow.loading_end")
    @JvmField
    public val CROSSBOW_LOADING_MIDDLE: RegistryReference<SoundEvent> = of("item.crossbow.loading_middle")
    @JvmField
    public val CROSSBOW_LOADING_START: RegistryReference<SoundEvent> = of("item.crossbow.loading_start")
    @JvmField
    public val CROSSBOW_QUICK_CHARGE_1: RegistryReference<SoundEvent> = of("item.crossbow.quick_charge_1")
    @JvmField
    public val CROSSBOW_QUICK_CHARGE_2: RegistryReference<SoundEvent> = of("item.crossbow.quick_charge_2")
    @JvmField
    public val CROSSBOW_QUICK_CHARGE_3: RegistryReference<SoundEvent> = of("item.crossbow.quick_charge_3")
    @JvmField
    public val CROSSBOW_SHOOT: RegistryReference<SoundEvent> = of("item.crossbow.shoot")
    @JvmField
    public val DEEPSLATE_BRICKS_BREAK: RegistryReference<SoundEvent> = of("block.deepslate_bricks.break")
    @JvmField
    public val DEEPSLATE_BRICKS_FALL: RegistryReference<SoundEvent> = of("block.deepslate_bricks.fall")
    @JvmField
    public val DEEPSLATE_BRICKS_HIT: RegistryReference<SoundEvent> = of("block.deepslate_bricks.hit")
    @JvmField
    public val DEEPSLATE_BRICKS_PLACE: RegistryReference<SoundEvent> = of("block.deepslate_bricks.place")
    @JvmField
    public val DEEPSLATE_BRICKS_STEP: RegistryReference<SoundEvent> = of("block.deepslate_bricks.step")
    @JvmField
    public val DEEPSLATE_BREAK: RegistryReference<SoundEvent> = of("block.deepslate.break")
    @JvmField
    public val DEEPSLATE_FALL: RegistryReference<SoundEvent> = of("block.deepslate.fall")
    @JvmField
    public val DEEPSLATE_HIT: RegistryReference<SoundEvent> = of("block.deepslate.hit")
    @JvmField
    public val DEEPSLATE_PLACE: RegistryReference<SoundEvent> = of("block.deepslate.place")
    @JvmField
    public val DEEPSLATE_STEP: RegistryReference<SoundEvent> = of("block.deepslate.step")
    @JvmField
    public val DEEPSLATE_TILES_BREAK: RegistryReference<SoundEvent> = of("block.deepslate_tiles.break")
    @JvmField
    public val DEEPSLATE_TILES_FALL: RegistryReference<SoundEvent> = of("block.deepslate_tiles.fall")
    @JvmField
    public val DEEPSLATE_TILES_HIT: RegistryReference<SoundEvent> = of("block.deepslate_tiles.hit")
    @JvmField
    public val DEEPSLATE_TILES_PLACE: RegistryReference<SoundEvent> = of("block.deepslate_tiles.place")
    @JvmField
    public val DEEPSLATE_TILES_STEP: RegistryReference<SoundEvent> = of("block.deepslate_tiles.step")
    @JvmField
    public val DISPENSER_DISPENSE: RegistryReference<SoundEvent> = of("block.dispenser.dispense")
    @JvmField
    public val DISPENSER_FAIL: RegistryReference<SoundEvent> = of("block.dispenser.fail")
    @JvmField
    public val DISPENSER_LAUNCH: RegistryReference<SoundEvent> = of("block.dispenser.launch")
    @JvmField
    public val DOLPHIN_AMBIENT: RegistryReference<SoundEvent> = of("entity.dolphin.ambient")
    @JvmField
    public val DOLPHIN_AMBIENT_WATER: RegistryReference<SoundEvent> = of("entity.dolphin.ambient_water")
    @JvmField
    public val DOLPHIN_ATTACK: RegistryReference<SoundEvent> = of("entity.dolphin.attack")
    @JvmField
    public val DOLPHIN_DEATH: RegistryReference<SoundEvent> = of("entity.dolphin.death")
    @JvmField
    public val DOLPHIN_EAT: RegistryReference<SoundEvent> = of("entity.dolphin.eat")
    @JvmField
    public val DOLPHIN_HURT: RegistryReference<SoundEvent> = of("entity.dolphin.hurt")
    @JvmField
    public val DOLPHIN_JUMP: RegistryReference<SoundEvent> = of("entity.dolphin.jump")
    @JvmField
    public val DOLPHIN_PLAY: RegistryReference<SoundEvent> = of("entity.dolphin.play")
    @JvmField
    public val DOLPHIN_SPLASH: RegistryReference<SoundEvent> = of("entity.dolphin.splash")
    @JvmField
    public val DOLPHIN_SWIM: RegistryReference<SoundEvent> = of("entity.dolphin.swim")
    @JvmField
    public val DONKEY_AMBIENT: RegistryReference<SoundEvent> = of("entity.donkey.ambient")
    @JvmField
    public val DONKEY_ANGRY: RegistryReference<SoundEvent> = of("entity.donkey.angry")
    @JvmField
    public val DONKEY_CHEST: RegistryReference<SoundEvent> = of("entity.donkey.chest")
    @JvmField
    public val DONKEY_DEATH: RegistryReference<SoundEvent> = of("entity.donkey.death")
    @JvmField
    public val DONKEY_EAT: RegistryReference<SoundEvent> = of("entity.donkey.eat")
    @JvmField
    public val DONKEY_HURT: RegistryReference<SoundEvent> = of("entity.donkey.hurt")
    @JvmField
    public val DRIPSTONE_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.dripstone_block.break")
    @JvmField
    public val DRIPSTONE_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.dripstone_block.step")
    @JvmField
    public val DRIPSTONE_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.dripstone_block.place")
    @JvmField
    public val DRIPSTONE_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.dripstone_block.hit")
    @JvmField
    public val DRIPSTONE_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.dripstone_block.fall")
    @JvmField
    public val POINTED_DRIPSTONE_BREAK: RegistryReference<SoundEvent> = of("block.pointed_dripstone.break")
    @JvmField
    public val POINTED_DRIPSTONE_STEP: RegistryReference<SoundEvent> = of("block.pointed_dripstone.step")
    @JvmField
    public val POINTED_DRIPSTONE_PLACE: RegistryReference<SoundEvent> = of("block.pointed_dripstone.place")
    @JvmField
    public val POINTED_DRIPSTONE_HIT: RegistryReference<SoundEvent> = of("block.pointed_dripstone.hit")
    @JvmField
    public val POINTED_DRIPSTONE_FALL: RegistryReference<SoundEvent> = of("block.pointed_dripstone.fall")
    @JvmField
    public val POINTED_DRIPSTONE_LAND: RegistryReference<SoundEvent> = of("block.pointed_dripstone.land")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_LAVA: RegistryReference<SoundEvent> = of("block.pointed_dripstone.drip_lava")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_WATER: RegistryReference<SoundEvent> = of("block.pointed_dripstone.drip_water")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON: RegistryReference<SoundEvent> = of("block.pointed_dripstone.drip_lava_into_cauldron")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON: RegistryReference<SoundEvent> = of("block.pointed_dripstone.drip_water_into_cauldron")
    @JvmField
    public val BIG_DRIPLEAF_TILT_DOWN: RegistryReference<SoundEvent> = of("block.big_dripleaf.tilt_down")
    @JvmField
    public val BIG_DRIPLEAF_TILT_UP: RegistryReference<SoundEvent> = of("block.big_dripleaf.tilt_up")
    @JvmField
    public val DROWNED_AMBIENT: RegistryReference<SoundEvent> = of("entity.drowned.ambient")
    @JvmField
    public val DROWNED_AMBIENT_WATER: RegistryReference<SoundEvent> = of("entity.drowned.ambient_water")
    @JvmField
    public val DROWNED_DEATH: RegistryReference<SoundEvent> = of("entity.drowned.death")
    @JvmField
    public val DROWNED_DEATH_WATER: RegistryReference<SoundEvent> = of("entity.drowned.death_water")
    @JvmField
    public val DROWNED_HURT: RegistryReference<SoundEvent> = of("entity.drowned.hurt")
    @JvmField
    public val DROWNED_HURT_WATER: RegistryReference<SoundEvent> = of("entity.drowned.hurt_water")
    @JvmField
    public val DROWNED_SHOOT: RegistryReference<SoundEvent> = of("entity.drowned.shoot")
    @JvmField
    public val DROWNED_STEP: RegistryReference<SoundEvent> = of("entity.drowned.step")
    @JvmField
    public val DROWNED_SWIM: RegistryReference<SoundEvent> = of("entity.drowned.swim")
    @JvmField
    public val DYE_USE: RegistryReference<SoundEvent> = of("item.dye.use")
    @JvmField
    public val EGG_THROW: RegistryReference<SoundEvent> = of("entity.egg.throw")
    @JvmField
    public val ELDER_GUARDIAN_AMBIENT: RegistryReference<SoundEvent> = of("entity.elder_guardian.ambient")
    @JvmField
    public val ELDER_GUARDIAN_AMBIENT_LAND: RegistryReference<SoundEvent> = of("entity.elder_guardian.ambient_land")
    @JvmField
    public val ELDER_GUARDIAN_CURSE: RegistryReference<SoundEvent> = of("entity.elder_guardian.curse")
    @JvmField
    public val ELDER_GUARDIAN_DEATH: RegistryReference<SoundEvent> = of("entity.elder_guardian.death")
    @JvmField
    public val ELDER_GUARDIAN_DEATH_LAND: RegistryReference<SoundEvent> = of("entity.elder_guardian.death_land")
    @JvmField
    public val ELDER_GUARDIAN_FLOP: RegistryReference<SoundEvent> = of("entity.elder_guardian.flop")
    @JvmField
    public val ELDER_GUARDIAN_HURT: RegistryReference<SoundEvent> = of("entity.elder_guardian.hurt")
    @JvmField
    public val ELDER_GUARDIAN_HURT_LAND: RegistryReference<SoundEvent> = of("entity.elder_guardian.hurt_land")
    @JvmField
    public val ELYTRA_FLYING: RegistryReference<SoundEvent> = of("item.elytra.flying")
    @JvmField
    public val ENCHANTMENT_TABLE_USE: RegistryReference<SoundEvent> = of("block.enchantment_table.use")
    @JvmField
    public val ENDER_CHEST_CLOSE: RegistryReference<SoundEvent> = of("block.ender_chest.close")
    @JvmField
    public val ENDER_CHEST_OPEN: RegistryReference<SoundEvent> = of("block.ender_chest.open")
    @JvmField
    public val ENDER_DRAGON_AMBIENT: RegistryReference<SoundEvent> = of("entity.ender_dragon.ambient")
    @JvmField
    public val ENDER_DRAGON_DEATH: RegistryReference<SoundEvent> = of("entity.ender_dragon.death")
    @JvmField
    public val DRAGON_FIREBALL_EXPLODE: RegistryReference<SoundEvent> = of("entity.dragon_fireball.explode")
    @JvmField
    public val ENDER_DRAGON_FLAP: RegistryReference<SoundEvent> = of("entity.ender_dragon.flap")
    @JvmField
    public val ENDER_DRAGON_GROWL: RegistryReference<SoundEvent> = of("entity.ender_dragon.growl")
    @JvmField
    public val ENDER_DRAGON_HURT: RegistryReference<SoundEvent> = of("entity.ender_dragon.hurt")
    @JvmField
    public val ENDER_DRAGON_SHOOT: RegistryReference<SoundEvent> = of("entity.ender_dragon.shoot")
    @JvmField
    public val ENDER_EYE_DEATH: RegistryReference<SoundEvent> = of("entity.ender_eye.death")
    @JvmField
    public val ENDER_EYE_LAUNCH: RegistryReference<SoundEvent> = of("entity.ender_eye.launch")
    @JvmField
    public val ENDERMAN_AMBIENT: RegistryReference<SoundEvent> = of("entity.enderman.ambient")
    @JvmField
    public val ENDERMAN_DEATH: RegistryReference<SoundEvent> = of("entity.enderman.death")
    @JvmField
    public val ENDERMAN_HURT: RegistryReference<SoundEvent> = of("entity.enderman.hurt")
    @JvmField
    public val ENDERMAN_SCREAM: RegistryReference<SoundEvent> = of("entity.enderman.scream")
    @JvmField
    public val ENDERMAN_STARE: RegistryReference<SoundEvent> = of("entity.enderman.stare")
    @JvmField
    public val ENDERMAN_TELEPORT: RegistryReference<SoundEvent> = of("entity.enderman.teleport")
    @JvmField
    public val ENDERMITE_AMBIENT: RegistryReference<SoundEvent> = of("entity.endermite.ambient")
    @JvmField
    public val ENDERMITE_DEATH: RegistryReference<SoundEvent> = of("entity.endermite.death")
    @JvmField
    public val ENDERMITE_HURT: RegistryReference<SoundEvent> = of("entity.endermite.hurt")
    @JvmField
    public val ENDERMITE_STEP: RegistryReference<SoundEvent> = of("entity.endermite.step")
    @JvmField
    public val ENDER_PEARL_THROW: RegistryReference<SoundEvent> = of("entity.ender_pearl.throw")
    @JvmField
    public val END_GATEWAY_SPAWN: RegistryReference<SoundEvent> = of("block.end_gateway.spawn")
    @JvmField
    public val END_PORTAL_FRAME_FILL: RegistryReference<SoundEvent> = of("block.end_portal_frame.fill")
    @JvmField
    public val END_PORTAL_SPAWN: RegistryReference<SoundEvent> = of("block.end_portal.spawn")
    @JvmField
    public val EVOKER_AMBIENT: RegistryReference<SoundEvent> = of("entity.evoker.ambient")
    @JvmField
    public val EVOKER_CAST_SPELL: RegistryReference<SoundEvent> = of("entity.evoker.cast_spell")
    @JvmField
    public val EVOKER_CELEBRATE: RegistryReference<SoundEvent> = of("entity.evoker.celebrate")
    @JvmField
    public val EVOKER_DEATH: RegistryReference<SoundEvent> = of("entity.evoker.death")
    @JvmField
    public val EVOKER_FANGS_ATTACK: RegistryReference<SoundEvent> = of("entity.evoker_fangs.attack")
    @JvmField
    public val EVOKER_HURT: RegistryReference<SoundEvent> = of("entity.evoker.hurt")
    @JvmField
    public val EVOKER_PREPARE_ATTACK: RegistryReference<SoundEvent> = of("entity.evoker.prepare_attack")
    @JvmField
    public val EVOKER_PREPARE_SUMMON: RegistryReference<SoundEvent> = of("entity.evoker.prepare_summon")
    @JvmField
    public val EVOKER_PREPARE_WOLOLO: RegistryReference<SoundEvent> = of("entity.evoker.prepare_wololo")
    @JvmField
    public val EXPERIENCE_BOTTLE_THROW: RegistryReference<SoundEvent> = of("entity.experience_bottle.throw")
    @JvmField
    public val EXPERIENCE_ORB_PICKUP: RegistryReference<SoundEvent> = of("entity.experience_orb.pickup")
    @JvmField
    public val FENCE_GATE_CLOSE: RegistryReference<SoundEvent> = of("block.fence_gate.close")
    @JvmField
    public val FENCE_GATE_OPEN: RegistryReference<SoundEvent> = of("block.fence_gate.open")
    @JvmField
    public val FIRECHARGE_USE: RegistryReference<SoundEvent> = of("item.firecharge.use")
    @JvmField
    public val FIREWORK_ROCKET_BLAST: RegistryReference<SoundEvent> = of("entity.firework_rocket.blast")
    @JvmField
    public val FIREWORK_ROCKET_BLAST_FAR: RegistryReference<SoundEvent> = of("entity.firework_rocket.blast_far")
    @JvmField
    public val FIREWORK_ROCKET_LARGE_BLAST: RegistryReference<SoundEvent> = of("entity.firework_rocket.large_blast")
    @JvmField
    public val FIREWORK_ROCKET_LARGE_BLAST_FAR: RegistryReference<SoundEvent> = of("entity.firework_rocket.large_blast_far")
    @JvmField
    public val FIREWORK_ROCKET_LAUNCH: RegistryReference<SoundEvent> = of("entity.firework_rocket.launch")
    @JvmField
    public val FIREWORK_ROCKET_SHOOT: RegistryReference<SoundEvent> = of("entity.firework_rocket.shoot")
    @JvmField
    public val FIREWORK_ROCKET_TWINKLE: RegistryReference<SoundEvent> = of("entity.firework_rocket.twinkle")
    @JvmField
    public val FIREWORK_ROCKET_TWINKLE_FAR: RegistryReference<SoundEvent> = of("entity.firework_rocket.twinkle_far")
    @JvmField
    public val FIRE_AMBIENT: RegistryReference<SoundEvent> = of("block.fire.ambient")
    @JvmField
    public val FIRE_EXTINGUISH: RegistryReference<SoundEvent> = of("block.fire.extinguish")
    @JvmField
    public val FISH_SWIM: RegistryReference<SoundEvent> = of("entity.fish.swim")
    @JvmField
    public val FISHING_BOBBER_RETRIEVE: RegistryReference<SoundEvent> = of("entity.fishing_bobber.retrieve")
    @JvmField
    public val FISHING_BOBBER_SPLASH: RegistryReference<SoundEvent> = of("entity.fishing_bobber.splash")
    @JvmField
    public val FISHING_BOBBER_THROW: RegistryReference<SoundEvent> = of("entity.fishing_bobber.throw")
    @JvmField
    public val FLINTANDSTEEL_USE: RegistryReference<SoundEvent> = of("item.flintandsteel.use")
    @JvmField
    public val FLOWERING_AZALEA_BREAK: RegistryReference<SoundEvent> = of("block.flowering_azalea.break")
    @JvmField
    public val FLOWERING_AZALEA_FALL: RegistryReference<SoundEvent> = of("block.flowering_azalea.fall")
    @JvmField
    public val FLOWERING_AZALEA_HIT: RegistryReference<SoundEvent> = of("block.flowering_azalea.hit")
    @JvmField
    public val FLOWERING_AZALEA_PLACE: RegistryReference<SoundEvent> = of("block.flowering_azalea.place")
    @JvmField
    public val FLOWERING_AZALEA_STEP: RegistryReference<SoundEvent> = of("block.flowering_azalea.step")
    @JvmField
    public val FOX_AGGRO: RegistryReference<SoundEvent> = of("entity.fox.aggro")
    @JvmField
    public val FOX_AMBIENT: RegistryReference<SoundEvent> = of("entity.fox.ambient")
    @JvmField
    public val FOX_BITE: RegistryReference<SoundEvent> = of("entity.fox.bite")
    @JvmField
    public val FOX_DEATH: RegistryReference<SoundEvent> = of("entity.fox.death")
    @JvmField
    public val FOX_EAT: RegistryReference<SoundEvent> = of("entity.fox.eat")
    @JvmField
    public val FOX_HURT: RegistryReference<SoundEvent> = of("entity.fox.hurt")
    @JvmField
    public val FOX_SCREECH: RegistryReference<SoundEvent> = of("entity.fox.screech")
    @JvmField
    public val FOX_SLEEP: RegistryReference<SoundEvent> = of("entity.fox.sleep")
    @JvmField
    public val FOX_SNIFF: RegistryReference<SoundEvent> = of("entity.fox.sniff")
    @JvmField
    public val FOX_SPIT: RegistryReference<SoundEvent> = of("entity.fox.spit")
    @JvmField
    public val FOX_TELEPORT: RegistryReference<SoundEvent> = of("entity.fox.teleport")
    @JvmField
    public val FROGLIGHT_BREAK: RegistryReference<SoundEvent> = of("block.froglight.break")
    @JvmField
    public val FROGLIGHT_FALL: RegistryReference<SoundEvent> = of("block.froglight.fall")
    @JvmField
    public val FROGLIGHT_HIT: RegistryReference<SoundEvent> = of("block.froglight.hit")
    @JvmField
    public val FROGLIGHT_PLACE: RegistryReference<SoundEvent> = of("block.froglight.place")
    @JvmField
    public val FROGLIGHT_STEP: RegistryReference<SoundEvent> = of("block.froglight.step")
    @JvmField
    public val FROGSPAWNSTEP: RegistryReference<SoundEvent> = of("block.frogspawn.step")
    @JvmField
    public val FROGSPAWN_BREAK: RegistryReference<SoundEvent> = of("block.frogspawn.break")
    @JvmField
    public val FROGSPAWN_FALL: RegistryReference<SoundEvent> = of("block.frogspawn.fall")
    @JvmField
    public val FROGSPAWN_HATCH: RegistryReference<SoundEvent> = of("block.frogspawn.hatch")
    @JvmField
    public val FROGSPAWN_HIT: RegistryReference<SoundEvent> = of("block.frogspawn.hit")
    @JvmField
    public val FROGSPAWN_PLACE: RegistryReference<SoundEvent> = of("block.frogspawn.place")
    @JvmField
    public val FROG_AMBIENT: RegistryReference<SoundEvent> = of("entity.frog.ambient")
    @JvmField
    public val FROG_DEATH: RegistryReference<SoundEvent> = of("entity.frog.death")
    @JvmField
    public val FROG_EAT: RegistryReference<SoundEvent> = of("entity.frog.eat")
    @JvmField
    public val FROG_HURT: RegistryReference<SoundEvent> = of("entity.frog.hurt")
    @JvmField
    public val FROG_LAY_SPAWN: RegistryReference<SoundEvent> = of("entity.frog.lay_spawn")
    @JvmField
    public val FROG_LONG_JUMP: RegistryReference<SoundEvent> = of("entity.frog.long_jump")
    @JvmField
    public val FROG_STEP: RegistryReference<SoundEvent> = of("entity.frog.step")
    @JvmField
    public val FROG_TONGUE: RegistryReference<SoundEvent> = of("entity.frog.tongue")
    @JvmField
    public val ROOTS_BREAK: RegistryReference<SoundEvent> = of("block.roots.break")
    @JvmField
    public val ROOTS_STEP: RegistryReference<SoundEvent> = of("block.roots.step")
    @JvmField
    public val ROOTS_PLACE: RegistryReference<SoundEvent> = of("block.roots.place")
    @JvmField
    public val ROOTS_HIT: RegistryReference<SoundEvent> = of("block.roots.hit")
    @JvmField
    public val ROOTS_FALL: RegistryReference<SoundEvent> = of("block.roots.fall")
    @JvmField
    public val FURNACE_FIRE_CRACKLE: RegistryReference<SoundEvent> = of("block.furnace.fire_crackle")
    @JvmField
    public val GENERIC_BIG_FALL: RegistryReference<SoundEvent> = of("entity.generic.big_fall")
    @JvmField
    public val GENERIC_BURN: RegistryReference<SoundEvent> = of("entity.generic.burn")
    @JvmField
    public val GENERIC_DEATH: RegistryReference<SoundEvent> = of("entity.generic.death")
    @JvmField
    public val GENERIC_DRINK: RegistryReference<SoundEvent> = of("entity.generic.drink")
    @JvmField
    public val GENERIC_EAT: RegistryReference<SoundEvent> = of("entity.generic.eat")
    @JvmField
    public val GENERIC_EXPLODE: RegistryReference<SoundEvent> = of("entity.generic.explode")
    @JvmField
    public val GENERIC_EXTINGUISH_FIRE: RegistryReference<SoundEvent> = of("entity.generic.extinguish_fire")
    @JvmField
    public val GENERIC_HURT: RegistryReference<SoundEvent> = of("entity.generic.hurt")
    @JvmField
    public val GENERIC_SMALL_FALL: RegistryReference<SoundEvent> = of("entity.generic.small_fall")
    @JvmField
    public val GENERIC_SPLASH: RegistryReference<SoundEvent> = of("entity.generic.splash")
    @JvmField
    public val GENERIC_SWIM: RegistryReference<SoundEvent> = of("entity.generic.swim")
    @JvmField
    public val GHAST_AMBIENT: RegistryReference<SoundEvent> = of("entity.ghast.ambient")
    @JvmField
    public val GHAST_DEATH: RegistryReference<SoundEvent> = of("entity.ghast.death")
    @JvmField
    public val GHAST_HURT: RegistryReference<SoundEvent> = of("entity.ghast.hurt")
    @JvmField
    public val GHAST_SCREAM: RegistryReference<SoundEvent> = of("entity.ghast.scream")
    @JvmField
    public val GHAST_SHOOT: RegistryReference<SoundEvent> = of("entity.ghast.shoot")
    @JvmField
    public val GHAST_WARN: RegistryReference<SoundEvent> = of("entity.ghast.warn")
    @JvmField
    public val GILDED_BLACKSTONE_BREAK: RegistryReference<SoundEvent> = of("block.gilded_blackstone.break")
    @JvmField
    public val GILDED_BLACKSTONE_FALL: RegistryReference<SoundEvent> = of("block.gilded_blackstone.fall")
    @JvmField
    public val GILDED_BLACKSTONE_HIT: RegistryReference<SoundEvent> = of("block.gilded_blackstone.hit")
    @JvmField
    public val GILDED_BLACKSTONE_PLACE: RegistryReference<SoundEvent> = of("block.gilded_blackstone.place")
    @JvmField
    public val GILDED_BLACKSTONE_STEP: RegistryReference<SoundEvent> = of("block.gilded_blackstone.step")
    @JvmField
    public val GLASS_BREAK: RegistryReference<SoundEvent> = of("block.glass.break")
    @JvmField
    public val GLASS_FALL: RegistryReference<SoundEvent> = of("block.glass.fall")
    @JvmField
    public val GLASS_HIT: RegistryReference<SoundEvent> = of("block.glass.hit")
    @JvmField
    public val GLASS_PLACE: RegistryReference<SoundEvent> = of("block.glass.place")
    @JvmField
    public val GLASS_STEP: RegistryReference<SoundEvent> = of("block.glass.step")
    @JvmField
    public val GLOW_INK_SAC_USE: RegistryReference<SoundEvent> = of("item.glow_ink_sac.use")
    @JvmField
    public val GLOW_ITEM_FRAME_ADD_ITEM: RegistryReference<SoundEvent> = of("entity.glow_item_frame.add_item")
    @JvmField
    public val GLOW_ITEM_FRAME_BREAK: RegistryReference<SoundEvent> = of("entity.glow_item_frame.break")
    @JvmField
    public val GLOW_ITEM_FRAME_PLACE: RegistryReference<SoundEvent> = of("entity.glow_item_frame.place")
    @JvmField
    public val GLOW_ITEM_FRAME_REMOVE_ITEM: RegistryReference<SoundEvent> = of("entity.glow_item_frame.remove_item")
    @JvmField
    public val GLOW_ITEM_FRAME_ROTATE_ITEM: RegistryReference<SoundEvent> = of("entity.glow_item_frame.rotate_item")
    @JvmField
    public val GLOW_SQUID_AMBIENT: RegistryReference<SoundEvent> = of("entity.glow_squid.ambient")
    @JvmField
    public val GLOW_SQUID_DEATH: RegistryReference<SoundEvent> = of("entity.glow_squid.death")
    @JvmField
    public val GLOW_SQUID_HURT: RegistryReference<SoundEvent> = of("entity.glow_squid.hurt")
    @JvmField
    public val GLOW_SQUID_SQUIRT: RegistryReference<SoundEvent> = of("entity.glow_squid.squirt")
    @JvmField
    public val GOAT_AMBIENT: RegistryReference<SoundEvent> = of("entity.goat.ambient")
    @JvmField
    public val GOAT_DEATH: RegistryReference<SoundEvent> = of("entity.goat.death")
    @JvmField
    public val GOAT_EAT: RegistryReference<SoundEvent> = of("entity.goat.eat")
    @JvmField
    public val GOAT_HURT: RegistryReference<SoundEvent> = of("entity.goat.hurt")
    @JvmField
    public val GOAT_LONG_JUMP: RegistryReference<SoundEvent> = of("entity.goat.long_jump")
    @JvmField
    public val GOAT_MILK: RegistryReference<SoundEvent> = of("entity.goat.milk")
    @JvmField
    public val GOAT_PREPARE_RAM: RegistryReference<SoundEvent> = of("entity.goat.prepare_ram")
    @JvmField
    public val GOAT_RAM_IMPACT: RegistryReference<SoundEvent> = of("entity.goat.ram_impact")
    @JvmField
    public val GOAT_HORN_BREAK: RegistryReference<SoundEvent> = of("entity.goat.horn_break")
    @JvmField
    public val GOAT_HORN_PLAY: RegistryReference<SoundEvent> = of("item.goat_horn.play")
    @JvmField
    public val GOAT_SCREAMING_AMBIENT: RegistryReference<SoundEvent> = of("entity.goat.screaming.ambient")
    @JvmField
    public val GOAT_SCREAMING_DEATH: RegistryReference<SoundEvent> = of("entity.goat.screaming.death")
    @JvmField
    public val GOAT_SCREAMING_EAT: RegistryReference<SoundEvent> = of("entity.goat.screaming.eat")
    @JvmField
    public val GOAT_SCREAMING_HURT: RegistryReference<SoundEvent> = of("entity.goat.screaming.hurt")
    @JvmField
    public val GOAT_SCREAMING_LONG_JUMP: RegistryReference<SoundEvent> = of("entity.goat.screaming.long_jump")
    @JvmField
    public val GOAT_SCREAMING_MILK: RegistryReference<SoundEvent> = of("entity.goat.screaming.milk")
    @JvmField
    public val GOAT_SCREAMING_PREPARE_RAM: RegistryReference<SoundEvent> = of("entity.goat.screaming.prepare_ram")
    @JvmField
    public val GOAT_SCREAMING_RAM_IMPACT: RegistryReference<SoundEvent> = of("entity.goat.screaming.ram_impact")
    @JvmField
    public val GOAT_SCREAMING_HORN_BREAK: RegistryReference<SoundEvent> = of("entity.goat.screaming.horn_break")
    @JvmField
    public val GOAT_STEP: RegistryReference<SoundEvent> = of("entity.goat.step")
    @JvmField
    public val GRASS_BREAK: RegistryReference<SoundEvent> = of("block.grass.break")
    @JvmField
    public val GRASS_FALL: RegistryReference<SoundEvent> = of("block.grass.fall")
    @JvmField
    public val GRASS_HIT: RegistryReference<SoundEvent> = of("block.grass.hit")
    @JvmField
    public val GRASS_PLACE: RegistryReference<SoundEvent> = of("block.grass.place")
    @JvmField
    public val GRASS_STEP: RegistryReference<SoundEvent> = of("block.grass.step")
    @JvmField
    public val GRAVEL_BREAK: RegistryReference<SoundEvent> = of("block.gravel.break")
    @JvmField
    public val GRAVEL_FALL: RegistryReference<SoundEvent> = of("block.gravel.fall")
    @JvmField
    public val GRAVEL_HIT: RegistryReference<SoundEvent> = of("block.gravel.hit")
    @JvmField
    public val GRAVEL_PLACE: RegistryReference<SoundEvent> = of("block.gravel.place")
    @JvmField
    public val GRAVEL_STEP: RegistryReference<SoundEvent> = of("block.gravel.step")
    @JvmField
    public val GRINDSTONE_USE: RegistryReference<SoundEvent> = of("block.grindstone.use")
    @JvmField
    public val GROWING_PLANT_CROP: RegistryReference<SoundEvent> = of("block.growing_plant.crop")
    @JvmField
    public val GUARDIAN_AMBIENT: RegistryReference<SoundEvent> = of("entity.guardian.ambient")
    @JvmField
    public val GUARDIAN_AMBIENT_LAND: RegistryReference<SoundEvent> = of("entity.guardian.ambient_land")
    @JvmField
    public val GUARDIAN_ATTACK: RegistryReference<SoundEvent> = of("entity.guardian.attack")
    @JvmField
    public val GUARDIAN_DEATH: RegistryReference<SoundEvent> = of("entity.guardian.death")
    @JvmField
    public val GUARDIAN_DEATH_LAND: RegistryReference<SoundEvent> = of("entity.guardian.death_land")
    @JvmField
    public val GUARDIAN_FLOP: RegistryReference<SoundEvent> = of("entity.guardian.flop")
    @JvmField
    public val GUARDIAN_HURT: RegistryReference<SoundEvent> = of("entity.guardian.hurt")
    @JvmField
    public val GUARDIAN_HURT_LAND: RegistryReference<SoundEvent> = of("entity.guardian.hurt_land")
    @JvmField
    public val HANGING_ROOTS_BREAK: RegistryReference<SoundEvent> = of("block.hanging_roots.break")
    @JvmField
    public val HANGING_ROOTS_FALL: RegistryReference<SoundEvent> = of("block.hanging_roots.fall")
    @JvmField
    public val HANGING_ROOTS_HIT: RegistryReference<SoundEvent> = of("block.hanging_roots.hit")
    @JvmField
    public val HANGING_ROOTS_PLACE: RegistryReference<SoundEvent> = of("block.hanging_roots.place")
    @JvmField
    public val HANGING_ROOTS_STEP: RegistryReference<SoundEvent> = of("block.hanging_roots.step")
    @JvmField
    public val HANGING_SIGN_STEP: RegistryReference<SoundEvent> = of("block.hanging_sign.step")
    @JvmField
    public val HANGING_SIGN_BREAK: RegistryReference<SoundEvent> = of("block.hanging_sign.break")
    @JvmField
    public val HANGING_SIGN_FALL: RegistryReference<SoundEvent> = of("block.hanging_sign.fall")
    @JvmField
    public val HANGING_SIGN_HIT: RegistryReference<SoundEvent> = of("block.hanging_sign.hit")
    @JvmField
    public val HANGING_SIGN_PLACE: RegistryReference<SoundEvent> = of("block.hanging_sign.place")
    @JvmField
    public val NETHER_WOOD_HANGING_SIGN_STEP: RegistryReference<SoundEvent> = of("block.nether_wood_hanging_sign.step")
    @JvmField
    public val NETHER_WOOD_HANGING_SIGN_BREAK: RegistryReference<SoundEvent> = of("block.nether_wood_hanging_sign.break")
    @JvmField
    public val NETHER_WOOD_HANGING_SIGN_FALL: RegistryReference<SoundEvent> = of("block.nether_wood_hanging_sign.fall")
    @JvmField
    public val NETHER_WOOD_HANGING_SIGN_HIT: RegistryReference<SoundEvent> = of("block.nether_wood_hanging_sign.hit")
    @JvmField
    public val NETHER_WOOD_HANGING_SIGN_PLACE: RegistryReference<SoundEvent> = of("block.nether_wood_hanging_sign.place")
    @JvmField
    public val BAMBOO_WOOD_HANGING_SIGN_STEP: RegistryReference<SoundEvent> = of("block.bamboo_wood_hanging_sign.step")
    @JvmField
    public val BAMBOO_WOOD_HANGING_SIGN_BREAK: RegistryReference<SoundEvent> = of("block.bamboo_wood_hanging_sign.break")
    @JvmField
    public val BAMBOO_WOOD_HANGING_SIGN_FALL: RegistryReference<SoundEvent> = of("block.bamboo_wood_hanging_sign.fall")
    @JvmField
    public val BAMBOO_WOOD_HANGING_SIGN_HIT: RegistryReference<SoundEvent> = of("block.bamboo_wood_hanging_sign.hit")
    @JvmField
    public val BAMBOO_WOOD_HANGING_SIGN_PLACE: RegistryReference<SoundEvent> = of("block.bamboo_wood_hanging_sign.place")
    @JvmField
    public val HOE_TILL: RegistryReference<SoundEvent> = of("item.hoe.till")
    @JvmField
    public val HOGLIN_AMBIENT: RegistryReference<SoundEvent> = of("entity.hoglin.ambient")
    @JvmField
    public val HOGLIN_ANGRY: RegistryReference<SoundEvent> = of("entity.hoglin.angry")
    @JvmField
    public val HOGLIN_ATTACK: RegistryReference<SoundEvent> = of("entity.hoglin.attack")
    @JvmField
    public val HOGLIN_CONVERTED_TO_ZOMBIFIED: RegistryReference<SoundEvent> = of("entity.hoglin.converted_to_zombified")
    @JvmField
    public val HOGLIN_DEATH: RegistryReference<SoundEvent> = of("entity.hoglin.death")
    @JvmField
    public val HOGLIN_HURT: RegistryReference<SoundEvent> = of("entity.hoglin.hurt")
    @JvmField
    public val HOGLIN_RETREAT: RegistryReference<SoundEvent> = of("entity.hoglin.retreat")
    @JvmField
    public val HOGLIN_STEP: RegistryReference<SoundEvent> = of("entity.hoglin.step")
    @JvmField
    public val HONEY_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.honey_block.break")
    @JvmField
    public val HONEY_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.honey_block.fall")
    @JvmField
    public val HONEY_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.honey_block.hit")
    @JvmField
    public val HONEY_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.honey_block.place")
    @JvmField
    public val HONEY_BLOCK_SLIDE: RegistryReference<SoundEvent> = of("block.honey_block.slide")
    @JvmField
    public val HONEY_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.honey_block.step")
    @JvmField
    public val HONEYCOMB_WAX_ON: RegistryReference<SoundEvent> = of("item.honeycomb.wax_on")
    @JvmField
    public val HONEY_DRINK: RegistryReference<SoundEvent> = of("item.honey_bottle.drink")
    @JvmField
    public val HORSE_AMBIENT: RegistryReference<SoundEvent> = of("entity.horse.ambient")
    @JvmField
    public val HORSE_ANGRY: RegistryReference<SoundEvent> = of("entity.horse.angry")
    @JvmField
    public val HORSE_ARMOR: RegistryReference<SoundEvent> = of("entity.horse.armor")
    @JvmField
    public val HORSE_BREATHE: RegistryReference<SoundEvent> = of("entity.horse.breathe")
    @JvmField
    public val HORSE_DEATH: RegistryReference<SoundEvent> = of("entity.horse.death")
    @JvmField
    public val HORSE_EAT: RegistryReference<SoundEvent> = of("entity.horse.eat")
    @JvmField
    public val HORSE_GALLOP: RegistryReference<SoundEvent> = of("entity.horse.gallop")
    @JvmField
    public val HORSE_HURT: RegistryReference<SoundEvent> = of("entity.horse.hurt")
    @JvmField
    public val HORSE_JUMP: RegistryReference<SoundEvent> = of("entity.horse.jump")
    @JvmField
    public val HORSE_LAND: RegistryReference<SoundEvent> = of("entity.horse.land")
    @JvmField
    public val HORSE_SADDLE: RegistryReference<SoundEvent> = of("entity.horse.saddle")
    @JvmField
    public val HORSE_STEP: RegistryReference<SoundEvent> = of("entity.horse.step")
    @JvmField
    public val HORSE_STEP_WOOD: RegistryReference<SoundEvent> = of("entity.horse.step_wood")
    @JvmField
    public val HOSTILE_BIG_FALL: RegistryReference<SoundEvent> = of("entity.hostile.big_fall")
    @JvmField
    public val HOSTILE_DEATH: RegistryReference<SoundEvent> = of("entity.hostile.death")
    @JvmField
    public val HOSTILE_HURT: RegistryReference<SoundEvent> = of("entity.hostile.hurt")
    @JvmField
    public val HOSTILE_SMALL_FALL: RegistryReference<SoundEvent> = of("entity.hostile.small_fall")
    @JvmField
    public val HOSTILE_SPLASH: RegistryReference<SoundEvent> = of("entity.hostile.splash")
    @JvmField
    public val HOSTILE_SWIM: RegistryReference<SoundEvent> = of("entity.hostile.swim")
    @JvmField
    public val HUSK_AMBIENT: RegistryReference<SoundEvent> = of("entity.husk.ambient")
    @JvmField
    public val HUSK_CONVERTED_TO_ZOMBIE: RegistryReference<SoundEvent> = of("entity.husk.converted_to_zombie")
    @JvmField
    public val HUSK_DEATH: RegistryReference<SoundEvent> = of("entity.husk.death")
    @JvmField
    public val HUSK_HURT: RegistryReference<SoundEvent> = of("entity.husk.hurt")
    @JvmField
    public val HUSK_STEP: RegistryReference<SoundEvent> = of("entity.husk.step")
    @JvmField
    public val ILLUSIONER_AMBIENT: RegistryReference<SoundEvent> = of("entity.illusioner.ambient")
    @JvmField
    public val ILLUSIONER_CAST_SPELL: RegistryReference<SoundEvent> = of("entity.illusioner.cast_spell")
    @JvmField
    public val ILLUSIONER_DEATH: RegistryReference<SoundEvent> = of("entity.illusioner.death")
    @JvmField
    public val ILLUSIONER_HURT: RegistryReference<SoundEvent> = of("entity.illusioner.hurt")
    @JvmField
    public val ILLUSIONER_MIRROR_MOVE: RegistryReference<SoundEvent> = of("entity.illusioner.mirror_move")
    @JvmField
    public val ILLUSIONER_PREPARE_BLINDNESS: RegistryReference<SoundEvent> = of("entity.illusioner.prepare_blindness")
    @JvmField
    public val ILLUSIONER_PREPARE_MIRROR: RegistryReference<SoundEvent> = of("entity.illusioner.prepare_mirror")
    @JvmField
    public val INK_SAC_USE: RegistryReference<SoundEvent> = of("item.ink_sac.use")
    @JvmField
    public val IRON_DOOR_CLOSE: RegistryReference<SoundEvent> = of("block.iron_door.close")
    @JvmField
    public val IRON_DOOR_OPEN: RegistryReference<SoundEvent> = of("block.iron_door.open")
    @JvmField
    public val IRON_GOLEM_ATTACK: RegistryReference<SoundEvent> = of("entity.iron_golem.attack")
    @JvmField
    public val IRON_GOLEM_DAMAGE: RegistryReference<SoundEvent> = of("entity.iron_golem.damage")
    @JvmField
    public val IRON_GOLEM_DEATH: RegistryReference<SoundEvent> = of("entity.iron_golem.death")
    @JvmField
    public val IRON_GOLEM_HURT: RegistryReference<SoundEvent> = of("entity.iron_golem.hurt")
    @JvmField
    public val IRON_GOLEM_REPAIR: RegistryReference<SoundEvent> = of("entity.iron_golem.repair")
    @JvmField
    public val IRON_GOLEM_STEP: RegistryReference<SoundEvent> = of("entity.iron_golem.step")
    @JvmField
    public val IRON_TRAPDOOR_CLOSE: RegistryReference<SoundEvent> = of("block.iron_trapdoor.close")
    @JvmField
    public val IRON_TRAPDOOR_OPEN: RegistryReference<SoundEvent> = of("block.iron_trapdoor.open")
    @JvmField
    public val ITEM_FRAME_ADD_ITEM: RegistryReference<SoundEvent> = of("entity.item_frame.add_item")
    @JvmField
    public val ITEM_FRAME_BREAK: RegistryReference<SoundEvent> = of("entity.item_frame.break")
    @JvmField
    public val ITEM_FRAME_PLACE: RegistryReference<SoundEvent> = of("entity.item_frame.place")
    @JvmField
    public val ITEM_FRAME_REMOVE_ITEM: RegistryReference<SoundEvent> = of("entity.item_frame.remove_item")
    @JvmField
    public val ITEM_FRAME_ROTATE_ITEM: RegistryReference<SoundEvent> = of("entity.item_frame.rotate_item")
    @JvmField
    public val ITEM_BREAK: RegistryReference<SoundEvent> = of("entity.item.break")
    @JvmField
    public val ITEM_PICKUP: RegistryReference<SoundEvent> = of("entity.item.pickup")
    @JvmField
    public val LADDER_BREAK: RegistryReference<SoundEvent> = of("block.ladder.break")
    @JvmField
    public val LADDER_FALL: RegistryReference<SoundEvent> = of("block.ladder.fall")
    @JvmField
    public val LADDER_HIT: RegistryReference<SoundEvent> = of("block.ladder.hit")
    @JvmField
    public val LADDER_PLACE: RegistryReference<SoundEvent> = of("block.ladder.place")
    @JvmField
    public val LADDER_STEP: RegistryReference<SoundEvent> = of("block.ladder.step")
    @JvmField
    public val LANTERN_BREAK: RegistryReference<SoundEvent> = of("block.lantern.break")
    @JvmField
    public val LANTERN_FALL: RegistryReference<SoundEvent> = of("block.lantern.fall")
    @JvmField
    public val LANTERN_HIT: RegistryReference<SoundEvent> = of("block.lantern.hit")
    @JvmField
    public val LANTERN_PLACE: RegistryReference<SoundEvent> = of("block.lantern.place")
    @JvmField
    public val LANTERN_STEP: RegistryReference<SoundEvent> = of("block.lantern.step")
    @JvmField
    public val LARGE_AMETHYST_BUD_BREAK: RegistryReference<SoundEvent> = of("block.large_amethyst_bud.break")
    @JvmField
    public val LARGE_AMETHYST_BUD_PLACE: RegistryReference<SoundEvent> = of("block.large_amethyst_bud.place")
    @JvmField
    public val LAVA_AMBIENT: RegistryReference<SoundEvent> = of("block.lava.ambient")
    @JvmField
    public val LAVA_EXTINGUISH: RegistryReference<SoundEvent> = of("block.lava.extinguish")
    @JvmField
    public val LAVA_POP: RegistryReference<SoundEvent> = of("block.lava.pop")
    @JvmField
    public val LEASH_KNOT_BREAK: RegistryReference<SoundEvent> = of("entity.leash_knot.break")
    @JvmField
    public val LEASH_KNOT_PLACE: RegistryReference<SoundEvent> = of("entity.leash_knot.place")
    @JvmField
    public val LEVER_CLICK: RegistryReference<SoundEvent> = of("block.lever.click")
    @JvmField
    public val LIGHTNING_BOLT_IMPACT: RegistryReference<SoundEvent> = of("entity.lightning_bolt.impact")
    @JvmField
    public val LIGHTNING_BOLT_THUNDER: RegistryReference<SoundEvent> = of("entity.lightning_bolt.thunder")
    @JvmField
    public val LINGERING_POTION_THROW: RegistryReference<SoundEvent> = of("entity.lingering_potion.throw")
    @JvmField
    public val LLAMA_AMBIENT: RegistryReference<SoundEvent> = of("entity.llama.ambient")
    @JvmField
    public val LLAMA_ANGRY: RegistryReference<SoundEvent> = of("entity.llama.angry")
    @JvmField
    public val LLAMA_CHEST: RegistryReference<SoundEvent> = of("entity.llama.chest")
    @JvmField
    public val LLAMA_DEATH: RegistryReference<SoundEvent> = of("entity.llama.death")
    @JvmField
    public val LLAMA_EAT: RegistryReference<SoundEvent> = of("entity.llama.eat")
    @JvmField
    public val LLAMA_HURT: RegistryReference<SoundEvent> = of("entity.llama.hurt")
    @JvmField
    public val LLAMA_SPIT: RegistryReference<SoundEvent> = of("entity.llama.spit")
    @JvmField
    public val LLAMA_STEP: RegistryReference<SoundEvent> = of("entity.llama.step")
    @JvmField
    public val LLAMA_SWAG: RegistryReference<SoundEvent> = of("entity.llama.swag")
    @JvmField
    public val MAGMA_CUBE_DEATH_SMALL: RegistryReference<SoundEvent> = of("entity.magma_cube.death_small")
    @JvmField
    public val LODESTONE_BREAK: RegistryReference<SoundEvent> = of("block.lodestone.break")
    @JvmField
    public val LODESTONE_STEP: RegistryReference<SoundEvent> = of("block.lodestone.step")
    @JvmField
    public val LODESTONE_PLACE: RegistryReference<SoundEvent> = of("block.lodestone.place")
    @JvmField
    public val LODESTONE_HIT: RegistryReference<SoundEvent> = of("block.lodestone.hit")
    @JvmField
    public val LODESTONE_FALL: RegistryReference<SoundEvent> = of("block.lodestone.fall")
    @JvmField
    public val LODESTONE_COMPASS_LOCK: RegistryReference<SoundEvent> = of("item.lodestone_compass.lock")
    @JvmField
    public val MAGMA_CUBE_DEATH: RegistryReference<SoundEvent> = of("entity.magma_cube.death")
    @JvmField
    public val MAGMA_CUBE_HURT: RegistryReference<SoundEvent> = of("entity.magma_cube.hurt")
    @JvmField
    public val MAGMA_CUBE_HURT_SMALL: RegistryReference<SoundEvent> = of("entity.magma_cube.hurt_small")
    @JvmField
    public val MAGMA_CUBE_JUMP: RegistryReference<SoundEvent> = of("entity.magma_cube.jump")
    @JvmField
    public val MAGMA_CUBE_SQUISH: RegistryReference<SoundEvent> = of("entity.magma_cube.squish")
    @JvmField
    public val MAGMA_CUBE_SQUISH_SMALL: RegistryReference<SoundEvent> = of("entity.magma_cube.squish_small")
    @JvmField
    public val MANGROVE_ROOTS_BREAK: RegistryReference<SoundEvent> = of("block.mangrove_roots.break")
    @JvmField
    public val MANGROVE_ROOTS_FALL: RegistryReference<SoundEvent> = of("block.mangrove_roots.fall")
    @JvmField
    public val MANGROVE_ROOTS_HIT: RegistryReference<SoundEvent> = of("block.mangrove_roots.hit")
    @JvmField
    public val MANGROVE_ROOTS_PLACE: RegistryReference<SoundEvent> = of("block.mangrove_roots.place")
    @JvmField
    public val MANGROVE_ROOTS_STEP: RegistryReference<SoundEvent> = of("block.mangrove_roots.step")
    @JvmField
    public val MEDIUM_AMETHYST_BUD_BREAK: RegistryReference<SoundEvent> = of("block.medium_amethyst_bud.break")
    @JvmField
    public val MEDIUM_AMETHYST_BUD_PLACE: RegistryReference<SoundEvent> = of("block.medium_amethyst_bud.place")
    @JvmField
    public val METAL_BREAK: RegistryReference<SoundEvent> = of("block.metal.break")
    @JvmField
    public val METAL_FALL: RegistryReference<SoundEvent> = of("block.metal.fall")
    @JvmField
    public val METAL_HIT: RegistryReference<SoundEvent> = of("block.metal.hit")
    @JvmField
    public val METAL_PLACE: RegistryReference<SoundEvent> = of("block.metal.place")
    @JvmField
    public val METAL_PRESSURE_PLATE_CLICK_OFF: RegistryReference<SoundEvent> = of("block.metal_pressure_plate.click_off")
    @JvmField
    public val METAL_PRESSURE_PLATE_CLICK_ON: RegistryReference<SoundEvent> = of("block.metal_pressure_plate.click_on")
    @JvmField
    public val METAL_STEP: RegistryReference<SoundEvent> = of("block.metal.step")
    @JvmField
    public val MINECART_INSIDE_UNDERWATER: RegistryReference<SoundEvent> = of("entity.minecart.inside.underwater")
    @JvmField
    public val MINECART_INSIDE: RegistryReference<SoundEvent> = of("entity.minecart.inside")
    @JvmField
    public val MINECART_RIDING: RegistryReference<SoundEvent> = of("entity.minecart.riding")
    @JvmField
    public val MOOSHROOM_CONVERT: RegistryReference<SoundEvent> = of("entity.mooshroom.convert")
    @JvmField
    public val MOOSHROOM_EAT: RegistryReference<SoundEvent> = of("entity.mooshroom.eat")
    @JvmField
    public val MOOSHROOM_MILK: RegistryReference<SoundEvent> = of("entity.mooshroom.milk")
    @JvmField
    public val MOOSHROOM_MILK_SUSPICIOUSLY: RegistryReference<SoundEvent> = of("entity.mooshroom.suspicious_milk")
    @JvmField
    public val MOOSHROOM_SHEAR: RegistryReference<SoundEvent> = of("entity.mooshroom.shear")
    @JvmField
    public val MOSS_CARPET_BREAK: RegistryReference<SoundEvent> = of("block.moss_carpet.break")
    @JvmField
    public val MOSS_CARPET_FALL: RegistryReference<SoundEvent> = of("block.moss_carpet.fall")
    @JvmField
    public val MOSS_CARPET_HIT: RegistryReference<SoundEvent> = of("block.moss_carpet.hit")
    @JvmField
    public val MOSS_CARPET_PLACE: RegistryReference<SoundEvent> = of("block.moss_carpet.place")
    @JvmField
    public val MOSS_CARPET_STEP: RegistryReference<SoundEvent> = of("block.moss_carpet.step")
    @JvmField
    public val MOSS_BREAK: RegistryReference<SoundEvent> = of("block.moss.break")
    @JvmField
    public val MOSS_FALL: RegistryReference<SoundEvent> = of("block.moss.fall")
    @JvmField
    public val MOSS_HIT: RegistryReference<SoundEvent> = of("block.moss.hit")
    @JvmField
    public val MOSS_PLACE: RegistryReference<SoundEvent> = of("block.moss.place")
    @JvmField
    public val MOSS_STEP: RegistryReference<SoundEvent> = of("block.moss.step")
    @JvmField
    public val MUD_BREAK: RegistryReference<SoundEvent> = of("block.mud.break")
    @JvmField
    public val MUD_FALL: RegistryReference<SoundEvent> = of("block.mud.fall")
    @JvmField
    public val MUD_HIT: RegistryReference<SoundEvent> = of("block.mud.hit")
    @JvmField
    public val MUD_PLACE: RegistryReference<SoundEvent> = of("block.mud.place")
    @JvmField
    public val MUD_STEP: RegistryReference<SoundEvent> = of("block.mud.step")
    @JvmField
    public val MUD_BRICKS_BREAK: RegistryReference<SoundEvent> = of("block.mud_bricks.break")
    @JvmField
    public val MUD_BRICKS_FALL: RegistryReference<SoundEvent> = of("block.mud_bricks.fall")
    @JvmField
    public val MUD_BRICKS_HIT: RegistryReference<SoundEvent> = of("block.mud_bricks.hit")
    @JvmField
    public val MUD_BRICKS_PLACE: RegistryReference<SoundEvent> = of("block.mud_bricks.place")
    @JvmField
    public val MUD_BRICKS_STEP: RegistryReference<SoundEvent> = of("block.mud_bricks.step")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_BREAK: RegistryReference<SoundEvent> = of("block.muddy_mangrove_roots.break")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_FALL: RegistryReference<SoundEvent> = of("block.muddy_mangrove_roots.fall")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_HIT: RegistryReference<SoundEvent> = of("block.muddy_mangrove_roots.hit")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_PLACE: RegistryReference<SoundEvent> = of("block.muddy_mangrove_roots.place")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_STEP: RegistryReference<SoundEvent> = of("block.muddy_mangrove_roots.step")
    @JvmField
    public val MULE_AMBIENT: RegistryReference<SoundEvent> = of("entity.mule.ambient")
    @JvmField
    public val MULE_ANGRY: RegistryReference<SoundEvent> = of("entity.mule.angry")
    @JvmField
    public val MULE_CHEST: RegistryReference<SoundEvent> = of("entity.mule.chest")
    @JvmField
    public val MULE_DEATH: RegistryReference<SoundEvent> = of("entity.mule.death")
    @JvmField
    public val MULE_EAT: RegistryReference<SoundEvent> = of("entity.mule.eat")
    @JvmField
    public val MULE_HURT: RegistryReference<SoundEvent> = of("entity.mule.hurt")
    @JvmField
    public val MUSIC_CREATIVE: RegistryReference<SoundEvent> = of("music.creative")
    @JvmField
    public val MUSIC_CREDITS: RegistryReference<SoundEvent> = of("music.credits")
    @JvmField
    public val MUSIC_DISC_5: RegistryReference<SoundEvent> = of("music_disc.5")
    @JvmField
    public val MUSIC_DISC_11: RegistryReference<SoundEvent> = of("music_disc.11")
    @JvmField
    public val MUSIC_DISC_13: RegistryReference<SoundEvent> = of("music_disc.13")
    @JvmField
    public val MUSIC_DISC_BLOCKS: RegistryReference<SoundEvent> = of("music_disc.blocks")
    @JvmField
    public val MUSIC_DISC_CAT: RegistryReference<SoundEvent> = of("music_disc.cat")
    @JvmField
    public val MUSIC_DISC_CHIRP: RegistryReference<SoundEvent> = of("music_disc.chirp")
    @JvmField
    public val MUSIC_DISC_FAR: RegistryReference<SoundEvent> = of("music_disc.far")
    @JvmField
    public val MUSIC_DISC_MALL: RegistryReference<SoundEvent> = of("music_disc.mall")
    @JvmField
    public val MUSIC_DISC_MELLOHI: RegistryReference<SoundEvent> = of("music_disc.mellohi")
    @JvmField
    public val MUSIC_DISC_PIGSTEP: RegistryReference<SoundEvent> = of("music_disc.pigstep")
    @JvmField
    public val MUSIC_DISC_STAL: RegistryReference<SoundEvent> = of("music_disc.stal")
    @JvmField
    public val MUSIC_DISC_STRAD: RegistryReference<SoundEvent> = of("music_disc.strad")
    @JvmField
    public val MUSIC_DISC_WAIT: RegistryReference<SoundEvent> = of("music_disc.wait")
    @JvmField
    public val MUSIC_DISC_WARD: RegistryReference<SoundEvent> = of("music_disc.ward")
    @JvmField
    public val MUSIC_DISC_OTHERSIDE: RegistryReference<SoundEvent> = of("music_disc.otherside")
    @JvmField
    public val MUSIC_DRAGON: RegistryReference<SoundEvent> = of("music.dragon")
    @JvmField
    public val MUSIC_END: RegistryReference<SoundEvent> = of("music.end")
    @JvmField
    public val MUSIC_GAME: RegistryReference<SoundEvent> = of("music.game")
    @JvmField
    public val MUSIC_MENU: RegistryReference<SoundEvent> = of("music.menu")
    @JvmField
    public val MUSIC_BIOME_BASALT_DELTAS: RegistryReference<SoundEvent> = of("music.nether.basalt_deltas")
    @JvmField
    public val MUSIC_BIOME_CRIMSON_FOREST: RegistryReference<SoundEvent> = of("music.nether.crimson_forest")
    @JvmField
    public val MUSIC_BIOME_DEEP_DARK: RegistryReference<SoundEvent> = of("music.overworld.deep_dark")
    @JvmField
    public val MUSIC_BIOME_DRIPSTONE_CAVES: RegistryReference<SoundEvent> = of("music.overworld.dripstone_caves")
    @JvmField
    public val MUSIC_BIOME_GROVE: RegistryReference<SoundEvent> = of("music.overworld.grove")
    @JvmField
    public val MUSIC_BIOME_JAGGED_PEAKS: RegistryReference<SoundEvent> = of("music.overworld.jagged_peaks")
    @JvmField
    public val MUSIC_BIOME_LUSH_CAVES: RegistryReference<SoundEvent> = of("music.overworld.lush_caves")
    @JvmField
    public val MUSIC_BIOME_SWAMP: RegistryReference<SoundEvent> = of("music.overworld.swamp")
    @JvmField
    public val MUSIC_BIOME_JUNGLE_AND_FOREST: RegistryReference<SoundEvent> = of("music.overworld.jungle_and_forest")
    @JvmField
    public val MUSIC_BIOME_OLD_GROWTH_TAIGA: RegistryReference<SoundEvent> = of("music.overworld.old_growth_taiga")
    @JvmField
    public val MUSIC_BIOME_MEADOW: RegistryReference<SoundEvent> = of("music.overworld.meadow")
    @JvmField
    public val MUSIC_BIOME_NETHER_WASTES: RegistryReference<SoundEvent> = of("music.nether.nether_wastes")
    @JvmField
    public val MUSIC_BIOME_FROZEN_PEAKS: RegistryReference<SoundEvent> = of("music.overworld.frozen_peaks")
    @JvmField
    public val MUSIC_BIOME_SNOWY_SLOPES: RegistryReference<SoundEvent> = of("music.overworld.snowy_slopes")
    @JvmField
    public val MUSIC_BIOME_SOUL_SAND_VALLEY: RegistryReference<SoundEvent> = of("music.nether.soul_sand_valley")
    @JvmField
    public val MUSIC_BIOME_STONY_PEAKS: RegistryReference<SoundEvent> = of("music.overworld.stony_peaks")
    @JvmField
    public val MUSIC_BIOME_WARPED_FOREST: RegistryReference<SoundEvent> = of("music.nether.warped_forest")
    @JvmField
    public val MUSIC_UNDER_WATER: RegistryReference<SoundEvent> = of("music.under_water")
    @JvmField
    public val NETHER_BRICKS_BREAK: RegistryReference<SoundEvent> = of("block.nether_bricks.break")
    @JvmField
    public val NETHER_BRICKS_STEP: RegistryReference<SoundEvent> = of("block.nether_bricks.step")
    @JvmField
    public val NETHER_BRICKS_PLACE: RegistryReference<SoundEvent> = of("block.nether_bricks.place")
    @JvmField
    public val NETHER_BRICKS_HIT: RegistryReference<SoundEvent> = of("block.nether_bricks.hit")
    @JvmField
    public val NETHER_BRICKS_FALL: RegistryReference<SoundEvent> = of("block.nether_bricks.fall")
    @JvmField
    public val NETHER_WART_BREAK: RegistryReference<SoundEvent> = of("block.nether_wart.break")
    @JvmField
    public val NETHER_WART_PLANTED: RegistryReference<SoundEvent> = of("item.nether_wart.plant")
    @JvmField
    public val NETHER_WOOD_BREAK: RegistryReference<SoundEvent> = of("block.nether_wood.break")
    @JvmField
    public val NETHER_WOOD_FALL: RegistryReference<SoundEvent> = of("block.nether_wood.fall")
    @JvmField
    public val NETHER_WOOD_HIT: RegistryReference<SoundEvent> = of("block.nether_wood.hit")
    @JvmField
    public val NETHER_WOOD_PLACE: RegistryReference<SoundEvent> = of("block.nether_wood.place")
    @JvmField
    public val NETHER_WOOD_STEP: RegistryReference<SoundEvent> = of("block.nether_wood.step")
    @JvmField
    public val NETHER_WOOD_DOOR_CLOSE: RegistryReference<SoundEvent> = of("block.nether_wood_door.close")
    @JvmField
    public val NETHER_WOOD_DOOR_OPEN: RegistryReference<SoundEvent> = of("block.nether_wood_door.open")
    @JvmField
    public val NETHER_WOOD_TRAPDOOR_CLOSE: RegistryReference<SoundEvent> = of("block.nether_wood_trapdoor.close")
    @JvmField
    public val NETHER_WOOD_TRAPDOOR_OPEN: RegistryReference<SoundEvent> = of("block.nether_wood_trapdoor.open")
    @JvmField
    public val NETHER_WOOD_BUTTON_CLICK_OFF: RegistryReference<SoundEvent> = of("block.nether_wood_button.click_off")
    @JvmField
    public val NETHER_WOOD_BUTTON_CLICK_ON: RegistryReference<SoundEvent> = of("block.nether_wood_button.click_on")
    @JvmField
    public val NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF: RegistryReference<SoundEvent> = of("block.nether_wood_pressure_plate.click_off")
    @JvmField
    public val NETHER_WOOD_PRESSURE_PLATE_CLICK_ON: RegistryReference<SoundEvent> = of("block.nether_wood_pressure_plate.click_on")
    @JvmField
    public val NETHER_WOOD_FENCE_GATE_CLOSE: RegistryReference<SoundEvent> = of("block.nether_wood_fence_gate.close")
    @JvmField
    public val NETHER_WOOD_FENCE_GATE_OPEN: RegistryReference<SoundEvent> = of("block.nether_wood_fence_gate.open")
    @JvmField
    public val PACKED_MUD_BREAK: RegistryReference<SoundEvent> = of("block.packed_mud.break")
    @JvmField
    public val PACKED_MUD_FALL: RegistryReference<SoundEvent> = of("block.packed_mud.fall")
    @JvmField
    public val PACKED_MUD_HIT: RegistryReference<SoundEvent> = of("block.packed_mud.hit")
    @JvmField
    public val PACKED_MUD_PLACE: RegistryReference<SoundEvent> = of("block.packed_mud.place")
    @JvmField
    public val PACKED_MUD_STEP: RegistryReference<SoundEvent> = of("block.packed_mud.step")
    @JvmField
    public val STEM_BREAK: RegistryReference<SoundEvent> = of("block.stem.break")
    @JvmField
    public val STEM_STEP: RegistryReference<SoundEvent> = of("block.stem.step")
    @JvmField
    public val STEM_PLACE: RegistryReference<SoundEvent> = of("block.stem.place")
    @JvmField
    public val STEM_HIT: RegistryReference<SoundEvent> = of("block.stem.hit")
    @JvmField
    public val STEM_FALL: RegistryReference<SoundEvent> = of("block.stem.fall")
    @JvmField
    public val NYLIUM_BREAK: RegistryReference<SoundEvent> = of("block.nylium.break")
    @JvmField
    public val NYLIUM_STEP: RegistryReference<SoundEvent> = of("block.nylium.step")
    @JvmField
    public val NYLIUM_PLACE: RegistryReference<SoundEvent> = of("block.nylium.place")
    @JvmField
    public val NYLIUM_HIT: RegistryReference<SoundEvent> = of("block.nylium.hit")
    @JvmField
    public val NYLIUM_FALL: RegistryReference<SoundEvent> = of("block.nylium.fall")
    @JvmField
    public val NETHER_SPROUTS_BREAK: RegistryReference<SoundEvent> = of("block.nether_sprouts.break")
    @JvmField
    public val NETHER_SPROUTS_STEP: RegistryReference<SoundEvent> = of("block.nether_sprouts.step")
    @JvmField
    public val NETHER_SPROUTS_PLACE: RegistryReference<SoundEvent> = of("block.nether_sprouts.place")
    @JvmField
    public val NETHER_SPROUTS_HIT: RegistryReference<SoundEvent> = of("block.nether_sprouts.hit")
    @JvmField
    public val NETHER_SPROUTS_FALL: RegistryReference<SoundEvent> = of("block.nether_sprouts.fall")
    @JvmField
    public val FUNGUS_BREAK: RegistryReference<SoundEvent> = of("block.fungus.break")
    @JvmField
    public val FUNGUS_STEP: RegistryReference<SoundEvent> = of("block.fungus.step")
    @JvmField
    public val FUNGUS_PLACE: RegistryReference<SoundEvent> = of("block.fungus.place")
    @JvmField
    public val FUNGUS_HIT: RegistryReference<SoundEvent> = of("block.fungus.hit")
    @JvmField
    public val FUNGUS_FALL: RegistryReference<SoundEvent> = of("block.fungus.fall")
    @JvmField
    public val WEEPING_VINES_BREAK: RegistryReference<SoundEvent> = of("block.weeping_vines.break")
    @JvmField
    public val WEEPING_VINES_STEP: RegistryReference<SoundEvent> = of("block.weeping_vines.step")
    @JvmField
    public val WEEPING_VINES_PLACE: RegistryReference<SoundEvent> = of("block.weeping_vines.place")
    @JvmField
    public val WEEPING_VINES_HIT: RegistryReference<SoundEvent> = of("block.weeping_vines.hit")
    @JvmField
    public val WEEPING_VINES_FALL: RegistryReference<SoundEvent> = of("block.weeping_vines.fall")
    @JvmField
    public val WART_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.wart_block.break")
    @JvmField
    public val WART_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.wart_block.step")
    @JvmField
    public val WART_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.wart_block.place")
    @JvmField
    public val WART_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.wart_block.hit")
    @JvmField
    public val WART_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.wart_block.fall")
    @JvmField
    public val NETHERITE_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.netherite_block.break")
    @JvmField
    public val NETHERITE_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.netherite_block.step")
    @JvmField
    public val NETHERITE_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.netherite_block.place")
    @JvmField
    public val NETHERITE_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.netherite_block.hit")
    @JvmField
    public val NETHERITE_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.netherite_block.fall")
    @JvmField
    public val NETHERRACK_BREAK: RegistryReference<SoundEvent> = of("block.netherrack.break")
    @JvmField
    public val NETHERRACK_STEP: RegistryReference<SoundEvent> = of("block.netherrack.step")
    @JvmField
    public val NETHERRACK_PLACE: RegistryReference<SoundEvent> = of("block.netherrack.place")
    @JvmField
    public val NETHERRACK_HIT: RegistryReference<SoundEvent> = of("block.netherrack.hit")
    @JvmField
    public val NETHERRACK_FALL: RegistryReference<SoundEvent> = of("block.netherrack.fall")
    @JvmField
    public val NOTE_BLOCK_BASEDRUM: RegistryReference<SoundEvent> = of("block.note_block.basedrum")
    @JvmField
    public val NOTE_BLOCK_BASS: RegistryReference<SoundEvent> = of("block.note_block.bass")
    @JvmField
    public val NOTE_BLOCK_BELL: RegistryReference<SoundEvent> = of("block.note_block.bell")
    @JvmField
    public val NOTE_BLOCK_CHIME: RegistryReference<SoundEvent> = of("block.note_block.chime")
    @JvmField
    public val NOTE_BLOCK_FLUTE: RegistryReference<SoundEvent> = of("block.note_block.flute")
    @JvmField
    public val NOTE_BLOCK_GUITAR: RegistryReference<SoundEvent> = of("block.note_block.guitar")
    @JvmField
    public val NOTE_BLOCK_HARP: RegistryReference<SoundEvent> = of("block.note_block.harp")
    @JvmField
    public val NOTE_BLOCK_HAT: RegistryReference<SoundEvent> = of("block.note_block.hat")
    @JvmField
    public val NOTE_BLOCK_PLING: RegistryReference<SoundEvent> = of("block.note_block.pling")
    @JvmField
    public val NOTE_BLOCK_SNARE: RegistryReference<SoundEvent> = of("block.note_block.snare")
    @JvmField
    public val NOTE_BLOCK_XYLOPHONE: RegistryReference<SoundEvent> = of("block.note_block.xylophone")
    @JvmField
    public val NOTE_BLOCK_IRON_XYLOPHONE: RegistryReference<SoundEvent> = of("block.note_block.iron_xylophone")
    @JvmField
    public val NOTE_BLOCK_COW_BELL: RegistryReference<SoundEvent> = of("block.note_block.cow_bell")
    @JvmField
    public val NOTE_BLOCK_DIDGERIDOO: RegistryReference<SoundEvent> = of("block.note_block.didgeridoo")
    @JvmField
    public val NOTE_BLOCK_BIT: RegistryReference<SoundEvent> = of("block.note_block.bit")
    @JvmField
    public val NOTE_BLOCK_BANJO: RegistryReference<SoundEvent> = of("block.note_block.banjo")
    @JvmField
    public val NOTE_BLOCK_IMITATE_ZOMBIE: RegistryReference<SoundEvent> = of("block.note_block.imitate.zombie")
    @JvmField
    public val NOTE_BLOCK_IMITATE_SKELETON: RegistryReference<SoundEvent> = of("block.note_block.imitate.skeleton")
    @JvmField
    public val NOTE_BLOCK_IMITATE_CREEPER: RegistryReference<SoundEvent> = of("block.note_block.imitate.creeper")
    @JvmField
    public val NOTE_BLOCK_IMITATE_ENDER_DRAGON: RegistryReference<SoundEvent> = of("block.note_block.imitate.ender_dragon")
    @JvmField
    public val NOTE_BLOCK_IMITATE_WITHER_SKELETON: RegistryReference<SoundEvent> = of("block.note_block.imitate.wither_skeleton")
    @JvmField
    public val NOTE_BLOCK_IMITATE_PIGLIN: RegistryReference<SoundEvent> = of("block.note_block.imitate.piglin")
    @JvmField
    public val OCELOT_HURT: RegistryReference<SoundEvent> = of("entity.ocelot.hurt")
    @JvmField
    public val OCELOT_AMBIENT: RegistryReference<SoundEvent> = of("entity.ocelot.ambient")
    @JvmField
    public val OCELOT_DEATH: RegistryReference<SoundEvent> = of("entity.ocelot.death")
    @JvmField
    public val PAINTING_BREAK: RegistryReference<SoundEvent> = of("entity.painting.break")
    @JvmField
    public val PAINTING_PLACE: RegistryReference<SoundEvent> = of("entity.painting.place")
    @JvmField
    public val PANDA_PRE_SNEEZE: RegistryReference<SoundEvent> = of("entity.panda.pre_sneeze")
    @JvmField
    public val PANDA_SNEEZE: RegistryReference<SoundEvent> = of("entity.panda.sneeze")
    @JvmField
    public val PANDA_AMBIENT: RegistryReference<SoundEvent> = of("entity.panda.ambient")
    @JvmField
    public val PANDA_DEATH: RegistryReference<SoundEvent> = of("entity.panda.death")
    @JvmField
    public val PANDA_EAT: RegistryReference<SoundEvent> = of("entity.panda.eat")
    @JvmField
    public val PANDA_STEP: RegistryReference<SoundEvent> = of("entity.panda.step")
    @JvmField
    public val PANDA_CANT_BREED: RegistryReference<SoundEvent> = of("entity.panda.cant_breed")
    @JvmField
    public val PANDA_AGGRESSIVE_AMBIENT: RegistryReference<SoundEvent> = of("entity.panda.aggressive_ambient")
    @JvmField
    public val PANDA_WORRIED_AMBIENT: RegistryReference<SoundEvent> = of("entity.panda.worried_ambient")
    @JvmField
    public val PANDA_HURT: RegistryReference<SoundEvent> = of("entity.panda.hurt")
    @JvmField
    public val PANDA_BITE: RegistryReference<SoundEvent> = of("entity.panda.bite")
    @JvmField
    public val PARROT_AMBIENT: RegistryReference<SoundEvent> = of("entity.parrot.ambient")
    @JvmField
    public val PARROT_DEATH: RegistryReference<SoundEvent> = of("entity.parrot.death")
    @JvmField
    public val PARROT_EAT: RegistryReference<SoundEvent> = of("entity.parrot.eat")
    @JvmField
    public val PARROT_FLY: RegistryReference<SoundEvent> = of("entity.parrot.fly")
    @JvmField
    public val PARROT_HURT: RegistryReference<SoundEvent> = of("entity.parrot.hurt")
    @JvmField
    public val PARROT_IMITATE_BLAZE: RegistryReference<SoundEvent> = of("entity.parrot.imitate.blaze")
    @JvmField
    public val PARROT_IMITATE_CREEPER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.creeper")
    @JvmField
    public val PARROT_IMITATE_DROWNED: RegistryReference<SoundEvent> = of("entity.parrot.imitate.drowned")
    @JvmField
    public val PARROT_IMITATE_ELDER_GUARDIAN: RegistryReference<SoundEvent> = of("entity.parrot.imitate.elder_guardian")
    @JvmField
    public val PARROT_IMITATE_ENDER_DRAGON: RegistryReference<SoundEvent> = of("entity.parrot.imitate.ender_dragon")
    @JvmField
    public val PARROT_IMITATE_ENDERMITE: RegistryReference<SoundEvent> = of("entity.parrot.imitate.endermite")
    @JvmField
    public val PARROT_IMITATE_EVOKER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.evoker")
    @JvmField
    public val PARROT_IMITATE_GHAST: RegistryReference<SoundEvent> = of("entity.parrot.imitate.ghast")
    @JvmField
    public val PARROT_IMITATE_GUARDIAN: RegistryReference<SoundEvent> = of("entity.parrot.imitate.guardian")
    @JvmField
    public val PARROT_IMITATE_HOGLIN: RegistryReference<SoundEvent> = of("entity.parrot.imitate.hoglin")
    @JvmField
    public val PARROT_IMITATE_HUSK: RegistryReference<SoundEvent> = of("entity.parrot.imitate.husk")
    @JvmField
    public val PARROT_IMITATE_ILLUSIONER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.illusioner")
    @JvmField
    public val PARROT_IMITATE_MAGMA_CUBE: RegistryReference<SoundEvent> = of("entity.parrot.imitate.magma_cube")
    @JvmField
    public val PARROT_IMITATE_PHANTOM: RegistryReference<SoundEvent> = of("entity.parrot.imitate.phantom")
    @JvmField
    public val PARROT_IMITATE_PIGLIN: RegistryReference<SoundEvent> = of("entity.parrot.imitate.piglin")
    @JvmField
    public val PARROT_IMITATE_PIGLIN_BRUTE: RegistryReference<SoundEvent> = of("entity.parrot.imitate.piglin_brute")
    @JvmField
    public val PARROT_IMITATE_PILLAGER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.pillager")
    @JvmField
    public val PARROT_IMITATE_RAVAGER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.ravager")
    @JvmField
    public val PARROT_IMITATE_SHULKER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.shulker")
    @JvmField
    public val PARROT_IMITATE_SILVERFISH: RegistryReference<SoundEvent> = of("entity.parrot.imitate.silverfish")
    @JvmField
    public val PARROT_IMITATE_SKELETON: RegistryReference<SoundEvent> = of("entity.parrot.imitate.skeleton")
    @JvmField
    public val PARROT_IMITATE_SLIME: RegistryReference<SoundEvent> = of("entity.parrot.imitate.slime")
    @JvmField
    public val PARROT_IMITATE_SPIDER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.spider")
    @JvmField
    public val PARROT_IMITATE_STRAY: RegistryReference<SoundEvent> = of("entity.parrot.imitate.stray")
    @JvmField
    public val PARROT_IMITATE_VEX: RegistryReference<SoundEvent> = of("entity.parrot.imitate.vex")
    @JvmField
    public val PARROT_IMITATE_VINDICATOR: RegistryReference<SoundEvent> = of("entity.parrot.imitate.vindicator")
    @JvmField
    public val PARROT_IMITATE_WARDEN: RegistryReference<SoundEvent> = of("entity.parrot.imitate.warden")
    @JvmField
    public val PARROT_IMITATE_WITCH: RegistryReference<SoundEvent> = of("entity.parrot.imitate.witch")
    @JvmField
    public val PARROT_IMITATE_WITHER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.wither")
    @JvmField
    public val PARROT_IMITATE_WITHER_SKELETON: RegistryReference<SoundEvent> = of("entity.parrot.imitate.wither_skeleton")
    @JvmField
    public val PARROT_IMITATE_ZOGLIN: RegistryReference<SoundEvent> = of("entity.parrot.imitate.zoglin")
    @JvmField
    public val PARROT_IMITATE_ZOMBIE: RegistryReference<SoundEvent> = of("entity.parrot.imitate.zombie")
    @JvmField
    public val PARROT_IMITATE_ZOMBIE_VILLAGER: RegistryReference<SoundEvent> = of("entity.parrot.imitate.zombie_villager")
    @JvmField
    public val PARROT_STEP: RegistryReference<SoundEvent> = of("entity.parrot.step")
    @JvmField
    public val PHANTOM_AMBIENT: RegistryReference<SoundEvent> = of("entity.phantom.ambient")
    @JvmField
    public val PHANTOM_BITE: RegistryReference<SoundEvent> = of("entity.phantom.bite")
    @JvmField
    public val PHANTOM_DEATH: RegistryReference<SoundEvent> = of("entity.phantom.death")
    @JvmField
    public val PHANTOM_FLAP: RegistryReference<SoundEvent> = of("entity.phantom.flap")
    @JvmField
    public val PHANTOM_HURT: RegistryReference<SoundEvent> = of("entity.phantom.hurt")
    @JvmField
    public val PHANTOM_SWOOP: RegistryReference<SoundEvent> = of("entity.phantom.swoop")
    @JvmField
    public val PIG_AMBIENT: RegistryReference<SoundEvent> = of("entity.pig.ambient")
    @JvmField
    public val PIG_DEATH: RegistryReference<SoundEvent> = of("entity.pig.death")
    @JvmField
    public val PIG_HURT: RegistryReference<SoundEvent> = of("entity.pig.hurt")
    @JvmField
    public val PIG_SADDLE: RegistryReference<SoundEvent> = of("entity.pig.saddle")
    @JvmField
    public val PIG_STEP: RegistryReference<SoundEvent> = of("entity.pig.step")
    @JvmField
    public val PIGLIN_ADMIRING_ITEM: RegistryReference<SoundEvent> = of("entity.piglin.admiring_item")
    @JvmField
    public val PIGLIN_AMBIENT: RegistryReference<SoundEvent> = of("entity.piglin.ambient")
    @JvmField
    public val PIGLIN_ANGRY: RegistryReference<SoundEvent> = of("entity.piglin.angry")
    @JvmField
    public val PIGLIN_CELEBRATE: RegistryReference<SoundEvent> = of("entity.piglin.celebrate")
    @JvmField
    public val PIGLIN_DEATH: RegistryReference<SoundEvent> = of("entity.piglin.death")
    @JvmField
    public val PIGLIN_JEALOUS: RegistryReference<SoundEvent> = of("entity.piglin.jealous")
    @JvmField
    public val PIGLIN_HURT: RegistryReference<SoundEvent> = of("entity.piglin.hurt")
    @JvmField
    public val PIGLIN_RETREAT: RegistryReference<SoundEvent> = of("entity.piglin.retreat")
    @JvmField
    public val PIGLIN_STEP: RegistryReference<SoundEvent> = of("entity.piglin.step")
    @JvmField
    public val PIGLIN_CONVERTED_TO_ZOMBIFIED: RegistryReference<SoundEvent> = of("entity.piglin.converted_to_zombified")
    @JvmField
    public val PIGLIN_BRUTE_AMBIENT: RegistryReference<SoundEvent> = of("entity.piglin_brute.ambient")
    @JvmField
    public val PIGLIN_BRUTE_ANGRY: RegistryReference<SoundEvent> = of("entity.piglin_brute.angry")
    @JvmField
    public val PIGLIN_BRUTE_DEATH: RegistryReference<SoundEvent> = of("entity.piglin_brute.death")
    @JvmField
    public val PIGLIN_BRUTE_HURT: RegistryReference<SoundEvent> = of("entity.piglin_brute.hurt")
    @JvmField
    public val PIGLIN_BRUTE_STEP: RegistryReference<SoundEvent> = of("entity.piglin_brute.step")
    @JvmField
    public val PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED: RegistryReference<SoundEvent> = of("entity.piglin_brute.converted_to_zombified")
    @JvmField
    public val PILLAGER_AMBIENT: RegistryReference<SoundEvent> = of("entity.pillager.ambient")
    @JvmField
    public val PILLAGER_CELEBRATE: RegistryReference<SoundEvent> = of("entity.pillager.celebrate")
    @JvmField
    public val PILLAGER_DEATH: RegistryReference<SoundEvent> = of("entity.pillager.death")
    @JvmField
    public val PILLAGER_HURT: RegistryReference<SoundEvent> = of("entity.pillager.hurt")
    @JvmField
    public val PISTON_CONTRACT: RegistryReference<SoundEvent> = of("block.piston.contract")
    @JvmField
    public val PISTON_EXTEND: RegistryReference<SoundEvent> = of("block.piston.extend")
    @JvmField
    public val PLAYER_ATTACK_CRIT: RegistryReference<SoundEvent> = of("entity.player.attack.crit")
    @JvmField
    public val PLAYER_ATTACK_KNOCKBACK: RegistryReference<SoundEvent> = of("entity.player.attack.knockback")
    @JvmField
    public val PLAYER_ATTACK_NODAMAGE: RegistryReference<SoundEvent> = of("entity.player.attack.nodamage")
    @JvmField
    public val PLAYER_ATTACK_STRONG: RegistryReference<SoundEvent> = of("entity.player.attack.strong")
    @JvmField
    public val PLAYER_ATTACK_SWEEP: RegistryReference<SoundEvent> = of("entity.player.attack.sweep")
    @JvmField
    public val PLAYER_ATTACK_WEAK: RegistryReference<SoundEvent> = of("entity.player.attack.weak")
    @JvmField
    public val PLAYER_BIG_FALL: RegistryReference<SoundEvent> = of("entity.player.big_fall")
    @JvmField
    public val PLAYER_BREATH: RegistryReference<SoundEvent> = of("entity.player.breath")
    @JvmField
    public val PLAYER_BURP: RegistryReference<SoundEvent> = of("entity.player.burp")
    @JvmField
    public val PLAYER_DEATH: RegistryReference<SoundEvent> = of("entity.player.death")
    @JvmField
    public val PLAYER_HURT: RegistryReference<SoundEvent> = of("entity.player.hurt")
    @JvmField
    public val PLAYER_HURT_DROWN: RegistryReference<SoundEvent> = of("entity.player.hurt_drown")
    @JvmField
    public val PLAYER_HURT_FREEZE: RegistryReference<SoundEvent> = of("entity.player.hurt_freeze")
    @JvmField
    public val PLAYER_HURT_ON_FIRE: RegistryReference<SoundEvent> = of("entity.player.hurt_on_fire")
    @JvmField
    public val PLAYER_HURT_SWEET_BERRY_BUSH: RegistryReference<SoundEvent> = of("entity.player.hurt_sweet_berry_bush")
    @JvmField
    public val PLAYER_LEVELUP: RegistryReference<SoundEvent> = of("entity.player.levelup")
    @JvmField
    public val PLAYER_SMALL_FALL: RegistryReference<SoundEvent> = of("entity.player.small_fall")
    @JvmField
    public val PLAYER_SPLASH: RegistryReference<SoundEvent> = of("entity.player.splash")
    @JvmField
    public val PLAYER_SPLASH_HIGH_SPEED: RegistryReference<SoundEvent> = of("entity.player.splash.high_speed")
    @JvmField
    public val PLAYER_SWIM: RegistryReference<SoundEvent> = of("entity.player.swim")
    @JvmField
    public val POLAR_BEAR_AMBIENT: RegistryReference<SoundEvent> = of("entity.polar_bear.ambient")
    @JvmField
    public val POLAR_BEAR_AMBIENT_BABY: RegistryReference<SoundEvent> = of("entity.polar_bear.ambient_baby")
    @JvmField
    public val POLAR_BEAR_DEATH: RegistryReference<SoundEvent> = of("entity.polar_bear.death")
    @JvmField
    public val POLAR_BEAR_HURT: RegistryReference<SoundEvent> = of("entity.polar_bear.hurt")
    @JvmField
    public val POLAR_BEAR_STEP: RegistryReference<SoundEvent> = of("entity.polar_bear.step")
    @JvmField
    public val POLAR_BEAR_WARNING: RegistryReference<SoundEvent> = of("entity.polar_bear.warning")
    @JvmField
    public val POLISHED_DEEPSLATE_BREAK: RegistryReference<SoundEvent> = of("block.polished_deepslate.break")
    @JvmField
    public val POLISHED_DEEPSLATE_FALL: RegistryReference<SoundEvent> = of("block.polished_deepslate.fall")
    @JvmField
    public val POLISHED_DEEPSLATE_HIT: RegistryReference<SoundEvent> = of("block.polished_deepslate.hit")
    @JvmField
    public val POLISHED_DEEPSLATE_PLACE: RegistryReference<SoundEvent> = of("block.polished_deepslate.place")
    @JvmField
    public val POLISHED_DEEPSLATE_STEP: RegistryReference<SoundEvent> = of("block.polished_deepslate.step")
    @JvmField
    public val PORTAL_AMBIENT: RegistryReference<SoundEvent> = of("block.portal.ambient")
    @JvmField
    public val PORTAL_TRAVEL: RegistryReference<SoundEvent> = of("block.portal.travel")
    @JvmField
    public val PORTAL_TRIGGER: RegistryReference<SoundEvent> = of("block.portal.trigger")
    @JvmField
    public val POWDER_SNOW_BREAK: RegistryReference<SoundEvent> = of("block.powder_snow.break")
    @JvmField
    public val POWDER_SNOW_FALL: RegistryReference<SoundEvent> = of("block.powder_snow.fall")
    @JvmField
    public val POWDER_SNOW_HIT: RegistryReference<SoundEvent> = of("block.powder_snow.hit")
    @JvmField
    public val POWDER_SNOW_PLACE: RegistryReference<SoundEvent> = of("block.powder_snow.place")
    @JvmField
    public val POWDER_SNOW_STEP: RegistryReference<SoundEvent> = of("block.powder_snow.step")
    @JvmField
    public val PUFFER_FISH_AMBIENT: RegistryReference<SoundEvent> = of("entity.puffer_fish.ambient")
    @JvmField
    public val PUFFER_FISH_BLOW_OUT: RegistryReference<SoundEvent> = of("entity.puffer_fish.blow_out")
    @JvmField
    public val PUFFER_FISH_BLOW_UP: RegistryReference<SoundEvent> = of("entity.puffer_fish.blow_up")
    @JvmField
    public val PUFFER_FISH_DEATH: RegistryReference<SoundEvent> = of("entity.puffer_fish.death")
    @JvmField
    public val PUFFER_FISH_FLOP: RegistryReference<SoundEvent> = of("entity.puffer_fish.flop")
    @JvmField
    public val PUFFER_FISH_HURT: RegistryReference<SoundEvent> = of("entity.puffer_fish.hurt")
    @JvmField
    public val PUFFER_FISH_STING: RegistryReference<SoundEvent> = of("entity.puffer_fish.sting")
    @JvmField
    public val PUMPKIN_CARVE: RegistryReference<SoundEvent> = of("block.pumpkin.carve")
    @JvmField
    public val RABBIT_AMBIENT: RegistryReference<SoundEvent> = of("entity.rabbit.ambient")
    @JvmField
    public val RABBIT_ATTACK: RegistryReference<SoundEvent> = of("entity.rabbit.attack")
    @JvmField
    public val RABBIT_DEATH: RegistryReference<SoundEvent> = of("entity.rabbit.death")
    @JvmField
    public val RABBIT_HURT: RegistryReference<SoundEvent> = of("entity.rabbit.hurt")
    @JvmField
    public val RABBIT_JUMP: RegistryReference<SoundEvent> = of("entity.rabbit.jump")
    @JvmField
    public val RAID_HORN: RegistryReference<SoundEvent> = of("event.raid.horn")
    @JvmField
    public val RAVAGER_AMBIENT: RegistryReference<SoundEvent> = of("entity.ravager.ambient")
    @JvmField
    public val RAVAGER_ATTACK: RegistryReference<SoundEvent> = of("entity.ravager.attack")
    @JvmField
    public val RAVAGER_CELEBRATE: RegistryReference<SoundEvent> = of("entity.ravager.celebrate")
    @JvmField
    public val RAVAGER_DEATH: RegistryReference<SoundEvent> = of("entity.ravager.death")
    @JvmField
    public val RAVAGER_HURT: RegistryReference<SoundEvent> = of("entity.ravager.hurt")
    @JvmField
    public val RAVAGER_STEP: RegistryReference<SoundEvent> = of("entity.ravager.step")
    @JvmField
    public val RAVAGER_STUNNED: RegistryReference<SoundEvent> = of("entity.ravager.stunned")
    @JvmField
    public val RAVAGER_ROAR: RegistryReference<SoundEvent> = of("entity.ravager.roar")
    @JvmField
    public val NETHER_GOLD_ORE_BREAK: RegistryReference<SoundEvent> = of("block.nether_gold_ore.break")
    @JvmField
    public val NETHER_GOLD_ORE_FALL: RegistryReference<SoundEvent> = of("block.nether_gold_ore.fall")
    @JvmField
    public val NETHER_GOLD_ORE_HIT: RegistryReference<SoundEvent> = of("block.nether_gold_ore.hit")
    @JvmField
    public val NETHER_GOLD_ORE_PLACE: RegistryReference<SoundEvent> = of("block.nether_gold_ore.place")
    @JvmField
    public val NETHER_GOLD_ORE_STEP: RegistryReference<SoundEvent> = of("block.nether_gold_ore.step")
    @JvmField
    public val NETHER_ORE_BREAK: RegistryReference<SoundEvent> = of("block.nether_ore.break")
    @JvmField
    public val NETHER_ORE_FALL: RegistryReference<SoundEvent> = of("block.nether_ore.fall")
    @JvmField
    public val NETHER_ORE_HIT: RegistryReference<SoundEvent> = of("block.nether_ore.hit")
    @JvmField
    public val NETHER_ORE_PLACE: RegistryReference<SoundEvent> = of("block.nether_ore.place")
    @JvmField
    public val NETHER_ORE_STEP: RegistryReference<SoundEvent> = of("block.nether_ore.step")
    @JvmField
    public val REDSTONE_TORCH_BURNOUT: RegistryReference<SoundEvent> = of("block.redstone_torch.burnout")
    @JvmField
    public val RESPAWN_ANCHOR_AMBIENT: RegistryReference<SoundEvent> = of("block.respawn_anchor.ambient")
    @JvmField
    public val RESPAWN_ANCHOR_CHARGE: RegistryReference<SoundEvent> = of("block.respawn_anchor.charge")
    @JvmField
    public val RESPAWN_ANCHOR_DEPLETE: RegistryReference<SoundEvent> = of("block.respawn_anchor.deplete")
    @JvmField
    public val RESPAWN_ANCHOR_SET_SPAWN: RegistryReference<SoundEvent> = of("block.respawn_anchor.set_spawn")
    @JvmField
    public val ROOTED_DIRT_BREAK: RegistryReference<SoundEvent> = of("block.rooted_dirt.break")
    @JvmField
    public val ROOTED_DIRT_FALL: RegistryReference<SoundEvent> = of("block.rooted_dirt.fall")
    @JvmField
    public val ROOTED_DIRT_HIT: RegistryReference<SoundEvent> = of("block.rooted_dirt.hit")
    @JvmField
    public val ROOTED_DIRT_PLACE: RegistryReference<SoundEvent> = of("block.rooted_dirt.place")
    @JvmField
    public val ROOTED_DIRT_STEP: RegistryReference<SoundEvent> = of("block.rooted_dirt.step")
    @JvmField
    public val SALMON_AMBIENT: RegistryReference<SoundEvent> = of("entity.salmon.ambient")
    @JvmField
    public val SALMON_DEATH: RegistryReference<SoundEvent> = of("entity.salmon.death")
    @JvmField
    public val SALMON_FLOP: RegistryReference<SoundEvent> = of("entity.salmon.flop")
    @JvmField
    public val SALMON_HURT: RegistryReference<SoundEvent> = of("entity.salmon.hurt")
    @JvmField
    public val SAND_BREAK: RegistryReference<SoundEvent> = of("block.sand.break")
    @JvmField
    public val SAND_FALL: RegistryReference<SoundEvent> = of("block.sand.fall")
    @JvmField
    public val SAND_HIT: RegistryReference<SoundEvent> = of("block.sand.hit")
    @JvmField
    public val SAND_PLACE: RegistryReference<SoundEvent> = of("block.sand.place")
    @JvmField
    public val SAND_STEP: RegistryReference<SoundEvent> = of("block.sand.step")
    @JvmField
    public val SCAFFOLDING_BREAK: RegistryReference<SoundEvent> = of("block.scaffolding.break")
    @JvmField
    public val SCAFFOLDING_FALL: RegistryReference<SoundEvent> = of("block.scaffolding.fall")
    @JvmField
    public val SCAFFOLDING_HIT: RegistryReference<SoundEvent> = of("block.scaffolding.hit")
    @JvmField
    public val SCAFFOLDING_PLACE: RegistryReference<SoundEvent> = of("block.scaffolding.place")
    @JvmField
    public val SCAFFOLDING_STEP: RegistryReference<SoundEvent> = of("block.scaffolding.step")
    @JvmField
    public val SCULK_BLOCK_SPREAD: RegistryReference<SoundEvent> = of("block.sculk.spread")
    @JvmField
    public val SCULK_BLOCK_CHARGE: RegistryReference<SoundEvent> = of("block.sculk.charge")
    @JvmField
    public val SCULK_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.sculk.break")
    @JvmField
    public val SCULK_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.sculk.fall")
    @JvmField
    public val SCULK_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.sculk.hit")
    @JvmField
    public val SCULK_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.sculk.place")
    @JvmField
    public val SCULK_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.sculk.step")
    @JvmField
    public val SCULK_CATALYST_BLOOM: RegistryReference<SoundEvent> = of("block.sculk_catalyst.bloom")
    @JvmField
    public val SCULK_CATALYST_BREAK: RegistryReference<SoundEvent> = of("block.sculk_catalyst.break")
    @JvmField
    public val SCULK_CATALYST_FALL: RegistryReference<SoundEvent> = of("block.sculk_catalyst.fall")
    @JvmField
    public val SCULK_CATALYST_HIT: RegistryReference<SoundEvent> = of("block.sculk_catalyst.hit")
    @JvmField
    public val SCULK_CATALYST_PLACE: RegistryReference<SoundEvent> = of("block.sculk_catalyst.place")
    @JvmField
    public val SCULK_CATALYST_STEP: RegistryReference<SoundEvent> = of("block.sculk_catalyst.step")
    @JvmField
    public val SCULK_CLICKING: RegistryReference<SoundEvent> = of("block.sculk_sensor.clicking")
    @JvmField
    public val SCULK_CLICKING_STOP: RegistryReference<SoundEvent> = of("block.sculk_sensor.clicking_stop")
    @JvmField
    public val SCULK_SENSOR_BREAK: RegistryReference<SoundEvent> = of("block.sculk_sensor.break")
    @JvmField
    public val SCULK_SENSOR_FALL: RegistryReference<SoundEvent> = of("block.sculk_sensor.fall")
    @JvmField
    public val SCULK_SENSOR_HIT: RegistryReference<SoundEvent> = of("block.sculk_sensor.hit")
    @JvmField
    public val SCULK_SENSOR_PLACE: RegistryReference<SoundEvent> = of("block.sculk_sensor.place")
    @JvmField
    public val SCULK_SENSOR_STEP: RegistryReference<SoundEvent> = of("block.sculk_sensor.step")
    @JvmField
    public val SCULK_SHRIEKER_BREAK: RegistryReference<SoundEvent> = of("block.sculk_shrieker.break")
    @JvmField
    public val SCULK_SHRIEKER_FALL: RegistryReference<SoundEvent> = of("block.sculk_shrieker.fall")
    @JvmField
    public val SCULK_SHRIEKER_HIT: RegistryReference<SoundEvent> = of("block.sculk_shrieker.hit")
    @JvmField
    public val SCULK_SHRIEKER_PLACE: RegistryReference<SoundEvent> = of("block.sculk_shrieker.place")
    @JvmField
    public val SCULK_SHRIEKER_SHRIEK: RegistryReference<SoundEvent> = of("block.sculk_shrieker.shriek")
    @JvmField
    public val SCULK_SHRIEKER_STEP: RegistryReference<SoundEvent> = of("block.sculk_shrieker.step")
    @JvmField
    public val SCULK_VEIN_BREAK: RegistryReference<SoundEvent> = of("block.sculk_vein.break")
    @JvmField
    public val SCULK_VEIN_FALL: RegistryReference<SoundEvent> = of("block.sculk_vein.fall")
    @JvmField
    public val SCULK_VEIN_HIT: RegistryReference<SoundEvent> = of("block.sculk_vein.hit")
    @JvmField
    public val SCULK_VEIN_PLACE: RegistryReference<SoundEvent> = of("block.sculk_vein.place")
    @JvmField
    public val SCULK_VEIN_STEP: RegistryReference<SoundEvent> = of("block.sculk_vein.step")
    @JvmField
    public val SHEEP_AMBIENT: RegistryReference<SoundEvent> = of("entity.sheep.ambient")
    @JvmField
    public val SHEEP_DEATH: RegistryReference<SoundEvent> = of("entity.sheep.death")
    @JvmField
    public val SHEEP_HURT: RegistryReference<SoundEvent> = of("entity.sheep.hurt")
    @JvmField
    public val SHEEP_SHEAR: RegistryReference<SoundEvent> = of("entity.sheep.shear")
    @JvmField
    public val SHEEP_STEP: RegistryReference<SoundEvent> = of("entity.sheep.step")
    @JvmField
    public val SHIELD_BLOCK: RegistryReference<SoundEvent> = of("item.shield.block")
    @JvmField
    public val SHIELD_BREAK: RegistryReference<SoundEvent> = of("item.shield.break")
    @JvmField
    public val SHROOMLIGHT_BREAK: RegistryReference<SoundEvent> = of("block.shroomlight.break")
    @JvmField
    public val SHROOMLIGHT_STEP: RegistryReference<SoundEvent> = of("block.shroomlight.step")
    @JvmField
    public val SHROOMLIGHT_PLACE: RegistryReference<SoundEvent> = of("block.shroomlight.place")
    @JvmField
    public val SHROOMLIGHT_HIT: RegistryReference<SoundEvent> = of("block.shroomlight.hit")
    @JvmField
    public val SHROOMLIGHT_FALL: RegistryReference<SoundEvent> = of("block.shroomlight.fall")
    @JvmField
    public val SHOVEL_FLATTEN: RegistryReference<SoundEvent> = of("item.shovel.flatten")
    @JvmField
    public val SHULKER_AMBIENT: RegistryReference<SoundEvent> = of("entity.shulker.ambient")
    @JvmField
    public val SHULKER_BOX_CLOSE: RegistryReference<SoundEvent> = of("block.shulker_box.close")
    @JvmField
    public val SHULKER_BOX_OPEN: RegistryReference<SoundEvent> = of("block.shulker_box.open")
    @JvmField
    public val SHULKER_BULLET_HIT: RegistryReference<SoundEvent> = of("entity.shulker_bullet.hit")
    @JvmField
    public val SHULKER_BULLET_HURT: RegistryReference<SoundEvent> = of("entity.shulker_bullet.hurt")
    @JvmField
    public val SHULKER_CLOSE: RegistryReference<SoundEvent> = of("entity.shulker.close")
    @JvmField
    public val SHULKER_DEATH: RegistryReference<SoundEvent> = of("entity.shulker.death")
    @JvmField
    public val SHULKER_HURT: RegistryReference<SoundEvent> = of("entity.shulker.hurt")
    @JvmField
    public val SHULKER_HURT_CLOSED: RegistryReference<SoundEvent> = of("entity.shulker.hurt_closed")
    @JvmField
    public val SHULKER_OPEN: RegistryReference<SoundEvent> = of("entity.shulker.open")
    @JvmField
    public val SHULKER_SHOOT: RegistryReference<SoundEvent> = of("entity.shulker.shoot")
    @JvmField
    public val SHULKER_TELEPORT: RegistryReference<SoundEvent> = of("entity.shulker.teleport")
    @JvmField
    public val SILVERFISH_AMBIENT: RegistryReference<SoundEvent> = of("entity.silverfish.ambient")
    @JvmField
    public val SILVERFISH_DEATH: RegistryReference<SoundEvent> = of("entity.silverfish.death")
    @JvmField
    public val SILVERFISH_HURT: RegistryReference<SoundEvent> = of("entity.silverfish.hurt")
    @JvmField
    public val SILVERFISH_STEP: RegistryReference<SoundEvent> = of("entity.silverfish.step")
    @JvmField
    public val SKELETON_AMBIENT: RegistryReference<SoundEvent> = of("entity.skeleton.ambient")
    @JvmField
    public val SKELETON_CONVERTED_TO_STRAY: RegistryReference<SoundEvent> = of("entity.skeleton.converted_to_stray")
    @JvmField
    public val SKELETON_DEATH: RegistryReference<SoundEvent> = of("entity.skeleton.death")
    @JvmField
    public val SKELETON_HORSE_AMBIENT: RegistryReference<SoundEvent> = of("entity.skeleton_horse.ambient")
    @JvmField
    public val SKELETON_HORSE_DEATH: RegistryReference<SoundEvent> = of("entity.skeleton_horse.death")
    @JvmField
    public val SKELETON_HORSE_HURT: RegistryReference<SoundEvent> = of("entity.skeleton_horse.hurt")
    @JvmField
    public val SKELETON_HORSE_SWIM: RegistryReference<SoundEvent> = of("entity.skeleton_horse.swim")
    @JvmField
    public val SKELETON_HORSE_AMBIENT_WATER: RegistryReference<SoundEvent> = of("entity.skeleton_horse.ambient_water")
    @JvmField
    public val SKELETON_HORSE_GALLOP_WATER: RegistryReference<SoundEvent> = of("entity.skeleton_horse.gallop_water")
    @JvmField
    public val SKELETON_HORSE_JUMP_WATER: RegistryReference<SoundEvent> = of("entity.skeleton_horse.jump_water")
    @JvmField
    public val SKELETON_HORSE_STEP_WATER: RegistryReference<SoundEvent> = of("entity.skeleton_horse.step_water")
    @JvmField
    public val SKELETON_HURT: RegistryReference<SoundEvent> = of("entity.skeleton.hurt")
    @JvmField
    public val SKELETON_SHOOT: RegistryReference<SoundEvent> = of("entity.skeleton.shoot")
    @JvmField
    public val SKELETON_STEP: RegistryReference<SoundEvent> = of("entity.skeleton.step")
    @JvmField
    public val SLIME_ATTACK: RegistryReference<SoundEvent> = of("entity.slime.attack")
    @JvmField
    public val SLIME_DEATH: RegistryReference<SoundEvent> = of("entity.slime.death")
    @JvmField
    public val SLIME_HURT: RegistryReference<SoundEvent> = of("entity.slime.hurt")
    @JvmField
    public val SLIME_JUMP: RegistryReference<SoundEvent> = of("entity.slime.jump")
    @JvmField
    public val SLIME_SQUISH: RegistryReference<SoundEvent> = of("entity.slime.squish")
    @JvmField
    public val SLIME_BLOCK_BREAK: RegistryReference<SoundEvent> = of("block.slime_block.break")
    @JvmField
    public val SLIME_BLOCK_FALL: RegistryReference<SoundEvent> = of("block.slime_block.fall")
    @JvmField
    public val SLIME_BLOCK_HIT: RegistryReference<SoundEvent> = of("block.slime_block.hit")
    @JvmField
    public val SLIME_BLOCK_PLACE: RegistryReference<SoundEvent> = of("block.slime_block.place")
    @JvmField
    public val SLIME_BLOCK_STEP: RegistryReference<SoundEvent> = of("block.slime_block.step")
    @JvmField
    public val SMALL_AMETHYST_BUD_BREAK: RegistryReference<SoundEvent> = of("block.small_amethyst_bud.break")
    @JvmField
    public val SMALL_AMETHYST_BUD_PLACE: RegistryReference<SoundEvent> = of("block.small_amethyst_bud.place")
    @JvmField
    public val SMALL_DRIPLEAF_BREAK: RegistryReference<SoundEvent> = of("block.small_dripleaf.break")
    @JvmField
    public val SMALL_DRIPLEAF_FALL: RegistryReference<SoundEvent> = of("block.small_dripleaf.fall")
    @JvmField
    public val SMALL_DRIPLEAF_HIT: RegistryReference<SoundEvent> = of("block.small_dripleaf.hit")
    @JvmField
    public val SMALL_DRIPLEAF_PLACE: RegistryReference<SoundEvent> = of("block.small_dripleaf.place")
    @JvmField
    public val SMALL_DRIPLEAF_STEP: RegistryReference<SoundEvent> = of("block.small_dripleaf.step")
    @JvmField
    public val SOUL_SAND_BREAK: RegistryReference<SoundEvent> = of("block.soul_sand.break")
    @JvmField
    public val SOUL_SAND_STEP: RegistryReference<SoundEvent> = of("block.soul_sand.step")
    @JvmField
    public val SOUL_SAND_PLACE: RegistryReference<SoundEvent> = of("block.soul_sand.place")
    @JvmField
    public val SOUL_SAND_HIT: RegistryReference<SoundEvent> = of("block.soul_sand.hit")
    @JvmField
    public val SOUL_SAND_FALL: RegistryReference<SoundEvent> = of("block.soul_sand.fall")
    @JvmField
    public val SOUL_SOIL_BREAK: RegistryReference<SoundEvent> = of("block.soul_soil.break")
    @JvmField
    public val SOUL_SOIL_STEP: RegistryReference<SoundEvent> = of("block.soul_soil.step")
    @JvmField
    public val SOUL_SOIL_PLACE: RegistryReference<SoundEvent> = of("block.soul_soil.place")
    @JvmField
    public val SOUL_SOIL_HIT: RegistryReference<SoundEvent> = of("block.soul_soil.hit")
    @JvmField
    public val SOUL_SOIL_FALL: RegistryReference<SoundEvent> = of("block.soul_soil.fall")
    @JvmField
    public val SOUL_ESCAPE: RegistryReference<SoundEvent> = of("particle.soul_escape")
    @JvmField
    public val SPORE_BLOSSOM_BREAK: RegistryReference<SoundEvent> = of("block.spore_blossom.break")
    @JvmField
    public val SPORE_BLOSSOM_FALL: RegistryReference<SoundEvent> = of("block.spore_blossom.fall")
    @JvmField
    public val SPORE_BLOSSOM_HIT: RegistryReference<SoundEvent> = of("block.spore_blossom.hit")
    @JvmField
    public val SPORE_BLOSSOM_PLACE: RegistryReference<SoundEvent> = of("block.spore_blossom.place")
    @JvmField
    public val SPORE_BLOSSOM_STEP: RegistryReference<SoundEvent> = of("block.spore_blossom.step")
    @JvmField
    public val STRIDER_AMBIENT: RegistryReference<SoundEvent> = of("entity.strider.ambient")
    @JvmField
    public val STRIDER_HAPPY: RegistryReference<SoundEvent> = of("entity.strider.happy")
    @JvmField
    public val STRIDER_RETREAT: RegistryReference<SoundEvent> = of("entity.strider.retreat")
    @JvmField
    public val STRIDER_DEATH: RegistryReference<SoundEvent> = of("entity.strider.death")
    @JvmField
    public val STRIDER_HURT: RegistryReference<SoundEvent> = of("entity.strider.hurt")
    @JvmField
    public val STRIDER_STEP: RegistryReference<SoundEvent> = of("entity.strider.step")
    @JvmField
    public val STRIDER_STEP_LAVA: RegistryReference<SoundEvent> = of("entity.strider.step_lava")
    @JvmField
    public val STRIDER_EAT: RegistryReference<SoundEvent> = of("entity.strider.eat")
    @JvmField
    public val STRIDER_SADDLE: RegistryReference<SoundEvent> = of("entity.strider.saddle")
    @JvmField
    public val SLIME_DEATH_SMALL: RegistryReference<SoundEvent> = of("entity.slime.death_small")
    @JvmField
    public val SLIME_HURT_SMALL: RegistryReference<SoundEvent> = of("entity.slime.hurt_small")
    @JvmField
    public val SLIME_JUMP_SMALL: RegistryReference<SoundEvent> = of("entity.slime.jump_small")
    @JvmField
    public val SLIME_SQUISH_SMALL: RegistryReference<SoundEvent> = of("entity.slime.squish_small")
    @JvmField
    public val SMITHING_TABLE_USE: RegistryReference<SoundEvent> = of("block.smithing_table.use")
    @JvmField
    public val SMOKER_SMOKE: RegistryReference<SoundEvent> = of("block.smoker.smoke")
    @JvmField
    public val SNOWBALL_THROW: RegistryReference<SoundEvent> = of("entity.snowball.throw")
    @JvmField
    public val SNOW_BREAK: RegistryReference<SoundEvent> = of("block.snow.break")
    @JvmField
    public val SNOW_FALL: RegistryReference<SoundEvent> = of("block.snow.fall")
    @JvmField
    public val SNOW_GOLEM_AMBIENT: RegistryReference<SoundEvent> = of("entity.snow_golem.ambient")
    @JvmField
    public val SNOW_GOLEM_DEATH: RegistryReference<SoundEvent> = of("entity.snow_golem.death")
    @JvmField
    public val SNOW_GOLEM_HURT: RegistryReference<SoundEvent> = of("entity.snow_golem.hurt")
    @JvmField
    public val SNOW_GOLEM_SHOOT: RegistryReference<SoundEvent> = of("entity.snow_golem.shoot")
    @JvmField
    public val SNOW_GOLEM_SHEAR: RegistryReference<SoundEvent> = of("entity.snow_golem.shear")
    @JvmField
    public val SNOW_HIT: RegistryReference<SoundEvent> = of("block.snow.hit")
    @JvmField
    public val SNOW_PLACE: RegistryReference<SoundEvent> = of("block.snow.place")
    @JvmField
    public val SNOW_STEP: RegistryReference<SoundEvent> = of("block.snow.step")
    @JvmField
    public val SPIDER_AMBIENT: RegistryReference<SoundEvent> = of("entity.spider.ambient")
    @JvmField
    public val SPIDER_DEATH: RegistryReference<SoundEvent> = of("entity.spider.death")
    @JvmField
    public val SPIDER_HURT: RegistryReference<SoundEvent> = of("entity.spider.hurt")
    @JvmField
    public val SPIDER_STEP: RegistryReference<SoundEvent> = of("entity.spider.step")
    @JvmField
    public val SPLASH_POTION_BREAK: RegistryReference<SoundEvent> = of("entity.splash_potion.break")
    @JvmField
    public val SPLASH_POTION_THROW: RegistryReference<SoundEvent> = of("entity.splash_potion.throw")
    @JvmField
    public val SPYGLASS_USE: RegistryReference<SoundEvent> = of("item.spyglass.use")
    @JvmField
    public val SPYGLASS_STOP_USING: RegistryReference<SoundEvent> = of("item.spyglass.stop_using")
    @JvmField
    public val SQUID_AMBIENT: RegistryReference<SoundEvent> = of("entity.squid.ambient")
    @JvmField
    public val SQUID_DEATH: RegistryReference<SoundEvent> = of("entity.squid.death")
    @JvmField
    public val SQUID_HURT: RegistryReference<SoundEvent> = of("entity.squid.hurt")
    @JvmField
    public val SQUID_SQUIRT: RegistryReference<SoundEvent> = of("entity.squid.squirt")
    @JvmField
    public val STONE_BREAK: RegistryReference<SoundEvent> = of("block.stone.break")
    @JvmField
    public val STONE_BUTTON_CLICK_OFF: RegistryReference<SoundEvent> = of("block.stone_button.click_off")
    @JvmField
    public val STONE_BUTTON_CLICK_ON: RegistryReference<SoundEvent> = of("block.stone_button.click_on")
    @JvmField
    public val STONE_FALL: RegistryReference<SoundEvent> = of("block.stone.fall")
    @JvmField
    public val STONE_HIT: RegistryReference<SoundEvent> = of("block.stone.hit")
    @JvmField
    public val STONE_PLACE: RegistryReference<SoundEvent> = of("block.stone.place")
    @JvmField
    public val STONE_PRESSURE_PLATE_CLICK_OFF: RegistryReference<SoundEvent> = of("block.stone_pressure_plate.click_off")
    @JvmField
    public val STONE_PRESSURE_PLATE_CLICK_ON: RegistryReference<SoundEvent> = of("block.stone_pressure_plate.click_on")
    @JvmField
    public val STONE_STEP: RegistryReference<SoundEvent> = of("block.stone.step")
    @JvmField
    public val STRAY_AMBIENT: RegistryReference<SoundEvent> = of("entity.stray.ambient")
    @JvmField
    public val STRAY_DEATH: RegistryReference<SoundEvent> = of("entity.stray.death")
    @JvmField
    public val STRAY_HURT: RegistryReference<SoundEvent> = of("entity.stray.hurt")
    @JvmField
    public val STRAY_STEP: RegistryReference<SoundEvent> = of("entity.stray.step")
    @JvmField
    public val SWEET_BERRY_BUSH_BREAK: RegistryReference<SoundEvent> = of("block.sweet_berry_bush.break")
    @JvmField
    public val SWEET_BERRY_BUSH_PLACE: RegistryReference<SoundEvent> = of("block.sweet_berry_bush.place")
    @JvmField
    public val SWEET_BERRY_BUSH_PICK_BERRIES: RegistryReference<SoundEvent> = of("block.sweet_berry_bush.pick_berries")
    @JvmField
    public val TADPOLE_DEATH: RegistryReference<SoundEvent> = of("entity.tadpole.death")
    @JvmField
    public val TADPOLE_FLOP: RegistryReference<SoundEvent> = of("entity.tadpole.flop")
    @JvmField
    public val TADPOLE_GROW_UP: RegistryReference<SoundEvent> = of("entity.tadpole.grow_up")
    @JvmField
    public val TADPOLE_HURT: RegistryReference<SoundEvent> = of("entity.tadpole.hurt")
    @JvmField
    public val THORNS_HIT: RegistryReference<SoundEvent> = of("enchant.thorns.hit")
    @JvmField
    public val TNT_PRIMED: RegistryReference<SoundEvent> = of("entity.tnt.primed")
    @JvmField
    public val TOTEM_USE: RegistryReference<SoundEvent> = of("item.totem.use")
    @JvmField
    public val TRIDENT_HIT: RegistryReference<SoundEvent> = of("item.trident.hit")
    @JvmField
    public val TRIDENT_HIT_GROUND: RegistryReference<SoundEvent> = of("item.trident.hit_ground")
    @JvmField
    public val TRIDENT_RETURN: RegistryReference<SoundEvent> = of("item.trident.return")
    @JvmField
    public val TRIDENT_RIPTIDE_1: RegistryReference<SoundEvent> = of("item.trident.riptide_1")
    @JvmField
    public val TRIDENT_RIPTIDE_2: RegistryReference<SoundEvent> = of("item.trident.riptide_2")
    @JvmField
    public val TRIDENT_RIPTIDE_3: RegistryReference<SoundEvent> = of("item.trident.riptide_3")
    @JvmField
    public val TRIDENT_THROW: RegistryReference<SoundEvent> = of("item.trident.throw")
    @JvmField
    public val TRIDENT_THUNDER: RegistryReference<SoundEvent> = of("item.trident.thunder")
    @JvmField
    public val TRIPWIRE_ATTACH: RegistryReference<SoundEvent> = of("block.tripwire.attach")
    @JvmField
    public val TRIPWIRE_CLICK_OFF: RegistryReference<SoundEvent> = of("block.tripwire.click_off")
    @JvmField
    public val TRIPWIRE_CLICK_ON: RegistryReference<SoundEvent> = of("block.tripwire.click_on")
    @JvmField
    public val TRIPWIRE_DETACH: RegistryReference<SoundEvent> = of("block.tripwire.detach")
    @JvmField
    public val TROPICAL_FISH_AMBIENT: RegistryReference<SoundEvent> = of("entity.tropical_fish.ambient")
    @JvmField
    public val TROPICAL_FISH_DEATH: RegistryReference<SoundEvent> = of("entity.tropical_fish.death")
    @JvmField
    public val TROPICAL_FISH_FLOP: RegistryReference<SoundEvent> = of("entity.tropical_fish.flop")
    @JvmField
    public val TROPICAL_FISH_HURT: RegistryReference<SoundEvent> = of("entity.tropical_fish.hurt")
    @JvmField
    public val TUFF_BREAK: RegistryReference<SoundEvent> = of("block.tuff.break")
    @JvmField
    public val TUFF_STEP: RegistryReference<SoundEvent> = of("block.tuff.step")
    @JvmField
    public val TUFF_PLACE: RegistryReference<SoundEvent> = of("block.tuff.place")
    @JvmField
    public val TUFF_HIT: RegistryReference<SoundEvent> = of("block.tuff.hit")
    @JvmField
    public val TUFF_FALL: RegistryReference<SoundEvent> = of("block.tuff.fall")
    @JvmField
    public val TURTLE_AMBIENT_LAND: RegistryReference<SoundEvent> = of("entity.turtle.ambient_land")
    @JvmField
    public val TURTLE_DEATH: RegistryReference<SoundEvent> = of("entity.turtle.death")
    @JvmField
    public val TURTLE_DEATH_BABY: RegistryReference<SoundEvent> = of("entity.turtle.death_baby")
    @JvmField
    public val TURTLE_EGG_BREAK: RegistryReference<SoundEvent> = of("entity.turtle.egg_break")
    @JvmField
    public val TURTLE_EGG_CRACK: RegistryReference<SoundEvent> = of("entity.turtle.egg_crack")
    @JvmField
    public val TURTLE_EGG_HATCH: RegistryReference<SoundEvent> = of("entity.turtle.egg_hatch")
    @JvmField
    public val TURTLE_HURT: RegistryReference<SoundEvent> = of("entity.turtle.hurt")
    @JvmField
    public val TURTLE_HURT_BABY: RegistryReference<SoundEvent> = of("entity.turtle.hurt_baby")
    @JvmField
    public val TURTLE_LAY_EGG: RegistryReference<SoundEvent> = of("entity.turtle.lay_egg")
    @JvmField
    public val TURTLE_SHAMBLE: RegistryReference<SoundEvent> = of("entity.turtle.shamble")
    @JvmField
    public val TURTLE_SHAMBLE_BABY: RegistryReference<SoundEvent> = of("entity.turtle.shamble_baby")
    @JvmField
    public val TURTLE_SWIM: RegistryReference<SoundEvent> = of("entity.turtle.swim")
    @JvmField
    public val UI_BUTTON_CLICK: RegistryReference<SoundEvent> = of("ui.button.click")
    @JvmField
    public val UI_LOOM_SELECT_PATTERN: RegistryReference<SoundEvent> = of("ui.loom.select_pattern")
    @JvmField
    public val UI_LOOM_TAKE_RESULT: RegistryReference<SoundEvent> = of("ui.loom.take_result")
    @JvmField
    public val UI_CARTOGRAPHY_TABLE_TAKE_RESULT: RegistryReference<SoundEvent> = of("ui.cartography_table.take_result")
    @JvmField
    public val UI_STONECUTTER_TAKE_RESULT: RegistryReference<SoundEvent> = of("ui.stonecutter.take_result")
    @JvmField
    public val UI_STONECUTTER_SELECT_RECIPE: RegistryReference<SoundEvent> = of("ui.stonecutter.select_recipe")
    @JvmField
    public val UI_TOAST_CHALLENGE_COMPLETE: RegistryReference<SoundEvent> = of("ui.toast.challenge_complete")
    @JvmField
    public val UI_TOAST_IN: RegistryReference<SoundEvent> = of("ui.toast.in")
    @JvmField
    public val UI_TOAST_OUT: RegistryReference<SoundEvent> = of("ui.toast.out")
    @JvmField
    public val VEX_AMBIENT: RegistryReference<SoundEvent> = of("entity.vex.ambient")
    @JvmField
    public val VEX_CHARGE: RegistryReference<SoundEvent> = of("entity.vex.charge")
    @JvmField
    public val VEX_DEATH: RegistryReference<SoundEvent> = of("entity.vex.death")
    @JvmField
    public val VEX_HURT: RegistryReference<SoundEvent> = of("entity.vex.hurt")
    @JvmField
    public val VILLAGER_AMBIENT: RegistryReference<SoundEvent> = of("entity.villager.ambient")
    @JvmField
    public val VILLAGER_CELEBRATE: RegistryReference<SoundEvent> = of("entity.villager.celebrate")
    @JvmField
    public val VILLAGER_DEATH: RegistryReference<SoundEvent> = of("entity.villager.death")
    @JvmField
    public val VILLAGER_HURT: RegistryReference<SoundEvent> = of("entity.villager.hurt")
    @JvmField
    public val VILLAGER_NO: RegistryReference<SoundEvent> = of("entity.villager.no")
    @JvmField
    public val VILLAGER_TRADE: RegistryReference<SoundEvent> = of("entity.villager.trade")
    @JvmField
    public val VILLAGER_YES: RegistryReference<SoundEvent> = of("entity.villager.yes")
    @JvmField
    public val VILLAGER_WORK_ARMORER: RegistryReference<SoundEvent> = of("entity.villager.work_armorer")
    @JvmField
    public val VILLAGER_WORK_BUTCHER: RegistryReference<SoundEvent> = of("entity.villager.work_butcher")
    @JvmField
    public val VILLAGER_WORK_CARTOGRAPHER: RegistryReference<SoundEvent> = of("entity.villager.work_cartographer")
    @JvmField
    public val VILLAGER_WORK_CLERIC: RegistryReference<SoundEvent> = of("entity.villager.work_cleric")
    @JvmField
    public val VILLAGER_WORK_FARMER: RegistryReference<SoundEvent> = of("entity.villager.work_farmer")
    @JvmField
    public val VILLAGER_WORK_FISHERMAN: RegistryReference<SoundEvent> = of("entity.villager.work_fisherman")
    @JvmField
    public val VILLAGER_WORK_FLETCHER: RegistryReference<SoundEvent> = of("entity.villager.work_fletcher")
    @JvmField
    public val VILLAGER_WORK_LEATHERWORKER: RegistryReference<SoundEvent> = of("entity.villager.work_leatherworker")
    @JvmField
    public val VILLAGER_WORK_LIBRARIAN: RegistryReference<SoundEvent> = of("entity.villager.work_librarian")
    @JvmField
    public val VILLAGER_WORK_MASON: RegistryReference<SoundEvent> = of("entity.villager.work_mason")
    @JvmField
    public val VILLAGER_WORK_SHEPHERD: RegistryReference<SoundEvent> = of("entity.villager.work_shepherd")
    @JvmField
    public val VILLAGER_WORK_TOOLSMITH: RegistryReference<SoundEvent> = of("entity.villager.work_toolsmith")
    @JvmField
    public val VILLAGER_WORK_WEAPONSMITH: RegistryReference<SoundEvent> = of("entity.villager.work_weaponsmith")
    @JvmField
    public val VINDICATOR_AMBIENT: RegistryReference<SoundEvent> = of("entity.vindicator.ambient")
    @JvmField
    public val VINDICATOR_CELEBRATE: RegistryReference<SoundEvent> = of("entity.vindicator.celebrate")
    @JvmField
    public val VINDICATOR_DEATH: RegistryReference<SoundEvent> = of("entity.vindicator.death")
    @JvmField
    public val VINDICATOR_HURT: RegistryReference<SoundEvent> = of("entity.vindicator.hurt")
    @JvmField
    public val VINE_BREAK: RegistryReference<SoundEvent> = of("block.vine.break")
    @JvmField
    public val VINE_FALL: RegistryReference<SoundEvent> = of("block.vine.fall")
    @JvmField
    public val VINE_HIT: RegistryReference<SoundEvent> = of("block.vine.hit")
    @JvmField
    public val VINE_PLACE: RegistryReference<SoundEvent> = of("block.vine.place")
    @JvmField
    public val VINE_STEP: RegistryReference<SoundEvent> = of("block.vine.step")
    @JvmField
    public val LILY_PAD_PLACE: RegistryReference<SoundEvent> = of("block.lily_pad.place")
    @JvmField
    public val WANDERING_TRADER_AMBIENT: RegistryReference<SoundEvent> = of("entity.wandering_trader.ambient")
    @JvmField
    public val WANDERING_TRADER_DEATH: RegistryReference<SoundEvent> = of("entity.wandering_trader.death")
    @JvmField
    public val WANDERING_TRADER_DISAPPEARED: RegistryReference<SoundEvent> = of("entity.wandering_trader.disappeared")
    @JvmField
    public val WANDERING_TRADER_DRINK_MILK: RegistryReference<SoundEvent> = of("entity.wandering_trader.drink_milk")
    @JvmField
    public val WANDERING_TRADER_DRINK_POTION: RegistryReference<SoundEvent> = of("entity.wandering_trader.drink_potion")
    @JvmField
    public val WANDERING_TRADER_HURT: RegistryReference<SoundEvent> = of("entity.wandering_trader.hurt")
    @JvmField
    public val WANDERING_TRADER_NO: RegistryReference<SoundEvent> = of("entity.wandering_trader.no")
    @JvmField
    public val WANDERING_TRADER_REAPPEARED: RegistryReference<SoundEvent> = of("entity.wandering_trader.reappeared")
    @JvmField
    public val WANDERING_TRADER_TRADE: RegistryReference<SoundEvent> = of("entity.wandering_trader.trade")
    @JvmField
    public val WANDERING_TRADER_YES: RegistryReference<SoundEvent> = of("entity.wandering_trader.yes")
    @JvmField
    public val WARDEN_AGITATED: RegistryReference<SoundEvent> = of("entity.warden.agitated")
    @JvmField
    public val WARDEN_AMBIENT: RegistryReference<SoundEvent> = of("entity.warden.ambient")
    @JvmField
    public val WARDEN_ANGRY: RegistryReference<SoundEvent> = of("entity.warden.angry")
    @JvmField
    public val WARDEN_ATTACK_IMPACT: RegistryReference<SoundEvent> = of("entity.warden.attack_impact")
    @JvmField
    public val WARDEN_DEATH: RegistryReference<SoundEvent> = of("entity.warden.death")
    @JvmField
    public val WARDEN_DIG: RegistryReference<SoundEvent> = of("entity.warden.dig")
    @JvmField
    public val WARDEN_EMERGE: RegistryReference<SoundEvent> = of("entity.warden.emerge")
    @JvmField
    public val WARDEN_HEARTBEAT: RegistryReference<SoundEvent> = of("entity.warden.heartbeat")
    @JvmField
    public val WARDEN_HURT: RegistryReference<SoundEvent> = of("entity.warden.hurt")
    @JvmField
    public val WARDEN_LISTENING: RegistryReference<SoundEvent> = of("entity.warden.listening")
    @JvmField
    public val WARDEN_LISTENING_ANGRY: RegistryReference<SoundEvent> = of("entity.warden.listening_angry")
    @JvmField
    public val WARDEN_NEARBY_CLOSE: RegistryReference<SoundEvent> = of("entity.warden.nearby_close")
    @JvmField
    public val WARDEN_NEARBY_CLOSER: RegistryReference<SoundEvent> = of("entity.warden.nearby_closer")
    @JvmField
    public val WARDEN_NEARBY_CLOSEST: RegistryReference<SoundEvent> = of("entity.warden.nearby_closest")
    @JvmField
    public val WARDEN_ROAR: RegistryReference<SoundEvent> = of("entity.warden.roar")
    @JvmField
    public val WARDEN_SNIFF: RegistryReference<SoundEvent> = of("entity.warden.sniff")
    @JvmField
    public val WARDEN_SONIC_BOOM: RegistryReference<SoundEvent> = of("entity.warden.sonic_boom")
    @JvmField
    public val WARDEN_SONIC_CHARGE: RegistryReference<SoundEvent> = of("entity.warden.sonic_charge")
    @JvmField
    public val WARDEN_STEP: RegistryReference<SoundEvent> = of("entity.warden.step")
    @JvmField
    public val WARDEN_TENDRIL_CLICKS: RegistryReference<SoundEvent> = of("entity.warden.tendril_clicks")
    @JvmField
    public val WATER_AMBIENT: RegistryReference<SoundEvent> = of("block.water.ambient")
    @JvmField
    public val WEATHER_RAIN: RegistryReference<SoundEvent> = of("weather.rain")
    @JvmField
    public val WEATHER_RAIN_ABOVE: RegistryReference<SoundEvent> = of("weather.rain.above")
    @JvmField
    public val WET_GRASS_BREAK: RegistryReference<SoundEvent> = of("block.wet_grass.break")
    @JvmField
    public val WET_GRASS_FALL: RegistryReference<SoundEvent> = of("block.wet_grass.fall")
    @JvmField
    public val WET_GRASS_HIT: RegistryReference<SoundEvent> = of("block.wet_grass.hit")
    @JvmField
    public val WET_GRASS_PLACE: RegistryReference<SoundEvent> = of("block.wet_grass.place")
    @JvmField
    public val WET_GRASS_STEP: RegistryReference<SoundEvent> = of("block.wet_grass.step")
    @JvmField
    public val WITCH_AMBIENT: RegistryReference<SoundEvent> = of("entity.witch.ambient")
    @JvmField
    public val WITCH_CELEBRATE: RegistryReference<SoundEvent> = of("entity.witch.celebrate")
    @JvmField
    public val WITCH_DEATH: RegistryReference<SoundEvent> = of("entity.witch.death")
    @JvmField
    public val WITCH_DRINK: RegistryReference<SoundEvent> = of("entity.witch.drink")
    @JvmField
    public val WITCH_HURT: RegistryReference<SoundEvent> = of("entity.witch.hurt")
    @JvmField
    public val WITCH_THROW: RegistryReference<SoundEvent> = of("entity.witch.throw")
    @JvmField
    public val WITHER_AMBIENT: RegistryReference<SoundEvent> = of("entity.wither.ambient")
    @JvmField
    public val WITHER_BREAK_BLOCK: RegistryReference<SoundEvent> = of("entity.wither.break_block")
    @JvmField
    public val WITHER_DEATH: RegistryReference<SoundEvent> = of("entity.wither.death")
    @JvmField
    public val WITHER_HURT: RegistryReference<SoundEvent> = of("entity.wither.hurt")
    @JvmField
    public val WITHER_SHOOT: RegistryReference<SoundEvent> = of("entity.wither.shoot")
    @JvmField
    public val WITHER_SKELETON_AMBIENT: RegistryReference<SoundEvent> = of("entity.wither_skeleton.ambient")
    @JvmField
    public val WITHER_SKELETON_DEATH: RegistryReference<SoundEvent> = of("entity.wither_skeleton.death")
    @JvmField
    public val WITHER_SKELETON_HURT: RegistryReference<SoundEvent> = of("entity.wither_skeleton.hurt")
    @JvmField
    public val WITHER_SKELETON_STEP: RegistryReference<SoundEvent> = of("entity.wither_skeleton.step")
    @JvmField
    public val WITHER_SPAWN: RegistryReference<SoundEvent> = of("entity.wither.spawn")
    @JvmField
    public val WOLF_AMBIENT: RegistryReference<SoundEvent> = of("entity.wolf.ambient")
    @JvmField
    public val WOLF_DEATH: RegistryReference<SoundEvent> = of("entity.wolf.death")
    @JvmField
    public val WOLF_GROWL: RegistryReference<SoundEvent> = of("entity.wolf.growl")
    @JvmField
    public val WOLF_HOWL: RegistryReference<SoundEvent> = of("entity.wolf.howl")
    @JvmField
    public val WOLF_HURT: RegistryReference<SoundEvent> = of("entity.wolf.hurt")
    @JvmField
    public val WOLF_PANT: RegistryReference<SoundEvent> = of("entity.wolf.pant")
    @JvmField
    public val WOLF_SHAKE: RegistryReference<SoundEvent> = of("entity.wolf.shake")
    @JvmField
    public val WOLF_STEP: RegistryReference<SoundEvent> = of("entity.wolf.step")
    @JvmField
    public val WOLF_WHINE: RegistryReference<SoundEvent> = of("entity.wolf.whine")
    @JvmField
    public val WOODEN_DOOR_CLOSE: RegistryReference<SoundEvent> = of("block.wooden_door.close")
    @JvmField
    public val WOODEN_DOOR_OPEN: RegistryReference<SoundEvent> = of("block.wooden_door.open")
    @JvmField
    public val WOODEN_TRAPDOOR_CLOSE: RegistryReference<SoundEvent> = of("block.wooden_trapdoor.close")
    @JvmField
    public val WOODEN_TRAPDOOR_OPEN: RegistryReference<SoundEvent> = of("block.wooden_trapdoor.open")
    @JvmField
    public val WOOD_BREAK: RegistryReference<SoundEvent> = of("block.wood.break")
    @JvmField
    public val WOODEN_BUTTON_CLICK_OFF: RegistryReference<SoundEvent> = of("block.wooden_button.click_off")
    @JvmField
    public val WOODEN_BUTTON_CLICK_ON: RegistryReference<SoundEvent> = of("block.wooden_button.click_on")
    @JvmField
    public val WOOD_FALL: RegistryReference<SoundEvent> = of("block.wood.fall")
    @JvmField
    public val WOOD_HIT: RegistryReference<SoundEvent> = of("block.wood.hit")
    @JvmField
    public val WOOD_PLACE: RegistryReference<SoundEvent> = of("block.wood.place")
    @JvmField
    public val WOODEN_PRESSURE_PLATE_CLICK_OFF: RegistryReference<SoundEvent> = of("block.wooden_pressure_plate.click_off")
    @JvmField
    public val WOODEN_PRESSURE_PLATE_CLICK_ON: RegistryReference<SoundEvent> = of("block.wooden_pressure_plate.click_on")
    @JvmField
    public val WOOD_STEP: RegistryReference<SoundEvent> = of("block.wood.step")
    @JvmField
    public val WOOL_BREAK: RegistryReference<SoundEvent> = of("block.wool.break")
    @JvmField
    public val WOOL_FALL: RegistryReference<SoundEvent> = of("block.wool.fall")
    @JvmField
    public val WOOL_HIT: RegistryReference<SoundEvent> = of("block.wool.hit")
    @JvmField
    public val WOOL_PLACE: RegistryReference<SoundEvent> = of("block.wool.place")
    @JvmField
    public val WOOL_STEP: RegistryReference<SoundEvent> = of("block.wool.step")
    @JvmField
    public val ZOGLIN_AMBIENT: RegistryReference<SoundEvent> = of("entity.zoglin.ambient")
    @JvmField
    public val ZOGLIN_ANGRY: RegistryReference<SoundEvent> = of("entity.zoglin.angry")
    @JvmField
    public val ZOGLIN_ATTACK: RegistryReference<SoundEvent> = of("entity.zoglin.attack")
    @JvmField
    public val ZOGLIN_DEATH: RegistryReference<SoundEvent> = of("entity.zoglin.death")
    @JvmField
    public val ZOGLIN_HURT: RegistryReference<SoundEvent> = of("entity.zoglin.hurt")
    @JvmField
    public val ZOGLIN_STEP: RegistryReference<SoundEvent> = of("entity.zoglin.step")
    @JvmField
    public val ZOMBIE_AMBIENT: RegistryReference<SoundEvent> = of("entity.zombie.ambient")
    @JvmField
    public val ZOMBIE_ATTACK_WOODEN_DOOR: RegistryReference<SoundEvent> = of("entity.zombie.attack_wooden_door")
    @JvmField
    public val ZOMBIE_ATTACK_IRON_DOOR: RegistryReference<SoundEvent> = of("entity.zombie.attack_iron_door")
    @JvmField
    public val ZOMBIE_BREAK_WOODEN_DOOR: RegistryReference<SoundEvent> = of("entity.zombie.break_wooden_door")
    @JvmField
    public val ZOMBIE_CONVERTED_TO_DROWNED: RegistryReference<SoundEvent> = of("entity.zombie.converted_to_drowned")
    @JvmField
    public val ZOMBIE_DEATH: RegistryReference<SoundEvent> = of("entity.zombie.death")
    @JvmField
    public val ZOMBIE_DESTROY_EGG: RegistryReference<SoundEvent> = of("entity.zombie.destroy_egg")
    @JvmField
    public val ZOMBIE_HORSE_AMBIENT: RegistryReference<SoundEvent> = of("entity.zombie_horse.ambient")
    @JvmField
    public val ZOMBIE_HORSE_DEATH: RegistryReference<SoundEvent> = of("entity.zombie_horse.death")
    @JvmField
    public val ZOMBIE_HORSE_HURT: RegistryReference<SoundEvent> = of("entity.zombie_horse.hurt")
    @JvmField
    public val ZOMBIE_HURT: RegistryReference<SoundEvent> = of("entity.zombie.hurt")
    @JvmField
    public val ZOMBIE_INFECT: RegistryReference<SoundEvent> = of("entity.zombie.infect")
    @JvmField
    public val ZOMBIFIED_PIGLIN_AMBIENT: RegistryReference<SoundEvent> = of("entity.zombified_piglin.ambient")
    @JvmField
    public val ZOMBIFIED_PIGLIN_ANGRY: RegistryReference<SoundEvent> = of("entity.zombified_piglin.angry")
    @JvmField
    public val ZOMBIFIED_PIGLIN_DEATH: RegistryReference<SoundEvent> = of("entity.zombified_piglin.death")
    @JvmField
    public val ZOMBIFIED_PIGLIN_HURT: RegistryReference<SoundEvent> = of("entity.zombified_piglin.hurt")
    @JvmField
    public val ZOMBIE_STEP: RegistryReference<SoundEvent> = of("entity.zombie.step")
    @JvmField
    public val ZOMBIE_VILLAGER_AMBIENT: RegistryReference<SoundEvent> = of("entity.zombie_villager.ambient")
    @JvmField
    public val ZOMBIE_VILLAGER_CONVERTED: RegistryReference<SoundEvent> = of("entity.zombie_villager.converted")
    @JvmField
    public val ZOMBIE_VILLAGER_CURE: RegistryReference<SoundEvent> = of("entity.zombie_villager.cure")
    @JvmField
    public val ZOMBIE_VILLAGER_DEATH: RegistryReference<SoundEvent> = of("entity.zombie_villager.death")
    @JvmField
    public val ZOMBIE_VILLAGER_HURT: RegistryReference<SoundEvent> = of("entity.zombie_villager.hurt")
    @JvmField
    public val ZOMBIE_VILLAGER_STEP: RegistryReference<SoundEvent> = of("entity.zombie_villager.step")
    @JvmField
    public val GOAT_HORN_0: RegistryReference<SoundEvent> = of("item.goat_horn.sound.0")
    @JvmField
    public val GOAT_HORN_1: RegistryReference<SoundEvent> = of("item.goat_horn.sound.1")
    @JvmField
    public val GOAT_HORN_2: RegistryReference<SoundEvent> = of("item.goat_horn.sound.2")
    @JvmField
    public val GOAT_HORN_3: RegistryReference<SoundEvent> = of("item.goat_horn.sound.3")
    @JvmField
    public val GOAT_HORN_4: RegistryReference<SoundEvent> = of("item.goat_horn.sound.4")
    @JvmField
    public val GOAT_HORN_5: RegistryReference<SoundEvent> = of("item.goat_horn.sound.5")
    @JvmField
    public val GOAT_HORN_6: RegistryReference<SoundEvent> = of("item.goat_horn.sound.6")
    @JvmField
    public val GOAT_HORN_7: RegistryReference<SoundEvent> = of("item.goat_horn.sound.7")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<SoundEvent> = RegistryReference.of(Registries.SOUND_EVENT, Key.key(name))
}
