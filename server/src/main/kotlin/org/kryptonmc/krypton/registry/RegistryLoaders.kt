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
package org.kryptonmc.krypton.registry

import com.google.common.collect.ImmutableSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.criteria.KeyedCriterion
import org.kryptonmc.api.statistic.StatisticFormatter
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.krypton.adventure.KryptonAdventure
import org.kryptonmc.krypton.effect.particle.KryptonBlockParticleType
import org.kryptonmc.krypton.effect.particle.KryptonColorParticleType
import org.kryptonmc.krypton.effect.particle.KryptonDirectionalParticleType
import org.kryptonmc.krypton.effect.particle.KryptonDustParticleType
import org.kryptonmc.krypton.effect.particle.KryptonDustTransitionParticleType
import org.kryptonmc.krypton.effect.particle.KryptonItemParticleType
import org.kryptonmc.krypton.effect.particle.KryptonNoteParticleType
import org.kryptonmc.krypton.effect.particle.KryptonSimpleParticleType
import org.kryptonmc.krypton.effect.particle.KryptonVibrationParticleType
import org.kryptonmc.krypton.entity.KryptonEntityCategory
import org.kryptonmc.krypton.entity.hanging.KryptonPaintingVariant
import org.kryptonmc.krypton.inventory.KryptonInventoryType
import org.kryptonmc.krypton.item.KryptonItemRarity
import org.kryptonmc.krypton.statistic.KryptonStatisticType
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntityType
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPatternType
import org.kryptonmc.krypton.world.damage.type.KryptonDamageType
import org.kryptonmc.krypton.world.rule.KryptonGameRule
import org.kryptonmc.krypton.world.scoreboard.KryptonKeyedCriterion

/**
 * Contains all the built-in registry loaders for Krypton.
 */
@Suppress("StringLiteralDuplication")
object RegistryLoaders {

    @JvmStatic
    fun bannerPatternType(): RegistryLoader<BannerPatternType> = loader {
        add(Key.key("base")) { KryptonBannerPatternType(it, "b") }
        add(Key.key("square_bottom_left")) { KryptonBannerPatternType(it, "bl") }
        add(Key.key("square_bottom_right")) { KryptonBannerPatternType(it, "br") }
        add(Key.key("square_top_left")) { KryptonBannerPatternType(it, "tl") }
        add(Key.key("square_top_right")) { KryptonBannerPatternType(it, "tr") }
        add(Key.key("stripe_bottom")) { KryptonBannerPatternType(it, "bs") }
        add(Key.key("stripe_top")) { KryptonBannerPatternType(it, "ts") }
        add(Key.key("stripe_left")) { KryptonBannerPatternType(it, "ls") }
        add(Key.key("stripe_right")) { KryptonBannerPatternType(it, "rs") }
        add(Key.key("stripe_center")) { KryptonBannerPatternType(it, "cs") }
        add(Key.key("stripe_middle")) { KryptonBannerPatternType(it, "ms") }
        add(Key.key("stripe_downright")) { KryptonBannerPatternType(it, "drs") }
        add(Key.key("stripe_downleft")) { KryptonBannerPatternType(it, "dls") }
        add(Key.key("stripe_small")) { KryptonBannerPatternType(it, "ss") }
        add(Key.key("cross")) { KryptonBannerPatternType(it, "cr") }
        add(Key.key("straight_cross")) { KryptonBannerPatternType(it, "sc") }
        add(Key.key("triangle_bottom")) { KryptonBannerPatternType(it, "bt") }
        add(Key.key("triangle_top")) { KryptonBannerPatternType(it, "tt") }
        add(Key.key("triangles_bottom")) { KryptonBannerPatternType(it, "bts") }
        add(Key.key("triangles_top")) { KryptonBannerPatternType(it, "tts") }
        add(Key.key("diagonal_left")) { KryptonBannerPatternType(it, "ld") }
        add(Key.key("diagonal_right")) { KryptonBannerPatternType(it, "rd") }
        add(Key.key("diagonal_left_mirror")) { KryptonBannerPatternType(it, "lud") }
        add(Key.key("diagonal_right_mirror")) { KryptonBannerPatternType(it, "rud") }
        add(Key.key("circle_middle")) { KryptonBannerPatternType(it, "mc") }
        add(Key.key("rhombus_middle")) { KryptonBannerPatternType(it, "mr") }
        add(Key.key("half_vertical")) { KryptonBannerPatternType(it, "vh") }
        add(Key.key("half_horizontal")) { KryptonBannerPatternType(it, "hh") }
        add(Key.key("half_vertical_mirror")) { KryptonBannerPatternType(it, "vhr") }
        add(Key.key("half_horizontal_mirror")) { KryptonBannerPatternType(it, "hhb") }
        add(Key.key("border")) { KryptonBannerPatternType(it, "bo") }
        add(Key.key("curly_border")) { KryptonBannerPatternType(it, "cbo") }
        add(Key.key("gradient")) { KryptonBannerPatternType(it, "gra") }
        add(Key.key("gradient_up")) { KryptonBannerPatternType(it, "gru") }
        add(Key.key("bricks")) { KryptonBannerPatternType(it, "bri") }
        add(Key.key("globe")) { KryptonBannerPatternType(it, "glb") }
        add(Key.key("creeper")) { KryptonBannerPatternType(it, "cre") }
        add(Key.key("skull")) { KryptonBannerPatternType(it, "sku") }
        add(Key.key("flower")) { KryptonBannerPatternType(it, "flo") }
        add(Key.key("mojang")) { KryptonBannerPatternType(it, "moj") }
        add(Key.key("piglin")) { KryptonBannerPatternType(it, "pig") }
    }

