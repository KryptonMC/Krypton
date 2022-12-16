/*
 * This file is part of the Krypton API, and originates from the Sponge API,
 * licensed under the MIT license.
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors (Sponge API)
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/SpongePowered/SpongeAPI/blob/api-8/src/main/java/org/spongepowered/api/world/gamerule/GameRules.java
 */
package org.kryptonmc.api.world.rule

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the built-in game rules.
 */
@Catalogue(GameRule::class)
public object GameRules {

    /**
     * If advancements should be announced to the server.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val ANNOUNCE_ADVANCEMENTS: GameRule<Boolean> = get("announce_advancements")

    /**
     * Whether command blocks should notify admins when they perform commands.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val COMMAND_BLOCK_OUTPUT: GameRule<Boolean> = get("command_block_output")

    /**
     * Whether the server should skip checking player speed when the player is
     * wearing elytra.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DISABLE_ELYTRA_MOVEMENT_CHECK: GameRule<Boolean> = get("disable_elytra_movement_check")

    /**
     * Whether raids are disabled.
     *
     * If the value of this game rule is `true`, all raids will stop.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DISABLE_RAIDS: GameRule<Boolean> = get("disable_raids")

    /**
     * Whether the day-night cycle and moon phases progress.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_DAYLIGHT_CYCLE: GameRule<Boolean> = get("do_daylight_cycle")

    /**
     * Whether entities that are not mobs should have drops.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_ENTITY_DROPS: GameRule<Boolean> = get("do_entity_drops")

    /**
     * Whether fire should spread and naturally extinguish.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_FIRE_TICK: GameRule<Boolean> = get("do_fire_tick")

    /**
     * Whether phantoms can spawn in the night-time.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_INSOMNIA: GameRule<Boolean> = get("do_insomnia")

    /**
     * Whether [Player]s should respawn immediately without showing the death screen.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DO_IMMEDIATE_RESPAWN: GameRule<Boolean> = get("do_immediate_respawn")

    /**
     * Whether [Player]s can only craft recipes they have unlocked.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DO_LIMITED_CRAFTING: GameRule<Boolean> = get("do_limited_crafting")

    /**
     * Whether [Mob]s should drop items.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_MOB_LOOT: GameRule<Boolean> = get("do_mob_loot")

    /**
     * Whether [Mob]s should naturally spawn.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_MOB_SPAWNING: GameRule<Boolean> = get("do_mob_spawning")

    /**
     * Whether patrollers will go out on patrol (typically in a raid).
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_PATROL_SPAWNING: GameRule<Boolean> = get("do_patrol_spawning")

    /**
     * Whether blocks should have drops.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_TILE_DROPS: GameRule<Boolean> = get("do_tile_drops")

    /**
     * Whether wandering traders will naturally spawn.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_TRADER_SPAWNING: GameRule<Boolean> = get("do_trader_spawning")

    /**
     * Whether the weather will change.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_WEATHER_CYCLE: GameRule<Boolean> = get("do_weather_cycle")

    /**
     * Whether entities should take drowning damage.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DROWNING_DAMAGE: GameRule<Boolean> = get("drowning_damage")

    /**
     * Whether entities should take fall damage.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val FALL_DAMAGE: GameRule<Boolean> = get("fall_damage")

    /**
     * Whether entities should take fire damage.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val FIRE_DAMAGE: GameRule<Boolean> = get("fire_damage")

    /**
     * Makes angered neutral mobs stop being angry when the targeted player
     * dies nearby.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val FORGIVE_DEAD_PLAYERS: GameRule<Boolean> = get("forgive_dead_players")

    /**
     * Whether [Player]s should keep items in their inventory after death.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val KEEP_INVENTORY: GameRule<Boolean> = get("keep_inventory")

    /**
     * Whether to log admin commands to server log.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val LOG_ADMIN_COMMANDS: GameRule<Boolean> = get("log_admin_commands")

    /**
     * The total number of chain commands that can run during a single tick.
     *
     * This is a numerical game rule, with a default value of `65536`.
     */
    @JvmField
    public val MAX_COMMAND_CHAIN_LENGTH: GameRule<Int> = get("max_command_chain_length")

    /**
     * The maximum number of other pushable entities a mob or player can push,
     * before taking 3 suffocation damage per half-second.
     *
     * Damage affects
     * [survival mode][org.kryptonmc.api.world.GameMode.SURVIVAL] or
     * [adventure mode][org.kryptonmc.api.world.GameMode.ADVENTURE]
     * players, and all mobs but bats.
     * Pushable entities include non-spectator-mode players, any mob except
     * bats, as well as minecarts and boats.
     *
     * Setting to `0` disables the rule.
     *
     * This is a numerical game rule, with a default value of `24`.
     */
    @JvmField
    public val MAX_ENTITY_CRAMMING: GameRule<Int> = get("max_entity_cramming")

    /**
     * Whether [Mob]s should be able to change blocks and pick up items.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val MOB_GRIEFING: GameRule<Boolean> = get("mob_griefing")

    /**
     * Whether [Player]s can regenerate health naturally if their hunger is
     * full enough (doesn't affect external healing, such as golden apples,
     * the Regeneration effect, etc.).
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val NATURAL_REGENERATION: GameRule<Boolean> = get("natural_regeneration")

    /**
     * How often a random block tick occurs (such as plant growth, leaf decay,
     * etc.) per chunk section per game tick.
     *
     * 0 will disable random ticks, higher numbers will increase random ticks
     *
     * This is a numerical game rule, with a default value of `3`.
     */
    @JvmField
    public val RANDOM_TICK_SPEED: GameRule<Int> = get("random_tick_speed")

    /**
     * Whether the debug screen shows all or reduced information.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val REDUCED_DEBUG_INFO: GameRule<Boolean> = get("reduced_debug_info")

    /**
     * Whether the feedback from commands executed by a [Player] should show up
     * in chat.
     *
     * This game rule affects the default behavior of whether command blocks
     * store their output
     * text.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val SEND_COMMAND_FEEDBACK: GameRule<Boolean> = get("send_command_feedback")

    /**
     * Whether a message appears in chat when a [Player] dies.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val SHOW_DEATH_MESSAGES: GameRule<Boolean> = get("show_death_messages")

    /**
     * The number of blocks outward from the world spawn coordinates that a
     * player will spawn in when first joining a server or when dying without
     * a spawn point.
     *
     * This is a numerical game rule, with a default value of `10`.
     */
    @JvmField
    public val SPAWN_RADIUS: GameRule<Int> = get("spawn_radius")

    /**
     * Whether players in
     * [spectator mode][org.kryptonmc.api.world.GameMode.SPECTATOR] can
     * generate chunks.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val SPECTATORS_GENERATE_CHUNKS: GameRule<Boolean> = get("spectators_generate_chunks")

    /**
     * Makes angered neutral mobs attack any nearby player, not just the player
     * that angered them.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val UNIVERSAL_ANGER: GameRule<Boolean> = get("universal_anger")

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <V : Any> get(name: String): GameRule<V> = Registries.GAME_RULES.get(Key.key(name))!! as GameRule<V>
}
