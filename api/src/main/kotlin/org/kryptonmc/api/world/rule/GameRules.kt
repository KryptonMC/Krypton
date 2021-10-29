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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.Mob
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All the built-in game rules.
 */
@Suppress("MagicNumber")
@Catalogue(GameRule::class)
public object GameRules {

    /**
     * If advancements should be announced to the server.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val ANNOUNCE_ADVANCEMENTS: GameRule<Boolean> = register("announce_advancements", "announceAdvancements", true)

    /**
     * Whether command blocks should notify admins when they perform commands.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val COMMAND_BLOCK_OUTPUT: GameRule<Boolean> = register("command_block_output", "commandBlockOutput", true)

    /**
     * Whether the server should skip checking player speed when the player is
     * wearing elytra.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DISABLE_ELYTRA_MOVEMENT_CHECK: GameRule<Boolean> = register(
        "disable_elytra_movement_check",
        "disableElytraMovementCheck",
        false
    )

    /**
     * Whether raids are disabled.
     *
     * If the value of this game rule is `true`, all raids will stop.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DISABLE_RAIDS: GameRule<Boolean> = register("disable_raids", "disableRaids", false)

    /**
     * Whether the day-night cycle and moon phases progress.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_DAYLIGHT_CYCLE: GameRule<Boolean> = register("do_daylight_cycle", "doDaylightCycle", true)

    /**
     * Whether entities that are not mobs should have drops.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_ENTITY_DROPS: GameRule<Boolean> = register("do_entity_drops", "doEntityDrops", true)

    /**
     * Whether fire should spread and naturally extinguish.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_FIRE_TICK: GameRule<Boolean> = register("do_fire_tick", "doFireTick", true)

    /**
     * Whether phantoms can spawn in the night-time.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_INSOMNIA: GameRule<Boolean> = register("do_insomnia", "doInsomnia", true)

    /**
     * Whether [Player]s should respawn immediately without showing the death screen.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DO_IMMEDIATE_RESPAWN: GameRule<Boolean> = register("do_immediate_respawn", "doImmediateRespawn", false)

    /**
     * Whether [Player]s can only craft recipes they have unlocked.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val DO_LIMITED_CRAFTING: GameRule<Boolean> = register("do_limited_crafting", "doLimitedCrafting", false)

    /**
     * Whether [Mob]s should drop items.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_MOB_LOOT: GameRule<Boolean> = register("do_mob_loot", "doMobLoot", true)

    /**
     * Whether [Mob]s should naturally spawn.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_MOB_SPAWNING: GameRule<Boolean> = register("do_mob_spawning", "doMobSpawning", true)

    /**
     * Whether patrollers will go out on patrol (typically in a raid).
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_PATROL_SPAWNING: GameRule<Boolean> = register("do_patrol_spawning", "doPatrolSpawning", true)

    /**
     * Whether blocks should have drops.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_TILE_DROPS: GameRule<Boolean> = register("do_tile_drops", "doTileDrops", true)

    /**
     * Whether wandering traders will naturally spawn.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_TRADER_SPAWNING: GameRule<Boolean> = register("do_trader_spawning", "doTraderSpawning", true)

    /**
     * Whether the weather will change.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DO_WEATHER_CYCLE: GameRule<Boolean> = register("do_weather_cycle", "doWeatherCycle", true)

    /**
     * Whether entities should take drowning damage.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val DROWNING_DAMAGE: GameRule<Boolean> = register("drowning_damage", "drowningDamage", true)

    /**
     * Whether entities should take fall damage.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val FALL_DAMAGE: GameRule<Boolean> = register("fall_damage", "fallDamage", true)

    /**
     * Whether entities should take fire damage.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val FIRE_DAMAGE: GameRule<Boolean> = register("fire_damage", "fireDamage", true)

    /**
     * Makes angered neutral mobs stop being angry when the targeted player
     * dies nearby.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val FORGIVE_DEAD_PLAYERS: GameRule<Boolean> = register("forgive_dead_players", "forgiveDeadPlayers", true)

    /**
     * Whether [Player]s should keep items in their inventory after death.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val KEEP_INVENTORY: GameRule<Boolean> = register("keep_inventory", "keepInventory", false)

    /**
     * Whether to log admin commands to server log.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val LOG_ADMIN_COMMANDS: GameRule<Boolean> = register("log_admin_commands", "logAdminCommands", true)

    /**
     * The total number of chain commands that can run during a single tick.
     *
     * This is a numerical game rule, with a default value of `65536`.
     */
    @JvmField
    public val MAX_COMMAND_CHAIN_LENGTH: GameRule<Int> = register("max_command_chain_length", "maxCommandChainLength", 65536)

