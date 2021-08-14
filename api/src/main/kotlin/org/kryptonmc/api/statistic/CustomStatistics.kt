/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.statistic

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * All of the built-in custom statistics.
 */
object CustomStatistics {

    // @formatter:off
    @JvmField val LEAVE_GAME = register("leave_game", StatisticFormatter.DEFAULT)
    @JvmField val PLAY_TIME = register("play_time", StatisticFormatter.TIME)
    @JvmField val TOTAL_WORLD_TIME = register("total_world_time", StatisticFormatter.TIME)
    @JvmField val TIME_SINCE_DEATH = register("time_since_death", StatisticFormatter.TIME)
    @JvmField val TIME_SINCE_REST = register("time_since_rest", StatisticFormatter.TIME)
    @JvmField val CROUCH_TIME = register("sneak_time", StatisticFormatter.TIME)
    @JvmField val WALK_ONE_CM = register("walk_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val CROUCH_ONE_CM = register("crouch_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val SPRINT_ONE_CM = register("sprint_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val WALK_ON_WATER_ONE_CM = register("walk_on_water_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val FALL_ONE_CM = register("fall_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val CLIMB_ONE_CM = register("climb_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val FLY_ONE_CM = register("fly_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val WALK_UNDER_WATER_ONE_CM = register("walk_under_water_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val MINECART_ONE_CM = register("minecart_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val BOAT_ONE_CM = register("boat_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val PIG_ONE_CM = register("pig_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val HORSE_ONE_CM = register("horse_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val AVIATE_ONE_CM = register("aviate_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val SWIM_ONE_CM = register("swim_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val STRIDER_ONE_CM = register("strider_one_cm", StatisticFormatter.DISTANCE)
    @JvmField val JUMP = register("jump", StatisticFormatter.DEFAULT)
    @JvmField val DROP = register("drop", StatisticFormatter.DEFAULT)
    @JvmField val DAMAGE_DEALT = register("damage_dealt", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DAMAGE_DEALT_ABSORBED = register("damage_dealt_absorbed", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DAMAGE_DEALT_RESISTED = register("damage_dealt_resisted", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DAMAGE_TAKEN = register("damage_taken", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DAMAGE_BLOCKED_BY_SHIELD = register("damage_blocked_by_shield", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DAMAGE_ABSORBED = register("damage_absorbed", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DAMAGE_RESISTED = register("damage_resisted", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField val DEATHS = register("deaths", StatisticFormatter.DEFAULT)
    @JvmField val MOB_KILLS = register("mob_kills", StatisticFormatter.DEFAULT)
    @JvmField val ANIMALS_BRED = register("animals_bred", StatisticFormatter.DEFAULT)
    @JvmField val PLAYER_KILLS = register("player_kills", StatisticFormatter.DEFAULT)
    @JvmField val FISH_CAUGHT = register("fish_caught", StatisticFormatter.DEFAULT)
    @JvmField val TALKED_TO_VILLAGER = register("talked_to_villager", StatisticFormatter.DEFAULT)
    @JvmField val TRADED_WITH_VILLAGER = register("traded_with_villager", StatisticFormatter.DEFAULT)
    @JvmField val EAT_CAKE_SLICE = register("eat_cake_slice", StatisticFormatter.DEFAULT)
    @JvmField val FILL_CAULDRON = register("fill_cauldron", StatisticFormatter.DEFAULT)
    @JvmField val USE_CAULDRON = register("use_cauldron", StatisticFormatter.DEFAULT)
    @JvmField val CLEAN_ARMOR = register("clean_armor", StatisticFormatter.DEFAULT)
    @JvmField val CLEAN_BANNER = register("clean_banner", StatisticFormatter.DEFAULT)
    @JvmField val CLEAN_SHULKER_BOX = register("clean_shulker_box", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_BREWINGSTAND = register("interact_with_brewingstand", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_BEACON = register("interact_with_beacon", StatisticFormatter.DEFAULT)
    @JvmField val INSPECT_DROPPER = register("inspect_dropper", StatisticFormatter.DEFAULT)
    @JvmField val INSPECT_HOPPER = register("inspect_hopper", StatisticFormatter.DEFAULT)
    @JvmField val INSPECT_DISPENSER = register("inspect_dispenser", StatisticFormatter.DEFAULT)
    @JvmField val PLAY_NOTEBLOCK = register("play_noteblock", StatisticFormatter.DEFAULT)
    @JvmField val TUNE_NOTEBLOCK = register("tune_noteblock", StatisticFormatter.DEFAULT)
    @JvmField val POT_FLOWER = register("pot_flower", StatisticFormatter.DEFAULT)
    @JvmField val TRIGGER_TRAPPED_CHEST = register("trigger_trapped_chest", StatisticFormatter.DEFAULT)
    @JvmField val OPEN_ENDERCHEST = register("open_enderchest", StatisticFormatter.DEFAULT)
    @JvmField val ENCHANT_ITEM = register("enchant_item", StatisticFormatter.DEFAULT)
    @JvmField val PLAY_RECORD = register("play_record", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_FURNACE = register("interact_with_furnace", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_CRAFTING_TABLE = register("interact_with_crafting_table", StatisticFormatter.DEFAULT)
    @JvmField val OPEN_CHEST = register("open_chest", StatisticFormatter.DEFAULT)
    @JvmField val SLEEP_IN_BED = register("sleep_in_bed", StatisticFormatter.DEFAULT)
    @JvmField val OPEN_SHULKER_BOX = register("open_shulker_box", StatisticFormatter.DEFAULT)
    @JvmField val OPEN_BARREL = register("open_barrel", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_BLAST_FURNACE = register("interact_with_blast_furnace", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_SMOKER = register("interact_with_smoker", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_LECTERN = register("interact_with_lectern", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_CAMPFIRE = register("interact_with_campfire", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_CARTOGRAPHY_TABLE = register("interact_with_cartography_table", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_LOOM = register("interact_with_loom", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_STONECUTTER = register("interact_with_stonecutter", StatisticFormatter.DEFAULT)
    @JvmField val BELL_RING = register("bell_ring", StatisticFormatter.DEFAULT)
    @JvmField val RAID_TRIGGER = register("raid_trigger", StatisticFormatter.DEFAULT)
    @JvmField val RAID_WIN = register("raid_win", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_ANVIL = register("interact_with_anvil", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_GRINDSTONE = register("interact_with_grindstone", StatisticFormatter.DEFAULT)
    @JvmField val TARGET_HIT = register("target_hit", StatisticFormatter.DEFAULT)
    @JvmField val INTERACT_WITH_SMITHING_TABLE = register("interact_with_smithing_table", StatisticFormatter.DEFAULT)

    // @formatter:on
    private fun register(name: String, formatter: StatisticFormatter): Key {
        val key = Key.key(name)
        Registries.register(Registries.CUSTOM_STATISTIC, key, key)
        StatisticTypes.CUSTOM[key, formatter]
        return key
    }
}
