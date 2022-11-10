/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
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
@Catalogue(Key::class)
public object CustomStatistics {

    // @formatter:off
    @JvmField
    public val LEAVE_GAME: Key = get("leave_game")
    @JvmField
    public val PLAY_TIME: Key = get("play_time")
    @JvmField
    public val TOTAL_WORLD_TIME: Key = get("total_world_time")
    @JvmField
    public val TIME_SINCE_DEATH: Key = get("time_since_death")
    @JvmField
    public val TIME_SINCE_REST: Key = get("time_since_rest")
    @JvmField
    public val CROUCH_TIME: Key = get("sneak_time")
    @JvmField
    public val WALK_ONE_CM: Key = get("walk_one_cm")
    @JvmField
    public val CROUCH_ONE_CM: Key = get("crouch_one_cm")
    @JvmField
    public val SPRINT_ONE_CM: Key = get("sprint_one_cm")
    @JvmField
    public val WALK_ON_WATER_ONE_CM: Key = get("walk_on_water_one_cm")
    @JvmField
    public val FALL_ONE_CM: Key = get("fall_one_cm")
    @JvmField
    public val CLIMB_ONE_CM: Key = get("climb_one_cm")
    @JvmField
    public val FLY_ONE_CM: Key = get("fly_one_cm")
    @JvmField
    public val WALK_UNDER_WATER_ONE_CM: Key = get("walk_under_water_one_cm")
    @JvmField
    public val MINECART_ONE_CM: Key = get("minecart_one_cm")
    @JvmField
    public val BOAT_ONE_CM: Key = get("boat_one_cm")
    @JvmField
    public val PIG_ONE_CM: Key = get("pig_one_cm")
    @JvmField
    public val HORSE_ONE_CM: Key = get("horse_one_cm")
    @JvmField
    public val AVIATE_ONE_CM: Key = get("aviate_one_cm")
    @JvmField
    public val SWIM_ONE_CM: Key = get("swim_one_cm")
    @JvmField
    public val STRIDER_ONE_CM: Key = get("strider_one_cm")
    @JvmField
    public val JUMP: Key = get("jump")
    @JvmField
    public val DROP: Key = get("drop")
    @JvmField
    public val DAMAGE_DEALT: Key = get("damage_dealt")
    @JvmField
    public val DAMAGE_DEALT_ABSORBED: Key = get("damage_dealt_absorbed")
    @JvmField
    public val DAMAGE_DEALT_RESISTED: Key = get("damage_dealt_resisted")
    @JvmField
    public val DAMAGE_TAKEN: Key = get("damage_taken")
    @JvmField
    public val DAMAGE_BLOCKED_BY_SHIELD: Key = get("damage_blocked_by_shield")
    @JvmField
    public val DAMAGE_ABSORBED: Key = get("damage_absorbed")
    @JvmField
    public val DAMAGE_RESISTED: Key = get("damage_resisted")
    @JvmField
    public val DEATHS: Key = get("deaths")
    @JvmField
    public val MOB_KILLS: Key = get("mob_kills")
    @JvmField
    public val ANIMALS_BRED: Key = get("animals_bred")
    @JvmField
    public val PLAYER_KILLS: Key = get("player_kills")
    @JvmField
    public val FISH_CAUGHT: Key = get("fish_caught")
    @JvmField
    public val TALKED_TO_VILLAGER: Key = get("talked_to_villager")
    @JvmField
    public val TRADED_WITH_VILLAGER: Key = get("traded_with_villager")
    @JvmField
    public val EAT_CAKE_SLICE: Key = get("eat_cake_slice")
    @JvmField
    public val FILL_CAULDRON: Key = get("fill_cauldron")
    @JvmField
    public val USE_CAULDRON: Key = get("use_cauldron")
    @JvmField
    public val CLEAN_ARMOR: Key = get("clean_armor")
    @JvmField
    public val CLEAN_BANNER: Key = get("clean_banner")
    @JvmField
    public val CLEAN_SHULKER_BOX: Key = get("clean_shulker_box")
    @JvmField
    public val INTERACT_WITH_BREWINGSTAND: Key = get("interact_with_brewingstand")
    @JvmField
    public val INTERACT_WITH_BEACON: Key = get("interact_with_beacon")
    @JvmField
    public val INSPECT_DROPPER: Key = get("inspect_dropper")
    @JvmField
    public val INSPECT_HOPPER: Key = get("inspect_hopper")
    @JvmField
    public val INSPECT_DISPENSER: Key = get("inspect_dispenser")
    @JvmField
    public val PLAY_NOTEBLOCK: Key = get("play_noteblock")
    @JvmField
    public val TUNE_NOTEBLOCK: Key = get("tune_noteblock")
    @JvmField
    public val POT_FLOWER: Key = get("pot_flower")
    @JvmField
    public val TRIGGER_TRAPPED_CHEST: Key = get("trigger_trapped_chest")
    @JvmField
    public val OPEN_ENDERCHEST: Key = get("open_enderchest")
    @JvmField
    public val ENCHANT_ITEM: Key = get("enchant_item")
    @JvmField
    public val PLAY_RECORD: Key = get("play_record")
    @JvmField
    public val INTERACT_WITH_FURNACE: Key = get("interact_with_furnace")
    @JvmField
    public val INTERACT_WITH_CRAFTING_TABLE: Key = get("interact_with_crafting_table")
    @JvmField
    public val OPEN_CHEST: Key = get("open_chest")
    @JvmField
    public val SLEEP_IN_BED: Key = get("sleep_in_bed")
    @JvmField
    public val OPEN_SHULKER_BOX: Key = get("open_shulker_box")
    @JvmField
    public val OPEN_BARREL: Key = get("open_barrel")
    @JvmField
    public val INTERACT_WITH_BLAST_FURNACE: Key = get("interact_with_blast_furnace")
    @JvmField
    public val INTERACT_WITH_SMOKER: Key = get("interact_with_smoker")
    @JvmField
    public val INTERACT_WITH_LECTERN: Key = get("interact_with_lectern")
    @JvmField
    public val INTERACT_WITH_CAMPFIRE: Key = get("interact_with_campfire")
    @JvmField
    public val INTERACT_WITH_CARTOGRAPHY_TABLE: Key = get("interact_with_cartography_table")
    @JvmField
    public val INTERACT_WITH_LOOM: Key = get("interact_with_loom")
    @JvmField
    public val INTERACT_WITH_STONECUTTER: Key = get("interact_with_stonecutter")
    @JvmField
    public val BELL_RING: Key = get("bell_ring")
    @JvmField
    public val RAID_TRIGGER: Key = get("raid_trigger")
    @JvmField
    public val RAID_WIN: Key = get("raid_win")
    @JvmField
    public val INTERACT_WITH_ANVIL: Key = get("interact_with_anvil")
    @JvmField
    public val INTERACT_WITH_GRINDSTONE: Key = get("interact_with_grindstone")
    @JvmField
    public val TARGET_HIT: Key = get("target_hit")
    @JvmField
    public val INTERACT_WITH_SMITHING_TABLE: Key = get("interact_with_smithing_table")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Key = Registries.CUSTOM_STATISTIC.get(Key.key(key))!!
}
