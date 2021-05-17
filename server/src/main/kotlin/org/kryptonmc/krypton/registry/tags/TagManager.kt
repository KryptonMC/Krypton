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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.util.minecraftKey
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.util.fromJson
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
    RequiredTag(TagType.BLOCKS, minecraftKey("acacia_logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("anvil")),
    RequiredTag(TagType.BLOCKS, minecraftKey("bamboo_plantable_on")),
    RequiredTag(TagType.BLOCKS, minecraftKey("banners")),
    RequiredTag(TagType.BLOCKS, minecraftKey("base_stone_nether")),
    RequiredTag(TagType.BLOCKS, minecraftKey("base_stone_overworld")),
    RequiredTag(TagType.BLOCKS, minecraftKey("beacon_base_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("beds")),
    RequiredTag(TagType.BLOCKS, minecraftKey("beehives")),
    RequiredTag(TagType.BLOCKS, minecraftKey("bee_growables")),
    RequiredTag(TagType.BLOCKS, minecraftKey("birch_logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("buttons")),
    RequiredTag(TagType.BLOCKS, minecraftKey("campfires")),
    RequiredTag(TagType.BLOCKS, minecraftKey("carpets")),
    RequiredTag(TagType.BLOCKS, minecraftKey("climbable")),
    RequiredTag(TagType.BLOCKS, minecraftKey("corals")),
    RequiredTag(TagType.BLOCKS, minecraftKey("coral_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("coral_plants")),
    RequiredTag(TagType.BLOCKS, minecraftKey("crimson_stems")),
    RequiredTag(TagType.BLOCKS, minecraftKey("crops")),
    RequiredTag(TagType.BLOCKS, minecraftKey("dark_oak_logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("doors")),
    RequiredTag(TagType.BLOCKS, minecraftKey("dragon_immune")),
    RequiredTag(TagType.BLOCKS, minecraftKey("enderman_holdable")),
    RequiredTag(TagType.BLOCKS, minecraftKey("fences")),
    RequiredTag(TagType.BLOCKS, minecraftKey("fence_gates")),
    RequiredTag(TagType.BLOCKS, minecraftKey("fire")),
    RequiredTag(TagType.BLOCKS, minecraftKey("flowers")),
    RequiredTag(TagType.BLOCKS, minecraftKey("flower_pots")),
    RequiredTag(TagType.BLOCKS, minecraftKey("gold_ores")),
    RequiredTag(TagType.BLOCKS, minecraftKey("guarded_by_piglins")),
    RequiredTag(TagType.BLOCKS, minecraftKey("hoglin_repellents")),
    RequiredTag(TagType.BLOCKS, minecraftKey("ice")),
    RequiredTag(TagType.BLOCKS, minecraftKey("impermeable")),
    RequiredTag(TagType.BLOCKS, minecraftKey("infiniburn_end")),
    RequiredTag(TagType.BLOCKS, minecraftKey("infiniburn_nether")),
    RequiredTag(TagType.BLOCKS, minecraftKey("infiniburn_overworld")),
    RequiredTag(TagType.BLOCKS, minecraftKey("jungle_logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("leaves")),
    RequiredTag(TagType.BLOCKS, minecraftKey("logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("logs_that_burn")),
    RequiredTag(TagType.BLOCKS, minecraftKey("mushroom_grow_block")),
    RequiredTag(TagType.BLOCKS, minecraftKey("non_flammable_wood")),
    RequiredTag(TagType.BLOCKS, minecraftKey("nylium")),
    RequiredTag(TagType.BLOCKS, minecraftKey("oak_logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("piglin_repellents")),
    RequiredTag(TagType.BLOCKS, minecraftKey("planks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("portals")),
    RequiredTag(TagType.BLOCKS, minecraftKey("pressure_plates")),
    RequiredTag(TagType.BLOCKS, minecraftKey("prevent_mob_spawning_inside")),
    RequiredTag(TagType.BLOCKS, minecraftKey("rails")),
    RequiredTag(TagType.BLOCKS, minecraftKey("sand")),
    RequiredTag(TagType.BLOCKS, minecraftKey("saplings")),
    RequiredTag(TagType.BLOCKS, minecraftKey("shulker_boxes")),
    RequiredTag(TagType.BLOCKS, minecraftKey("signs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("slabs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("small_flowers")),
    RequiredTag(TagType.BLOCKS, minecraftKey("soul_fire_base_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("soul_speed_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("spruce_logs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("stairs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("standing_signs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("stone_bricks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("stone_pressure_plates")),
    RequiredTag(TagType.BLOCKS, minecraftKey("strider_warm_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("tall_flowers")),
    RequiredTag(TagType.BLOCKS, minecraftKey("trapdoors")),
    RequiredTag(TagType.BLOCKS, minecraftKey("underwater_bonemeals")),
    RequiredTag(TagType.BLOCKS, minecraftKey("unstable_bottom_center")),
    RequiredTag(TagType.BLOCKS, minecraftKey("valid_spawn")),
    RequiredTag(TagType.BLOCKS, minecraftKey("walls")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wall_corals")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wall_post_override")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wall_signs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("warped_stems")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wart_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wither_immune")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wither_summon_base_blocks")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_buttons")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_doors")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_fences")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_pressure_plates")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_slabs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_stairs")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wooden_trapdoors")),
    RequiredTag(TagType.BLOCKS, minecraftKey("wool")),
    RequiredTag(TagType.ENTITY_TYPES, minecraftKey("arrows")),
    RequiredTag(TagType.ENTITY_TYPES, minecraftKey("beehive_inhabitors")),
    RequiredTag(TagType.ENTITY_TYPES, minecraftKey("impact_projectiles")),
    RequiredTag(TagType.ENTITY_TYPES, minecraftKey("raiders")),
    RequiredTag(TagType.ENTITY_TYPES, minecraftKey("skeletons")),
    RequiredTag(TagType.FLUIDS, minecraftKey("lava")),
    RequiredTag(TagType.FLUIDS, minecraftKey("water")),
    RequiredTag(TagType.ITEMS, minecraftKey("acacia_logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("anvil")),
    RequiredTag(TagType.ITEMS, minecraftKey("arrows")),
    RequiredTag(TagType.ITEMS, minecraftKey("banners")),
    RequiredTag(TagType.ITEMS, minecraftKey("beacon_payment_items")),
    RequiredTag(TagType.ITEMS, minecraftKey("beds")),
    RequiredTag(TagType.ITEMS, minecraftKey("birch_logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("boats")),
    RequiredTag(TagType.ITEMS, minecraftKey("buttons")),
    RequiredTag(TagType.ITEMS, minecraftKey("carpets")),
    RequiredTag(TagType.ITEMS, minecraftKey("coals")),
    RequiredTag(TagType.ITEMS, minecraftKey("creeper_drop_music_discs")),
    RequiredTag(TagType.ITEMS, minecraftKey("crimson_stems")),
    RequiredTag(TagType.ITEMS, minecraftKey("dark_oak_logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("doors")),
    RequiredTag(TagType.ITEMS, minecraftKey("fences")),
    RequiredTag(TagType.ITEMS, minecraftKey("fishes")),
    RequiredTag(TagType.ITEMS, minecraftKey("flowers")),
    RequiredTag(TagType.ITEMS, minecraftKey("gold_ores")),
    RequiredTag(TagType.ITEMS, minecraftKey("jungle_logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("leaves")),
    RequiredTag(TagType.ITEMS, minecraftKey("lectern_books")),
    RequiredTag(TagType.ITEMS, minecraftKey("logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("logs_that_burn")),
    RequiredTag(TagType.ITEMS, minecraftKey("music_discs")),
    RequiredTag(TagType.ITEMS, minecraftKey("non_flammable_wood")),
    RequiredTag(TagType.ITEMS, minecraftKey("oak_logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("piglin_loved")),
    RequiredTag(TagType.ITEMS, minecraftKey("piglin_repellents")),
    RequiredTag(TagType.ITEMS, minecraftKey("planks")),
    RequiredTag(TagType.ITEMS, minecraftKey("rails")),
    RequiredTag(TagType.ITEMS, minecraftKey("sand")),
    RequiredTag(TagType.ITEMS, minecraftKey("saplings")),
    RequiredTag(TagType.ITEMS, minecraftKey("signs")),
    RequiredTag(TagType.ITEMS, minecraftKey("slabs")),
    RequiredTag(TagType.ITEMS, minecraftKey("small_flowers")),
    RequiredTag(TagType.ITEMS, minecraftKey("soul_fire_base_blocks")),
    RequiredTag(TagType.ITEMS, minecraftKey("spruce_logs")),
    RequiredTag(TagType.ITEMS, minecraftKey("stairs")),
    RequiredTag(TagType.ITEMS, minecraftKey("stone_bricks")),
    RequiredTag(TagType.ITEMS, minecraftKey("stone_crafting_materials")),
    RequiredTag(TagType.ITEMS, minecraftKey("stone_tool_materials")),
    RequiredTag(TagType.ITEMS, minecraftKey("tall_flowers")),
    RequiredTag(TagType.ITEMS, minecraftKey("trapdoors")),
    RequiredTag(TagType.ITEMS, minecraftKey("walls")),
    RequiredTag(TagType.ITEMS, minecraftKey("warped_stems")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_buttons")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_doors")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_fences")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_pressure_plates")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_slabs")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_stairs")),
    RequiredTag(TagType.ITEMS, minecraftKey("wooden_trapdoors")),
    RequiredTag(TagType.ITEMS, minecraftKey("wool"))
)