    @JvmStatic
    fun blockEntityType(): RegistryLoader<BlockEntityType> = loader {
        add("furnace", Blocks.FURNACE)
        add("chest", Blocks.CHEST)
        add("trapped_chest", Blocks.TRAPPED_CHEST)
        add("ender_chest", Blocks.ENDER_CHEST)
        add("jukebox", Blocks.JUKEBOX)
        add("dispenser", Blocks.DISPENSER)
        add("dropper", Blocks.DROPPER)
        add("sign", Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN,
            Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN,
            Blocks.DARK_OAK_WALL_SIGN, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN,
            Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN)
        add("mob_spawner", Blocks.SPAWNER)
        add("piston", Blocks.MOVING_PISTON)
        add("brewing_stand", Blocks.BREWING_STAND)
        add("enchanting_table", Blocks.ENCHANTING_TABLE)
        add("end_portal", Blocks.END_PORTAL)
        add("beacon", Blocks.BEACON)
        add("skull", Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_HEAD,
            Blocks.DRAGON_WALL_HEAD, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL,
            Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD)
        add("daylight_detector", Blocks.DAYLIGHT_DETECTOR)
        add("hopper", Blocks.HOPPER)
        add("comparator", Blocks.COMPARATOR)
        add("banner", Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER,
            Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER,
            Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER, Blocks.WHITE_WALL_BANNER,
            Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER,
            Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER,
            Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER,
            Blocks.BLACK_WALL_BANNER)
        add("structure_block", Blocks.STRUCTURE_BLOCK)
        add("end_gateway", Blocks.END_GATEWAY)
        add("command_block", Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK)
        add("shulker_box", Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX)
        add("bed", Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED,
            Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, Blocks.PINK_BED,
            Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED)
        add("conduit", Blocks.CONDUIT)
        add("barrel", Blocks.BARREL)
        add("smoker", Blocks.SMOKER)
        add("blast_furnace", Blocks.BLAST_FURNACE)
        add("lectern", Blocks.LECTERN)
        add("bell", Blocks.BELL)
        add("jigsaw", Blocks.JIGSAW)
        add("campfire", Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE)
        add("beehive", Blocks.BEE_NEST, Blocks.BEEHIVE)
        add("sculk_sensor", Blocks.SCULK_SENSOR)
        add("sculk_catalyst", Blocks.SCULK_CATALYST)
        add("sculk_shrieker", Blocks.SCULK_SHRIEKER)
    }

    @JvmStatic
    fun criterion(): RegistryLoader<KeyedCriterion> = loader {
        add(Key.key("dummy")) { KryptonKeyedCriterion(it, "dummy", false, ObjectiveRenderType.INTEGER) }
        add(Key.key("trigger")) { KryptonKeyedCriterion(it, "trigger", false, ObjectiveRenderType.INTEGER) }
        add(Key.key("death_count")) { KryptonKeyedCriterion(it, "deathCount", false, ObjectiveRenderType.INTEGER) }
        add(Key.key("player_kill_count")) { KryptonKeyedCriterion(it, "playerKillCount", false, ObjectiveRenderType.INTEGER) }
        add(Key.key("total_kill_count")) { KryptonKeyedCriterion(it, "totalKillCount", false, ObjectiveRenderType.INTEGER) }
        add(Key.key("health")) { KryptonKeyedCriterion(it, "health", true, ObjectiveRenderType.HEARTS) }
        add(Key.key("food")) { KryptonKeyedCriterion(it, "food", true, ObjectiveRenderType.INTEGER) }
        add(Key.key("air")) { KryptonKeyedCriterion(it, "air", true, ObjectiveRenderType.INTEGER) }
        add(Key.key("armor")) { KryptonKeyedCriterion(it, "armor", true, ObjectiveRenderType.INTEGER) }
        add(Key.key("xp")) { KryptonKeyedCriterion(it, "xp", true, ObjectiveRenderType.INTEGER) }
        add(Key.key("level")) { KryptonKeyedCriterion(it, "level", true, ObjectiveRenderType.INTEGER) }
        KryptonAdventure.colors().forEach { color ->
            val name = NamedTextColor.NAMES.key(color)
            add(Key.key("team_kill_$name")) { KryptonKeyedCriterion(it, "teamkill.$name", false, ObjectiveRenderType.INTEGER) }
            add(Key.key("killed_by_team_$name")) { KryptonKeyedCriterion(it, "killedByTeam.$name", false, ObjectiveRenderType.INTEGER) }
        }
    }

