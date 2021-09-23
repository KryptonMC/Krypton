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
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in custom statistics.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(Key::class)
public object CustomStatistics {

    // @formatter:off
    @JvmField public val LEAVE_GAME: Key = register("leave_game", StatisticFormatter.DEFAULT)
    @JvmField public val PLAY_TIME: Key = register("play_time", StatisticFormatter.TIME)
    @JvmField public val TOTAL_WORLD_TIME: Key = register("total_world_time", StatisticFormatter.TIME)
    @JvmField public val TIME_SINCE_DEATH: Key = register("time_since_death", StatisticFormatter.TIME)
    @JvmField public val TIME_SINCE_REST: Key = register("time_since_rest", StatisticFormatter.TIME)
    @JvmField public val CROUCH_TIME: Key = register("sneak_time", StatisticFormatter.TIME)
    @JvmField public val WALK_ONE_CM: Key = register("walk_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val CROUCH_ONE_CM: Key = register("crouch_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val SPRINT_ONE_CM: Key = register("sprint_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val WALK_ON_WATER_ONE_CM: Key = register("walk_on_water_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val FALL_ONE_CM: Key = register("fall_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val CLIMB_ONE_CM: Key = register("climb_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val FLY_ONE_CM: Key = register("fly_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val WALK_UNDER_WATER_ONE_CM: Key =
        register("walk_under_water_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val MINECART_ONE_CM: Key = register("minecart_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val BOAT_ONE_CM: Key = register("boat_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val PIG_ONE_CM: Key = register("pig_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val HORSE_ONE_CM: Key = register("horse_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val AVIATE_ONE_CM: Key = register("aviate_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val SWIM_ONE_CM: Key = register("swim_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val STRIDER_ONE_CM: Key = register("strider_one_cm", StatisticFormatter.DISTANCE)
    @JvmField public val JUMP: Key = register("jump", StatisticFormatter.DEFAULT)
    @JvmField public val DROP: Key = register("drop", StatisticFormatter.DEFAULT)
    @JvmField public val DAMAGE_DEALT: Key = register("damage_dealt", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DAMAGE_DEALT_ABSORBED: Key =
        register("damage_dealt_absorbed", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DAMAGE_DEALT_RESISTED: Key =
        register("damage_dealt_resisted", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DAMAGE_TAKEN: Key = register("damage_taken", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DAMAGE_BLOCKED_BY_SHIELD: Key =
        register("damage_blocked_by_shield", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DAMAGE_ABSORBED: Key = register("damage_absorbed", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DAMAGE_RESISTED: Key = register("damage_resisted", StatisticFormatter.DIVIDE_BY_TEN)
    @JvmField public val DEATHS: Key = register("deaths", StatisticFormatter.DEFAULT)
    @JvmField public val MOB_KILLS: Key = register("mob_kills", StatisticFormatter.DEFAULT)
    @JvmField public val ANIMALS_BRED: Key = register("animals_bred", StatisticFormatter.DEFAULT)
    @JvmField public val PLAYER_KILLS: Key = register("player_kills", StatisticFormatter.DEFAULT)
    @JvmField public val FISH_CAUGHT: Key = register("fish_caught", StatisticFormatter.DEFAULT)
    @JvmField public val TALKED_TO_VILLAGER: Key = register("talked_to_villager", StatisticFormatter.DEFAULT)
    @JvmField public val TRADED_WITH_VILLAGER: Key = register("traded_with_villager", StatisticFormatter.DEFAULT)
    @JvmField public val EAT_CAKE_SLICE: Key = register("eat_cake_slice", StatisticFormatter.DEFAULT)
    @JvmField public val FILL_CAULDRON: Key = register("fill_cauldron", StatisticFormatter.DEFAULT)
    @JvmField public val USE_CAULDRON: Key = register("use_cauldron", StatisticFormatter.DEFAULT)
    @JvmField public val CLEAN_ARMOR: Key = register("clean_armor", StatisticFormatter.DEFAULT)
    @JvmField public val CLEAN_BANNER: Key = register("clean_banner", StatisticFormatter.DEFAULT)
    @JvmField public val CLEAN_SHULKER_BOX: Key = register("clean_shulker_box", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_BREWINGSTAND: Key =
        register("interact_with_brewingstand", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_BEACON: Key = register("interact_with_beacon", StatisticFormatter.DEFAULT)
    @JvmField public val INSPECT_DROPPER: Key = register("inspect_dropper", StatisticFormatter.DEFAULT)
    @JvmField public val INSPECT_HOPPER: Key = register("inspect_hopper", StatisticFormatter.DEFAULT)
    @JvmField public val INSPECT_DISPENSER: Key = register("inspect_dispenser", StatisticFormatter.DEFAULT)
    @JvmField public val PLAY_NOTEBLOCK: Key = register("play_noteblock", StatisticFormatter.DEFAULT)
    @JvmField public val TUNE_NOTEBLOCK: Key = register("tune_noteblock", StatisticFormatter.DEFAULT)
    @JvmField public val POT_FLOWER: Key = register("pot_flower", StatisticFormatter.DEFAULT)
    @JvmField public val TRIGGER_TRAPPED_CHEST: Key =
        register("trigger_trapped_chest", StatisticFormatter.DEFAULT)
    @JvmField public val OPEN_ENDERCHEST: Key = register("open_enderchest", StatisticFormatter.DEFAULT)
    @JvmField public val ENCHANT_ITEM: Key = register("enchant_item", StatisticFormatter.DEFAULT)
    @JvmField public val PLAY_RECORD: Key = register("play_record", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_FURNACE: Key =
        register("interact_with_furnace", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_CRAFTING_TABLE: Key =
        register("interact_with_crafting_table", StatisticFormatter.DEFAULT)
    @JvmField public val OPEN_CHEST: Key = register("open_chest", StatisticFormatter.DEFAULT)
    @JvmField public val SLEEP_IN_BED: Key = register("sleep_in_bed", StatisticFormatter.DEFAULT)
    @JvmField public val OPEN_SHULKER_BOX: Key = register("open_shulker_box", StatisticFormatter.DEFAULT)
    @JvmField public val OPEN_BARREL: Key = register("open_barrel", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_BLAST_FURNACE: Key =
        register("interact_with_blast_furnace", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_SMOKER: Key = register("interact_with_smoker", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_LECTERN: Key =
        register("interact_with_lectern", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_CAMPFIRE: Key =
        register("interact_with_campfire", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_CARTOGRAPHY_TABLE: Key =
        register("interact_with_cartography_table", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_LOOM: Key = register("interact_with_loom", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_STONECUTTER: Key =
        register("interact_with_stonecutter", StatisticFormatter.DEFAULT)
    @JvmField public val BELL_RING: Key = register("bell_ring", StatisticFormatter.DEFAULT)
    @JvmField public val RAID_TRIGGER: Key = register("raid_trigger", StatisticFormatter.DEFAULT)
    @JvmField public val RAID_WIN: Key = register("raid_win", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_ANVIL: Key = register("interact_with_anvil", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_GRINDSTONE: Key =
        register("interact_with_grindstone", StatisticFormatter.DEFAULT)
    @JvmField public val TARGET_HIT: Key = register("target_hit", StatisticFormatter.DEFAULT)
    @JvmField public val INTERACT_WITH_SMITHING_TABLE: Key =
        register("interact_with_smithing_table", StatisticFormatter.DEFAULT)

    // @formatter:on
    private fun register(name: String, formatter: StatisticFormatter): Key {
        val key = Key.key(name)
        Registries.register(Registries.CUSTOM_STATISTIC, key, key)
        StatisticTypes.CUSTOM[key, formatter]
        return key
    }
}
