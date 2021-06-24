/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DSL.remainderFinder
import com.mojang.datafixers.DataFix
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import org.apache.commons.lang3.StringUtils
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.upgradeBlock
import org.kryptonmc.krypton.util.datafix.fixes.item.updateItem
import org.kryptonmc.krypton.util.getIfPresent

class StatsCounterFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    @Suppress("UNREACHABLE_CODE") // See note below
    override fun makeRule(): TypeRewriteRule {
        val statsType = outputSchema.getType(References.STATS)
        return fixTypeEverywhereTyped("StatsCounterFix", inputSchema.getType(References.STATS), statsType) {
            val data = it[remainderFinder()]
            val statsMap = mutableMapOf<Dynamic<*>, Dynamic<*>>()
            val mapValues = data.mapValues.result().getIfPresent() ?: return@fixTypeEverywhereTyped statsType.readTyped(data.emptyMap().set("stats", data.createMap(statsMap)))
                .result()
                .orElseThrow { IllegalStateException("Could not parse new stats object!") }
                .first
            val iterator = mapValues.entries.iterator()
            while (true) {
                lateinit var next: Map.Entry<Dynamic<*>, Dynamic<*>>
                lateinit var statName: String
                lateinit var upgraded: String
                while (true) {
                    if (!iterator.hasNext()) return@fixTypeEverywhereTyped statsType.readTyped(data.emptyMap().set("stats", data.createMap(statsMap)))
                        .result()
                        .orElseThrow { IllegalStateException("Could not parse new stats object!") }
                        .first
                    next = iterator.next()
                    if (next.value.asNumber().result().isEmpty) continue
                    val oldStat = next.key.asString("")
                    if (oldStat in SKIP) continue
                    if (oldStat in CUSTOM_MAP) {
                        statName = NEW_CUSTOM_KEY
                        upgraded = CUSTOM_MAP.getValue(statName)
                        break
                    }
                    val index = StringUtils.ordinalIndexOf(oldStat, ".", 2)
                    if (index < 0) continue
                    val statNamePart = oldStat.substring(0, index)
                    if (statNamePart == BLOCK_KEY) {
                        statName = NEW_BLOCK_KEY
                        upgraded = oldStat.substring(index + 1).replace(".", ":").upgradeBlock()
                        break
                    }
                    if (statNamePart in ITEM_KEYS) {
                        statName = ITEM_KEYS.getValue(statNamePart)
                        val itemPart = oldStat.substring(index + 1).replace('.', ':')
                        upgraded = itemPart.updateItem(0) ?: itemPart
                        break
                    }
                    if (statNamePart in ENTITY_KEYS) {
                        statName = ENTITY_KEYS.getValue(statNamePart)
                        val entityPart = oldStat.substring(index + 1).replace('.', ':')
                        upgraded = ENTITIES.getOrDefault(entityPart, entityPart)
                        break
                    }
                }
                val key = data.createString(statName)
                val value = statsMap.getOrPut(key) { data.emptyMap() }
                statsMap[key] = value.set(upgraded, next.value)
            }
            // Kotlin can't figure out that we will always return because of the while true,
            // but figures out that this is unreachable :thonking:
            it
        }
    }

    companion object {

        private const val BLOCK_KEY = "stat.mineBlock"
        private const val NEW_BLOCK_KEY = "minecraft:mined"
        private const val NEW_CUSTOM_KEY = "minecraft:custom"
        private val SKIP = setOf(
            "stat.craftItem.minecraft.spawn_egg",
            "stat.useItem.minecraft.spawn_egg",
            "stat.breakItem.minecraft.spawn_egg",
            "stat.pickup.minecraft.spawn_egg",
            "stat.drop.minecraft.spawn_egg",
        )
        private val CUSTOM_MAP = mapOf(
            "stat.leaveGame" to "minecraft:leave_game",
            "stat.playOneMinute" to "minecraft:play_one_minute",
            "stat.timeSinceDeath" to "minecraft:time_since_death",
            "stat.sneakTime" to "minecraft:sneak_time",
            "stat.walkOneCm" to "minecraft:walk_one_cm",
            "stat.crouchOneCm" to "minecraft:crouch_one_cm",
            "stat.sprintOneCm" to "minecraft:sprint_one_cm",
            "stat.swimOneCm" to "minecraft:swim_one_cm",
            "stat.fallOneCm" to "minecraft:fall_one_cm",
            "stat.climbOneCm" to "minecraft:climb_one_cm",
            "stat.flyOneCm" to "minecraft:fly_one_cm",
            "stat.diveOneCm" to "minecraft:dive_one_cm",
            "stat.minecartOneCm" to "minecraft:minecart_one_cm",
            "stat.boatOneCm" to "minecraft:boat_one_cm",
            "stat.pigOneCm" to "minecraft:pig_one_cm",
            "stat.horseOneCm" to "minecraft:horse_one_cm",
            "stat.aviateOneCm" to "minecraft:aviate_one_cm",
            "stat.jump" to "minecraft:jump",
            "stat.drop" to "minecraft:drop",
            "stat.damageDealt" to "minecraft:damage_dealt",
            "stat.damageTaken" to "minecraft:damage_taken",
            "stat.deaths" to "minecraft:deaths",
            "stat.mobKills" to "minecraft:mob_kills",
            "stat.animalsBred" to "minecraft:animals_bred",
            "stat.playerKills" to "minecraft:player_kills",
            "stat.fishCaught" to "minecraft:fish_caught",
            "stat.talkedToVillager" to "minecraft:talked_to_villager",
            "stat.tradedWithVillager" to "minecraft:traded_with_villager",
            "stat.cakeSlicesEaten" to "minecraft:eat_cake_slice",
            "stat.cauldronFilled" to "minecraft:fill_cauldron",
            "stat.cauldronUsed" to "minecraft:use_cauldron",
            "stat.armorCleaned" to "minecraft:clean_armor",
            "stat.bannerCleaned" to "minecraft:clean_banner",
            "stat.brewingstandInteraction" to "minecraft:interact_with_brewingstand",
            "stat.beaconInteraction" to "minecraft:interact_with_beacon",
            "stat.dropperInspected" to "minecraft:inspect_dropper",
            "stat.hopperInspected" to "minecraft:inspect_hopper",
            "stat.dispenserInspected" to "minecraft:inspect_dispenser",
            "stat.noteblockPlayed" to "minecraft:play_noteblock",
            "stat.noteblockTuned" to "minecraft:tune_noteblock",
            "stat.flowerPotted" to "minecraft:pot_flower",
            "stat.trappedChestTriggered" to "minecraft:trigger_trapped_chest",
            "stat.enderchestOpened" to "minecraft:open_enderchest",
            "stat.itemEnchanted" to "minecraft:enchant_item",
            "stat.recordPlayed" to "minecraft:play_record",
            "stat.furnaceInteraction" to "minecraft:interact_with_furnace",
            "stat.craftingTableInteraction" to "minecraft:interact_with_crafting_table",
            "stat.chestOpened" to "minecraft:open_chest",
            "stat.sleepInBed" to "minecraft:sleep_in_bed",
            "stat.shulkerBoxOpened" to "minecraft:open_shulker_box"
        )
        private val ITEM_KEYS = mapOf(
            "stat.craftItem" to "minecraft:crafted",
            "stat.useItem" to "minecraft:used",
            "stat.breakItem" to "minecraft:broken",
            "stat.pickup" to "minecraft:picked_up",
            "stat.drop" to "minecraft:dropped"
        )
        private val ENTITY_KEYS = mapOf(
            "stat.entityKilledBy" to "minecraft:killed_by",
            "stat.killEntity" to "minecraft:killed"
        )
        private val ENTITIES = mapOf(
            "Bat" to "minecraft:bat",
            "Blaze" to "minecraft:blaze",
            "CaveSpider" to "minecraft:cave_spider",
            "Chicken" to "minecraft:chicken",
            "Cow" to "minecraft:cow",
            "Creeper" to "minecraft:creeper",
            "Donkey" to "minecraft:donkey",
            "ElderGuardian" to "minecraft:elder_guardian",
            "Enderman" to "minecraft:enderman",
            "Endermite" to "minecraft:endermite",
            "EvocationIllager" to "minecraft:evocation_illager",
            "Ghast" to "minecraft:ghast",
            "Guardian" to "minecraft:guardian",
            "Horse" to "minecraft:horse",
            "Husk" to "minecraft:husk",
            "Llama" to "minecraft:llama",
            "LavaSlime" to "minecraft:magma_cube",
            "MushroomCow" to "minecraft:mooshroom",
            "Mule" to "minecraft:mule",
            "Ozelot" to "minecraft:ocelot",
            "Parrot" to "minecraft:parrot",
            "Pig" to "minecraft:pig",
            "PolarBear" to "minecraft:polar_bear",
            "Rabbit" to "minecraft:rabbit",
            "Sheep" to "minecraft:sheep",
            "Shulker" to "minecraft:shulker",
            "Silverfish" to "minecraft:silverfish",
            "SkeletonHorse" to "minecraft:skeleton_horse",
            "Skeleton" to "minecraft:skeleton",
            "Slime" to "minecraft:slime",
            "Spider" to "minecraft:spider",
            "Squid" to "minecraft:squid",
            "Stray" to "minecraft:stray",
            "Vex" to "minecraft:vex",
            "Villager" to "minecraft:villager",
            "VindicationIllager" to "minecraft:vindication_illager",
            "Witch" to "minecraft:witch",
            "WitherSkeleton" to "minecraft:wither_skeleton",
            "Wolf" to "minecraft:wolf",
            "ZombieHorse" to "minecraft:zombie_horse",
            "PigZombie" to "minecraft:zombie_pigman",
            "ZombieVillager" to "minecraft:zombie_villager",
            "Zombie" to "minecraft:zombie"
        )
    }
}