    @JvmStatic
    fun customStatistic(): RegistryLoader<Key> = loader {
        add("leave_game", StatisticFormatter.DEFAULT)
        add("play_time", StatisticFormatter.TIME)
        add("total_world_time", StatisticFormatter.TIME)
        add("time_since_death", StatisticFormatter.TIME)
        add("time_since_rest", StatisticFormatter.TIME)
        add("sneak_time", StatisticFormatter.TIME)
        add("walk_one_cm", StatisticFormatter.DISTANCE)
        add("crouch_one_cm", StatisticFormatter.DISTANCE)
        add("sprint_one_cm", StatisticFormatter.DISTANCE)
        add("walk_on_water_one_cm", StatisticFormatter.DISTANCE)
        add("fall_one_cm", StatisticFormatter.DISTANCE)
        add("climb_one_cm", StatisticFormatter.DISTANCE)
        add("fly_one_cm", StatisticFormatter.DISTANCE)
        add("walk_under_water_one_cm", StatisticFormatter.DISTANCE)
        add("minecart_one_cm", StatisticFormatter.DISTANCE)
        add("boat_one_cm", StatisticFormatter.DISTANCE)
        add("pig_one_cm", StatisticFormatter.DISTANCE)
        add("horse_one_cm", StatisticFormatter.DISTANCE)
        add("aviate_one_cm", StatisticFormatter.DISTANCE)
        add("swim_one_cm", StatisticFormatter.DISTANCE)
        add("strider_one_cm", StatisticFormatter.DISTANCE)
        add("jump", StatisticFormatter.DEFAULT)
        add("drop", StatisticFormatter.DEFAULT)
        add("damage_dealt", StatisticFormatter.DIVIDE_BY_TEN)
        add("damage_dealt_absorbed", StatisticFormatter.DIVIDE_BY_TEN)
        add("damage_dealt_resisted", StatisticFormatter.DIVIDE_BY_TEN)
        add("damage_taken", StatisticFormatter.DIVIDE_BY_TEN)
        add("damage_blocked_by_shield", StatisticFormatter.DIVIDE_BY_TEN)
        add("damage_absorbed", StatisticFormatter.DIVIDE_BY_TEN)
        add("damage_resisted", StatisticFormatter.DIVIDE_BY_TEN)
        add("deaths", StatisticFormatter.DEFAULT)
        add("mob_kills", StatisticFormatter.DEFAULT)
        add("animals_bred", StatisticFormatter.DEFAULT)
        add("player_kills", StatisticFormatter.DEFAULT)
        add("fish_caught", StatisticFormatter.DEFAULT)
        add("talked_to_villager", StatisticFormatter.DEFAULT)
        add("traded_with_villager", StatisticFormatter.DEFAULT)
        add("eat_cake_slice", StatisticFormatter.DEFAULT)
        add("fill_cauldron", StatisticFormatter.DEFAULT)
        add("use_cauldron", StatisticFormatter.DEFAULT)
        add("clean_armor", StatisticFormatter.DEFAULT)
        add("clean_banner", StatisticFormatter.DEFAULT)
        add("clean_shulker_box", StatisticFormatter.DEFAULT)
        add("interact_with_brewingstand", StatisticFormatter.DEFAULT)
        add("interact_with_beacon", StatisticFormatter.DEFAULT)
        add("inspect_dropper", StatisticFormatter.DEFAULT)
        add("inspect_hopper", StatisticFormatter.DEFAULT)
        add("inspect_dispenser", StatisticFormatter.DEFAULT)
        add("play_noteblock", StatisticFormatter.DEFAULT)
        add("tune_noteblock", StatisticFormatter.DEFAULT)
        add("pot_flower", StatisticFormatter.DEFAULT)
        add("trigger_trapped_chest", StatisticFormatter.DEFAULT)
        add("open_enderchest", StatisticFormatter.DEFAULT)
        add("enchant_item", StatisticFormatter.DEFAULT)
        add("play_record", StatisticFormatter.DEFAULT)
        add("interact_with_furnace", StatisticFormatter.DEFAULT)
        add("interact_with_crafting_table", StatisticFormatter.DEFAULT)
        add("open_chest", StatisticFormatter.DEFAULT)
        add("sleep_in_bed", StatisticFormatter.DEFAULT)
        add("open_shulker_box", StatisticFormatter.DEFAULT)
        add("open_barrel", StatisticFormatter.DEFAULT)
        add("interact_with_blast_furnace", StatisticFormatter.DEFAULT)
        add("interact_with_smoker", StatisticFormatter.DEFAULT)
        add("interact_with_lectern", StatisticFormatter.DEFAULT)
        add("interact_with_campfire", StatisticFormatter.DEFAULT)
        add("interact_with_cartography_table", StatisticFormatter.DEFAULT)
        add("interact_with_loom", StatisticFormatter.DEFAULT)
        add("interact_with_stonecutter", StatisticFormatter.DEFAULT)
        add("bell_ring", StatisticFormatter.DEFAULT)
        add("raid_trigger", StatisticFormatter.DEFAULT)
        add("raid_win", StatisticFormatter.DEFAULT)
        add("interact_with_anvil", StatisticFormatter.DEFAULT)
        add("interact_with_grindstone", StatisticFormatter.DEFAULT)
        add("target_hit", StatisticFormatter.DEFAULT)
        add("interact_with_smithing_table", StatisticFormatter.DEFAULT)
    }

