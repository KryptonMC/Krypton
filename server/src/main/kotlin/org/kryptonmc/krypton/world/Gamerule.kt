package org.kryptonmc.krypton.world

import net.kyori.adventure.util.Index

enum class Gamerule(val rule: String) {

    ANNOUNCE_ADVANCEMENTS("announceAdvancements"),
    COMMAND_BLOCK_OUTPUT("commandBlockOutput"),
    DISABLE_ELYTRA_MOVEMENT_CHECK("disableElytraMovementCheck"),
    DISABLE_RAIDS("disableRaids"),
    DO_DAYLIGHT_CYCLE("doDaylightCycle"),
    DO_ENTITY_DROPS("doEntityDrops"),
    DO_FIRE_TICK("doFireTick"),
    DO_INSOMNIA("doInsomnia"),
    DO_IMMEDIATE_RESPAWN("doImmediateRespawn"),
    DO_LIMITED_CRAFTING("doLimitedCrafting"),
    DO_MOB_LOOT("doMobLoot"),
    DO_MOB_SPAWNING("doMobSpawning"),
    DO_PATROL_SPAWNING("doPatrolSpawning"),
    DO_TILE_DROPS("doTileDrops"),
    DO_TRADER_SPAWNING("doTraderSpawning"),
    DO_WEATHER_CYCLE("doWeatherCycle"),
    DROWNING_DAMAGE("drowningDamage"),
    FALL_DAMAGE("fallDamage"),
    FIRE_DAMAGE("fireDamage"),
    FORGIVE_DEAD_PLAYERS("forgiveDeadPlayers"),
    KEEP_INVENTORY("keepInventory"),
    LOG_ADMIN_COMMANDS("logAdminCommands"),
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength"),
    MAX_ENTITY_CRAMMING("maxEntityCramming"),
    MOB_GRIEFING("mobGriefing"),
    NATURAL_REGENERATION("naturalRegeneration"),
    RANDOM_TICK_SPEED("randomTickSpeed"),
    REDUCED_DEBUG_INFO("reducedDebugInfo"),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback"),
    SHOW_DEATH_MESSAGES("showDeathMessages"),
    SPAWN_RADIUS("spawnRadius"),
    SPECTATORS_GENERATE_CHUNKS("spectatorsGenerateChunks"),
    UNIVERSAL_ANGER("universalAnger");

    companion object {

        private val NAMES = Index.create(Gamerule::class.java) { it.rule }

        fun fromName(name: String) = requireNotNull(NAMES.value(name))
    }
}