    /**
     * The maximum number of other pushable entities a mob or player can push,
     * before taking 3 suffocation damage per half-second.
     *
     * Damage affects
     * [survival mode][org.kryptonmc.api.world.GameModes.SURVIVAL] or
     * [adventure mode][org.kryptonmc.api.world.GameModes.ADVENTURE] [Player]s,
     * and all mobs but bats. Pushable entities include non-spectator-mode
     * [Player], any mob except bats, as well as minecarts and boats.
     *
     * Setting to `0` disables the rule.
     *
     * This is a numerical game rule, with a default value of `24`.
     */
    @JvmField
    public val MAX_ENTITY_CRAMMING: GameRule<Int> = register("max_entity_cramming", "maxEntityCramming", 24)

    /**
     * Whether [Mob]s should be able to change blocks and pick up items.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val MOB_GRIEFING: GameRule<Boolean> = register("mob_griefing", "mobGriefing", true)

    /**
     * Whether [Player]s can regenerate health naturally if their hunger is
     * full enough (doesn't affect external healing, such as golden apples,
     * the Regeneration effect, etc.).
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val NATURAL_REGENERATION: GameRule<Boolean> = register("natural_regeneration", "naturalRegeneration", true)

    /**
     * How often a random block tick occurs (such as plant growth, leaf decay,
     * etc.) per chunk section per game tick.
     *
     * 0 will disable random ticks, higher numbers will increase random ticks
     *
     * This is a numerical game rule, with a default value of `3`.
     */
    @JvmField
    public val RANDOM_TICK_SPEED: GameRule<Int> = register("random_tick_speed", "randomTickSpeed", 3)

    /**
     * Whether the debug screen shows all or reduced information.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val REDUCED_DEBUG_INFO: GameRule<Boolean> = register("reduced_debug_info", "reducedDebugInfo", false)

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
    public val SEND_COMMAND_FEEDBACK: GameRule<Boolean> = register("send_command_feedback", "sendCommandFeedback", true)

    /**
     * Whether a message appears in chat when a [Player] dies.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val SHOW_DEATH_MESSAGES: GameRule<Boolean> = register("show_death_messages", "showDeathMessages", true)

    /**
     * The number of blocks outward from the world spawn coordinates that a
     * player will spawn in when first joining a server or when dying without
     * a spawn point.
     *
     * This is a numerical game rule, with a default value of `10`.
     */
    @JvmField
    public val SPAWN_RADIUS: GameRule<Int> = register("spawn_radius", "spawnRadius", 10)

    /**
     * Whether players in
     * [spectator mode][org.kryptonmc.api.world.GameModes.SPECTATOR] can
     * generate chunks.
     *
     * This is a boolean game rule, with a default value of `true`.
     */
    @JvmField
    public val SPECTATORS_GENERATE_CHUNKS: GameRule<Boolean> = register(
        "spectators_generate_chunks",
        "spectatorsGenerateChunks",
        true
    )

    /**
     * Makes angered neutral mobs attack any nearby player, not just the player
     * that angered them.
     *
     * This is a boolean game rule, with a default value of `false`.
     */
    @JvmField
    public val UNIVERSAL_ANGER: GameRule<Boolean> = register("universal_anger", "universalAnger", false)

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private fun <V : Any> register(key: String, name: String, default: V): GameRule<V> {
        val key1 = Key.key(key)
        return Registries.register(
            Registries.GAMERULES,
            key1,
            GameRule.of(key1, name, default, Component.translatable("gamerule.$name"))
        ) as GameRule<V>
    }
}