    @JvmStatic
    fun damageType(): RegistryLoader<DamageType> = loader {
        put("in_fire", "inFire") { bypassArmor().fire() }
        put("lightning_bolt", "lightningBolt")
        put("on_fire", "onFire") { bypassArmor().fire() }
        put("lava", "lava") { fire() }
        put("hot_floor", "hotFloor") { fire() }
        put("suffocation", "inWall") { bypassArmor() }
        put("cramming", "cramming") { bypassArmor() }
        put("drowning", "drown") { bypassArmor() }
        put("starving", "starve") { bypassArmor().bypassMagic() }
        put("cactus", "cactus")
        put("falling", "fall") { bypassArmor().fall() }
        put("fly_into_wall", "flyIntoWall") { bypassArmor() }
        put("void", "outOfWorld") { bypassArmor().bypassInvulnerability() }
        put("generic", "generic") { bypassArmor() }
        put("magic", "magic") { bypassArmor().magic() }
        put("wither", "wither") { bypassArmor() }
        put("anvil", "anvil") { damagesHelmet() }
        put("falling_block", "fallingBlock") { damagesHelmet() }
        put("dragon_breath", "dragonBreath") { bypassArmor() }
        put("dry_out", "dryout")
        put("sweet_berry_bush", "sweetBerryBush")
        put("freezing", "freeze") { bypassArmor() }
        put("falling_stalactite", "fallingStalactite") { damagesHelmet() }
        put("stalagmite", "stalagmite") { fall() }
        put("sting", "sting")
        put("generic_mob_attack", "mob")
        put("passive_mob_attack", "mob") { noAggravatesTarget() }
        put("indirect_mob_attack", "mob") { projectile() }
        put("player_attack", "player")
        put("arrow", "arrow") { projectile() }
        put("trident", "trident") { projectile() }
        put("fireworks", "fireworks") { explosion() }
        put("fireball", "fireball") { fire().projectile() }
        put("fireball_on_fire", "onFire") { fire().projectile() }
        put("wither_skull", "witherSkull") { projectile() }
        put("thrown_projectile", "thrown") { projectile() }
        put("indirect_magic", "indirectMagic") { bypassArmor().magic() }
        put("thorns", "thorns") { thorns().magic() }
        put("explosion", "explosion") { scalesWithDifficulty().explosion() }
        put("player_explosion", "explosion.player") { scalesWithDifficulty().explosion() }
        put("bad_respawn_point", "badRespawnPoint") { scalesWithDifficulty().explosion() }
    }

