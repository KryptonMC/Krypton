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
package org.kryptonmc.krypton.registry.tags

import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.krypton.GSON
import java.io.FileNotFoundException
import java.io.Reader
import java.util.concurrent.ConcurrentHashMap

/**
 * The tag manager, responsible for, well, managing tags.
 *
 * All credit goes to Minestom for the tag stuff (this manager and the tag class)
 */
object TagManager {

    private val cache = ConcurrentHashMap<Key, Tag>()

    val blockTags: Set<Tag>
    val itemTags: Set<Tag>
    val fluidTags: Set<Tag>
    val entityTags: Set<Tag>

    init {
        val mutableBlockTags = mutableSetOf<Tag>()
        val mutableItemTags = mutableSetOf<Tag>()
        val mutableFluidTags = mutableSetOf<Tag>()
        val mutableEntityTags = mutableSetOf<Tag>()

        REQUIRED_TAGS.forEach {
            val tag = load(it.name, it.type.name.lowercase())
            when (it.type) {
                TagType.BLOCKS -> mutableBlockTags += tag
                TagType.ITEMS -> mutableItemTags += tag
                TagType.FLUIDS -> mutableFluidTags += tag
                TagType.ENTITY_TYPES -> mutableEntityTags += tag
            }
        }

        blockTags = mutableBlockTags.toSet()
        itemTags = mutableItemTags.toSet()
        fluidTags = mutableFluidTags.toSet()
        entityTags = mutableEntityTags.toSet()
    }

    fun load(name: Key, type: String) = load(name, type) {
        Thread.currentThread().contextClassLoader
            .getResourceAsStream("registries/tags/$type/${name.value()}.json")!!
            .reader(Charsets.UTF_8)
    }

    private fun load(name: Key, type: String, reader: () -> Reader): Tag {
        val previous = cache.getOrDefault(name, Tag.EMPTY)
        return cache.getOrPut(name) { create(previous, name, type, reader) }
    }

    private fun create(previous: Tag, name: Key, type: String, reader: () -> Reader): Tag {
        val data = GSON.fromJson<TagData>(reader().readText())
        return try {
            Tag(this, name, type, previous, data)
        } catch (exception: FileNotFoundException) {
            Tag.EMPTY
        }
    }
}

