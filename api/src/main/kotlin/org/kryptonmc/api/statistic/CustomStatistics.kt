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
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.internal.annotations.IgnoreNotCataloguedBy

/**
 * All of the built-in custom statistics.
 */
@Catalogue(Key::class)
@IgnoreNotCataloguedBy
public object CustomStatistics {

    // @formatter:off
    @JvmField
    public val LEAVE_GAME: RegistryReference<Key> = of("leave_game")
    @JvmField
    public val PLAY_TIME: RegistryReference<Key> = of("play_time")
    @JvmField
    public val TOTAL_WORLD_TIME: RegistryReference<Key> = of("total_world_time")
    @JvmField
    public val TIME_SINCE_DEATH: RegistryReference<Key> = of("time_since_death")
    @JvmField
    public val TIME_SINCE_REST: RegistryReference<Key> = of("time_since_rest")
    @JvmField
    public val CROUCH_TIME: RegistryReference<Key> = of("sneak_time")
    @JvmField
    public val WALK_ONE_CM: RegistryReference<Key> = of("walk_one_cm")
    @JvmField
    public val CROUCH_ONE_CM: RegistryReference<Key> = of("crouch_one_cm")
    @JvmField
    public val SPRINT_ONE_CM: RegistryReference<Key> = of("sprint_one_cm")
    @JvmField
    public val WALK_ON_WATER_ONE_CM: RegistryReference<Key> = of("walk_on_water_one_cm")
    @JvmField
    public val FALL_ONE_CM: RegistryReference<Key> = of("fall_one_cm")
    @JvmField
    public val CLIMB_ONE_CM: RegistryReference<Key> = of("climb_one_cm")
    @JvmField
    public val FLY_ONE_CM: RegistryReference<Key> = of("fly_one_cm")
    @JvmField
    public val WALK_UNDER_WATER_ONE_CM: RegistryReference<Key> = of("walk_under_water_one_cm")
    @JvmField
    public val MINECART_ONE_CM: RegistryReference<Key> = of("minecart_one_cm")
    @JvmField
    public val BOAT_ONE_CM: RegistryReference<Key> = of("boat_one_cm")
    @JvmField
    public val PIG_ONE_CM: RegistryReference<Key> = of("pig_one_cm")
    @JvmField
    public val HORSE_ONE_CM: RegistryReference<Key> = of("horse_one_cm")
    @JvmField
    public val AVIATE_ONE_CM: RegistryReference<Key> = of("aviate_one_cm")
    @JvmField
    public val SWIM_ONE_CM: RegistryReference<Key> = of("swim_one_cm")
    @JvmField
    public val STRIDER_ONE_CM: RegistryReference<Key> = of("strider_one_cm")
    @JvmField
    public val JUMP: RegistryReference<Key> = of("jump")
    @JvmField
    public val DROP: RegistryReference<Key> = of("drop")
    @JvmField
    public val DAMAGE_DEALT: RegistryReference<Key> = of("damage_dealt")
    @JvmField
    public val DAMAGE_DEALT_ABSORBED: RegistryReference<Key> = of("damage_dealt_absorbed")
    @JvmField
    public val DAMAGE_DEALT_RESISTED: RegistryReference<Key> = of("damage_dealt_resisted")
    @JvmField
    public val DAMAGE_TAKEN: RegistryReference<Key> = of("damage_taken")
    @JvmField
    public val DAMAGE_BLOCKED_BY_SHIELD: RegistryReference<Key> = of("damage_blocked_by_shield")
    @JvmField
    public val DAMAGE_ABSORBED: RegistryReference<Key> = of("damage_absorbed")
    @JvmField
    public val DAMAGE_RESISTED: RegistryReference<Key> = of("damage_resisted")
    @JvmField
    public val DEATHS: RegistryReference<Key> = of("deaths")
    @JvmField
    public val MOB_KILLS: RegistryReference<Key> = of("mob_kills")
    @JvmField
    public val ANIMALS_BRED: RegistryReference<Key> = of("animals_bred")
    @JvmField
    public val PLAYER_KILLS: RegistryReference<Key> = of("player_kills")
    @JvmField
    public val FISH_CAUGHT: RegistryReference<Key> = of("fish_caught")
    @JvmField
    public val TALKED_TO_VILLAGER: RegistryReference<Key> = of("talked_to_villager")
    @JvmField
    public val TRADED_WITH_VILLAGER: RegistryReference<Key> = of("traded_with_villager")
    @JvmField
    public val EAT_CAKE_SLICE: RegistryReference<Key> = of("eat_cake_slice")
    @JvmField
    public val FILL_CAULDRON: RegistryReference<Key> = of("fill_cauldron")
    @JvmField
    public val USE_CAULDRON: RegistryReference<Key> = of("use_cauldron")
    @JvmField
    public val CLEAN_ARMOR: RegistryReference<Key> = of("clean_armor")
    @JvmField
    public val CLEAN_BANNER: RegistryReference<Key> = of("clean_banner")
    @JvmField
    public val CLEAN_SHULKER_BOX: RegistryReference<Key> = of("clean_shulker_box")
    @JvmField
    public val INTERACT_WITH_BREWINGSTAND: RegistryReference<Key> = of("interact_with_brewingstand")
    @JvmField
    public val INTERACT_WITH_BEACON: RegistryReference<Key> = of("interact_with_beacon")
    @JvmField
    public val INSPECT_DROPPER: RegistryReference<Key> = of("inspect_dropper")
    @JvmField
    public val INSPECT_HOPPER: RegistryReference<Key> = of("inspect_hopper")
    @JvmField
    public val INSPECT_DISPENSER: RegistryReference<Key> = of("inspect_dispenser")
    @JvmField
    public val PLAY_NOTEBLOCK: RegistryReference<Key> = of("play_noteblock")
    @JvmField
    public val TUNE_NOTEBLOCK: RegistryReference<Key> = of("tune_noteblock")
    @JvmField
    public val POT_FLOWER: RegistryReference<Key> = of("pot_flower")
    @JvmField
    public val TRIGGER_TRAPPED_CHEST: RegistryReference<Key> = of("trigger_trapped_chest")
    @JvmField
    public val OPEN_ENDERCHEST: RegistryReference<Key> = of("open_enderchest")
    @JvmField
    public val ENCHANT_ITEM: RegistryReference<Key> = of("enchant_item")
    @JvmField
    public val PLAY_RECORD: RegistryReference<Key> = of("play_record")
    @JvmField
    public val INTERACT_WITH_FURNACE: RegistryReference<Key> = of("interact_with_furnace")
    @JvmField
    public val INTERACT_WITH_CRAFTING_TABLE: RegistryReference<Key> = of("interact_with_crafting_table")
    @JvmField
    public val OPEN_CHEST: RegistryReference<Key> = of("open_chest")
    @JvmField
    public val SLEEP_IN_BED: RegistryReference<Key> = of("sleep_in_bed")
    @JvmField
    public val OPEN_SHULKER_BOX: RegistryReference<Key> = of("open_shulker_box")
    @JvmField
    public val OPEN_BARREL: RegistryReference<Key> = of("open_barrel")
    @JvmField
    public val INTERACT_WITH_BLAST_FURNACE: RegistryReference<Key> = of("interact_with_blast_furnace")
    @JvmField
    public val INTERACT_WITH_SMOKER: RegistryReference<Key> = of("interact_with_smoker")
    @JvmField
    public val INTERACT_WITH_LECTERN: RegistryReference<Key> = of("interact_with_lectern")
    @JvmField
    public val INTERACT_WITH_CAMPFIRE: RegistryReference<Key> = of("interact_with_campfire")
    @JvmField
    public val INTERACT_WITH_CARTOGRAPHY_TABLE: RegistryReference<Key> = of("interact_with_cartography_table")
    @JvmField
    public val INTERACT_WITH_LOOM: RegistryReference<Key> = of("interact_with_loom")
    @JvmField
    public val INTERACT_WITH_STONECUTTER: RegistryReference<Key> = of("interact_with_stonecutter")
    @JvmField
    public val BELL_RING: RegistryReference<Key> = of("bell_ring")
    @JvmField
    public val RAID_TRIGGER: RegistryReference<Key> = of("raid_trigger")
    @JvmField
    public val RAID_WIN: RegistryReference<Key> = of("raid_win")
    @JvmField
    public val INTERACT_WITH_ANVIL: RegistryReference<Key> = of("interact_with_anvil")
    @JvmField
    public val INTERACT_WITH_GRINDSTONE: RegistryReference<Key> = of("interact_with_grindstone")
    @JvmField
    public val TARGET_HIT: RegistryReference<Key> = of("target_hit")
    @JvmField
    public val INTERACT_WITH_SMITHING_TABLE: RegistryReference<Key> = of("interact_with_smithing_table")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<Key> = RegistryReference.of(Registries.CUSTOM_STATISTIC, Key.key(name))
}