    @JvmStatic
    fun entityCategory(): RegistryLoader<EntityCategory> = loader {
        val noDespawn = 32
        val despawn = 128
        add(Key.key("monster")) { KryptonEntityCategory(it, 70, false, false, despawn, noDespawn) }
        add(Key.key("creature")) { KryptonEntityCategory(it, 10, true, true, despawn, noDespawn) }
        add(Key.key("ambient")) { KryptonEntityCategory(it, 15, true, false, despawn, noDespawn) }
        add(Key.key("underground_water_creature")) { KryptonEntityCategory(it, 5, true, false, despawn, noDespawn) }
        add(Key.key("water_creature")) { KryptonEntityCategory(it, 5, true, false, despawn, noDespawn) }
        add(Key.key("water_ambient")) { KryptonEntityCategory(it, 20, true, false, 64, noDespawn) }
        add(Key.key("misc")) { KryptonEntityCategory(it, -1, true, true, despawn, noDespawn) }
    }

    @JvmStatic
    fun gameRule(): RegistryLoader<GameRule<*>> = loader {
        add(Key.key("announce_advancements")) { KryptonGameRule("announceAdvancements", true) }
        add(Key.key("command_block_output")) { KryptonGameRule("commandBlockOutput", true) }
        add(Key.key("disable_elytra_movement_check")) { KryptonGameRule("disableElytraMovementCheck", false) }
        add(Key.key("disable_raids")) { KryptonGameRule("disableRaids", false) }
        add(Key.key("do_daylight_cycle")) { KryptonGameRule("doDaylightCycle", true) }
        add(Key.key("do_entity_drops")) { KryptonGameRule("doEntityDrops", true) }
        add(Key.key("do_fire_tick")) { KryptonGameRule("doFireTick", true) }
        add(Key.key("do_insomnia")) { KryptonGameRule("doInsomnia", true) }
        add(Key.key("do_immediate_respawn")) { KryptonGameRule("doImmediateRespawn", false) }
        add(Key.key("do_limited_crafting")) { KryptonGameRule("doLimitedCrafting", false) }
        add(Key.key("do_mob_loot")) { KryptonGameRule("doMobLoot", true) }
        add(Key.key("do_mob_spawning")) { KryptonGameRule("doMobSpawning", true) }
        add(Key.key("do_patrol_spawning")) { KryptonGameRule("doPatrolSpawning", true) }
        add(Key.key("do_tile_drops")) { KryptonGameRule("doTileDrops", true) }
        add(Key.key("do_trader_spawning")) { KryptonGameRule("doTraderSpawning", true) }
        add(Key.key("do_weather_cycle")) { KryptonGameRule("doWeatherCycle", true) }
        add(Key.key("drowning_damage")) { KryptonGameRule("drowningDamage", true) }
        add(Key.key("fall_damage")) { KryptonGameRule("fallDamage", true) }
        add(Key.key("fire_damage")) { KryptonGameRule("fireDamage", true) }
        add(Key.key("forgive_dead_players")) { KryptonGameRule("forgiveDeadPlayers", true) }
        add(Key.key("keep_inventory")) { KryptonGameRule("keepInventory", false) }
        add(Key.key("log_admin_commands")) { KryptonGameRule("logAdminCommands", true) }
        add(Key.key("max_command_chain_length")) { KryptonGameRule("maxCommandChainLength", 65536) }
        add(Key.key("max_entity_cramming")) { KryptonGameRule("maxEntityCramming", 24) }
        add(Key.key("mob_griefing")) { KryptonGameRule("mobGriefing", true) }
        add(Key.key("natural_regeneration")) { KryptonGameRule("naturalRegeneration", true) }
        add(Key.key("random_tick_speed")) { KryptonGameRule("randomTickSpeed", 3) }
        add(Key.key("reduced_debug_info")) { KryptonGameRule("reducedDebugInfo", false) }
        add(Key.key("send_command_feedback")) { KryptonGameRule("sendCommandFeedback", true) }
        add(Key.key("show_death_messages")) { KryptonGameRule("showDeathMessages", true) }
        add(Key.key("spawn_radius")) { KryptonGameRule("spawnRadius", 10) }
        add(Key.key("spectators_generate_chunks")) { KryptonGameRule("spectatorsGenerateChunks", true) }
        add(Key.key("universal_anger")) { KryptonGameRule("universalAnger", false) }
    }