private val REQUIRED_TAGS = listOf(
    RequiredTag(TagType.BLOCKS, key("acacia_logs")),
    RequiredTag(TagType.BLOCKS, key("anvil")),
    RequiredTag(TagType.BLOCKS, key("bamboo_plantable_on")),
    RequiredTag(TagType.BLOCKS, key("banners")),
    RequiredTag(TagType.BLOCKS, key("base_stone_nether")),
    RequiredTag(TagType.BLOCKS, key("base_stone_overworld")),
    RequiredTag(TagType.BLOCKS, key("beacon_base_blocks")),
    RequiredTag(TagType.BLOCKS, key("beds")),
    RequiredTag(TagType.BLOCKS, key("beehives")),
    RequiredTag(TagType.BLOCKS, key("bee_growables")),
    RequiredTag(TagType.BLOCKS, key("birch_logs")),
    RequiredTag(TagType.BLOCKS, key("buttons")),
    RequiredTag(TagType.BLOCKS, key("campfires")),
    RequiredTag(TagType.BLOCKS, key("carpets")),
    RequiredTag(TagType.BLOCKS, key("climbable")),
    RequiredTag(TagType.BLOCKS, key("corals")),
    RequiredTag(TagType.BLOCKS, key("coral_blocks")),
    RequiredTag(TagType.BLOCKS, key("coral_plants")),
    RequiredTag(TagType.BLOCKS, key("crimson_stems")),
    RequiredTag(TagType.BLOCKS, key("crops")),
    RequiredTag(TagType.BLOCKS, key("dark_oak_logs")),
    RequiredTag(TagType.BLOCKS, key("doors")),
    RequiredTag(TagType.BLOCKS, key("dragon_immune")),
    RequiredTag(TagType.BLOCKS, key("enderman_holdable")),
    RequiredTag(TagType.BLOCKS, key("fences")),
    RequiredTag(TagType.BLOCKS, key("fence_gates")),
    RequiredTag(TagType.BLOCKS, key("fire")),
    RequiredTag(TagType.BLOCKS, key("flowers")),
    RequiredTag(TagType.BLOCKS, key("flower_pots")),
    RequiredTag(TagType.BLOCKS, key("gold_ores")),
    RequiredTag(TagType.BLOCKS, key("guarded_by_piglins")),
    RequiredTag(TagType.BLOCKS, key("hoglin_repellents")),
    RequiredTag(TagType.BLOCKS, key("ice")),
    RequiredTag(TagType.BLOCKS, key("impermeable")),
    RequiredTag(TagType.BLOCKS, key("infiniburn_end")),
    RequiredTag(TagType.BLOCKS, key("infiniburn_nether")),
    RequiredTag(TagType.BLOCKS, key("infiniburn_overworld")),
    RequiredTag(TagType.BLOCKS, key("jungle_logs")),
    RequiredTag(TagType.BLOCKS, key("leaves")),
    RequiredTag(TagType.BLOCKS, key("logs")),
    RequiredTag(TagType.BLOCKS, key("logs_that_burn")),
    RequiredTag(TagType.BLOCKS, key("mushroom_grow_block")),
    RequiredTag(TagType.BLOCKS, key("non_flammable_wood")),
    RequiredTag(TagType.BLOCKS, key("nylium")),
    RequiredTag(TagType.BLOCKS, key("oak_logs")),
    RequiredTag(TagType.BLOCKS, key("piglin_repellents")),
    RequiredTag(TagType.BLOCKS, key("planks")),
    RequiredTag(TagType.BLOCKS, key("portals")),
    RequiredTag(TagType.BLOCKS, key("pressure_plates")),
    RequiredTag(TagType.BLOCKS, key("prevent_mob_spawning_inside")),
    RequiredTag(TagType.BLOCKS, key("rails")),
    RequiredTag(TagType.BLOCKS, key("sand")),
    RequiredTag(TagType.BLOCKS, key("saplings")),
    RequiredTag(TagType.BLOCKS, key("shulker_boxes")),
    RequiredTag(TagType.BLOCKS, key("signs")),
    RequiredTag(TagType.BLOCKS, key("slabs")),
    RequiredTag(TagType.BLOCKS, key("small_flowers")),
    RequiredTag(TagType.BLOCKS, key("soul_fire_base_blocks")),
    RequiredTag(TagType.BLOCKS, key("soul_speed_blocks")),
    RequiredTag(TagType.BLOCKS, key("spruce_logs")),
    RequiredTag(TagType.BLOCKS, key("stairs")),
    RequiredTag(TagType.BLOCKS, key("standing_signs")),
    RequiredTag(TagType.BLOCKS, key("stone_bricks")),
    RequiredTag(TagType.BLOCKS, key("stone_pressure_plates")),
    RequiredTag(TagType.BLOCKS, key("strider_warm_blocks")),
    RequiredTag(TagType.BLOCKS, key("tall_flowers")),
    RequiredTag(TagType.BLOCKS, key("trapdoors")),
    RequiredTag(TagType.BLOCKS, key("underwater_bonemeals")),
    RequiredTag(TagType.BLOCKS, key("unstable_bottom_center")),
    RequiredTag(TagType.BLOCKS, key("valid_spawn")),
    RequiredTag(TagType.BLOCKS, key("walls")),
    RequiredTag(TagType.BLOCKS, key("wall_corals")),
    RequiredTag(TagType.BLOCKS, key("wall_post_override")),
    RequiredTag(TagType.BLOCKS, key("wall_signs")),
    RequiredTag(TagType.BLOCKS, key("warped_stems")),
    RequiredTag(TagType.BLOCKS, key("wart_blocks")),
    RequiredTag(TagType.BLOCKS, key("wither_immune")),
    RequiredTag(TagType.BLOCKS, key("wither_summon_base_blocks")),
    RequiredTag(TagType.BLOCKS, key("wooden_buttons")),
    RequiredTag(TagType.BLOCKS, key("wooden_doors")),
    RequiredTag(TagType.BLOCKS, key("wooden_fences")),
    RequiredTag(TagType.BLOCKS, key("wooden_pressure_plates")),
    RequiredTag(TagType.BLOCKS, key("wooden_slabs")),
    RequiredTag(TagType.BLOCKS, key("wooden_stairs")),
    RequiredTag(TagType.BLOCKS, key("wooden_trapdoors")),
    RequiredTag(TagType.BLOCKS, key("wool")),
    RequiredTag(TagType.ENTITY_TYPES, key("arrows")),
    RequiredTag(TagType.ENTITY_TYPES, key("beehive_inhabitors")),
    RequiredTag(TagType.ENTITY_TYPES, key("impact_projectiles")),
    RequiredTag(TagType.ENTITY_TYPES, key("raiders")),
    RequiredTag(TagType.ENTITY_TYPES, key("skeletons")),
    RequiredTag(TagType.FLUIDS, key("lava")),
    RequiredTag(TagType.FLUIDS, key("water")),
    RequiredTag(TagType.ITEMS, key("acacia_logs")),
    RequiredTag(TagType.ITEMS, key("anvil")),
    RequiredTag(TagType.ITEMS, key("arrows")),
    RequiredTag(TagType.ITEMS, key("banners")),
    RequiredTag(TagType.ITEMS, key("beacon_payment_items")),
    RequiredTag(TagType.ITEMS, key("beds")),
    RequiredTag(TagType.ITEMS, key("birch_logs")),
    RequiredTag(TagType.ITEMS, key("boats")),
    RequiredTag(TagType.ITEMS, key("buttons")),
    RequiredTag(TagType.ITEMS, key("carpets")),
    RequiredTag(TagType.ITEMS, key("coals")),
    RequiredTag(TagType.ITEMS, key("creeper_drop_music_discs")),
    RequiredTag(TagType.ITEMS, key("crimson_stems")),
    RequiredTag(TagType.ITEMS, key("dark_oak_logs")),
    RequiredTag(TagType.ITEMS, key("doors")),
    RequiredTag(TagType.ITEMS, key("fences")),
    RequiredTag(TagType.ITEMS, key("fishes")),
    RequiredTag(TagType.ITEMS, key("flowers")),
    RequiredTag(TagType.ITEMS, key("gold_ores")),
    RequiredTag(TagType.ITEMS, key("jungle_logs")),
    RequiredTag(TagType.ITEMS, key("leaves")),
    RequiredTag(TagType.ITEMS, key("lectern_books")),
    RequiredTag(TagType.ITEMS, key("logs")),
    RequiredTag(TagType.ITEMS, key("logs_that_burn")),
    RequiredTag(TagType.ITEMS, key("music_discs")),
    RequiredTag(TagType.ITEMS, key("non_flammable_wood")),
    RequiredTag(TagType.ITEMS, key("oak_logs")),
    RequiredTag(TagType.ITEMS, key("piglin_loved")),
    RequiredTag(TagType.ITEMS, key("piglin_repellents")),
    RequiredTag(TagType.ITEMS, key("planks")),
    RequiredTag(TagType.ITEMS, key("rails")),
    RequiredTag(TagType.ITEMS, key("sand")),
    RequiredTag(TagType.ITEMS, key("saplings")),
    RequiredTag(TagType.ITEMS, key("signs")),
    RequiredTag(TagType.ITEMS, key("slabs")),
    RequiredTag(TagType.ITEMS, key("small_flowers")),
    RequiredTag(TagType.ITEMS, key("soul_fire_base_blocks")),
    RequiredTag(TagType.ITEMS, key("spruce_logs")),
    RequiredTag(TagType.ITEMS, key("stairs")),
    RequiredTag(TagType.ITEMS, key("stone_bricks")),
    RequiredTag(TagType.ITEMS, key("stone_crafting_materials")),
    RequiredTag(TagType.ITEMS, key("stone_tool_materials")),
    RequiredTag(TagType.ITEMS, key("tall_flowers")),
    RequiredTag(TagType.ITEMS, key("trapdoors")),
    RequiredTag(TagType.ITEMS, key("walls")),
    RequiredTag(TagType.ITEMS, key("warped_stems")),
    RequiredTag(TagType.ITEMS, key("wooden_buttons")),
    RequiredTag(TagType.ITEMS, key("wooden_doors")),
    RequiredTag(TagType.ITEMS, key("wooden_fences")),
    RequiredTag(TagType.ITEMS, key("wooden_pressure_plates")),
    RequiredTag(TagType.ITEMS, key("wooden_slabs")),
    RequiredTag(TagType.ITEMS, key("wooden_stairs")),
    RequiredTag(TagType.ITEMS, key("wooden_trapdoors")),
    RequiredTag(TagType.ITEMS, key("wool"))
)
