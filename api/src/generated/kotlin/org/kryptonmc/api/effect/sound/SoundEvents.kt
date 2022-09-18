/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(SoundEvent::class)
public object SoundEvents {

    // @formatter:off
    @JvmField
    public val ALLAY_AMBIENT_WITH_ITEM: SoundEvent = get("entity.allay.ambient_with_item")
    @JvmField
    public val ALLAY_AMBIENT_WITHOUT_ITEM: SoundEvent = get("entity.allay.ambient_without_item")
    @JvmField
    public val ALLAY_DEATH: SoundEvent = get("entity.allay.death")
    @JvmField
    public val ALLAY_HURT: SoundEvent = get("entity.allay.hurt")
    @JvmField
    public val ALLAY_ITEM_GIVEN: SoundEvent = get("entity.allay.item_given")
    @JvmField
    public val ALLAY_ITEM_TAKEN: SoundEvent = get("entity.allay.item_taken")
    @JvmField
    public val ALLAY_THROW: SoundEvent = get("entity.allay.item_thrown")
    @JvmField
    public val AMBIENT_CAVE: SoundEvent = get("ambient.cave")
    @JvmField
    public val AMBIENT_BASALT_DELTAS_ADDITIONS: SoundEvent = get("ambient.basalt_deltas.additions")
    @JvmField
    public val AMBIENT_BASALT_DELTAS_LOOP: SoundEvent = get("ambient.basalt_deltas.loop")
    @JvmField
    public val AMBIENT_BASALT_DELTAS_MOOD: SoundEvent = get("ambient.basalt_deltas.mood")
    @JvmField
    public val AMBIENT_CRIMSON_FOREST_ADDITIONS: SoundEvent = get("ambient.crimson_forest.additions")
    @JvmField
    public val AMBIENT_CRIMSON_FOREST_LOOP: SoundEvent = get("ambient.crimson_forest.loop")
    @JvmField
    public val AMBIENT_CRIMSON_FOREST_MOOD: SoundEvent = get("ambient.crimson_forest.mood")
    @JvmField
    public val AMBIENT_NETHER_WASTES_ADDITIONS: SoundEvent = get("ambient.nether_wastes.additions")
    @JvmField
    public val AMBIENT_NETHER_WASTES_LOOP: SoundEvent = get("ambient.nether_wastes.loop")
    @JvmField
    public val AMBIENT_NETHER_WASTES_MOOD: SoundEvent = get("ambient.nether_wastes.mood")
    @JvmField
    public val AMBIENT_SOUL_SAND_VALLEY_ADDITIONS: SoundEvent = get("ambient.soul_sand_valley.additions")
    @JvmField
    public val AMBIENT_SOUL_SAND_VALLEY_LOOP: SoundEvent = get("ambient.soul_sand_valley.loop")
    @JvmField
    public val AMBIENT_SOUL_SAND_VALLEY_MOOD: SoundEvent = get("ambient.soul_sand_valley.mood")
    @JvmField
    public val AMBIENT_WARPED_FOREST_ADDITIONS: SoundEvent = get("ambient.warped_forest.additions")
    @JvmField
    public val AMBIENT_WARPED_FOREST_LOOP: SoundEvent = get("ambient.warped_forest.loop")
    @JvmField
    public val AMBIENT_WARPED_FOREST_MOOD: SoundEvent = get("ambient.warped_forest.mood")
    @JvmField
    public val AMBIENT_UNDERWATER_ENTER: SoundEvent = get("ambient.underwater.enter")
    @JvmField
    public val AMBIENT_UNDERWATER_EXIT: SoundEvent = get("ambient.underwater.exit")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP: SoundEvent = get("ambient.underwater.loop")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP_ADDITIONS: SoundEvent = get("ambient.underwater.loop.additions")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE: SoundEvent = get("ambient.underwater.loop.additions.rare")
    @JvmField
    public val AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE: SoundEvent = get("ambient.underwater.loop.additions.ultra_rare")
    @JvmField
    public val AMETHYST_BLOCK_BREAK: SoundEvent = get("block.amethyst_block.break")
    @JvmField
    public val AMETHYST_BLOCK_CHIME: SoundEvent = get("block.amethyst_block.chime")
    @JvmField
    public val AMETHYST_BLOCK_FALL: SoundEvent = get("block.amethyst_block.fall")
    @JvmField
    public val AMETHYST_BLOCK_HIT: SoundEvent = get("block.amethyst_block.hit")
    @JvmField
    public val AMETHYST_BLOCK_PLACE: SoundEvent = get("block.amethyst_block.place")
    @JvmField
    public val AMETHYST_BLOCK_STEP: SoundEvent = get("block.amethyst_block.step")
    @JvmField
    public val AMETHYST_CLUSTER_BREAK: SoundEvent = get("block.amethyst_cluster.break")
    @JvmField
    public val AMETHYST_CLUSTER_FALL: SoundEvent = get("block.amethyst_cluster.fall")
    @JvmField
    public val AMETHYST_CLUSTER_HIT: SoundEvent = get("block.amethyst_cluster.hit")
    @JvmField
    public val AMETHYST_CLUSTER_PLACE: SoundEvent = get("block.amethyst_cluster.place")
    @JvmField
    public val AMETHYST_CLUSTER_STEP: SoundEvent = get("block.amethyst_cluster.step")
    @JvmField
    public val ANCIENT_DEBRIS_BREAK: SoundEvent = get("block.ancient_debris.break")
    @JvmField
    public val ANCIENT_DEBRIS_STEP: SoundEvent = get("block.ancient_debris.step")
    @JvmField
    public val ANCIENT_DEBRIS_PLACE: SoundEvent = get("block.ancient_debris.place")
    @JvmField
    public val ANCIENT_DEBRIS_HIT: SoundEvent = get("block.ancient_debris.hit")
    @JvmField
    public val ANCIENT_DEBRIS_FALL: SoundEvent = get("block.ancient_debris.fall")
    @JvmField
    public val ANVIL_BREAK: SoundEvent = get("block.anvil.break")
    @JvmField
    public val ANVIL_DESTROY: SoundEvent = get("block.anvil.destroy")
    @JvmField
    public val ANVIL_FALL: SoundEvent = get("block.anvil.fall")
    @JvmField
    public val ANVIL_HIT: SoundEvent = get("block.anvil.hit")
    @JvmField
    public val ANVIL_LAND: SoundEvent = get("block.anvil.land")
    @JvmField
    public val ANVIL_PLACE: SoundEvent = get("block.anvil.place")
    @JvmField
    public val ANVIL_STEP: SoundEvent = get("block.anvil.step")
    @JvmField
    public val ANVIL_USE: SoundEvent = get("block.anvil.use")
    @JvmField
    public val ARMOR_EQUIP_CHAIN: SoundEvent = get("item.armor.equip_chain")
    @JvmField
    public val ARMOR_EQUIP_DIAMOND: SoundEvent = get("item.armor.equip_diamond")
    @JvmField
    public val ARMOR_EQUIP_ELYTRA: SoundEvent = get("item.armor.equip_elytra")
    @JvmField
    public val ARMOR_EQUIP_GENERIC: SoundEvent = get("item.armor.equip_generic")
    @JvmField
    public val ARMOR_EQUIP_GOLD: SoundEvent = get("item.armor.equip_gold")
    @JvmField
    public val ARMOR_EQUIP_IRON: SoundEvent = get("item.armor.equip_iron")
    @JvmField
    public val ARMOR_EQUIP_LEATHER: SoundEvent = get("item.armor.equip_leather")
    @JvmField
    public val ARMOR_EQUIP_NETHERITE: SoundEvent = get("item.armor.equip_netherite")
    @JvmField
    public val ARMOR_EQUIP_TURTLE: SoundEvent = get("item.armor.equip_turtle")
    @JvmField
    public val ARMOR_STAND_BREAK: SoundEvent = get("entity.armor_stand.break")
    @JvmField
    public val ARMOR_STAND_FALL: SoundEvent = get("entity.armor_stand.fall")
    @JvmField
    public val ARMOR_STAND_HIT: SoundEvent = get("entity.armor_stand.hit")
    @JvmField
    public val ARMOR_STAND_PLACE: SoundEvent = get("entity.armor_stand.place")
    @JvmField
    public val ARROW_HIT: SoundEvent = get("entity.arrow.hit")
    @JvmField
    public val ARROW_HIT_PLAYER: SoundEvent = get("entity.arrow.hit_player")
    @JvmField
    public val ARROW_SHOOT: SoundEvent = get("entity.arrow.shoot")
    @JvmField
    public val AXE_STRIP: SoundEvent = get("item.axe.strip")
    @JvmField
    public val AXE_SCRAPE: SoundEvent = get("item.axe.scrape")
    @JvmField
    public val AXE_WAX_OFF: SoundEvent = get("item.axe.wax_off")
    @JvmField
    public val AXOLOTL_ATTACK: SoundEvent = get("entity.axolotl.attack")
    @JvmField
    public val AXOLOTL_DEATH: SoundEvent = get("entity.axolotl.death")
    @JvmField
    public val AXOLOTL_HURT: SoundEvent = get("entity.axolotl.hurt")
    @JvmField
    public val AXOLOTL_IDLE_AIR: SoundEvent = get("entity.axolotl.idle_air")
    @JvmField
    public val AXOLOTL_IDLE_WATER: SoundEvent = get("entity.axolotl.idle_water")
    @JvmField
    public val AXOLOTL_SPLASH: SoundEvent = get("entity.axolotl.splash")
    @JvmField
    public val AXOLOTL_SWIM: SoundEvent = get("entity.axolotl.swim")
    @JvmField
    public val AZALEA_BREAK: SoundEvent = get("block.azalea.break")
    @JvmField
    public val AZALEA_FALL: SoundEvent = get("block.azalea.fall")
    @JvmField
    public val AZALEA_HIT: SoundEvent = get("block.azalea.hit")
    @JvmField
    public val AZALEA_PLACE: SoundEvent = get("block.azalea.place")
    @JvmField
    public val AZALEA_STEP: SoundEvent = get("block.azalea.step")
    @JvmField
    public val AZALEA_LEAVES_BREAK: SoundEvent = get("block.azalea_leaves.break")
    @JvmField
    public val AZALEA_LEAVES_FALL: SoundEvent = get("block.azalea_leaves.fall")
    @JvmField
    public val AZALEA_LEAVES_HIT: SoundEvent = get("block.azalea_leaves.hit")
    @JvmField
    public val AZALEA_LEAVES_PLACE: SoundEvent = get("block.azalea_leaves.place")
    @JvmField
    public val AZALEA_LEAVES_STEP: SoundEvent = get("block.azalea_leaves.step")
    @JvmField
    public val BAMBOO_BREAK: SoundEvent = get("block.bamboo.break")
    @JvmField
    public val BAMBOO_FALL: SoundEvent = get("block.bamboo.fall")
    @JvmField
    public val BAMBOO_HIT: SoundEvent = get("block.bamboo.hit")
    @JvmField
    public val BAMBOO_PLACE: SoundEvent = get("block.bamboo.place")
    @JvmField
    public val BAMBOO_STEP: SoundEvent = get("block.bamboo.step")
    @JvmField
    public val BAMBOO_SAPLING_BREAK: SoundEvent = get("block.bamboo_sapling.break")
    @JvmField
    public val BAMBOO_SAPLING_HIT: SoundEvent = get("block.bamboo_sapling.hit")
    @JvmField
    public val BAMBOO_SAPLING_PLACE: SoundEvent = get("block.bamboo_sapling.place")
    @JvmField
    public val BARREL_CLOSE: SoundEvent = get("block.barrel.close")
    @JvmField
    public val BARREL_OPEN: SoundEvent = get("block.barrel.open")
    @JvmField
    public val BASALT_BREAK: SoundEvent = get("block.basalt.break")
    @JvmField
    public val BASALT_STEP: SoundEvent = get("block.basalt.step")
    @JvmField
    public val BASALT_PLACE: SoundEvent = get("block.basalt.place")
    @JvmField
    public val BASALT_HIT: SoundEvent = get("block.basalt.hit")
    @JvmField
    public val BASALT_FALL: SoundEvent = get("block.basalt.fall")
    @JvmField
    public val BAT_AMBIENT: SoundEvent = get("entity.bat.ambient")
    @JvmField
    public val BAT_DEATH: SoundEvent = get("entity.bat.death")
    @JvmField
    public val BAT_HURT: SoundEvent = get("entity.bat.hurt")
    @JvmField
    public val BAT_LOOP: SoundEvent = get("entity.bat.loop")
    @JvmField
    public val BAT_TAKEOFF: SoundEvent = get("entity.bat.takeoff")
    @JvmField
    public val BEACON_ACTIVATE: SoundEvent = get("block.beacon.activate")
    @JvmField
    public val BEACON_AMBIENT: SoundEvent = get("block.beacon.ambient")
    @JvmField
    public val BEACON_DEACTIVATE: SoundEvent = get("block.beacon.deactivate")
    @JvmField
    public val BEACON_POWER_SELECT: SoundEvent = get("block.beacon.power_select")
    @JvmField
    public val BEE_DEATH: SoundEvent = get("entity.bee.death")
    @JvmField
    public val BEE_HURT: SoundEvent = get("entity.bee.hurt")
    @JvmField
    public val BEE_LOOP_AGGRESSIVE: SoundEvent = get("entity.bee.loop_aggressive")
    @JvmField
    public val BEE_LOOP: SoundEvent = get("entity.bee.loop")
    @JvmField
    public val BEE_STING: SoundEvent = get("entity.bee.sting")
    @JvmField
    public val BEE_POLLINATE: SoundEvent = get("entity.bee.pollinate")
    @JvmField
    public val BEEHIVE_DRIP: SoundEvent = get("block.beehive.drip")
    @JvmField
    public val BEEHIVE_ENTER: SoundEvent = get("block.beehive.enter")
    @JvmField
    public val BEEHIVE_EXIT: SoundEvent = get("block.beehive.exit")
    @JvmField
    public val BEEHIVE_SHEAR: SoundEvent = get("block.beehive.shear")
    @JvmField
    public val BEEHIVE_WORK: SoundEvent = get("block.beehive.work")
    @JvmField
    public val BELL_BLOCK: SoundEvent = get("block.bell.use")
    @JvmField
    public val BELL_RESONATE: SoundEvent = get("block.bell.resonate")
    @JvmField
    public val BIG_DRIPLEAF_BREAK: SoundEvent = get("block.big_dripleaf.break")
    @JvmField
    public val BIG_DRIPLEAF_FALL: SoundEvent = get("block.big_dripleaf.fall")
    @JvmField
    public val BIG_DRIPLEAF_HIT: SoundEvent = get("block.big_dripleaf.hit")
    @JvmField
    public val BIG_DRIPLEAF_PLACE: SoundEvent = get("block.big_dripleaf.place")
    @JvmField
    public val BIG_DRIPLEAF_STEP: SoundEvent = get("block.big_dripleaf.step")
    @JvmField
    public val BLAZE_AMBIENT: SoundEvent = get("entity.blaze.ambient")
    @JvmField
    public val BLAZE_BURN: SoundEvent = get("entity.blaze.burn")
    @JvmField
    public val BLAZE_DEATH: SoundEvent = get("entity.blaze.death")
    @JvmField
    public val BLAZE_HURT: SoundEvent = get("entity.blaze.hurt")
    @JvmField
    public val BLAZE_SHOOT: SoundEvent = get("entity.blaze.shoot")
    @JvmField
    public val BOAT_PADDLE_LAND: SoundEvent = get("entity.boat.paddle_land")
    @JvmField
    public val BOAT_PADDLE_WATER: SoundEvent = get("entity.boat.paddle_water")
    @JvmField
    public val BONE_BLOCK_BREAK: SoundEvent = get("block.bone_block.break")
    @JvmField
    public val BONE_BLOCK_FALL: SoundEvent = get("block.bone_block.fall")
    @JvmField
    public val BONE_BLOCK_HIT: SoundEvent = get("block.bone_block.hit")
    @JvmField
    public val BONE_BLOCK_PLACE: SoundEvent = get("block.bone_block.place")
    @JvmField
    public val BONE_BLOCK_STEP: SoundEvent = get("block.bone_block.step")
    @JvmField
    public val BONE_MEAL_USE: SoundEvent = get("item.bone_meal.use")
    @JvmField
    public val BOOK_PAGE_TURN: SoundEvent = get("item.book.page_turn")
    @JvmField
    public val BOOK_PUT: SoundEvent = get("item.book.put")
    @JvmField
    public val BLASTFURNACE_FIRE_CRACKLE: SoundEvent = get("block.blastfurnace.fire_crackle")
    @JvmField
    public val BOTTLE_EMPTY: SoundEvent = get("item.bottle.empty")
    @JvmField
    public val BOTTLE_FILL: SoundEvent = get("item.bottle.fill")
    @JvmField
    public val BOTTLE_FILL_DRAGONBREATH: SoundEvent = get("item.bottle.fill_dragonbreath")
    @JvmField
    public val BREWING_STAND_BREW: SoundEvent = get("block.brewing_stand.brew")
    @JvmField
    public val BUBBLE_COLUMN_BUBBLE_POP: SoundEvent = get("block.bubble_column.bubble_pop")
    @JvmField
    public val BUBBLE_COLUMN_UPWARDS_AMBIENT: SoundEvent = get("block.bubble_column.upwards_ambient")
    @JvmField
    public val BUBBLE_COLUMN_UPWARDS_INSIDE: SoundEvent = get("block.bubble_column.upwards_inside")
    @JvmField
    public val BUBBLE_COLUMN_WHIRLPOOL_AMBIENT: SoundEvent = get("block.bubble_column.whirlpool_ambient")
    @JvmField
    public val BUBBLE_COLUMN_WHIRLPOOL_INSIDE: SoundEvent = get("block.bubble_column.whirlpool_inside")
    @JvmField
    public val BUCKET_EMPTY: SoundEvent = get("item.bucket.empty")
    @JvmField
    public val BUCKET_EMPTY_AXOLOTL: SoundEvent = get("item.bucket.empty_axolotl")
    @JvmField
    public val BUCKET_EMPTY_FISH: SoundEvent = get("item.bucket.empty_fish")
    @JvmField
    public val BUCKET_EMPTY_LAVA: SoundEvent = get("item.bucket.empty_lava")
    @JvmField
    public val BUCKET_EMPTY_POWDER_SNOW: SoundEvent = get("item.bucket.empty_powder_snow")
    @JvmField
    public val BUCKET_EMPTY_TADPOLE: SoundEvent = get("item.bucket.empty_tadpole")
    @JvmField
    public val BUCKET_FILL: SoundEvent = get("item.bucket.fill")
    @JvmField
    public val BUCKET_FILL_AXOLOTL: SoundEvent = get("item.bucket.fill_axolotl")
    @JvmField
    public val BUCKET_FILL_FISH: SoundEvent = get("item.bucket.fill_fish")
    @JvmField
    public val BUCKET_FILL_LAVA: SoundEvent = get("item.bucket.fill_lava")
    @JvmField
    public val BUCKET_FILL_POWDER_SNOW: SoundEvent = get("item.bucket.fill_powder_snow")
    @JvmField
    public val BUCKET_FILL_TADPOLE: SoundEvent = get("item.bucket.fill_tadpole")
    @JvmField
    public val BUNDLE_DROP_CONTENTS: SoundEvent = get("item.bundle.drop_contents")
    @JvmField
    public val BUNDLE_INSERT: SoundEvent = get("item.bundle.insert")
    @JvmField
    public val BUNDLE_REMOVE_ONE: SoundEvent = get("item.bundle.remove_one")
    @JvmField
    public val CAKE_ADD_CANDLE: SoundEvent = get("block.cake.add_candle")
    @JvmField
    public val CALCITE_BREAK: SoundEvent = get("block.calcite.break")
    @JvmField
    public val CALCITE_STEP: SoundEvent = get("block.calcite.step")
    @JvmField
    public val CALCITE_PLACE: SoundEvent = get("block.calcite.place")
    @JvmField
    public val CALCITE_HIT: SoundEvent = get("block.calcite.hit")
    @JvmField
    public val CALCITE_FALL: SoundEvent = get("block.calcite.fall")
    @JvmField
    public val CAMPFIRE_CRACKLE: SoundEvent = get("block.campfire.crackle")
    @JvmField
    public val CANDLE_AMBIENT: SoundEvent = get("block.candle.ambient")
    @JvmField
    public val CANDLE_BREAK: SoundEvent = get("block.candle.break")
    @JvmField
    public val CANDLE_EXTINGUISH: SoundEvent = get("block.candle.extinguish")
    @JvmField
    public val CANDLE_FALL: SoundEvent = get("block.candle.fall")
    @JvmField
    public val CANDLE_HIT: SoundEvent = get("block.candle.hit")
    @JvmField
    public val CANDLE_PLACE: SoundEvent = get("block.candle.place")
    @JvmField
    public val CANDLE_STEP: SoundEvent = get("block.candle.step")
    @JvmField
    public val CAT_AMBIENT: SoundEvent = get("entity.cat.ambient")
    @JvmField
    public val CAT_STRAY_AMBIENT: SoundEvent = get("entity.cat.stray_ambient")
    @JvmField
    public val CAT_DEATH: SoundEvent = get("entity.cat.death")
    @JvmField
    public val CAT_EAT: SoundEvent = get("entity.cat.eat")
    @JvmField
    public val CAT_HISS: SoundEvent = get("entity.cat.hiss")
    @JvmField
    public val CAT_BEG_FOR_FOOD: SoundEvent = get("entity.cat.beg_for_food")
    @JvmField
    public val CAT_HURT: SoundEvent = get("entity.cat.hurt")
    @JvmField
    public val CAT_PURR: SoundEvent = get("entity.cat.purr")
    @JvmField
    public val CAT_PURREOW: SoundEvent = get("entity.cat.purreow")
    @JvmField
    public val CAVE_VINES_BREAK: SoundEvent = get("block.cave_vines.break")
    @JvmField
    public val CAVE_VINES_FALL: SoundEvent = get("block.cave_vines.fall")
    @JvmField
    public val CAVE_VINES_HIT: SoundEvent = get("block.cave_vines.hit")
    @JvmField
    public val CAVE_VINES_PLACE: SoundEvent = get("block.cave_vines.place")
    @JvmField
    public val CAVE_VINES_STEP: SoundEvent = get("block.cave_vines.step")
    @JvmField
    public val CAVE_VINES_PICK_BERRIES: SoundEvent = get("block.cave_vines.pick_berries")
    @JvmField
    public val CHAIN_BREAK: SoundEvent = get("block.chain.break")
    @JvmField
    public val CHAIN_FALL: SoundEvent = get("block.chain.fall")
    @JvmField
    public val CHAIN_HIT: SoundEvent = get("block.chain.hit")
    @JvmField
    public val CHAIN_PLACE: SoundEvent = get("block.chain.place")
    @JvmField
    public val CHAIN_STEP: SoundEvent = get("block.chain.step")
    @JvmField
    public val CHEST_CLOSE: SoundEvent = get("block.chest.close")
    @JvmField
    public val CHEST_LOCKED: SoundEvent = get("block.chest.locked")
    @JvmField
    public val CHEST_OPEN: SoundEvent = get("block.chest.open")
    @JvmField
    public val CHICKEN_AMBIENT: SoundEvent = get("entity.chicken.ambient")
    @JvmField
    public val CHICKEN_DEATH: SoundEvent = get("entity.chicken.death")
    @JvmField
    public val CHICKEN_EGG: SoundEvent = get("entity.chicken.egg")
    @JvmField
    public val CHICKEN_HURT: SoundEvent = get("entity.chicken.hurt")
    @JvmField
    public val CHICKEN_STEP: SoundEvent = get("entity.chicken.step")
    @JvmField
    public val CHORUS_FLOWER_DEATH: SoundEvent = get("block.chorus_flower.death")
    @JvmField
    public val CHORUS_FLOWER_GROW: SoundEvent = get("block.chorus_flower.grow")
    @JvmField
    public val CHORUS_FRUIT_TELEPORT: SoundEvent = get("item.chorus_fruit.teleport")
    @JvmField
    public val COD_AMBIENT: SoundEvent = get("entity.cod.ambient")
    @JvmField
    public val COD_DEATH: SoundEvent = get("entity.cod.death")
    @JvmField
    public val COD_FLOP: SoundEvent = get("entity.cod.flop")
    @JvmField
    public val COD_HURT: SoundEvent = get("entity.cod.hurt")
    @JvmField
    public val COMPARATOR_CLICK: SoundEvent = get("block.comparator.click")
    @JvmField
    public val COMPOSTER_EMPTY: SoundEvent = get("block.composter.empty")
    @JvmField
    public val COMPOSTER_FILL: SoundEvent = get("block.composter.fill")
    @JvmField
    public val COMPOSTER_FILL_SUCCESS: SoundEvent = get("block.composter.fill_success")
    @JvmField
    public val COMPOSTER_READY: SoundEvent = get("block.composter.ready")
    @JvmField
    public val CONDUIT_ACTIVATE: SoundEvent = get("block.conduit.activate")
    @JvmField
    public val CONDUIT_AMBIENT: SoundEvent = get("block.conduit.ambient")
    @JvmField
    public val CONDUIT_AMBIENT_SHORT: SoundEvent = get("block.conduit.ambient.short")
    @JvmField
    public val CONDUIT_ATTACK_TARGET: SoundEvent = get("block.conduit.attack.target")
    @JvmField
    public val CONDUIT_DEACTIVATE: SoundEvent = get("block.conduit.deactivate")
    @JvmField
    public val COPPER_BREAK: SoundEvent = get("block.copper.break")
    @JvmField
    public val COPPER_STEP: SoundEvent = get("block.copper.step")
    @JvmField
    public val COPPER_PLACE: SoundEvent = get("block.copper.place")
    @JvmField
    public val COPPER_HIT: SoundEvent = get("block.copper.hit")
    @JvmField
    public val COPPER_FALL: SoundEvent = get("block.copper.fall")
    @JvmField
    public val CORAL_BLOCK_BREAK: SoundEvent = get("block.coral_block.break")
    @JvmField
    public val CORAL_BLOCK_FALL: SoundEvent = get("block.coral_block.fall")
    @JvmField
    public val CORAL_BLOCK_HIT: SoundEvent = get("block.coral_block.hit")
    @JvmField
    public val CORAL_BLOCK_PLACE: SoundEvent = get("block.coral_block.place")
    @JvmField
    public val CORAL_BLOCK_STEP: SoundEvent = get("block.coral_block.step")
    @JvmField
    public val COW_AMBIENT: SoundEvent = get("entity.cow.ambient")
    @JvmField
    public val COW_DEATH: SoundEvent = get("entity.cow.death")
    @JvmField
    public val COW_HURT: SoundEvent = get("entity.cow.hurt")
    @JvmField
    public val COW_MILK: SoundEvent = get("entity.cow.milk")
    @JvmField
    public val COW_STEP: SoundEvent = get("entity.cow.step")
    @JvmField
    public val CREEPER_DEATH: SoundEvent = get("entity.creeper.death")
    @JvmField
    public val CREEPER_HURT: SoundEvent = get("entity.creeper.hurt")
    @JvmField
    public val CREEPER_PRIMED: SoundEvent = get("entity.creeper.primed")
    @JvmField
    public val CROP_BREAK: SoundEvent = get("block.crop.break")
    @JvmField
    public val CROP_PLANTED: SoundEvent = get("item.crop.plant")
    @JvmField
    public val CROSSBOW_HIT: SoundEvent = get("item.crossbow.hit")
    @JvmField
    public val CROSSBOW_LOADING_END: SoundEvent = get("item.crossbow.loading_end")
    @JvmField
    public val CROSSBOW_LOADING_MIDDLE: SoundEvent = get("item.crossbow.loading_middle")
    @JvmField
    public val CROSSBOW_LOADING_START: SoundEvent = get("item.crossbow.loading_start")
    @JvmField
    public val CROSSBOW_QUICK_CHARGE_1: SoundEvent = get("item.crossbow.quick_charge_1")
    @JvmField
    public val CROSSBOW_QUICK_CHARGE_2: SoundEvent = get("item.crossbow.quick_charge_2")
    @JvmField
    public val CROSSBOW_QUICK_CHARGE_3: SoundEvent = get("item.crossbow.quick_charge_3")
    @JvmField
    public val CROSSBOW_SHOOT: SoundEvent = get("item.crossbow.shoot")
    @JvmField
    public val DEEPSLATE_BRICKS_BREAK: SoundEvent = get("block.deepslate_bricks.break")
    @JvmField
    public val DEEPSLATE_BRICKS_FALL: SoundEvent = get("block.deepslate_bricks.fall")
    @JvmField
    public val DEEPSLATE_BRICKS_HIT: SoundEvent = get("block.deepslate_bricks.hit")
    @JvmField
    public val DEEPSLATE_BRICKS_PLACE: SoundEvent = get("block.deepslate_bricks.place")
    @JvmField
    public val DEEPSLATE_BRICKS_STEP: SoundEvent = get("block.deepslate_bricks.step")
    @JvmField
    public val DEEPSLATE_BREAK: SoundEvent = get("block.deepslate.break")
    @JvmField
    public val DEEPSLATE_FALL: SoundEvent = get("block.deepslate.fall")
    @JvmField
    public val DEEPSLATE_HIT: SoundEvent = get("block.deepslate.hit")
    @JvmField
    public val DEEPSLATE_PLACE: SoundEvent = get("block.deepslate.place")
    @JvmField
    public val DEEPSLATE_STEP: SoundEvent = get("block.deepslate.step")
    @JvmField
    public val DEEPSLATE_TILES_BREAK: SoundEvent = get("block.deepslate_tiles.break")
    @JvmField
    public val DEEPSLATE_TILES_FALL: SoundEvent = get("block.deepslate_tiles.fall")
    @JvmField
    public val DEEPSLATE_TILES_HIT: SoundEvent = get("block.deepslate_tiles.hit")
    @JvmField
    public val DEEPSLATE_TILES_PLACE: SoundEvent = get("block.deepslate_tiles.place")
    @JvmField
    public val DEEPSLATE_TILES_STEP: SoundEvent = get("block.deepslate_tiles.step")
    @JvmField
    public val DISPENSER_DISPENSE: SoundEvent = get("block.dispenser.dispense")
    @JvmField
    public val DISPENSER_FAIL: SoundEvent = get("block.dispenser.fail")
    @JvmField
    public val DISPENSER_LAUNCH: SoundEvent = get("block.dispenser.launch")
    @JvmField
    public val DOLPHIN_AMBIENT: SoundEvent = get("entity.dolphin.ambient")
    @JvmField
    public val DOLPHIN_AMBIENT_WATER: SoundEvent = get("entity.dolphin.ambient_water")
    @JvmField
    public val DOLPHIN_ATTACK: SoundEvent = get("entity.dolphin.attack")
    @JvmField
    public val DOLPHIN_DEATH: SoundEvent = get("entity.dolphin.death")
    @JvmField
    public val DOLPHIN_EAT: SoundEvent = get("entity.dolphin.eat")
    @JvmField
    public val DOLPHIN_HURT: SoundEvent = get("entity.dolphin.hurt")
    @JvmField
    public val DOLPHIN_JUMP: SoundEvent = get("entity.dolphin.jump")
    @JvmField
    public val DOLPHIN_PLAY: SoundEvent = get("entity.dolphin.play")
    @JvmField
    public val DOLPHIN_SPLASH: SoundEvent = get("entity.dolphin.splash")
    @JvmField
    public val DOLPHIN_SWIM: SoundEvent = get("entity.dolphin.swim")
    @JvmField
    public val DONKEY_AMBIENT: SoundEvent = get("entity.donkey.ambient")
    @JvmField
    public val DONKEY_ANGRY: SoundEvent = get("entity.donkey.angry")
    @JvmField
    public val DONKEY_CHEST: SoundEvent = get("entity.donkey.chest")
    @JvmField
    public val DONKEY_DEATH: SoundEvent = get("entity.donkey.death")
    @JvmField
    public val DONKEY_EAT: SoundEvent = get("entity.donkey.eat")
    @JvmField
    public val DONKEY_HURT: SoundEvent = get("entity.donkey.hurt")
    @JvmField
    public val DRIPSTONE_BLOCK_BREAK: SoundEvent = get("block.dripstone_block.break")
    @JvmField
    public val DRIPSTONE_BLOCK_STEP: SoundEvent = get("block.dripstone_block.step")
    @JvmField
    public val DRIPSTONE_BLOCK_PLACE: SoundEvent = get("block.dripstone_block.place")
    @JvmField
    public val DRIPSTONE_BLOCK_HIT: SoundEvent = get("block.dripstone_block.hit")
    @JvmField
    public val DRIPSTONE_BLOCK_FALL: SoundEvent = get("block.dripstone_block.fall")
    @JvmField
    public val POINTED_DRIPSTONE_BREAK: SoundEvent = get("block.pointed_dripstone.break")
    @JvmField
    public val POINTED_DRIPSTONE_STEP: SoundEvent = get("block.pointed_dripstone.step")
    @JvmField
    public val POINTED_DRIPSTONE_PLACE: SoundEvent = get("block.pointed_dripstone.place")
    @JvmField
    public val POINTED_DRIPSTONE_HIT: SoundEvent = get("block.pointed_dripstone.hit")
    @JvmField
    public val POINTED_DRIPSTONE_FALL: SoundEvent = get("block.pointed_dripstone.fall")
    @JvmField
    public val POINTED_DRIPSTONE_LAND: SoundEvent = get("block.pointed_dripstone.land")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_LAVA: SoundEvent = get("block.pointed_dripstone.drip_lava")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_WATER: SoundEvent = get("block.pointed_dripstone.drip_water")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON: SoundEvent = get("block.pointed_dripstone.drip_lava_into_cauldron")
    @JvmField
    public val POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON: SoundEvent = get("block.pointed_dripstone.drip_water_into_cauldron")
    @JvmField
    public val BIG_DRIPLEAF_TILT_DOWN: SoundEvent = get("block.big_dripleaf.tilt_down")
    @JvmField
    public val BIG_DRIPLEAF_TILT_UP: SoundEvent = get("block.big_dripleaf.tilt_up")
    @JvmField
    public val DROWNED_AMBIENT: SoundEvent = get("entity.drowned.ambient")
    @JvmField
    public val DROWNED_AMBIENT_WATER: SoundEvent = get("entity.drowned.ambient_water")
    @JvmField
    public val DROWNED_DEATH: SoundEvent = get("entity.drowned.death")
    @JvmField
    public val DROWNED_DEATH_WATER: SoundEvent = get("entity.drowned.death_water")
    @JvmField
    public val DROWNED_HURT: SoundEvent = get("entity.drowned.hurt")
    @JvmField
    public val DROWNED_HURT_WATER: SoundEvent = get("entity.drowned.hurt_water")
    @JvmField
    public val DROWNED_SHOOT: SoundEvent = get("entity.drowned.shoot")
    @JvmField
    public val DROWNED_STEP: SoundEvent = get("entity.drowned.step")
    @JvmField
    public val DROWNED_SWIM: SoundEvent = get("entity.drowned.swim")
    @JvmField
    public val DYE_USE: SoundEvent = get("item.dye.use")
    @JvmField
    public val EGG_THROW: SoundEvent = get("entity.egg.throw")
    @JvmField
    public val ELDER_GUARDIAN_AMBIENT: SoundEvent = get("entity.elder_guardian.ambient")
    @JvmField
    public val ELDER_GUARDIAN_AMBIENT_LAND: SoundEvent = get("entity.elder_guardian.ambient_land")
    @JvmField
    public val ELDER_GUARDIAN_CURSE: SoundEvent = get("entity.elder_guardian.curse")
    @JvmField
    public val ELDER_GUARDIAN_DEATH: SoundEvent = get("entity.elder_guardian.death")
    @JvmField
    public val ELDER_GUARDIAN_DEATH_LAND: SoundEvent = get("entity.elder_guardian.death_land")
    @JvmField
    public val ELDER_GUARDIAN_FLOP: SoundEvent = get("entity.elder_guardian.flop")
    @JvmField
    public val ELDER_GUARDIAN_HURT: SoundEvent = get("entity.elder_guardian.hurt")
    @JvmField
    public val ELDER_GUARDIAN_HURT_LAND: SoundEvent = get("entity.elder_guardian.hurt_land")
    @JvmField
    public val ELYTRA_FLYING: SoundEvent = get("item.elytra.flying")
    @JvmField
    public val ENCHANTMENT_TABLE_USE: SoundEvent = get("block.enchantment_table.use")
    @JvmField
    public val ENDER_CHEST_CLOSE: SoundEvent = get("block.ender_chest.close")
    @JvmField
    public val ENDER_CHEST_OPEN: SoundEvent = get("block.ender_chest.open")
    @JvmField
    public val ENDER_DRAGON_AMBIENT: SoundEvent = get("entity.ender_dragon.ambient")
    @JvmField
    public val ENDER_DRAGON_DEATH: SoundEvent = get("entity.ender_dragon.death")
    @JvmField
    public val DRAGON_FIREBALL_EXPLODE: SoundEvent = get("entity.dragon_fireball.explode")
    @JvmField
    public val ENDER_DRAGON_FLAP: SoundEvent = get("entity.ender_dragon.flap")
    @JvmField
    public val ENDER_DRAGON_GROWL: SoundEvent = get("entity.ender_dragon.growl")
    @JvmField
    public val ENDER_DRAGON_HURT: SoundEvent = get("entity.ender_dragon.hurt")
    @JvmField
    public val ENDER_DRAGON_SHOOT: SoundEvent = get("entity.ender_dragon.shoot")
    @JvmField
    public val ENDER_EYE_DEATH: SoundEvent = get("entity.ender_eye.death")
    @JvmField
    public val ENDER_EYE_LAUNCH: SoundEvent = get("entity.ender_eye.launch")
    @JvmField
    public val ENDERMAN_AMBIENT: SoundEvent = get("entity.enderman.ambient")
    @JvmField
    public val ENDERMAN_DEATH: SoundEvent = get("entity.enderman.death")
    @JvmField
    public val ENDERMAN_HURT: SoundEvent = get("entity.enderman.hurt")
    @JvmField
    public val ENDERMAN_SCREAM: SoundEvent = get("entity.enderman.scream")
    @JvmField
    public val ENDERMAN_STARE: SoundEvent = get("entity.enderman.stare")
    @JvmField
    public val ENDERMAN_TELEPORT: SoundEvent = get("entity.enderman.teleport")
    @JvmField
    public val ENDERMITE_AMBIENT: SoundEvent = get("entity.endermite.ambient")
    @JvmField
    public val ENDERMITE_DEATH: SoundEvent = get("entity.endermite.death")
    @JvmField
    public val ENDERMITE_HURT: SoundEvent = get("entity.endermite.hurt")
    @JvmField
    public val ENDERMITE_STEP: SoundEvent = get("entity.endermite.step")
    @JvmField
    public val ENDER_PEARL_THROW: SoundEvent = get("entity.ender_pearl.throw")
    @JvmField
    public val END_GATEWAY_SPAWN: SoundEvent = get("block.end_gateway.spawn")
    @JvmField
    public val END_PORTAL_FRAME_FILL: SoundEvent = get("block.end_portal_frame.fill")
    @JvmField
    public val END_PORTAL_SPAWN: SoundEvent = get("block.end_portal.spawn")
    @JvmField
    public val EVOKER_AMBIENT: SoundEvent = get("entity.evoker.ambient")
    @JvmField
    public val EVOKER_CAST_SPELL: SoundEvent = get("entity.evoker.cast_spell")
    @JvmField
    public val EVOKER_CELEBRATE: SoundEvent = get("entity.evoker.celebrate")
    @JvmField
    public val EVOKER_DEATH: SoundEvent = get("entity.evoker.death")
    @JvmField
    public val EVOKER_FANGS_ATTACK: SoundEvent = get("entity.evoker_fangs.attack")
    @JvmField
    public val EVOKER_HURT: SoundEvent = get("entity.evoker.hurt")
    @JvmField
    public val EVOKER_PREPARE_ATTACK: SoundEvent = get("entity.evoker.prepare_attack")
    @JvmField
    public val EVOKER_PREPARE_SUMMON: SoundEvent = get("entity.evoker.prepare_summon")
    @JvmField
    public val EVOKER_PREPARE_WOLOLO: SoundEvent = get("entity.evoker.prepare_wololo")
    @JvmField
    public val EXPERIENCE_BOTTLE_THROW: SoundEvent = get("entity.experience_bottle.throw")
    @JvmField
    public val EXPERIENCE_ORB_PICKUP: SoundEvent = get("entity.experience_orb.pickup")
    @JvmField
    public val FENCE_GATE_CLOSE: SoundEvent = get("block.fence_gate.close")
    @JvmField
    public val FENCE_GATE_OPEN: SoundEvent = get("block.fence_gate.open")
    @JvmField
    public val FIRECHARGE_USE: SoundEvent = get("item.firecharge.use")
    @JvmField
    public val FIREWORK_ROCKET_BLAST: SoundEvent = get("entity.firework_rocket.blast")
    @JvmField
    public val FIREWORK_ROCKET_BLAST_FAR: SoundEvent = get("entity.firework_rocket.blast_far")
    @JvmField
    public val FIREWORK_ROCKET_LARGE_BLAST: SoundEvent = get("entity.firework_rocket.large_blast")
    @JvmField
    public val FIREWORK_ROCKET_LARGE_BLAST_FAR: SoundEvent = get("entity.firework_rocket.large_blast_far")
    @JvmField
    public val FIREWORK_ROCKET_LAUNCH: SoundEvent = get("entity.firework_rocket.launch")
    @JvmField
    public val FIREWORK_ROCKET_SHOOT: SoundEvent = get("entity.firework_rocket.shoot")
    @JvmField
    public val FIREWORK_ROCKET_TWINKLE: SoundEvent = get("entity.firework_rocket.twinkle")
    @JvmField
    public val FIREWORK_ROCKET_TWINKLE_FAR: SoundEvent = get("entity.firework_rocket.twinkle_far")
    @JvmField
    public val FIRE_AMBIENT: SoundEvent = get("block.fire.ambient")
    @JvmField
    public val FIRE_EXTINGUISH: SoundEvent = get("block.fire.extinguish")
    @JvmField
    public val FISH_SWIM: SoundEvent = get("entity.fish.swim")
    @JvmField
    public val FISHING_BOBBER_RETRIEVE: SoundEvent = get("entity.fishing_bobber.retrieve")
    @JvmField
    public val FISHING_BOBBER_SPLASH: SoundEvent = get("entity.fishing_bobber.splash")
    @JvmField
    public val FISHING_BOBBER_THROW: SoundEvent = get("entity.fishing_bobber.throw")
    @JvmField
    public val FLINTANDSTEEL_USE: SoundEvent = get("item.flintandsteel.use")
    @JvmField
    public val FLOWERING_AZALEA_BREAK: SoundEvent = get("block.flowering_azalea.break")
    @JvmField
    public val FLOWERING_AZALEA_FALL: SoundEvent = get("block.flowering_azalea.fall")
    @JvmField
    public val FLOWERING_AZALEA_HIT: SoundEvent = get("block.flowering_azalea.hit")
    @JvmField
    public val FLOWERING_AZALEA_PLACE: SoundEvent = get("block.flowering_azalea.place")
    @JvmField
    public val FLOWERING_AZALEA_STEP: SoundEvent = get("block.flowering_azalea.step")
    @JvmField
    public val FOX_AGGRO: SoundEvent = get("entity.fox.aggro")
    @JvmField
    public val FOX_AMBIENT: SoundEvent = get("entity.fox.ambient")
    @JvmField
    public val FOX_BITE: SoundEvent = get("entity.fox.bite")
    @JvmField
    public val FOX_DEATH: SoundEvent = get("entity.fox.death")
    @JvmField
    public val FOX_EAT: SoundEvent = get("entity.fox.eat")
    @JvmField
    public val FOX_HURT: SoundEvent = get("entity.fox.hurt")
    @JvmField
    public val FOX_SCREECH: SoundEvent = get("entity.fox.screech")
    @JvmField
    public val FOX_SLEEP: SoundEvent = get("entity.fox.sleep")
    @JvmField
    public val FOX_SNIFF: SoundEvent = get("entity.fox.sniff")
    @JvmField
    public val FOX_SPIT: SoundEvent = get("entity.fox.spit")
    @JvmField
    public val FOX_TELEPORT: SoundEvent = get("entity.fox.teleport")
    @JvmField
    public val FROGLIGHT_BREAK: SoundEvent = get("block.froglight.break")
    @JvmField
    public val FROGLIGHT_FALL: SoundEvent = get("block.froglight.fall")
    @JvmField
    public val FROGLIGHT_HIT: SoundEvent = get("block.froglight.hit")
    @JvmField
    public val FROGLIGHT_PLACE: SoundEvent = get("block.froglight.place")
    @JvmField
    public val FROGLIGHT_STEP: SoundEvent = get("block.froglight.step")
    @JvmField
    public val FROGSPAWNSTEP: SoundEvent = get("block.frogspawn.step")
    @JvmField
    public val FROGSPAWN_BREAK: SoundEvent = get("block.frogspawn.break")
    @JvmField
    public val FROGSPAWN_FALL: SoundEvent = get("block.frogspawn.fall")
    @JvmField
    public val FROGSPAWN_HATCH: SoundEvent = get("block.frogspawn.hatch")
    @JvmField
    public val FROGSPAWN_HIT: SoundEvent = get("block.frogspawn.hit")
    @JvmField
    public val FROGSPAWN_PLACE: SoundEvent = get("block.frogspawn.place")
    @JvmField
    public val FROG_AMBIENT: SoundEvent = get("entity.frog.ambient")
    @JvmField
    public val FROG_DEATH: SoundEvent = get("entity.frog.death")
    @JvmField
    public val FROG_EAT: SoundEvent = get("entity.frog.eat")
    @JvmField
    public val FROG_HURT: SoundEvent = get("entity.frog.hurt")
    @JvmField
    public val FROG_LAY_SPAWN: SoundEvent = get("entity.frog.lay_spawn")
    @JvmField
    public val FROG_LONG_JUMP: SoundEvent = get("entity.frog.long_jump")
    @JvmField
    public val FROG_STEP: SoundEvent = get("entity.frog.step")
    @JvmField
    public val FROG_TONGUE: SoundEvent = get("entity.frog.tongue")
    @JvmField
    public val ROOTS_BREAK: SoundEvent = get("block.roots.break")
    @JvmField
    public val ROOTS_STEP: SoundEvent = get("block.roots.step")
    @JvmField
    public val ROOTS_PLACE: SoundEvent = get("block.roots.place")
    @JvmField
    public val ROOTS_HIT: SoundEvent = get("block.roots.hit")
    @JvmField
    public val ROOTS_FALL: SoundEvent = get("block.roots.fall")
    @JvmField
    public val FURNACE_FIRE_CRACKLE: SoundEvent = get("block.furnace.fire_crackle")
    @JvmField
    public val GENERIC_BIG_FALL: SoundEvent = get("entity.generic.big_fall")
    @JvmField
    public val GENERIC_BURN: SoundEvent = get("entity.generic.burn")
    @JvmField
    public val GENERIC_DEATH: SoundEvent = get("entity.generic.death")
    @JvmField
    public val GENERIC_DRINK: SoundEvent = get("entity.generic.drink")
    @JvmField
    public val GENERIC_EAT: SoundEvent = get("entity.generic.eat")
    @JvmField
    public val GENERIC_EXPLODE: SoundEvent = get("entity.generic.explode")
    @JvmField
    public val GENERIC_EXTINGUISH_FIRE: SoundEvent = get("entity.generic.extinguish_fire")
    @JvmField
    public val GENERIC_HURT: SoundEvent = get("entity.generic.hurt")
    @JvmField
    public val GENERIC_SMALL_FALL: SoundEvent = get("entity.generic.small_fall")
    @JvmField
    public val GENERIC_SPLASH: SoundEvent = get("entity.generic.splash")
    @JvmField
    public val GENERIC_SWIM: SoundEvent = get("entity.generic.swim")
    @JvmField
    public val GHAST_AMBIENT: SoundEvent = get("entity.ghast.ambient")
    @JvmField
    public val GHAST_DEATH: SoundEvent = get("entity.ghast.death")
    @JvmField
    public val GHAST_HURT: SoundEvent = get("entity.ghast.hurt")
    @JvmField
    public val GHAST_SCREAM: SoundEvent = get("entity.ghast.scream")
    @JvmField
    public val GHAST_SHOOT: SoundEvent = get("entity.ghast.shoot")
    @JvmField
    public val GHAST_WARN: SoundEvent = get("entity.ghast.warn")
    @JvmField
    public val GILDED_BLACKSTONE_BREAK: SoundEvent = get("block.gilded_blackstone.break")
    @JvmField
    public val GILDED_BLACKSTONE_FALL: SoundEvent = get("block.gilded_blackstone.fall")
    @JvmField
    public val GILDED_BLACKSTONE_HIT: SoundEvent = get("block.gilded_blackstone.hit")
    @JvmField
    public val GILDED_BLACKSTONE_PLACE: SoundEvent = get("block.gilded_blackstone.place")
    @JvmField
    public val GILDED_BLACKSTONE_STEP: SoundEvent = get("block.gilded_blackstone.step")
    @JvmField
    public val GLASS_BREAK: SoundEvent = get("block.glass.break")
    @JvmField
    public val GLASS_FALL: SoundEvent = get("block.glass.fall")
    @JvmField
    public val GLASS_HIT: SoundEvent = get("block.glass.hit")
    @JvmField
    public val GLASS_PLACE: SoundEvent = get("block.glass.place")
    @JvmField
    public val GLASS_STEP: SoundEvent = get("block.glass.step")
    @JvmField
    public val GLOW_INK_SAC_USE: SoundEvent = get("item.glow_ink_sac.use")
    @JvmField
    public val GLOW_ITEM_FRAME_ADD_ITEM: SoundEvent = get("entity.glow_item_frame.add_item")
    @JvmField
    public val GLOW_ITEM_FRAME_BREAK: SoundEvent = get("entity.glow_item_frame.break")
    @JvmField
    public val GLOW_ITEM_FRAME_PLACE: SoundEvent = get("entity.glow_item_frame.place")
    @JvmField
    public val GLOW_ITEM_FRAME_REMOVE_ITEM: SoundEvent = get("entity.glow_item_frame.remove_item")
    @JvmField
    public val GLOW_ITEM_FRAME_ROTATE_ITEM: SoundEvent = get("entity.glow_item_frame.rotate_item")
    @JvmField
    public val GLOW_SQUID_AMBIENT: SoundEvent = get("entity.glow_squid.ambient")
    @JvmField
    public val GLOW_SQUID_DEATH: SoundEvent = get("entity.glow_squid.death")
    @JvmField
    public val GLOW_SQUID_HURT: SoundEvent = get("entity.glow_squid.hurt")
    @JvmField
    public val GLOW_SQUID_SQUIRT: SoundEvent = get("entity.glow_squid.squirt")
    @JvmField
    public val GOAT_AMBIENT: SoundEvent = get("entity.goat.ambient")
    @JvmField
    public val GOAT_DEATH: SoundEvent = get("entity.goat.death")
    @JvmField
    public val GOAT_EAT: SoundEvent = get("entity.goat.eat")
    @JvmField
    public val GOAT_HURT: SoundEvent = get("entity.goat.hurt")
    @JvmField
    public val GOAT_LONG_JUMP: SoundEvent = get("entity.goat.long_jump")
    @JvmField
    public val GOAT_MILK: SoundEvent = get("entity.goat.milk")
    @JvmField
    public val GOAT_PREPARE_RAM: SoundEvent = get("entity.goat.prepare_ram")
    @JvmField
    public val GOAT_RAM_IMPACT: SoundEvent = get("entity.goat.ram_impact")
    @JvmField
    public val GOAT_HORN_BREAK: SoundEvent = get("entity.goat.horn_break")
    @JvmField
    public val GOAT_HORN_PLAY: SoundEvent = get("item.goat_horn.play")
    @JvmField
    public val GOAT_SCREAMING_AMBIENT: SoundEvent = get("entity.goat.screaming.ambient")
    @JvmField
    public val GOAT_SCREAMING_DEATH: SoundEvent = get("entity.goat.screaming.death")
    @JvmField
    public val GOAT_SCREAMING_EAT: SoundEvent = get("entity.goat.screaming.eat")
    @JvmField
    public val GOAT_SCREAMING_HURT: SoundEvent = get("entity.goat.screaming.hurt")
    @JvmField
    public val GOAT_SCREAMING_LONG_JUMP: SoundEvent = get("entity.goat.screaming.long_jump")
    @JvmField
    public val GOAT_SCREAMING_MILK: SoundEvent = get("entity.goat.screaming.milk")
    @JvmField
    public val GOAT_SCREAMING_PREPARE_RAM: SoundEvent = get("entity.goat.screaming.prepare_ram")
    @JvmField
    public val GOAT_SCREAMING_RAM_IMPACT: SoundEvent = get("entity.goat.screaming.ram_impact")
    @JvmField
    public val GOAT_SCREAMING_HORN_BREAK: SoundEvent = get("entity.goat.screaming.horn_break")
    @JvmField
    public val GOAT_STEP: SoundEvent = get("entity.goat.step")
    @JvmField
    public val GRASS_BREAK: SoundEvent = get("block.grass.break")
    @JvmField
    public val GRASS_FALL: SoundEvent = get("block.grass.fall")
    @JvmField
    public val GRASS_HIT: SoundEvent = get("block.grass.hit")
    @JvmField
    public val GRASS_PLACE: SoundEvent = get("block.grass.place")
    @JvmField
    public val GRASS_STEP: SoundEvent = get("block.grass.step")
    @JvmField
    public val GRAVEL_BREAK: SoundEvent = get("block.gravel.break")
    @JvmField
    public val GRAVEL_FALL: SoundEvent = get("block.gravel.fall")
    @JvmField
    public val GRAVEL_HIT: SoundEvent = get("block.gravel.hit")
    @JvmField
    public val GRAVEL_PLACE: SoundEvent = get("block.gravel.place")
    @JvmField
    public val GRAVEL_STEP: SoundEvent = get("block.gravel.step")
    @JvmField
    public val GRINDSTONE_USE: SoundEvent = get("block.grindstone.use")
    @JvmField
    public val GROWING_PLANT_CROP: SoundEvent = get("block.growing_plant.crop")
    @JvmField
    public val GUARDIAN_AMBIENT: SoundEvent = get("entity.guardian.ambient")
    @JvmField
    public val GUARDIAN_AMBIENT_LAND: SoundEvent = get("entity.guardian.ambient_land")
    @JvmField
    public val GUARDIAN_ATTACK: SoundEvent = get("entity.guardian.attack")
    @JvmField
    public val GUARDIAN_DEATH: SoundEvent = get("entity.guardian.death")
    @JvmField
    public val GUARDIAN_DEATH_LAND: SoundEvent = get("entity.guardian.death_land")
    @JvmField
    public val GUARDIAN_FLOP: SoundEvent = get("entity.guardian.flop")
    @JvmField
    public val GUARDIAN_HURT: SoundEvent = get("entity.guardian.hurt")
    @JvmField
    public val GUARDIAN_HURT_LAND: SoundEvent = get("entity.guardian.hurt_land")
    @JvmField
    public val HANGING_ROOTS_BREAK: SoundEvent = get("block.hanging_roots.break")
    @JvmField
    public val HANGING_ROOTS_FALL: SoundEvent = get("block.hanging_roots.fall")
    @JvmField
    public val HANGING_ROOTS_HIT: SoundEvent = get("block.hanging_roots.hit")
    @JvmField
    public val HANGING_ROOTS_PLACE: SoundEvent = get("block.hanging_roots.place")
    @JvmField
    public val HANGING_ROOTS_STEP: SoundEvent = get("block.hanging_roots.step")
    @JvmField
    public val HOE_TILL: SoundEvent = get("item.hoe.till")
    @JvmField
    public val HOGLIN_AMBIENT: SoundEvent = get("entity.hoglin.ambient")
    @JvmField
    public val HOGLIN_ANGRY: SoundEvent = get("entity.hoglin.angry")
    @JvmField
    public val HOGLIN_ATTACK: SoundEvent = get("entity.hoglin.attack")
    @JvmField
    public val HOGLIN_CONVERTED_TO_ZOMBIFIED: SoundEvent = get("entity.hoglin.converted_to_zombified")
    @JvmField
    public val HOGLIN_DEATH: SoundEvent = get("entity.hoglin.death")
    @JvmField
    public val HOGLIN_HURT: SoundEvent = get("entity.hoglin.hurt")
    @JvmField
    public val HOGLIN_RETREAT: SoundEvent = get("entity.hoglin.retreat")
    @JvmField
    public val HOGLIN_STEP: SoundEvent = get("entity.hoglin.step")
    @JvmField
    public val HONEY_BLOCK_BREAK: SoundEvent = get("block.honey_block.break")
    @JvmField
    public val HONEY_BLOCK_FALL: SoundEvent = get("block.honey_block.fall")
    @JvmField
    public val HONEY_BLOCK_HIT: SoundEvent = get("block.honey_block.hit")
    @JvmField
    public val HONEY_BLOCK_PLACE: SoundEvent = get("block.honey_block.place")
    @JvmField
    public val HONEY_BLOCK_SLIDE: SoundEvent = get("block.honey_block.slide")
    @JvmField
    public val HONEY_BLOCK_STEP: SoundEvent = get("block.honey_block.step")
    @JvmField
    public val HONEYCOMB_WAX_ON: SoundEvent = get("item.honeycomb.wax_on")
    @JvmField
    public val HONEY_DRINK: SoundEvent = get("item.honey_bottle.drink")
    @JvmField
    public val HORSE_AMBIENT: SoundEvent = get("entity.horse.ambient")
    @JvmField
    public val HORSE_ANGRY: SoundEvent = get("entity.horse.angry")
    @JvmField
    public val HORSE_ARMOR: SoundEvent = get("entity.horse.armor")
    @JvmField
    public val HORSE_BREATHE: SoundEvent = get("entity.horse.breathe")
    @JvmField
    public val HORSE_DEATH: SoundEvent = get("entity.horse.death")
    @JvmField
    public val HORSE_EAT: SoundEvent = get("entity.horse.eat")
    @JvmField
    public val HORSE_GALLOP: SoundEvent = get("entity.horse.gallop")
    @JvmField
    public val HORSE_HURT: SoundEvent = get("entity.horse.hurt")
    @JvmField
    public val HORSE_JUMP: SoundEvent = get("entity.horse.jump")
    @JvmField
    public val HORSE_LAND: SoundEvent = get("entity.horse.land")
    @JvmField
    public val HORSE_SADDLE: SoundEvent = get("entity.horse.saddle")
    @JvmField
    public val HORSE_STEP: SoundEvent = get("entity.horse.step")
    @JvmField
    public val HORSE_STEP_WOOD: SoundEvent = get("entity.horse.step_wood")
    @JvmField
    public val HOSTILE_BIG_FALL: SoundEvent = get("entity.hostile.big_fall")
    @JvmField
    public val HOSTILE_DEATH: SoundEvent = get("entity.hostile.death")
    @JvmField
    public val HOSTILE_HURT: SoundEvent = get("entity.hostile.hurt")
    @JvmField
    public val HOSTILE_SMALL_FALL: SoundEvent = get("entity.hostile.small_fall")
    @JvmField
    public val HOSTILE_SPLASH: SoundEvent = get("entity.hostile.splash")
    @JvmField
    public val HOSTILE_SWIM: SoundEvent = get("entity.hostile.swim")
    @JvmField
    public val HUSK_AMBIENT: SoundEvent = get("entity.husk.ambient")
    @JvmField
    public val HUSK_CONVERTED_TO_ZOMBIE: SoundEvent = get("entity.husk.converted_to_zombie")
    @JvmField
    public val HUSK_DEATH: SoundEvent = get("entity.husk.death")
    @JvmField
    public val HUSK_HURT: SoundEvent = get("entity.husk.hurt")
    @JvmField
    public val HUSK_STEP: SoundEvent = get("entity.husk.step")
    @JvmField
    public val ILLUSIONER_AMBIENT: SoundEvent = get("entity.illusioner.ambient")
    @JvmField
    public val ILLUSIONER_CAST_SPELL: SoundEvent = get("entity.illusioner.cast_spell")
    @JvmField
    public val ILLUSIONER_DEATH: SoundEvent = get("entity.illusioner.death")
    @JvmField
    public val ILLUSIONER_HURT: SoundEvent = get("entity.illusioner.hurt")
    @JvmField
    public val ILLUSIONER_MIRROR_MOVE: SoundEvent = get("entity.illusioner.mirror_move")
    @JvmField
    public val ILLUSIONER_PREPARE_BLINDNESS: SoundEvent = get("entity.illusioner.prepare_blindness")
    @JvmField
    public val ILLUSIONER_PREPARE_MIRROR: SoundEvent = get("entity.illusioner.prepare_mirror")
    @JvmField
    public val INK_SAC_USE: SoundEvent = get("item.ink_sac.use")
    @JvmField
    public val IRON_DOOR_CLOSE: SoundEvent = get("block.iron_door.close")
    @JvmField
    public val IRON_DOOR_OPEN: SoundEvent = get("block.iron_door.open")
    @JvmField
    public val IRON_GOLEM_ATTACK: SoundEvent = get("entity.iron_golem.attack")
    @JvmField
    public val IRON_GOLEM_DAMAGE: SoundEvent = get("entity.iron_golem.damage")
    @JvmField
    public val IRON_GOLEM_DEATH: SoundEvent = get("entity.iron_golem.death")
    @JvmField
    public val IRON_GOLEM_HURT: SoundEvent = get("entity.iron_golem.hurt")
    @JvmField
    public val IRON_GOLEM_REPAIR: SoundEvent = get("entity.iron_golem.repair")
    @JvmField
    public val IRON_GOLEM_STEP: SoundEvent = get("entity.iron_golem.step")
    @JvmField
    public val IRON_TRAPDOOR_CLOSE: SoundEvent = get("block.iron_trapdoor.close")
    @JvmField
    public val IRON_TRAPDOOR_OPEN: SoundEvent = get("block.iron_trapdoor.open")
    @JvmField
    public val ITEM_FRAME_ADD_ITEM: SoundEvent = get("entity.item_frame.add_item")
    @JvmField
    public val ITEM_FRAME_BREAK: SoundEvent = get("entity.item_frame.break")
    @JvmField
    public val ITEM_FRAME_PLACE: SoundEvent = get("entity.item_frame.place")
    @JvmField
    public val ITEM_FRAME_REMOVE_ITEM: SoundEvent = get("entity.item_frame.remove_item")
    @JvmField
    public val ITEM_FRAME_ROTATE_ITEM: SoundEvent = get("entity.item_frame.rotate_item")
    @JvmField
    public val ITEM_BREAK: SoundEvent = get("entity.item.break")
    @JvmField
    public val ITEM_PICKUP: SoundEvent = get("entity.item.pickup")
    @JvmField
    public val LADDER_BREAK: SoundEvent = get("block.ladder.break")
    @JvmField
    public val LADDER_FALL: SoundEvent = get("block.ladder.fall")
    @JvmField
    public val LADDER_HIT: SoundEvent = get("block.ladder.hit")
    @JvmField
    public val LADDER_PLACE: SoundEvent = get("block.ladder.place")
    @JvmField
    public val LADDER_STEP: SoundEvent = get("block.ladder.step")
    @JvmField
    public val LANTERN_BREAK: SoundEvent = get("block.lantern.break")
    @JvmField
    public val LANTERN_FALL: SoundEvent = get("block.lantern.fall")
    @JvmField
    public val LANTERN_HIT: SoundEvent = get("block.lantern.hit")
    @JvmField
    public val LANTERN_PLACE: SoundEvent = get("block.lantern.place")
    @JvmField
    public val LANTERN_STEP: SoundEvent = get("block.lantern.step")
    @JvmField
    public val LARGE_AMETHYST_BUD_BREAK: SoundEvent = get("block.large_amethyst_bud.break")
    @JvmField
    public val LARGE_AMETHYST_BUD_PLACE: SoundEvent = get("block.large_amethyst_bud.place")
    @JvmField
    public val LAVA_AMBIENT: SoundEvent = get("block.lava.ambient")
    @JvmField
    public val LAVA_EXTINGUISH: SoundEvent = get("block.lava.extinguish")
    @JvmField
    public val LAVA_POP: SoundEvent = get("block.lava.pop")
    @JvmField
    public val LEASH_KNOT_BREAK: SoundEvent = get("entity.leash_knot.break")
    @JvmField
    public val LEASH_KNOT_PLACE: SoundEvent = get("entity.leash_knot.place")
    @JvmField
    public val LEVER_CLICK: SoundEvent = get("block.lever.click")
    @JvmField
    public val LIGHTNING_BOLT_IMPACT: SoundEvent = get("entity.lightning_bolt.impact")
    @JvmField
    public val LIGHTNING_BOLT_THUNDER: SoundEvent = get("entity.lightning_bolt.thunder")
    @JvmField
    public val LINGERING_POTION_THROW: SoundEvent = get("entity.lingering_potion.throw")
    @JvmField
    public val LLAMA_AMBIENT: SoundEvent = get("entity.llama.ambient")
    @JvmField
    public val LLAMA_ANGRY: SoundEvent = get("entity.llama.angry")
    @JvmField
    public val LLAMA_CHEST: SoundEvent = get("entity.llama.chest")
    @JvmField
    public val LLAMA_DEATH: SoundEvent = get("entity.llama.death")
    @JvmField
    public val LLAMA_EAT: SoundEvent = get("entity.llama.eat")
    @JvmField
    public val LLAMA_HURT: SoundEvent = get("entity.llama.hurt")
    @JvmField
    public val LLAMA_SPIT: SoundEvent = get("entity.llama.spit")
    @JvmField
    public val LLAMA_STEP: SoundEvent = get("entity.llama.step")
    @JvmField
    public val LLAMA_SWAG: SoundEvent = get("entity.llama.swag")
    @JvmField
    public val MAGMA_CUBE_DEATH_SMALL: SoundEvent = get("entity.magma_cube.death_small")
    @JvmField
    public val LODESTONE_BREAK: SoundEvent = get("block.lodestone.break")
    @JvmField
    public val LODESTONE_STEP: SoundEvent = get("block.lodestone.step")
    @JvmField
    public val LODESTONE_PLACE: SoundEvent = get("block.lodestone.place")
    @JvmField
    public val LODESTONE_HIT: SoundEvent = get("block.lodestone.hit")
    @JvmField
    public val LODESTONE_FALL: SoundEvent = get("block.lodestone.fall")
    @JvmField
    public val LODESTONE_COMPASS_LOCK: SoundEvent = get("item.lodestone_compass.lock")
    @JvmField
    public val MAGMA_CUBE_DEATH: SoundEvent = get("entity.magma_cube.death")
    @JvmField
    public val MAGMA_CUBE_HURT: SoundEvent = get("entity.magma_cube.hurt")
    @JvmField
    public val MAGMA_CUBE_HURT_SMALL: SoundEvent = get("entity.magma_cube.hurt_small")
    @JvmField
    public val MAGMA_CUBE_JUMP: SoundEvent = get("entity.magma_cube.jump")
    @JvmField
    public val MAGMA_CUBE_SQUISH: SoundEvent = get("entity.magma_cube.squish")
    @JvmField
    public val MAGMA_CUBE_SQUISH_SMALL: SoundEvent = get("entity.magma_cube.squish_small")
    @JvmField
    public val MANGROVE_ROOTS_BREAK: SoundEvent = get("block.mangrove_roots.break")
    @JvmField
    public val MANGROVE_ROOTS_FALL: SoundEvent = get("block.mangrove_roots.fall")
    @JvmField
    public val MANGROVE_ROOTS_HIT: SoundEvent = get("block.mangrove_roots.hit")
    @JvmField
    public val MANGROVE_ROOTS_PLACE: SoundEvent = get("block.mangrove_roots.place")
    @JvmField
    public val MANGROVE_ROOTS_STEP: SoundEvent = get("block.mangrove_roots.step")
    @JvmField
    public val MEDIUM_AMETHYST_BUD_BREAK: SoundEvent = get("block.medium_amethyst_bud.break")
    @JvmField
    public val MEDIUM_AMETHYST_BUD_PLACE: SoundEvent = get("block.medium_amethyst_bud.place")
    @JvmField
    public val METAL_BREAK: SoundEvent = get("block.metal.break")
    @JvmField
    public val METAL_FALL: SoundEvent = get("block.metal.fall")
    @JvmField
    public val METAL_HIT: SoundEvent = get("block.metal.hit")
    @JvmField
    public val METAL_PLACE: SoundEvent = get("block.metal.place")
    @JvmField
    public val METAL_PRESSURE_PLATE_CLICK_OFF: SoundEvent = get("block.metal_pressure_plate.click_off")
    @JvmField
    public val METAL_PRESSURE_PLATE_CLICK_ON: SoundEvent = get("block.metal_pressure_plate.click_on")
    @JvmField
    public val METAL_STEP: SoundEvent = get("block.metal.step")
    @JvmField
    public val MINECART_INSIDE_UNDERWATER: SoundEvent = get("entity.minecart.inside.underwater")
    @JvmField
    public val MINECART_INSIDE: SoundEvent = get("entity.minecart.inside")
    @JvmField
    public val MINECART_RIDING: SoundEvent = get("entity.minecart.riding")
    @JvmField
    public val MOOSHROOM_CONVERT: SoundEvent = get("entity.mooshroom.convert")
    @JvmField
    public val MOOSHROOM_EAT: SoundEvent = get("entity.mooshroom.eat")
    @JvmField
    public val MOOSHROOM_MILK: SoundEvent = get("entity.mooshroom.milk")
    @JvmField
    public val MOOSHROOM_MILK_SUSPICIOUSLY: SoundEvent = get("entity.mooshroom.suspicious_milk")
    @JvmField
    public val MOOSHROOM_SHEAR: SoundEvent = get("entity.mooshroom.shear")
    @JvmField
    public val MOSS_CARPET_BREAK: SoundEvent = get("block.moss_carpet.break")
    @JvmField
    public val MOSS_CARPET_FALL: SoundEvent = get("block.moss_carpet.fall")
    @JvmField
    public val MOSS_CARPET_HIT: SoundEvent = get("block.moss_carpet.hit")
    @JvmField
    public val MOSS_CARPET_PLACE: SoundEvent = get("block.moss_carpet.place")
    @JvmField
    public val MOSS_CARPET_STEP: SoundEvent = get("block.moss_carpet.step")
    @JvmField
    public val MOSS_BREAK: SoundEvent = get("block.moss.break")
    @JvmField
    public val MOSS_FALL: SoundEvent = get("block.moss.fall")
    @JvmField
    public val MOSS_HIT: SoundEvent = get("block.moss.hit")
    @JvmField
    public val MOSS_PLACE: SoundEvent = get("block.moss.place")
    @JvmField
    public val MOSS_STEP: SoundEvent = get("block.moss.step")
    @JvmField
    public val MUD_BREAK: SoundEvent = get("block.mud.break")
    @JvmField
    public val MUD_FALL: SoundEvent = get("block.mud.fall")
    @JvmField
    public val MUD_HIT: SoundEvent = get("block.mud.hit")
    @JvmField
    public val MUD_PLACE: SoundEvent = get("block.mud.place")
    @JvmField
    public val MUD_STEP: SoundEvent = get("block.mud.step")
    @JvmField
    public val MUD_BRICKS_BREAK: SoundEvent = get("block.mud_bricks.break")
    @JvmField
    public val MUD_BRICKS_FALL: SoundEvent = get("block.mud_bricks.fall")
    @JvmField
    public val MUD_BRICKS_HIT: SoundEvent = get("block.mud_bricks.hit")
    @JvmField
    public val MUD_BRICKS_PLACE: SoundEvent = get("block.mud_bricks.place")
    @JvmField
    public val MUD_BRICKS_STEP: SoundEvent = get("block.mud_bricks.step")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_BREAK: SoundEvent = get("block.muddy_mangrove_roots.break")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_FALL: SoundEvent = get("block.muddy_mangrove_roots.fall")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_HIT: SoundEvent = get("block.muddy_mangrove_roots.hit")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_PLACE: SoundEvent = get("block.muddy_mangrove_roots.place")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS_STEP: SoundEvent = get("block.muddy_mangrove_roots.step")
    @JvmField
    public val MULE_AMBIENT: SoundEvent = get("entity.mule.ambient")
    @JvmField
    public val MULE_ANGRY: SoundEvent = get("entity.mule.angry")
    @JvmField
    public val MULE_CHEST: SoundEvent = get("entity.mule.chest")
    @JvmField
    public val MULE_DEATH: SoundEvent = get("entity.mule.death")
    @JvmField
    public val MULE_EAT: SoundEvent = get("entity.mule.eat")
    @JvmField
    public val MULE_HURT: SoundEvent = get("entity.mule.hurt")
    @JvmField
    public val MUSIC_CREATIVE: SoundEvent = get("music.creative")
    @JvmField
    public val MUSIC_CREDITS: SoundEvent = get("music.credits")
    @JvmField
    public val MUSIC_DISC_5: SoundEvent = get("music_disc.5")
    @JvmField
    public val MUSIC_DISC_11: SoundEvent = get("music_disc.11")
    @JvmField
    public val MUSIC_DISC_13: SoundEvent = get("music_disc.13")
    @JvmField
    public val MUSIC_DISC_BLOCKS: SoundEvent = get("music_disc.blocks")
    @JvmField
    public val MUSIC_DISC_CAT: SoundEvent = get("music_disc.cat")
    @JvmField
    public val MUSIC_DISC_CHIRP: SoundEvent = get("music_disc.chirp")
    @JvmField
    public val MUSIC_DISC_FAR: SoundEvent = get("music_disc.far")
    @JvmField
    public val MUSIC_DISC_MALL: SoundEvent = get("music_disc.mall")
    @JvmField
    public val MUSIC_DISC_MELLOHI: SoundEvent = get("music_disc.mellohi")
    @JvmField
    public val MUSIC_DISC_PIGSTEP: SoundEvent = get("music_disc.pigstep")
    @JvmField
    public val MUSIC_DISC_STAL: SoundEvent = get("music_disc.stal")
    @JvmField
    public val MUSIC_DISC_STRAD: SoundEvent = get("music_disc.strad")
    @JvmField
    public val MUSIC_DISC_WAIT: SoundEvent = get("music_disc.wait")
    @JvmField
    public val MUSIC_DISC_WARD: SoundEvent = get("music_disc.ward")
    @JvmField
    public val MUSIC_DISC_OTHERSIDE: SoundEvent = get("music_disc.otherside")
    @JvmField
    public val MUSIC_DRAGON: SoundEvent = get("music.dragon")
    @JvmField
    public val MUSIC_END: SoundEvent = get("music.end")
    @JvmField
    public val MUSIC_GAME: SoundEvent = get("music.game")
    @JvmField
    public val MUSIC_MENU: SoundEvent = get("music.menu")
    @JvmField
    public val MUSIC_BIOME_BASALT_DELTAS: SoundEvent = get("music.nether.basalt_deltas")
    @JvmField
    public val MUSIC_BIOME_CRIMSON_FOREST: SoundEvent = get("music.nether.crimson_forest")
    @JvmField
    public val MUSIC_BIOME_DEEP_DARK: SoundEvent = get("music.overworld.deep_dark")
    @JvmField
    public val MUSIC_BIOME_DRIPSTONE_CAVES: SoundEvent = get("music.overworld.dripstone_caves")
    @JvmField
    public val MUSIC_BIOME_GROVE: SoundEvent = get("music.overworld.grove")
    @JvmField
    public val MUSIC_BIOME_JAGGED_PEAKS: SoundEvent = get("music.overworld.jagged_peaks")
    @JvmField
    public val MUSIC_BIOME_LUSH_CAVES: SoundEvent = get("music.overworld.lush_caves")
    @JvmField
    public val MUSIC_BIOME_SWAMP: SoundEvent = get("music.overworld.swamp")
    @JvmField
    public val MUSIC_BIOME_JUNGLE_AND_FOREST: SoundEvent = get("music.overworld.jungle_and_forest")
    @JvmField
    public val MUSIC_BIOME_OLD_GROWTH_TAIGA: SoundEvent = get("music.overworld.old_growth_taiga")
    @JvmField
    public val MUSIC_BIOME_MEADOW: SoundEvent = get("music.overworld.meadow")
    @JvmField
    public val MUSIC_BIOME_NETHER_WASTES: SoundEvent = get("music.nether.nether_wastes")
    @JvmField
    public val MUSIC_BIOME_FROZEN_PEAKS: SoundEvent = get("music.overworld.frozen_peaks")
    @JvmField
    public val MUSIC_BIOME_SNOWY_SLOPES: SoundEvent = get("music.overworld.snowy_slopes")
    @JvmField
    public val MUSIC_BIOME_SOUL_SAND_VALLEY: SoundEvent = get("music.nether.soul_sand_valley")
    @JvmField
    public val MUSIC_BIOME_STONY_PEAKS: SoundEvent = get("music.overworld.stony_peaks")
    @JvmField
    public val MUSIC_BIOME_WARPED_FOREST: SoundEvent = get("music.nether.warped_forest")
    @JvmField
    public val MUSIC_UNDER_WATER: SoundEvent = get("music.under_water")
    @JvmField
    public val NETHER_BRICKS_BREAK: SoundEvent = get("block.nether_bricks.break")
    @JvmField
    public val NETHER_BRICKS_STEP: SoundEvent = get("block.nether_bricks.step")
    @JvmField
    public val NETHER_BRICKS_PLACE: SoundEvent = get("block.nether_bricks.place")
    @JvmField
    public val NETHER_BRICKS_HIT: SoundEvent = get("block.nether_bricks.hit")
    @JvmField
    public val NETHER_BRICKS_FALL: SoundEvent = get("block.nether_bricks.fall")
    @JvmField
    public val NETHER_WART_BREAK: SoundEvent = get("block.nether_wart.break")
    @JvmField
    public val NETHER_WART_PLANTED: SoundEvent = get("item.nether_wart.plant")
    @JvmField
    public val PACKED_MUD_BREAK: SoundEvent = get("block.packed_mud.break")
    @JvmField
    public val PACKED_MUD_FALL: SoundEvent = get("block.packed_mud.fall")
    @JvmField
    public val PACKED_MUD_HIT: SoundEvent = get("block.packed_mud.hit")
    @JvmField
    public val PACKED_MUD_PLACE: SoundEvent = get("block.packed_mud.place")
    @JvmField
    public val PACKED_MUD_STEP: SoundEvent = get("block.packed_mud.step")
    @JvmField
    public val STEM_BREAK: SoundEvent = get("block.stem.break")
    @JvmField
    public val STEM_STEP: SoundEvent = get("block.stem.step")
    @JvmField
    public val STEM_PLACE: SoundEvent = get("block.stem.place")
    @JvmField
    public val STEM_HIT: SoundEvent = get("block.stem.hit")
    @JvmField
    public val STEM_FALL: SoundEvent = get("block.stem.fall")
    @JvmField
    public val NYLIUM_BREAK: SoundEvent = get("block.nylium.break")
    @JvmField
    public val NYLIUM_STEP: SoundEvent = get("block.nylium.step")
    @JvmField
    public val NYLIUM_PLACE: SoundEvent = get("block.nylium.place")
    @JvmField
    public val NYLIUM_HIT: SoundEvent = get("block.nylium.hit")
    @JvmField
    public val NYLIUM_FALL: SoundEvent = get("block.nylium.fall")
    @JvmField
    public val NETHER_SPROUTS_BREAK: SoundEvent = get("block.nether_sprouts.break")
    @JvmField
    public val NETHER_SPROUTS_STEP: SoundEvent = get("block.nether_sprouts.step")
    @JvmField
    public val NETHER_SPROUTS_PLACE: SoundEvent = get("block.nether_sprouts.place")
    @JvmField
    public val NETHER_SPROUTS_HIT: SoundEvent = get("block.nether_sprouts.hit")
    @JvmField
    public val NETHER_SPROUTS_FALL: SoundEvent = get("block.nether_sprouts.fall")
    @JvmField
    public val FUNGUS_BREAK: SoundEvent = get("block.fungus.break")
    @JvmField
    public val FUNGUS_STEP: SoundEvent = get("block.fungus.step")
    @JvmField
    public val FUNGUS_PLACE: SoundEvent = get("block.fungus.place")
    @JvmField
    public val FUNGUS_HIT: SoundEvent = get("block.fungus.hit")
    @JvmField
    public val FUNGUS_FALL: SoundEvent = get("block.fungus.fall")
    @JvmField
    public val WEEPING_VINES_BREAK: SoundEvent = get("block.weeping_vines.break")
    @JvmField
    public val WEEPING_VINES_STEP: SoundEvent = get("block.weeping_vines.step")
    @JvmField
    public val WEEPING_VINES_PLACE: SoundEvent = get("block.weeping_vines.place")
    @JvmField
    public val WEEPING_VINES_HIT: SoundEvent = get("block.weeping_vines.hit")
    @JvmField
    public val WEEPING_VINES_FALL: SoundEvent = get("block.weeping_vines.fall")
    @JvmField
    public val WART_BLOCK_BREAK: SoundEvent = get("block.wart_block.break")
    @JvmField
    public val WART_BLOCK_STEP: SoundEvent = get("block.wart_block.step")
    @JvmField
    public val WART_BLOCK_PLACE: SoundEvent = get("block.wart_block.place")
    @JvmField
    public val WART_BLOCK_HIT: SoundEvent = get("block.wart_block.hit")
    @JvmField
    public val WART_BLOCK_FALL: SoundEvent = get("block.wart_block.fall")
    @JvmField
    public val NETHERITE_BLOCK_BREAK: SoundEvent = get("block.netherite_block.break")
    @JvmField
    public val NETHERITE_BLOCK_STEP: SoundEvent = get("block.netherite_block.step")
    @JvmField
    public val NETHERITE_BLOCK_PLACE: SoundEvent = get("block.netherite_block.place")
    @JvmField
    public val NETHERITE_BLOCK_HIT: SoundEvent = get("block.netherite_block.hit")
    @JvmField
    public val NETHERITE_BLOCK_FALL: SoundEvent = get("block.netherite_block.fall")
    @JvmField
    public val NETHERRACK_BREAK: SoundEvent = get("block.netherrack.break")
    @JvmField
    public val NETHERRACK_STEP: SoundEvent = get("block.netherrack.step")
    @JvmField
    public val NETHERRACK_PLACE: SoundEvent = get("block.netherrack.place")
    @JvmField
    public val NETHERRACK_HIT: SoundEvent = get("block.netherrack.hit")
    @JvmField
    public val NETHERRACK_FALL: SoundEvent = get("block.netherrack.fall")
    @JvmField
    public val NOTE_BLOCK_BASEDRUM: SoundEvent = get("block.note_block.basedrum")
    @JvmField
    public val NOTE_BLOCK_BASS: SoundEvent = get("block.note_block.bass")
    @JvmField
    public val NOTE_BLOCK_BELL: SoundEvent = get("block.note_block.bell")
    @JvmField
    public val NOTE_BLOCK_CHIME: SoundEvent = get("block.note_block.chime")
    @JvmField
    public val NOTE_BLOCK_FLUTE: SoundEvent = get("block.note_block.flute")
    @JvmField
    public val NOTE_BLOCK_GUITAR: SoundEvent = get("block.note_block.guitar")
    @JvmField
    public val NOTE_BLOCK_HARP: SoundEvent = get("block.note_block.harp")
    @JvmField
    public val NOTE_BLOCK_HAT: SoundEvent = get("block.note_block.hat")
    @JvmField
    public val NOTE_BLOCK_PLING: SoundEvent = get("block.note_block.pling")
    @JvmField
    public val NOTE_BLOCK_SNARE: SoundEvent = get("block.note_block.snare")
    @JvmField
    public val NOTE_BLOCK_XYLOPHONE: SoundEvent = get("block.note_block.xylophone")
    @JvmField
    public val NOTE_BLOCK_IRON_XYLOPHONE: SoundEvent = get("block.note_block.iron_xylophone")
    @JvmField
    public val NOTE_BLOCK_COW_BELL: SoundEvent = get("block.note_block.cow_bell")
    @JvmField
    public val NOTE_BLOCK_DIDGERIDOO: SoundEvent = get("block.note_block.didgeridoo")
    @JvmField
    public val NOTE_BLOCK_BIT: SoundEvent = get("block.note_block.bit")
    @JvmField
    public val NOTE_BLOCK_BANJO: SoundEvent = get("block.note_block.banjo")
    @JvmField
    public val OCELOT_HURT: SoundEvent = get("entity.ocelot.hurt")
    @JvmField
    public val OCELOT_AMBIENT: SoundEvent = get("entity.ocelot.ambient")
    @JvmField
    public val OCELOT_DEATH: SoundEvent = get("entity.ocelot.death")
    @JvmField
    public val PAINTING_BREAK: SoundEvent = get("entity.painting.break")
    @JvmField
    public val PAINTING_PLACE: SoundEvent = get("entity.painting.place")
    @JvmField
    public val PANDA_PRE_SNEEZE: SoundEvent = get("entity.panda.pre_sneeze")
    @JvmField
    public val PANDA_SNEEZE: SoundEvent = get("entity.panda.sneeze")
    @JvmField
    public val PANDA_AMBIENT: SoundEvent = get("entity.panda.ambient")
    @JvmField
    public val PANDA_DEATH: SoundEvent = get("entity.panda.death")
    @JvmField
    public val PANDA_EAT: SoundEvent = get("entity.panda.eat")
    @JvmField
    public val PANDA_STEP: SoundEvent = get("entity.panda.step")
    @JvmField
    public val PANDA_CANT_BREED: SoundEvent = get("entity.panda.cant_breed")
    @JvmField
    public val PANDA_AGGRESSIVE_AMBIENT: SoundEvent = get("entity.panda.aggressive_ambient")
    @JvmField
    public val PANDA_WORRIED_AMBIENT: SoundEvent = get("entity.panda.worried_ambient")
    @JvmField
    public val PANDA_HURT: SoundEvent = get("entity.panda.hurt")
    @JvmField
    public val PANDA_BITE: SoundEvent = get("entity.panda.bite")
    @JvmField
    public val PARROT_AMBIENT: SoundEvent = get("entity.parrot.ambient")
    @JvmField
    public val PARROT_DEATH: SoundEvent = get("entity.parrot.death")
    @JvmField
    public val PARROT_EAT: SoundEvent = get("entity.parrot.eat")
    @JvmField
    public val PARROT_FLY: SoundEvent = get("entity.parrot.fly")
    @JvmField
    public val PARROT_HURT: SoundEvent = get("entity.parrot.hurt")
    @JvmField
    public val PARROT_IMITATE_BLAZE: SoundEvent = get("entity.parrot.imitate.blaze")
    @JvmField
    public val PARROT_IMITATE_CREEPER: SoundEvent = get("entity.parrot.imitate.creeper")
    @JvmField
    public val PARROT_IMITATE_DROWNED: SoundEvent = get("entity.parrot.imitate.drowned")
    @JvmField
    public val PARROT_IMITATE_ELDER_GUARDIAN: SoundEvent = get("entity.parrot.imitate.elder_guardian")
    @JvmField
    public val PARROT_IMITATE_ENDER_DRAGON: SoundEvent = get("entity.parrot.imitate.ender_dragon")
    @JvmField
    public val PARROT_IMITATE_ENDERMITE: SoundEvent = get("entity.parrot.imitate.endermite")
    @JvmField
    public val PARROT_IMITATE_EVOKER: SoundEvent = get("entity.parrot.imitate.evoker")
    @JvmField
    public val PARROT_IMITATE_GHAST: SoundEvent = get("entity.parrot.imitate.ghast")
    @JvmField
    public val PARROT_IMITATE_GUARDIAN: SoundEvent = get("entity.parrot.imitate.guardian")
    @JvmField
    public val PARROT_IMITATE_HOGLIN: SoundEvent = get("entity.parrot.imitate.hoglin")
    @JvmField
    public val PARROT_IMITATE_HUSK: SoundEvent = get("entity.parrot.imitate.husk")
    @JvmField
    public val PARROT_IMITATE_ILLUSIONER: SoundEvent = get("entity.parrot.imitate.illusioner")
    @JvmField
    public val PARROT_IMITATE_MAGMA_CUBE: SoundEvent = get("entity.parrot.imitate.magma_cube")
    @JvmField
    public val PARROT_IMITATE_PHANTOM: SoundEvent = get("entity.parrot.imitate.phantom")
    @JvmField
    public val PARROT_IMITATE_PIGLIN: SoundEvent = get("entity.parrot.imitate.piglin")
    @JvmField
    public val PARROT_IMITATE_PIGLIN_BRUTE: SoundEvent = get("entity.parrot.imitate.piglin_brute")
    @JvmField
    public val PARROT_IMITATE_PILLAGER: SoundEvent = get("entity.parrot.imitate.pillager")
    @JvmField
    public val PARROT_IMITATE_RAVAGER: SoundEvent = get("entity.parrot.imitate.ravager")
    @JvmField
    public val PARROT_IMITATE_SHULKER: SoundEvent = get("entity.parrot.imitate.shulker")
    @JvmField
    public val PARROT_IMITATE_SILVERFISH: SoundEvent = get("entity.parrot.imitate.silverfish")
    @JvmField
    public val PARROT_IMITATE_SKELETON: SoundEvent = get("entity.parrot.imitate.skeleton")
    @JvmField
    public val PARROT_IMITATE_SLIME: SoundEvent = get("entity.parrot.imitate.slime")
    @JvmField
    public val PARROT_IMITATE_SPIDER: SoundEvent = get("entity.parrot.imitate.spider")
    @JvmField
    public val PARROT_IMITATE_STRAY: SoundEvent = get("entity.parrot.imitate.stray")
    @JvmField
    public val PARROT_IMITATE_VEX: SoundEvent = get("entity.parrot.imitate.vex")
    @JvmField
    public val PARROT_IMITATE_VINDICATOR: SoundEvent = get("entity.parrot.imitate.vindicator")
    @JvmField
    public val PARROT_IMITATE_WARDEN: SoundEvent = get("entity.parrot.imitate.warden")
    @JvmField
    public val PARROT_IMITATE_WITCH: SoundEvent = get("entity.parrot.imitate.witch")
    @JvmField
    public val PARROT_IMITATE_WITHER: SoundEvent = get("entity.parrot.imitate.wither")
    @JvmField
    public val PARROT_IMITATE_WITHER_SKELETON: SoundEvent = get("entity.parrot.imitate.wither_skeleton")
    @JvmField
    public val PARROT_IMITATE_ZOGLIN: SoundEvent = get("entity.parrot.imitate.zoglin")
    @JvmField
    public val PARROT_IMITATE_ZOMBIE: SoundEvent = get("entity.parrot.imitate.zombie")
    @JvmField
    public val PARROT_IMITATE_ZOMBIE_VILLAGER: SoundEvent = get("entity.parrot.imitate.zombie_villager")
    @JvmField
    public val PARROT_STEP: SoundEvent = get("entity.parrot.step")
    @JvmField
    public val PHANTOM_AMBIENT: SoundEvent = get("entity.phantom.ambient")
    @JvmField
    public val PHANTOM_BITE: SoundEvent = get("entity.phantom.bite")
    @JvmField
    public val PHANTOM_DEATH: SoundEvent = get("entity.phantom.death")
    @JvmField
    public val PHANTOM_FLAP: SoundEvent = get("entity.phantom.flap")
    @JvmField
    public val PHANTOM_HURT: SoundEvent = get("entity.phantom.hurt")
    @JvmField
    public val PHANTOM_SWOOP: SoundEvent = get("entity.phantom.swoop")
    @JvmField
    public val PIG_AMBIENT: SoundEvent = get("entity.pig.ambient")
    @JvmField
    public val PIG_DEATH: SoundEvent = get("entity.pig.death")
    @JvmField
    public val PIG_HURT: SoundEvent = get("entity.pig.hurt")
    @JvmField
    public val PIG_SADDLE: SoundEvent = get("entity.pig.saddle")
    @JvmField
    public val PIG_STEP: SoundEvent = get("entity.pig.step")
    @JvmField
    public val PIGLIN_ADMIRING_ITEM: SoundEvent = get("entity.piglin.admiring_item")
    @JvmField
    public val PIGLIN_AMBIENT: SoundEvent = get("entity.piglin.ambient")
    @JvmField
    public val PIGLIN_ANGRY: SoundEvent = get("entity.piglin.angry")
    @JvmField
    public val PIGLIN_CELEBRATE: SoundEvent = get("entity.piglin.celebrate")
    @JvmField
    public val PIGLIN_DEATH: SoundEvent = get("entity.piglin.death")
    @JvmField
    public val PIGLIN_JEALOUS: SoundEvent = get("entity.piglin.jealous")
    @JvmField
    public val PIGLIN_HURT: SoundEvent = get("entity.piglin.hurt")
    @JvmField
    public val PIGLIN_RETREAT: SoundEvent = get("entity.piglin.retreat")
    @JvmField
    public val PIGLIN_STEP: SoundEvent = get("entity.piglin.step")
    @JvmField
    public val PIGLIN_CONVERTED_TO_ZOMBIFIED: SoundEvent = get("entity.piglin.converted_to_zombified")
    @JvmField
    public val PIGLIN_BRUTE_AMBIENT: SoundEvent = get("entity.piglin_brute.ambient")
    @JvmField
    public val PIGLIN_BRUTE_ANGRY: SoundEvent = get("entity.piglin_brute.angry")
    @JvmField
    public val PIGLIN_BRUTE_DEATH: SoundEvent = get("entity.piglin_brute.death")
    @JvmField
    public val PIGLIN_BRUTE_HURT: SoundEvent = get("entity.piglin_brute.hurt")
    @JvmField
    public val PIGLIN_BRUTE_STEP: SoundEvent = get("entity.piglin_brute.step")
    @JvmField
    public val PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED: SoundEvent = get("entity.piglin_brute.converted_to_zombified")
    @JvmField
    public val PILLAGER_AMBIENT: SoundEvent = get("entity.pillager.ambient")
    @JvmField
    public val PILLAGER_CELEBRATE: SoundEvent = get("entity.pillager.celebrate")
    @JvmField
    public val PILLAGER_DEATH: SoundEvent = get("entity.pillager.death")
    @JvmField
    public val PILLAGER_HURT: SoundEvent = get("entity.pillager.hurt")
    @JvmField
    public val PISTON_CONTRACT: SoundEvent = get("block.piston.contract")
    @JvmField
    public val PISTON_EXTEND: SoundEvent = get("block.piston.extend")
    @JvmField
    public val PLAYER_ATTACK_CRIT: SoundEvent = get("entity.player.attack.crit")
    @JvmField
    public val PLAYER_ATTACK_KNOCKBACK: SoundEvent = get("entity.player.attack.knockback")
    @JvmField
    public val PLAYER_ATTACK_NODAMAGE: SoundEvent = get("entity.player.attack.nodamage")
    @JvmField
    public val PLAYER_ATTACK_STRONG: SoundEvent = get("entity.player.attack.strong")
    @JvmField
    public val PLAYER_ATTACK_SWEEP: SoundEvent = get("entity.player.attack.sweep")
    @JvmField
    public val PLAYER_ATTACK_WEAK: SoundEvent = get("entity.player.attack.weak")
    @JvmField
    public val PLAYER_BIG_FALL: SoundEvent = get("entity.player.big_fall")
    @JvmField
    public val PLAYER_BREATH: SoundEvent = get("entity.player.breath")
    @JvmField
    public val PLAYER_BURP: SoundEvent = get("entity.player.burp")
    @JvmField
    public val PLAYER_DEATH: SoundEvent = get("entity.player.death")
    @JvmField
    public val PLAYER_HURT: SoundEvent = get("entity.player.hurt")
    @JvmField
    public val PLAYER_HURT_DROWN: SoundEvent = get("entity.player.hurt_drown")
    @JvmField
    public val PLAYER_HURT_FREEZE: SoundEvent = get("entity.player.hurt_freeze")
    @JvmField
    public val PLAYER_HURT_ON_FIRE: SoundEvent = get("entity.player.hurt_on_fire")
    @JvmField
    public val PLAYER_HURT_SWEET_BERRY_BUSH: SoundEvent = get("entity.player.hurt_sweet_berry_bush")
    @JvmField
    public val PLAYER_LEVELUP: SoundEvent = get("entity.player.levelup")
    @JvmField
    public val PLAYER_SMALL_FALL: SoundEvent = get("entity.player.small_fall")
    @JvmField
    public val PLAYER_SPLASH: SoundEvent = get("entity.player.splash")
    @JvmField
    public val PLAYER_SPLASH_HIGH_SPEED: SoundEvent = get("entity.player.splash.high_speed")
    @JvmField
    public val PLAYER_SWIM: SoundEvent = get("entity.player.swim")
    @JvmField
    public val POLAR_BEAR_AMBIENT: SoundEvent = get("entity.polar_bear.ambient")
    @JvmField
    public val POLAR_BEAR_AMBIENT_BABY: SoundEvent = get("entity.polar_bear.ambient_baby")
    @JvmField
    public val POLAR_BEAR_DEATH: SoundEvent = get("entity.polar_bear.death")
    @JvmField
    public val POLAR_BEAR_HURT: SoundEvent = get("entity.polar_bear.hurt")
    @JvmField
    public val POLAR_BEAR_STEP: SoundEvent = get("entity.polar_bear.step")
    @JvmField
    public val POLAR_BEAR_WARNING: SoundEvent = get("entity.polar_bear.warning")
    @JvmField
    public val POLISHED_DEEPSLATE_BREAK: SoundEvent = get("block.polished_deepslate.break")
    @JvmField
    public val POLISHED_DEEPSLATE_FALL: SoundEvent = get("block.polished_deepslate.fall")
    @JvmField
    public val POLISHED_DEEPSLATE_HIT: SoundEvent = get("block.polished_deepslate.hit")
    @JvmField
    public val POLISHED_DEEPSLATE_PLACE: SoundEvent = get("block.polished_deepslate.place")
    @JvmField
    public val POLISHED_DEEPSLATE_STEP: SoundEvent = get("block.polished_deepslate.step")
    @JvmField
    public val PORTAL_AMBIENT: SoundEvent = get("block.portal.ambient")
    @JvmField
    public val PORTAL_TRAVEL: SoundEvent = get("block.portal.travel")
    @JvmField
    public val PORTAL_TRIGGER: SoundEvent = get("block.portal.trigger")
    @JvmField
    public val POWDER_SNOW_BREAK: SoundEvent = get("block.powder_snow.break")
    @JvmField
    public val POWDER_SNOW_FALL: SoundEvent = get("block.powder_snow.fall")
    @JvmField
    public val POWDER_SNOW_HIT: SoundEvent = get("block.powder_snow.hit")
    @JvmField
    public val POWDER_SNOW_PLACE: SoundEvent = get("block.powder_snow.place")
    @JvmField
    public val POWDER_SNOW_STEP: SoundEvent = get("block.powder_snow.step")
    @JvmField
    public val PUFFER_FISH_AMBIENT: SoundEvent = get("entity.puffer_fish.ambient")
    @JvmField
    public val PUFFER_FISH_BLOW_OUT: SoundEvent = get("entity.puffer_fish.blow_out")
    @JvmField
    public val PUFFER_FISH_BLOW_UP: SoundEvent = get("entity.puffer_fish.blow_up")
    @JvmField
    public val PUFFER_FISH_DEATH: SoundEvent = get("entity.puffer_fish.death")
    @JvmField
    public val PUFFER_FISH_FLOP: SoundEvent = get("entity.puffer_fish.flop")
    @JvmField
    public val PUFFER_FISH_HURT: SoundEvent = get("entity.puffer_fish.hurt")
    @JvmField
    public val PUFFER_FISH_STING: SoundEvent = get("entity.puffer_fish.sting")
    @JvmField
    public val PUMPKIN_CARVE: SoundEvent = get("block.pumpkin.carve")
    @JvmField
    public val RABBIT_AMBIENT: SoundEvent = get("entity.rabbit.ambient")
    @JvmField
    public val RABBIT_ATTACK: SoundEvent = get("entity.rabbit.attack")
    @JvmField
    public val RABBIT_DEATH: SoundEvent = get("entity.rabbit.death")
    @JvmField
    public val RABBIT_HURT: SoundEvent = get("entity.rabbit.hurt")
    @JvmField
    public val RABBIT_JUMP: SoundEvent = get("entity.rabbit.jump")
    @JvmField
    public val RAID_HORN: SoundEvent = get("event.raid.horn")
    @JvmField
    public val RAVAGER_AMBIENT: SoundEvent = get("entity.ravager.ambient")
    @JvmField
    public val RAVAGER_ATTACK: SoundEvent = get("entity.ravager.attack")
    @JvmField
    public val RAVAGER_CELEBRATE: SoundEvent = get("entity.ravager.celebrate")
    @JvmField
    public val RAVAGER_DEATH: SoundEvent = get("entity.ravager.death")
    @JvmField
    public val RAVAGER_HURT: SoundEvent = get("entity.ravager.hurt")
    @JvmField
    public val RAVAGER_STEP: SoundEvent = get("entity.ravager.step")
    @JvmField
    public val RAVAGER_STUNNED: SoundEvent = get("entity.ravager.stunned")
    @JvmField
    public val RAVAGER_ROAR: SoundEvent = get("entity.ravager.roar")
    @JvmField
    public val NETHER_GOLD_ORE_BREAK: SoundEvent = get("block.nether_gold_ore.break")
    @JvmField
    public val NETHER_GOLD_ORE_FALL: SoundEvent = get("block.nether_gold_ore.fall")
    @JvmField
    public val NETHER_GOLD_ORE_HIT: SoundEvent = get("block.nether_gold_ore.hit")
    @JvmField
    public val NETHER_GOLD_ORE_PLACE: SoundEvent = get("block.nether_gold_ore.place")
    @JvmField
    public val NETHER_GOLD_ORE_STEP: SoundEvent = get("block.nether_gold_ore.step")
    @JvmField
    public val NETHER_ORE_BREAK: SoundEvent = get("block.nether_ore.break")
    @JvmField
    public val NETHER_ORE_FALL: SoundEvent = get("block.nether_ore.fall")
    @JvmField
    public val NETHER_ORE_HIT: SoundEvent = get("block.nether_ore.hit")
    @JvmField
    public val NETHER_ORE_PLACE: SoundEvent = get("block.nether_ore.place")
    @JvmField
    public val NETHER_ORE_STEP: SoundEvent = get("block.nether_ore.step")
    @JvmField
    public val REDSTONE_TORCH_BURNOUT: SoundEvent = get("block.redstone_torch.burnout")
    @JvmField
    public val RESPAWN_ANCHOR_AMBIENT: SoundEvent = get("block.respawn_anchor.ambient")
    @JvmField
    public val RESPAWN_ANCHOR_CHARGE: SoundEvent = get("block.respawn_anchor.charge")
    @JvmField
    public val RESPAWN_ANCHOR_DEPLETE: SoundEvent = get("block.respawn_anchor.deplete")
    @JvmField
    public val RESPAWN_ANCHOR_SET_SPAWN: SoundEvent = get("block.respawn_anchor.set_spawn")
    @JvmField
    public val ROOTED_DIRT_BREAK: SoundEvent = get("block.rooted_dirt.break")
    @JvmField
    public val ROOTED_DIRT_FALL: SoundEvent = get("block.rooted_dirt.fall")
    @JvmField
    public val ROOTED_DIRT_HIT: SoundEvent = get("block.rooted_dirt.hit")
    @JvmField
    public val ROOTED_DIRT_PLACE: SoundEvent = get("block.rooted_dirt.place")
    @JvmField
    public val ROOTED_DIRT_STEP: SoundEvent = get("block.rooted_dirt.step")
    @JvmField
    public val SALMON_AMBIENT: SoundEvent = get("entity.salmon.ambient")
    @JvmField
    public val SALMON_DEATH: SoundEvent = get("entity.salmon.death")
    @JvmField
    public val SALMON_FLOP: SoundEvent = get("entity.salmon.flop")
    @JvmField
    public val SALMON_HURT: SoundEvent = get("entity.salmon.hurt")
    @JvmField
    public val SAND_BREAK: SoundEvent = get("block.sand.break")
    @JvmField
    public val SAND_FALL: SoundEvent = get("block.sand.fall")
    @JvmField
    public val SAND_HIT: SoundEvent = get("block.sand.hit")
    @JvmField
    public val SAND_PLACE: SoundEvent = get("block.sand.place")
    @JvmField
    public val SAND_STEP: SoundEvent = get("block.sand.step")
    @JvmField
    public val SCAFFOLDING_BREAK: SoundEvent = get("block.scaffolding.break")
    @JvmField
    public val SCAFFOLDING_FALL: SoundEvent = get("block.scaffolding.fall")
    @JvmField
    public val SCAFFOLDING_HIT: SoundEvent = get("block.scaffolding.hit")
    @JvmField
    public val SCAFFOLDING_PLACE: SoundEvent = get("block.scaffolding.place")
    @JvmField
    public val SCAFFOLDING_STEP: SoundEvent = get("block.scaffolding.step")
    @JvmField
    public val SCULK_BLOCK_SPREAD: SoundEvent = get("block.sculk.spread")
    @JvmField
    public val SCULK_BLOCK_CHARGE: SoundEvent = get("block.sculk.charge")
    @JvmField
    public val SCULK_BLOCK_BREAK: SoundEvent = get("block.sculk.break")
    @JvmField
    public val SCULK_BLOCK_FALL: SoundEvent = get("block.sculk.fall")
    @JvmField
    public val SCULK_BLOCK_HIT: SoundEvent = get("block.sculk.hit")
    @JvmField
    public val SCULK_BLOCK_PLACE: SoundEvent = get("block.sculk.place")
    @JvmField
    public val SCULK_BLOCK_STEP: SoundEvent = get("block.sculk.step")
    @JvmField
    public val SCULK_CATALYST_BLOOM: SoundEvent = get("block.sculk_catalyst.bloom")
    @JvmField
    public val SCULK_CATALYST_BREAK: SoundEvent = get("block.sculk_catalyst.break")
    @JvmField
    public val SCULK_CATALYST_FALL: SoundEvent = get("block.sculk_catalyst.fall")
    @JvmField
    public val SCULK_CATALYST_HIT: SoundEvent = get("block.sculk_catalyst.hit")
    @JvmField
    public val SCULK_CATALYST_PLACE: SoundEvent = get("block.sculk_catalyst.place")
    @JvmField
    public val SCULK_CATALYST_STEP: SoundEvent = get("block.sculk_catalyst.step")
    @JvmField
    public val SCULK_CLICKING: SoundEvent = get("block.sculk_sensor.clicking")
    @JvmField
    public val SCULK_CLICKING_STOP: SoundEvent = get("block.sculk_sensor.clicking_stop")
    @JvmField
    public val SCULK_SENSOR_BREAK: SoundEvent = get("block.sculk_sensor.break")
    @JvmField
    public val SCULK_SENSOR_FALL: SoundEvent = get("block.sculk_sensor.fall")
    @JvmField
    public val SCULK_SENSOR_HIT: SoundEvent = get("block.sculk_sensor.hit")
    @JvmField
    public val SCULK_SENSOR_PLACE: SoundEvent = get("block.sculk_sensor.place")
    @JvmField
    public val SCULK_SENSOR_STEP: SoundEvent = get("block.sculk_sensor.step")
    @JvmField
    public val SCULK_SHRIEKER_BREAK: SoundEvent = get("block.sculk_shrieker.break")
    @JvmField
    public val SCULK_SHRIEKER_FALL: SoundEvent = get("block.sculk_shrieker.fall")
    @JvmField
    public val SCULK_SHRIEKER_HIT: SoundEvent = get("block.sculk_shrieker.hit")
    @JvmField
    public val SCULK_SHRIEKER_PLACE: SoundEvent = get("block.sculk_shrieker.place")
    @JvmField
    public val SCULK_SHRIEKER_SHRIEK: SoundEvent = get("block.sculk_shrieker.shriek")
    @JvmField
    public val SCULK_SHRIEKER_STEP: SoundEvent = get("block.sculk_shrieker.step")
    @JvmField
    public val SCULK_VEIN_BREAK: SoundEvent = get("block.sculk_vein.break")
    @JvmField
    public val SCULK_VEIN_FALL: SoundEvent = get("block.sculk_vein.fall")
    @JvmField
    public val SCULK_VEIN_HIT: SoundEvent = get("block.sculk_vein.hit")
    @JvmField
    public val SCULK_VEIN_PLACE: SoundEvent = get("block.sculk_vein.place")
    @JvmField
    public val SCULK_VEIN_STEP: SoundEvent = get("block.sculk_vein.step")
    @JvmField
    public val SHEEP_AMBIENT: SoundEvent = get("entity.sheep.ambient")
    @JvmField
    public val SHEEP_DEATH: SoundEvent = get("entity.sheep.death")
    @JvmField
    public val SHEEP_HURT: SoundEvent = get("entity.sheep.hurt")
    @JvmField
    public val SHEEP_SHEAR: SoundEvent = get("entity.sheep.shear")
    @JvmField
    public val SHEEP_STEP: SoundEvent = get("entity.sheep.step")
    @JvmField
    public val SHIELD_BLOCK: SoundEvent = get("item.shield.block")
    @JvmField
    public val SHIELD_BREAK: SoundEvent = get("item.shield.break")
    @JvmField
    public val SHROOMLIGHT_BREAK: SoundEvent = get("block.shroomlight.break")
    @JvmField
    public val SHROOMLIGHT_STEP: SoundEvent = get("block.shroomlight.step")
    @JvmField
    public val SHROOMLIGHT_PLACE: SoundEvent = get("block.shroomlight.place")
    @JvmField
    public val SHROOMLIGHT_HIT: SoundEvent = get("block.shroomlight.hit")
    @JvmField
    public val SHROOMLIGHT_FALL: SoundEvent = get("block.shroomlight.fall")
    @JvmField
    public val SHOVEL_FLATTEN: SoundEvent = get("item.shovel.flatten")
    @JvmField
    public val SHULKER_AMBIENT: SoundEvent = get("entity.shulker.ambient")
    @JvmField
    public val SHULKER_BOX_CLOSE: SoundEvent = get("block.shulker_box.close")
    @JvmField
    public val SHULKER_BOX_OPEN: SoundEvent = get("block.shulker_box.open")
    @JvmField
    public val SHULKER_BULLET_HIT: SoundEvent = get("entity.shulker_bullet.hit")
    @JvmField
    public val SHULKER_BULLET_HURT: SoundEvent = get("entity.shulker_bullet.hurt")
    @JvmField
    public val SHULKER_CLOSE: SoundEvent = get("entity.shulker.close")
    @JvmField
    public val SHULKER_DEATH: SoundEvent = get("entity.shulker.death")
    @JvmField
    public val SHULKER_HURT: SoundEvent = get("entity.shulker.hurt")
    @JvmField
    public val SHULKER_HURT_CLOSED: SoundEvent = get("entity.shulker.hurt_closed")
    @JvmField
    public val SHULKER_OPEN: SoundEvent = get("entity.shulker.open")
    @JvmField
    public val SHULKER_SHOOT: SoundEvent = get("entity.shulker.shoot")
    @JvmField
    public val SHULKER_TELEPORT: SoundEvent = get("entity.shulker.teleport")
    @JvmField
    public val SILVERFISH_AMBIENT: SoundEvent = get("entity.silverfish.ambient")
    @JvmField
    public val SILVERFISH_DEATH: SoundEvent = get("entity.silverfish.death")
    @JvmField
    public val SILVERFISH_HURT: SoundEvent = get("entity.silverfish.hurt")
    @JvmField
    public val SILVERFISH_STEP: SoundEvent = get("entity.silverfish.step")
    @JvmField
    public val SKELETON_AMBIENT: SoundEvent = get("entity.skeleton.ambient")
    @JvmField
    public val SKELETON_CONVERTED_TO_STRAY: SoundEvent = get("entity.skeleton.converted_to_stray")
    @JvmField
    public val SKELETON_DEATH: SoundEvent = get("entity.skeleton.death")
    @JvmField
    public val SKELETON_HORSE_AMBIENT: SoundEvent = get("entity.skeleton_horse.ambient")
    @JvmField
    public val SKELETON_HORSE_DEATH: SoundEvent = get("entity.skeleton_horse.death")
    @JvmField
    public val SKELETON_HORSE_HURT: SoundEvent = get("entity.skeleton_horse.hurt")
    @JvmField
    public val SKELETON_HORSE_SWIM: SoundEvent = get("entity.skeleton_horse.swim")
    @JvmField
    public val SKELETON_HORSE_AMBIENT_WATER: SoundEvent = get("entity.skeleton_horse.ambient_water")
    @JvmField
    public val SKELETON_HORSE_GALLOP_WATER: SoundEvent = get("entity.skeleton_horse.gallop_water")
    @JvmField
    public val SKELETON_HORSE_JUMP_WATER: SoundEvent = get("entity.skeleton_horse.jump_water")
    @JvmField
    public val SKELETON_HORSE_STEP_WATER: SoundEvent = get("entity.skeleton_horse.step_water")
    @JvmField
    public val SKELETON_HURT: SoundEvent = get("entity.skeleton.hurt")
    @JvmField
    public val SKELETON_SHOOT: SoundEvent = get("entity.skeleton.shoot")
    @JvmField
    public val SKELETON_STEP: SoundEvent = get("entity.skeleton.step")
    @JvmField
    public val SLIME_ATTACK: SoundEvent = get("entity.slime.attack")
    @JvmField
    public val SLIME_DEATH: SoundEvent = get("entity.slime.death")
    @JvmField
    public val SLIME_HURT: SoundEvent = get("entity.slime.hurt")
    @JvmField
    public val SLIME_JUMP: SoundEvent = get("entity.slime.jump")
    @JvmField
    public val SLIME_SQUISH: SoundEvent = get("entity.slime.squish")
    @JvmField
    public val SLIME_BLOCK_BREAK: SoundEvent = get("block.slime_block.break")
    @JvmField
    public val SLIME_BLOCK_FALL: SoundEvent = get("block.slime_block.fall")
    @JvmField
    public val SLIME_BLOCK_HIT: SoundEvent = get("block.slime_block.hit")
    @JvmField
    public val SLIME_BLOCK_PLACE: SoundEvent = get("block.slime_block.place")
    @JvmField
    public val SLIME_BLOCK_STEP: SoundEvent = get("block.slime_block.step")
    @JvmField
    public val SMALL_AMETHYST_BUD_BREAK: SoundEvent = get("block.small_amethyst_bud.break")
    @JvmField
    public val SMALL_AMETHYST_BUD_PLACE: SoundEvent = get("block.small_amethyst_bud.place")
    @JvmField
    public val SMALL_DRIPLEAF_BREAK: SoundEvent = get("block.small_dripleaf.break")
    @JvmField
    public val SMALL_DRIPLEAF_FALL: SoundEvent = get("block.small_dripleaf.fall")
    @JvmField
    public val SMALL_DRIPLEAF_HIT: SoundEvent = get("block.small_dripleaf.hit")
    @JvmField
    public val SMALL_DRIPLEAF_PLACE: SoundEvent = get("block.small_dripleaf.place")
    @JvmField
    public val SMALL_DRIPLEAF_STEP: SoundEvent = get("block.small_dripleaf.step")
    @JvmField
    public val SOUL_SAND_BREAK: SoundEvent = get("block.soul_sand.break")
    @JvmField
    public val SOUL_SAND_STEP: SoundEvent = get("block.soul_sand.step")
    @JvmField
    public val SOUL_SAND_PLACE: SoundEvent = get("block.soul_sand.place")
    @JvmField
    public val SOUL_SAND_HIT: SoundEvent = get("block.soul_sand.hit")
    @JvmField
    public val SOUL_SAND_FALL: SoundEvent = get("block.soul_sand.fall")
    @JvmField
    public val SOUL_SOIL_BREAK: SoundEvent = get("block.soul_soil.break")
    @JvmField
    public val SOUL_SOIL_STEP: SoundEvent = get("block.soul_soil.step")
    @JvmField
    public val SOUL_SOIL_PLACE: SoundEvent = get("block.soul_soil.place")
    @JvmField
    public val SOUL_SOIL_HIT: SoundEvent = get("block.soul_soil.hit")
    @JvmField
    public val SOUL_SOIL_FALL: SoundEvent = get("block.soul_soil.fall")
    @JvmField
    public val SOUL_ESCAPE: SoundEvent = get("particle.soul_escape")
    @JvmField
    public val SPORE_BLOSSOM_BREAK: SoundEvent = get("block.spore_blossom.break")
    @JvmField
    public val SPORE_BLOSSOM_FALL: SoundEvent = get("block.spore_blossom.fall")
    @JvmField
    public val SPORE_BLOSSOM_HIT: SoundEvent = get("block.spore_blossom.hit")
    @JvmField
    public val SPORE_BLOSSOM_PLACE: SoundEvent = get("block.spore_blossom.place")
    @JvmField
    public val SPORE_BLOSSOM_STEP: SoundEvent = get("block.spore_blossom.step")
    @JvmField
    public val STRIDER_AMBIENT: SoundEvent = get("entity.strider.ambient")
    @JvmField
    public val STRIDER_HAPPY: SoundEvent = get("entity.strider.happy")
    @JvmField
    public val STRIDER_RETREAT: SoundEvent = get("entity.strider.retreat")
    @JvmField
    public val STRIDER_DEATH: SoundEvent = get("entity.strider.death")
    @JvmField
    public val STRIDER_HURT: SoundEvent = get("entity.strider.hurt")
    @JvmField
    public val STRIDER_STEP: SoundEvent = get("entity.strider.step")
    @JvmField
    public val STRIDER_STEP_LAVA: SoundEvent = get("entity.strider.step_lava")
    @JvmField
    public val STRIDER_EAT: SoundEvent = get("entity.strider.eat")
    @JvmField
    public val STRIDER_SADDLE: SoundEvent = get("entity.strider.saddle")
    @JvmField
    public val SLIME_DEATH_SMALL: SoundEvent = get("entity.slime.death_small")
    @JvmField
    public val SLIME_HURT_SMALL: SoundEvent = get("entity.slime.hurt_small")
    @JvmField
    public val SLIME_JUMP_SMALL: SoundEvent = get("entity.slime.jump_small")
    @JvmField
    public val SLIME_SQUISH_SMALL: SoundEvent = get("entity.slime.squish_small")
    @JvmField
    public val SMITHING_TABLE_USE: SoundEvent = get("block.smithing_table.use")
    @JvmField
    public val SMOKER_SMOKE: SoundEvent = get("block.smoker.smoke")
    @JvmField
    public val SNOWBALL_THROW: SoundEvent = get("entity.snowball.throw")
    @JvmField
    public val SNOW_BREAK: SoundEvent = get("block.snow.break")
    @JvmField
    public val SNOW_FALL: SoundEvent = get("block.snow.fall")
    @JvmField
    public val SNOW_GOLEM_AMBIENT: SoundEvent = get("entity.snow_golem.ambient")
    @JvmField
    public val SNOW_GOLEM_DEATH: SoundEvent = get("entity.snow_golem.death")
    @JvmField
    public val SNOW_GOLEM_HURT: SoundEvent = get("entity.snow_golem.hurt")
    @JvmField
    public val SNOW_GOLEM_SHOOT: SoundEvent = get("entity.snow_golem.shoot")
    @JvmField
    public val SNOW_GOLEM_SHEAR: SoundEvent = get("entity.snow_golem.shear")
    @JvmField
    public val SNOW_HIT: SoundEvent = get("block.snow.hit")
    @JvmField
    public val SNOW_PLACE: SoundEvent = get("block.snow.place")
    @JvmField
    public val SNOW_STEP: SoundEvent = get("block.snow.step")
    @JvmField
    public val SPIDER_AMBIENT: SoundEvent = get("entity.spider.ambient")
    @JvmField
    public val SPIDER_DEATH: SoundEvent = get("entity.spider.death")
    @JvmField
    public val SPIDER_HURT: SoundEvent = get("entity.spider.hurt")
    @JvmField
    public val SPIDER_STEP: SoundEvent = get("entity.spider.step")
    @JvmField
    public val SPLASH_POTION_BREAK: SoundEvent = get("entity.splash_potion.break")
    @JvmField
    public val SPLASH_POTION_THROW: SoundEvent = get("entity.splash_potion.throw")
    @JvmField
    public val SPYGLASS_USE: SoundEvent = get("item.spyglass.use")
    @JvmField
    public val SPYGLASS_STOP_USING: SoundEvent = get("item.spyglass.stop_using")
    @JvmField
    public val SQUID_AMBIENT: SoundEvent = get("entity.squid.ambient")
    @JvmField
    public val SQUID_DEATH: SoundEvent = get("entity.squid.death")
    @JvmField
    public val SQUID_HURT: SoundEvent = get("entity.squid.hurt")
    @JvmField
    public val SQUID_SQUIRT: SoundEvent = get("entity.squid.squirt")
    @JvmField
    public val STONE_BREAK: SoundEvent = get("block.stone.break")
    @JvmField
    public val STONE_BUTTON_CLICK_OFF: SoundEvent = get("block.stone_button.click_off")
    @JvmField
    public val STONE_BUTTON_CLICK_ON: SoundEvent = get("block.stone_button.click_on")
    @JvmField
    public val STONE_FALL: SoundEvent = get("block.stone.fall")
    @JvmField
    public val STONE_HIT: SoundEvent = get("block.stone.hit")
    @JvmField
    public val STONE_PLACE: SoundEvent = get("block.stone.place")
    @JvmField
    public val STONE_PRESSURE_PLATE_CLICK_OFF: SoundEvent = get("block.stone_pressure_plate.click_off")
    @JvmField
    public val STONE_PRESSURE_PLATE_CLICK_ON: SoundEvent = get("block.stone_pressure_plate.click_on")
    @JvmField
    public val STONE_STEP: SoundEvent = get("block.stone.step")
    @JvmField
    public val STRAY_AMBIENT: SoundEvent = get("entity.stray.ambient")
    @JvmField
    public val STRAY_DEATH: SoundEvent = get("entity.stray.death")
    @JvmField
    public val STRAY_HURT: SoundEvent = get("entity.stray.hurt")
    @JvmField
    public val STRAY_STEP: SoundEvent = get("entity.stray.step")
    @JvmField
    public val SWEET_BERRY_BUSH_BREAK: SoundEvent = get("block.sweet_berry_bush.break")
    @JvmField
    public val SWEET_BERRY_BUSH_PLACE: SoundEvent = get("block.sweet_berry_bush.place")
    @JvmField
    public val SWEET_BERRY_BUSH_PICK_BERRIES: SoundEvent = get("block.sweet_berry_bush.pick_berries")
    @JvmField
    public val TADPOLE_DEATH: SoundEvent = get("entity.tadpole.death")
    @JvmField
    public val TADPOLE_FLOP: SoundEvent = get("entity.tadpole.flop")
    @JvmField
    public val TADPOLE_GROW_UP: SoundEvent = get("entity.tadpole.grow_up")
    @JvmField
    public val TADPOLE_HURT: SoundEvent = get("entity.tadpole.hurt")
    @JvmField
    public val THORNS_HIT: SoundEvent = get("enchant.thorns.hit")
    @JvmField
    public val TNT_PRIMED: SoundEvent = get("entity.tnt.primed")
    @JvmField
    public val TOTEM_USE: SoundEvent = get("item.totem.use")
    @JvmField
    public val TRIDENT_HIT: SoundEvent = get("item.trident.hit")
    @JvmField
    public val TRIDENT_HIT_GROUND: SoundEvent = get("item.trident.hit_ground")
    @JvmField
    public val TRIDENT_RETURN: SoundEvent = get("item.trident.return")
    @JvmField
    public val TRIDENT_RIPTIDE_1: SoundEvent = get("item.trident.riptide_1")
    @JvmField
    public val TRIDENT_RIPTIDE_2: SoundEvent = get("item.trident.riptide_2")
    @JvmField
    public val TRIDENT_RIPTIDE_3: SoundEvent = get("item.trident.riptide_3")
    @JvmField
    public val TRIDENT_THROW: SoundEvent = get("item.trident.throw")
    @JvmField
    public val TRIDENT_THUNDER: SoundEvent = get("item.trident.thunder")
    @JvmField
    public val TRIPWIRE_ATTACH: SoundEvent = get("block.tripwire.attach")
    @JvmField
    public val TRIPWIRE_CLICK_OFF: SoundEvent = get("block.tripwire.click_off")
    @JvmField
    public val TRIPWIRE_CLICK_ON: SoundEvent = get("block.tripwire.click_on")
    @JvmField
    public val TRIPWIRE_DETACH: SoundEvent = get("block.tripwire.detach")
    @JvmField
    public val TROPICAL_FISH_AMBIENT: SoundEvent = get("entity.tropical_fish.ambient")
    @JvmField
    public val TROPICAL_FISH_DEATH: SoundEvent = get("entity.tropical_fish.death")
    @JvmField
    public val TROPICAL_FISH_FLOP: SoundEvent = get("entity.tropical_fish.flop")
    @JvmField
    public val TROPICAL_FISH_HURT: SoundEvent = get("entity.tropical_fish.hurt")
    @JvmField
    public val TUFF_BREAK: SoundEvent = get("block.tuff.break")
    @JvmField
    public val TUFF_STEP: SoundEvent = get("block.tuff.step")
    @JvmField
    public val TUFF_PLACE: SoundEvent = get("block.tuff.place")
    @JvmField
    public val TUFF_HIT: SoundEvent = get("block.tuff.hit")
    @JvmField
    public val TUFF_FALL: SoundEvent = get("block.tuff.fall")
    @JvmField
    public val TURTLE_AMBIENT_LAND: SoundEvent = get("entity.turtle.ambient_land")
    @JvmField
    public val TURTLE_DEATH: SoundEvent = get("entity.turtle.death")
    @JvmField
    public val TURTLE_DEATH_BABY: SoundEvent = get("entity.turtle.death_baby")
    @JvmField
    public val TURTLE_EGG_BREAK: SoundEvent = get("entity.turtle.egg_break")
    @JvmField
    public val TURTLE_EGG_CRACK: SoundEvent = get("entity.turtle.egg_crack")
    @JvmField
    public val TURTLE_EGG_HATCH: SoundEvent = get("entity.turtle.egg_hatch")
    @JvmField
    public val TURTLE_HURT: SoundEvent = get("entity.turtle.hurt")
    @JvmField
    public val TURTLE_HURT_BABY: SoundEvent = get("entity.turtle.hurt_baby")
    @JvmField
    public val TURTLE_LAY_EGG: SoundEvent = get("entity.turtle.lay_egg")
    @JvmField
    public val TURTLE_SHAMBLE: SoundEvent = get("entity.turtle.shamble")
    @JvmField
    public val TURTLE_SHAMBLE_BABY: SoundEvent = get("entity.turtle.shamble_baby")
    @JvmField
    public val TURTLE_SWIM: SoundEvent = get("entity.turtle.swim")
    @JvmField
    public val UI_BUTTON_CLICK: SoundEvent = get("ui.button.click")
    @JvmField
    public val UI_LOOM_SELECT_PATTERN: SoundEvent = get("ui.loom.select_pattern")
    @JvmField
    public val UI_LOOM_TAKE_RESULT: SoundEvent = get("ui.loom.take_result")
    @JvmField
    public val UI_CARTOGRAPHY_TABLE_TAKE_RESULT: SoundEvent = get("ui.cartography_table.take_result")
    @JvmField
    public val UI_STONECUTTER_TAKE_RESULT: SoundEvent = get("ui.stonecutter.take_result")
    @JvmField
    public val UI_STONECUTTER_SELECT_RECIPE: SoundEvent = get("ui.stonecutter.select_recipe")
    @JvmField
    public val UI_TOAST_CHALLENGE_COMPLETE: SoundEvent = get("ui.toast.challenge_complete")
    @JvmField
    public val UI_TOAST_IN: SoundEvent = get("ui.toast.in")
    @JvmField
    public val UI_TOAST_OUT: SoundEvent = get("ui.toast.out")
    @JvmField
    public val VEX_AMBIENT: SoundEvent = get("entity.vex.ambient")
    @JvmField
    public val VEX_CHARGE: SoundEvent = get("entity.vex.charge")
    @JvmField
    public val VEX_DEATH: SoundEvent = get("entity.vex.death")
    @JvmField
    public val VEX_HURT: SoundEvent = get("entity.vex.hurt")
    @JvmField
    public val VILLAGER_AMBIENT: SoundEvent = get("entity.villager.ambient")
    @JvmField
    public val VILLAGER_CELEBRATE: SoundEvent = get("entity.villager.celebrate")
    @JvmField
    public val VILLAGER_DEATH: SoundEvent = get("entity.villager.death")
    @JvmField
    public val VILLAGER_HURT: SoundEvent = get("entity.villager.hurt")
    @JvmField
    public val VILLAGER_NO: SoundEvent = get("entity.villager.no")
    @JvmField
    public val VILLAGER_TRADE: SoundEvent = get("entity.villager.trade")
    @JvmField
    public val VILLAGER_YES: SoundEvent = get("entity.villager.yes")
    @JvmField
    public val VILLAGER_WORK_ARMORER: SoundEvent = get("entity.villager.work_armorer")
    @JvmField
    public val VILLAGER_WORK_BUTCHER: SoundEvent = get("entity.villager.work_butcher")
    @JvmField
    public val VILLAGER_WORK_CARTOGRAPHER: SoundEvent = get("entity.villager.work_cartographer")
    @JvmField
    public val VILLAGER_WORK_CLERIC: SoundEvent = get("entity.villager.work_cleric")
    @JvmField
    public val VILLAGER_WORK_FARMER: SoundEvent = get("entity.villager.work_farmer")
    @JvmField
    public val VILLAGER_WORK_FISHERMAN: SoundEvent = get("entity.villager.work_fisherman")
    @JvmField
    public val VILLAGER_WORK_FLETCHER: SoundEvent = get("entity.villager.work_fletcher")
    @JvmField
    public val VILLAGER_WORK_LEATHERWORKER: SoundEvent = get("entity.villager.work_leatherworker")
    @JvmField
    public val VILLAGER_WORK_LIBRARIAN: SoundEvent = get("entity.villager.work_librarian")
    @JvmField
    public val VILLAGER_WORK_MASON: SoundEvent = get("entity.villager.work_mason")
    @JvmField
    public val VILLAGER_WORK_SHEPHERD: SoundEvent = get("entity.villager.work_shepherd")
    @JvmField
    public val VILLAGER_WORK_TOOLSMITH: SoundEvent = get("entity.villager.work_toolsmith")
    @JvmField
    public val VILLAGER_WORK_WEAPONSMITH: SoundEvent = get("entity.villager.work_weaponsmith")
    @JvmField
    public val VINDICATOR_AMBIENT: SoundEvent = get("entity.vindicator.ambient")
    @JvmField
    public val VINDICATOR_CELEBRATE: SoundEvent = get("entity.vindicator.celebrate")
    @JvmField
    public val VINDICATOR_DEATH: SoundEvent = get("entity.vindicator.death")
    @JvmField
    public val VINDICATOR_HURT: SoundEvent = get("entity.vindicator.hurt")
    @JvmField
    public val VINE_BREAK: SoundEvent = get("block.vine.break")
    @JvmField
    public val VINE_FALL: SoundEvent = get("block.vine.fall")
    @JvmField
    public val VINE_HIT: SoundEvent = get("block.vine.hit")
    @JvmField
    public val VINE_PLACE: SoundEvent = get("block.vine.place")
    @JvmField
    public val VINE_STEP: SoundEvent = get("block.vine.step")
    @JvmField
    public val LILY_PAD_PLACE: SoundEvent = get("block.lily_pad.place")
    @JvmField
    public val WANDERING_TRADER_AMBIENT: SoundEvent = get("entity.wandering_trader.ambient")
    @JvmField
    public val WANDERING_TRADER_DEATH: SoundEvent = get("entity.wandering_trader.death")
    @JvmField
    public val WANDERING_TRADER_DISAPPEARED: SoundEvent = get("entity.wandering_trader.disappeared")
    @JvmField
    public val WANDERING_TRADER_DRINK_MILK: SoundEvent = get("entity.wandering_trader.drink_milk")
    @JvmField
    public val WANDERING_TRADER_DRINK_POTION: SoundEvent = get("entity.wandering_trader.drink_potion")
    @JvmField
    public val WANDERING_TRADER_HURT: SoundEvent = get("entity.wandering_trader.hurt")
    @JvmField
    public val WANDERING_TRADER_NO: SoundEvent = get("entity.wandering_trader.no")
    @JvmField
    public val WANDERING_TRADER_REAPPEARED: SoundEvent = get("entity.wandering_trader.reappeared")
    @JvmField
    public val WANDERING_TRADER_TRADE: SoundEvent = get("entity.wandering_trader.trade")
    @JvmField
    public val WANDERING_TRADER_YES: SoundEvent = get("entity.wandering_trader.yes")
    @JvmField
    public val WARDEN_AGITATED: SoundEvent = get("entity.warden.agitated")
    @JvmField
    public val WARDEN_AMBIENT: SoundEvent = get("entity.warden.ambient")
    @JvmField
    public val WARDEN_ANGRY: SoundEvent = get("entity.warden.angry")
    @JvmField
    public val WARDEN_ATTACK_IMPACT: SoundEvent = get("entity.warden.attack_impact")
    @JvmField
    public val WARDEN_DEATH: SoundEvent = get("entity.warden.death")
    @JvmField
    public val WARDEN_DIG: SoundEvent = get("entity.warden.dig")
    @JvmField
    public val WARDEN_EMERGE: SoundEvent = get("entity.warden.emerge")
    @JvmField
    public val WARDEN_HEARTBEAT: SoundEvent = get("entity.warden.heartbeat")
    @JvmField
    public val WARDEN_HURT: SoundEvent = get("entity.warden.hurt")
    @JvmField
    public val WARDEN_LISTENING: SoundEvent = get("entity.warden.listening")
    @JvmField
    public val WARDEN_LISTENING_ANGRY: SoundEvent = get("entity.warden.listening_angry")
    @JvmField
    public val WARDEN_NEARBY_CLOSE: SoundEvent = get("entity.warden.nearby_close")
    @JvmField
    public val WARDEN_NEARBY_CLOSER: SoundEvent = get("entity.warden.nearby_closer")
    @JvmField
    public val WARDEN_NEARBY_CLOSEST: SoundEvent = get("entity.warden.nearby_closest")
    @JvmField
    public val WARDEN_ROAR: SoundEvent = get("entity.warden.roar")
    @JvmField
    public val WARDEN_SNIFF: SoundEvent = get("entity.warden.sniff")
    @JvmField
    public val WARDEN_SONIC_BOOM: SoundEvent = get("entity.warden.sonic_boom")
    @JvmField
    public val WARDEN_SONIC_CHARGE: SoundEvent = get("entity.warden.sonic_charge")
    @JvmField
    public val WARDEN_STEP: SoundEvent = get("entity.warden.step")
    @JvmField
    public val WARDEN_TENDRIL_CLICKS: SoundEvent = get("entity.warden.tendril_clicks")
    @JvmField
    public val WATER_AMBIENT: SoundEvent = get("block.water.ambient")
    @JvmField
    public val WEATHER_RAIN: SoundEvent = get("weather.rain")
    @JvmField
    public val WEATHER_RAIN_ABOVE: SoundEvent = get("weather.rain.above")
    @JvmField
    public val WET_GRASS_BREAK: SoundEvent = get("block.wet_grass.break")
    @JvmField
    public val WET_GRASS_FALL: SoundEvent = get("block.wet_grass.fall")
    @JvmField
    public val WET_GRASS_HIT: SoundEvent = get("block.wet_grass.hit")
    @JvmField
    public val WET_GRASS_PLACE: SoundEvent = get("block.wet_grass.place")
    @JvmField
    public val WET_GRASS_STEP: SoundEvent = get("block.wet_grass.step")
    @JvmField
    public val WITCH_AMBIENT: SoundEvent = get("entity.witch.ambient")
    @JvmField
    public val WITCH_CELEBRATE: SoundEvent = get("entity.witch.celebrate")
    @JvmField
    public val WITCH_DEATH: SoundEvent = get("entity.witch.death")
    @JvmField
    public val WITCH_DRINK: SoundEvent = get("entity.witch.drink")
    @JvmField
    public val WITCH_HURT: SoundEvent = get("entity.witch.hurt")
    @JvmField
    public val WITCH_THROW: SoundEvent = get("entity.witch.throw")
    @JvmField
    public val WITHER_AMBIENT: SoundEvent = get("entity.wither.ambient")
    @JvmField
    public val WITHER_BREAK_BLOCK: SoundEvent = get("entity.wither.break_block")
    @JvmField
    public val WITHER_DEATH: SoundEvent = get("entity.wither.death")
    @JvmField
    public val WITHER_HURT: SoundEvent = get("entity.wither.hurt")
    @JvmField
    public val WITHER_SHOOT: SoundEvent = get("entity.wither.shoot")
    @JvmField
    public val WITHER_SKELETON_AMBIENT: SoundEvent = get("entity.wither_skeleton.ambient")
    @JvmField
    public val WITHER_SKELETON_DEATH: SoundEvent = get("entity.wither_skeleton.death")
    @JvmField
    public val WITHER_SKELETON_HURT: SoundEvent = get("entity.wither_skeleton.hurt")
    @JvmField
    public val WITHER_SKELETON_STEP: SoundEvent = get("entity.wither_skeleton.step")
    @JvmField
    public val WITHER_SPAWN: SoundEvent = get("entity.wither.spawn")
    @JvmField
    public val WOLF_AMBIENT: SoundEvent = get("entity.wolf.ambient")
    @JvmField
    public val WOLF_DEATH: SoundEvent = get("entity.wolf.death")
    @JvmField
    public val WOLF_GROWL: SoundEvent = get("entity.wolf.growl")
    @JvmField
    public val WOLF_HOWL: SoundEvent = get("entity.wolf.howl")
    @JvmField
    public val WOLF_HURT: SoundEvent = get("entity.wolf.hurt")
    @JvmField
    public val WOLF_PANT: SoundEvent = get("entity.wolf.pant")
    @JvmField
    public val WOLF_SHAKE: SoundEvent = get("entity.wolf.shake")
    @JvmField
    public val WOLF_STEP: SoundEvent = get("entity.wolf.step")
    @JvmField
    public val WOLF_WHINE: SoundEvent = get("entity.wolf.whine")
    @JvmField
    public val WOODEN_DOOR_CLOSE: SoundEvent = get("block.wooden_door.close")
    @JvmField
    public val WOODEN_DOOR_OPEN: SoundEvent = get("block.wooden_door.open")
    @JvmField
    public val WOODEN_TRAPDOOR_CLOSE: SoundEvent = get("block.wooden_trapdoor.close")
    @JvmField
    public val WOODEN_TRAPDOOR_OPEN: SoundEvent = get("block.wooden_trapdoor.open")
    @JvmField
    public val WOOD_BREAK: SoundEvent = get("block.wood.break")
    @JvmField
    public val WOODEN_BUTTON_CLICK_OFF: SoundEvent = get("block.wooden_button.click_off")
    @JvmField
    public val WOODEN_BUTTON_CLICK_ON: SoundEvent = get("block.wooden_button.click_on")
    @JvmField
    public val WOOD_FALL: SoundEvent = get("block.wood.fall")
    @JvmField
    public val WOOD_HIT: SoundEvent = get("block.wood.hit")
    @JvmField
    public val WOOD_PLACE: SoundEvent = get("block.wood.place")
    @JvmField
    public val WOODEN_PRESSURE_PLATE_CLICK_OFF: SoundEvent = get("block.wooden_pressure_plate.click_off")
    @JvmField
    public val WOODEN_PRESSURE_PLATE_CLICK_ON: SoundEvent = get("block.wooden_pressure_plate.click_on")
    @JvmField
    public val WOOD_STEP: SoundEvent = get("block.wood.step")
    @JvmField
    public val WOOL_BREAK: SoundEvent = get("block.wool.break")
    @JvmField
    public val WOOL_FALL: SoundEvent = get("block.wool.fall")
    @JvmField
    public val WOOL_HIT: SoundEvent = get("block.wool.hit")
    @JvmField
    public val WOOL_PLACE: SoundEvent = get("block.wool.place")
    @JvmField
    public val WOOL_STEP: SoundEvent = get("block.wool.step")
    @JvmField
    public val ZOGLIN_AMBIENT: SoundEvent = get("entity.zoglin.ambient")
    @JvmField
    public val ZOGLIN_ANGRY: SoundEvent = get("entity.zoglin.angry")
    @JvmField
    public val ZOGLIN_ATTACK: SoundEvent = get("entity.zoglin.attack")
    @JvmField
    public val ZOGLIN_DEATH: SoundEvent = get("entity.zoglin.death")
    @JvmField
    public val ZOGLIN_HURT: SoundEvent = get("entity.zoglin.hurt")
    @JvmField
    public val ZOGLIN_STEP: SoundEvent = get("entity.zoglin.step")
    @JvmField
    public val ZOMBIE_AMBIENT: SoundEvent = get("entity.zombie.ambient")
    @JvmField
    public val ZOMBIE_ATTACK_WOODEN_DOOR: SoundEvent = get("entity.zombie.attack_wooden_door")
    @JvmField
    public val ZOMBIE_ATTACK_IRON_DOOR: SoundEvent = get("entity.zombie.attack_iron_door")
    @JvmField
    public val ZOMBIE_BREAK_WOODEN_DOOR: SoundEvent = get("entity.zombie.break_wooden_door")
    @JvmField
    public val ZOMBIE_CONVERTED_TO_DROWNED: SoundEvent = get("entity.zombie.converted_to_drowned")
    @JvmField
    public val ZOMBIE_DEATH: SoundEvent = get("entity.zombie.death")
    @JvmField
    public val ZOMBIE_DESTROY_EGG: SoundEvent = get("entity.zombie.destroy_egg")
    @JvmField
    public val ZOMBIE_HORSE_AMBIENT: SoundEvent = get("entity.zombie_horse.ambient")
    @JvmField
    public val ZOMBIE_HORSE_DEATH: SoundEvent = get("entity.zombie_horse.death")
    @JvmField
    public val ZOMBIE_HORSE_HURT: SoundEvent = get("entity.zombie_horse.hurt")
    @JvmField
    public val ZOMBIE_HURT: SoundEvent = get("entity.zombie.hurt")
    @JvmField
    public val ZOMBIE_INFECT: SoundEvent = get("entity.zombie.infect")
    @JvmField
    public val ZOMBIFIED_PIGLIN_AMBIENT: SoundEvent = get("entity.zombified_piglin.ambient")
    @JvmField
    public val ZOMBIFIED_PIGLIN_ANGRY: SoundEvent = get("entity.zombified_piglin.angry")
    @JvmField
    public val ZOMBIFIED_PIGLIN_DEATH: SoundEvent = get("entity.zombified_piglin.death")
    @JvmField
    public val ZOMBIFIED_PIGLIN_HURT: SoundEvent = get("entity.zombified_piglin.hurt")
    @JvmField
    public val ZOMBIE_STEP: SoundEvent = get("entity.zombie.step")
    @JvmField
    public val ZOMBIE_VILLAGER_AMBIENT: SoundEvent = get("entity.zombie_villager.ambient")
    @JvmField
    public val ZOMBIE_VILLAGER_CONVERTED: SoundEvent = get("entity.zombie_villager.converted")
    @JvmField
    public val ZOMBIE_VILLAGER_CURE: SoundEvent = get("entity.zombie_villager.cure")
    @JvmField
    public val ZOMBIE_VILLAGER_DEATH: SoundEvent = get("entity.zombie_villager.death")
    @JvmField
    public val ZOMBIE_VILLAGER_HURT: SoundEvent = get("entity.zombie_villager.hurt")
    @JvmField
    public val ZOMBIE_VILLAGER_STEP: SoundEvent = get("entity.zombie_villager.step")
    @JvmField
    public val GOAT_HORN_0: SoundEvent = get("item.goat_horn.sound.0")
    @JvmField
    public val GOAT_HORN_1: SoundEvent = get("item.goat_horn.sound.1")
    @JvmField
    public val GOAT_HORN_2: SoundEvent = get("item.goat_horn.sound.2")
    @JvmField
    public val GOAT_HORN_3: SoundEvent = get("item.goat_horn.sound.3")
    @JvmField
    public val GOAT_HORN_4: SoundEvent = get("item.goat_horn.sound.4")
    @JvmField
    public val GOAT_HORN_5: SoundEvent = get("item.goat_horn.sound.5")
    @JvmField
    public val GOAT_HORN_6: SoundEvent = get("item.goat_horn.sound.6")
    @JvmField
    public val GOAT_HORN_7: SoundEvent = get("item.goat_horn.sound.7")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): SoundEvent = Registries.SOUND_EVENT.get(Key.key(key))!!
}