    @JvmStatic
    fun inventoryType(): RegistryLoader<InventoryType> = loader {
        val chestTitle = Component.translatable("container.chest")
        add(Key.key("chest_one_row")) { KryptonInventoryType(it, 9, chestTitle) }
        add(Key.key("generic_9x2")) { KryptonInventoryType(it, 9 * 2, chestTitle) }
        add(Key.key("generic_9x3")) { KryptonInventoryType(it, 9 * 3, chestTitle) }
        val doubleChestTitle = Component.translatable("container.chestDouble")
        add(Key.key("generic_9x4")) { KryptonInventoryType(it, 9 * 4, doubleChestTitle) }
        add(Key.key("generic_9x5")) { KryptonInventoryType(it, 9 * 5, doubleChestTitle) }
        add(Key.key("generic_9x6")) { KryptonInventoryType(it, 9 * 6, doubleChestTitle) }
        add(Key.key("generic_3x3")) { KryptonInventoryType(it, 3 * 3, Component.translatable("container.dispenser")) }
        add(Key.key("anvil")) { KryptonInventoryType(it, 3, Component.translatable("container.repair")) }
        add(Key.key("beacon")) { KryptonInventoryType(it, 1, Component.translatable("container.beacon")) }
        add(Key.key("blast_furnace")) { KryptonInventoryType(it, 3, Component.translatable("container.blast_furnace")) }
        add(Key.key("brewing_stand")) { KryptonInventoryType(it, 5, Component.translatable("container.brewing")) }
        add(Key.key("cartography_table")) { KryptonInventoryType(it, 3, Component.translatable("container.cartography_table")) }
        add(Key.key("crafting")) { KryptonInventoryType(it, 5, Component.translatable("container.crafting")) }
        add(Key.key("enchanting")) { KryptonInventoryType(it, 2, Component.translatable("container.enchant")) }
        add(Key.key("furnace")) { KryptonInventoryType(it, 3, Component.translatable("container.furnace")) }
        add(Key.key("grindstone")) { KryptonInventoryType(it, 3, Component.translatable("container.grindstone_title")) }
        add(Key.key("hopper")) { KryptonInventoryType(it, 5, Component.translatable("container.hopper")) }
        add(Key.key("lectern")) { KryptonInventoryType(it, 1, Component.translatable("container.lectern")) }
        add(Key.key("loom")) { KryptonInventoryType(it, 4, Component.translatable("container.loom")) }
        add(Key.key("merchant")) { KryptonInventoryType(it, 3, Component.translatable("merchant.trades")) }
        add(Key.key("shulker_box")) { KryptonInventoryType(it, 9 * 3, Component.translatable("container.shulkerBox")) }
        add(Key.key("smoker")) { KryptonInventoryType(it, 3, Component.translatable("container.smoker")) }
        add(Key.key("smithing")) { KryptonInventoryType(it, 3, Component.translatable("container.upgrade")) }
        add(Key.key("stonecutter")) { KryptonInventoryType(it, 2, Component.translatable("container.stonecutter")) }
    }

    @JvmStatic
    fun itemRarity(): RegistryLoader<ItemRarity> = loader {
        add(Key.key("common")) { KryptonItemRarity(it, NamedTextColor.WHITE) }
        add(Key.key("uncommon")) { KryptonItemRarity(it, NamedTextColor.YELLOW) }
        add(Key.key("rare")) { KryptonItemRarity(it, NamedTextColor.AQUA) }
        add(Key.key("epic")) { KryptonItemRarity(it, NamedTextColor.LIGHT_PURPLE) }
    }

    @JvmStatic
    fun paintingVariant(): RegistryLoader<PaintingVariant> = loader {
        add(Key.key("kebab")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("aztec")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("alban")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("aztec2")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("bomb")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("plant")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("wasteland")) { KryptonPaintingVariant(it, 16, 16) }
        add(Key.key("pool")) { KryptonPaintingVariant(it, 32, 16) }
        add(Key.key("courbet")) { KryptonPaintingVariant(it, 32, 16) }
        add(Key.key("sea")) { KryptonPaintingVariant(it, 32, 16) }
        add(Key.key("sunset")) { KryptonPaintingVariant(it, 32, 16) }
        add(Key.key("creebet")) { KryptonPaintingVariant(it, 32, 16) }
        add(Key.key("wanderer")) { KryptonPaintingVariant(it, 16, 32) }
        add(Key.key("graham")) { KryptonPaintingVariant(it, 16, 32) }
        add(Key.key("match")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("bust")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("stage")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("void")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("skull_and_roses")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("wither")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("fighters")) { KryptonPaintingVariant(it, 64, 32) }
        add(Key.key("pointer")) { KryptonPaintingVariant(it, 64, 64) }
        add(Key.key("pigscene")) { KryptonPaintingVariant(it, 64, 64) }
        add(Key.key("burning_skull")) { KryptonPaintingVariant(it, 64, 64) }
        add(Key.key("skeleton")) { KryptonPaintingVariant(it, 64, 48) }
        add(Key.key("earth")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("wind")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("water")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("fire")) { KryptonPaintingVariant(it, 32, 32) }
        add(Key.key("donkey_kong")) { KryptonPaintingVariant(it, 64, 48) }
    }

    @JvmStatic
    fun particleType(): RegistryLoader<ParticleType> = loader {
        add(Key.key("ambient_entity_effect"), ::KryptonSimpleParticleType)
        add(Key.key("angry_villager"), ::KryptonSimpleParticleType)
        add(Key.key("block"), ::KryptonBlockParticleType)
        add(Key.key("block_marker"), ::KryptonBlockParticleType)
        add(Key.key("bubble"), ::KryptonDirectionalParticleType)
        add(Key.key("cloud"), ::KryptonDirectionalParticleType)
        add(Key.key("crit"), ::KryptonDirectionalParticleType)
        add(Key.key("damage_indicator"), ::KryptonDirectionalParticleType)
        add(Key.key("dragon_breath"), ::KryptonDirectionalParticleType)
        add(Key.key("dripping_lava"), ::KryptonSimpleParticleType)
        add(Key.key("falling_lava"), ::KryptonSimpleParticleType)
        add(Key.key("landing_lava"), ::KryptonSimpleParticleType)
        add(Key.key("dripping_water"), ::KryptonSimpleParticleType)
        add(Key.key("falling_water"), ::KryptonSimpleParticleType)
        add(Key.key("dust"), ::KryptonDustParticleType)
        add(Key.key("dust_color_transition"), ::KryptonDustTransitionParticleType)
        add(Key.key("effect"), ::KryptonSimpleParticleType)
        add(Key.key("elder_guardian"), ::KryptonSimpleParticleType)
        add(Key.key("enchanted_hit"), ::KryptonDirectionalParticleType)
        add(Key.key("enchant"), ::KryptonDirectionalParticleType)
        add(Key.key("end_rod"), ::KryptonDirectionalParticleType)
        add(Key.key("entity_effect"), ::KryptonColorParticleType)
        add(Key.key("explosion_emitter"), ::KryptonSimpleParticleType)
        add(Key.key("explosion"), ::KryptonSimpleParticleType)
        add(Key.key("falling_dust"), ::KryptonBlockParticleType)
        add(Key.key("firework"), ::KryptonDirectionalParticleType)
        add(Key.key("fishing"), ::KryptonDirectionalParticleType)
        add(Key.key("flame"), ::KryptonDirectionalParticleType)
        add(Key.key("soul_fire_flame"), ::KryptonDirectionalParticleType)
        add(Key.key("soul"), ::KryptonDirectionalParticleType)
        add(Key.key("flash"), ::KryptonSimpleParticleType)
        add(Key.key("happy_villager"), ::KryptonSimpleParticleType)
        add(Key.key("composter"), ::KryptonSimpleParticleType)
        add(Key.key("heart"), ::KryptonSimpleParticleType)
        add(Key.key("instant_effect"), ::KryptonSimpleParticleType)
        add(Key.key("item"), ::KryptonItemParticleType)
        add(Key.key("vibration"), ::KryptonVibrationParticleType)
        add(Key.key("item_slime"), ::KryptonSimpleParticleType)
        add(Key.key("item_snowball"), ::KryptonSimpleParticleType)
        add(Key.key("large_smoke"), ::KryptonDirectionalParticleType)
        add(Key.key("lava"), ::KryptonSimpleParticleType)
        add(Key.key("mycelium"), ::KryptonSimpleParticleType)
        add(Key.key("note"), ::KryptonNoteParticleType)
        add(Key.key("poof"), ::KryptonDirectionalParticleType)
        add(Key.key("portal"), ::KryptonDirectionalParticleType)
        add(Key.key("rain"), ::KryptonSimpleParticleType)
        add(Key.key("smoke"), ::KryptonDirectionalParticleType)
        add(Key.key("sneeze"), ::KryptonDirectionalParticleType)
        add(Key.key("spit"), ::KryptonDirectionalParticleType)
        add(Key.key("squid_ink"), ::KryptonDirectionalParticleType)
        add(Key.key("sweep_attack"), ::KryptonSimpleParticleType)
        add(Key.key("totem_of_undying"), ::KryptonDirectionalParticleType)
        add(Key.key("underwater"), ::KryptonSimpleParticleType)
        add(Key.key("splash"), ::KryptonSimpleParticleType)
        add(Key.key("witch"), ::KryptonSimpleParticleType)
        add(Key.key("bubble_pop"), ::KryptonDirectionalParticleType)
        add(Key.key("current_down"), ::KryptonSimpleParticleType)
        add(Key.key("bubble_column_up"), ::KryptonDirectionalParticleType)
        add(Key.key("nautilus"), ::KryptonDirectionalParticleType)
        add(Key.key("dolphin"), ::KryptonSimpleParticleType)
        add(Key.key("campfire_cosy_smoke"), ::KryptonDirectionalParticleType)
        add(Key.key("campfire_signal_smoke"), ::KryptonDirectionalParticleType)
        add(Key.key("dripping_honey"), ::KryptonSimpleParticleType)
        add(Key.key("falling_honey"), ::KryptonSimpleParticleType)
        add(Key.key("landing_honey"), ::KryptonSimpleParticleType)
        add(Key.key("falling_nectar"), ::KryptonSimpleParticleType)
        add(Key.key("falling_spore_blossom"), ::KryptonSimpleParticleType)
        add(Key.key("ash"), ::KryptonSimpleParticleType)
        add(Key.key("crimson_spore"), ::KryptonSimpleParticleType)
        add(Key.key("warped_spore"), ::KryptonSimpleParticleType)
        add(Key.key("spore_blossom_air"), ::KryptonSimpleParticleType)
        add(Key.key("dripping_obsidian_tear"), ::KryptonSimpleParticleType)
        add(Key.key("falling_obsidian_tear"), ::KryptonSimpleParticleType)
        add(Key.key("landing_obsidian_tear"), ::KryptonSimpleParticleType)
        add(Key.key("reverse_portal"), ::KryptonDirectionalParticleType)
        add(Key.key("white_ash"), ::KryptonSimpleParticleType)
        add(Key.key("small_flame"), ::KryptonSimpleParticleType)
        add(Key.key("snowflake"), ::KryptonSimpleParticleType)
        add(Key.key("dripping_dripstone_lava"), ::KryptonSimpleParticleType)
        add(Key.key("falling_dripstone_lava"), ::KryptonSimpleParticleType)
        add(Key.key("dripping_dripstone_water"), ::KryptonSimpleParticleType)
        add(Key.key("falling_dripstone_water"), ::KryptonSimpleParticleType)
        add(Key.key("glow_squid_ink"), ::KryptonSimpleParticleType)
        add(Key.key("glow"), ::KryptonSimpleParticleType)
        add(Key.key("wax_on"), ::KryptonSimpleParticleType)
        add(Key.key("wax_off"), ::KryptonSimpleParticleType)
        add(Key.key("electric_spark"), ::KryptonSimpleParticleType)
        add(Key.key("scrape"), ::KryptonSimpleParticleType)
    }

    @JvmStatic
    fun statisticType(): RegistryLoader<StatisticType<*>> = loader {
        add(Key.key("mined")) { KryptonStatisticType(it, KryptonRegistries.BLOCK) }
        add(Key.key("crafted")) { KryptonStatisticType(it, KryptonRegistries.ITEM) }
        add(Key.key("used")) { KryptonStatisticType(it, KryptonRegistries.ITEM) }
        add(Key.key("broken")) { KryptonStatisticType(it, KryptonRegistries.ITEM) }
        add(Key.key("picked_up")) { KryptonStatisticType(it, KryptonRegistries.ITEM) }
        add(Key.key("dropped")) { KryptonStatisticType(it, KryptonRegistries.ITEM) }
        add(Key.key("killed")) { KryptonStatisticType(it, KryptonRegistries.ENTITY_TYPE) }
        add(Key.key("killed_by")) { KryptonStatisticType(it, KryptonRegistries.ENTITY_TYPE) }
        add(Key.key("custom")) { KryptonStatisticType(it, KryptonRegistries.CUSTOM_STATISTIC) }
    }

    /**
     * This method exists to allow creating loaders in a more DSL-like style, which is much cleaner.
     */
    @JvmStatic
    private inline fun <T> loader(init: RegistryLoader<T>.() -> Unit): RegistryLoader<T> = RegistryLoader<T>().apply(init)
}

private inline fun RegistryLoader<DamageType>.put(key: String, translationKey: String, builder: KryptonDamageType.Builder.() -> Unit = {}) {
    add(Key.key(key)) { KryptonDamageType.Builder(it, translationKey).apply(builder).build() }
}

private fun RegistryLoader<Key>.add(name: String, formatter: StatisticFormatter) {
    add(Key.key(name)) {
        StatisticTypes.CUSTOM.get(it, formatter)
        it
    }
}

private fun RegistryLoader<BlockEntityType>.add(name: String, vararg blocks: Block) {
    add(Key.key(name)) { KryptonBlockEntityType(it, ImmutableSet.copyOf(blocks)) }
}
