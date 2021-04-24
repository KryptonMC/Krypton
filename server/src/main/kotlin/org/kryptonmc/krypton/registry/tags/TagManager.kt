package org.kryptonmc.krypton.registry.tags

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kryptonmc.krypton.api.registry.NamespacedKey
import java.io.FileNotFoundException
import java.io.Reader
import java.util.concurrent.ConcurrentHashMap

/**
 * The tag manager, responsible for, well, managing tags.
 *
 * All credit goes to Minestom for the tag stuff (this manager and the tag class)
 */
object TagManager {

    private val cache = ConcurrentHashMap<NamespacedKey, Tag>()

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
            val tag = load(it.name, it.type.name.toLowerCase())
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

    fun load(name: NamespacedKey, type: String) = load(name, type) {
        Thread.currentThread().contextClassLoader
            .getResourceAsStream("registries/tags/$type/${name.value}.json")!!
            .reader(Charsets.UTF_8)
    }

    private fun load(name: NamespacedKey, type: String, reader: () -> Reader): Tag {
        val previous = cache.getOrDefault(name, Tag.EMPTY)
        return cache.getOrPut(name) { create(previous, name, type, reader) }
    }

    private fun create(previous: Tag, name: NamespacedKey, type: String, reader: () -> Reader): Tag {
        val data = Json.decodeFromString<TagData>(reader().readText())
        return try {
            Tag(this, name, type, previous, data)
        } catch (exception: FileNotFoundException) {
            Tag.EMPTY
        }
    }
}

private val REQUIRED_TAGS = listOf(
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "acacia_logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "anvil")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "bamboo_plantable_on")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "banners")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "base_stone_nether")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "base_stone_overworld")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "beacon_base_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "beds")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "beehives")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "bee_growables")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "birch_logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "buttons")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "campfires")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "carpets")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "climbable")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "corals")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "coral_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "coral_plants")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "crimson_stems")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "crops")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "dark_oak_logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "doors")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "dragon_immune")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "enderman_holdable")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "fences")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "fence_gates")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "fire")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "flowers")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "flower_pots")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "gold_ores")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "guarded_by_piglins")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "hoglin_repellents")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "ice")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "impermeable")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "infiniburn_end")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "infiniburn_nether")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "infiniburn_overworld")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "jungle_logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "leaves")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "logs_that_burn")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "mushroom_grow_block")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "non_flammable_wood")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "nylium")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "oak_logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "piglin_repellents")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "planks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "portals")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "pressure_plates")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "prevent_mob_spawning_inside")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "rails")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "sand")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "saplings")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "shulker_boxes")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "signs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "slabs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "small_flowers")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "soul_fire_base_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "soul_speed_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "spruce_logs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "stairs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "standing_signs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "stone_bricks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "stone_pressure_plates")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "strider_warm_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "tall_flowers")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "trapdoors")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "underwater_bonemeals")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "unstable_bottom_center")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "valid_spawn")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "walls")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wall_corals")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wall_post_override")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wall_signs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "warped_stems")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wart_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wither_immune")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wither_summon_base_blocks")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_buttons")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_doors")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_fences")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_pressure_plates")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_slabs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_stairs")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wooden_trapdoors")),
    RequiredTag(TagType.BLOCKS, NamespacedKey(value = "wool")),
    RequiredTag(TagType.ENTITY_TYPES, NamespacedKey(value = "arrows")),
    RequiredTag(TagType.ENTITY_TYPES, NamespacedKey(value = "beehive_inhabitors")),
    RequiredTag(TagType.ENTITY_TYPES, NamespacedKey(value = "impact_projectiles")),
    RequiredTag(TagType.ENTITY_TYPES, NamespacedKey(value = "raiders")),
    RequiredTag(TagType.ENTITY_TYPES, NamespacedKey(value = "skeletons")),
    RequiredTag(TagType.FLUIDS, NamespacedKey(value = "lava")),
    RequiredTag(TagType.FLUIDS, NamespacedKey(value = "water")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "acacia_logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "anvil")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "arrows")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "banners")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "beacon_payment_items")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "beds")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "birch_logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "boats")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "buttons")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "carpets")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "coals")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "creeper_drop_music_discs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "crimson_stems")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "dark_oak_logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "doors")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "fences")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "fishes")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "flowers")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "gold_ores")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "jungle_logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "leaves")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "lectern_books")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "logs_that_burn")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "music_discs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "non_flammable_wood")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "oak_logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "piglin_loved")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "piglin_repellents")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "planks")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "rails")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "sand")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "saplings")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "signs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "slabs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "small_flowers")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "soul_fire_base_blocks")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "spruce_logs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "stairs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "stone_bricks")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "stone_crafting_materials")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "stone_tool_materials")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "tall_flowers")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "trapdoors")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "walls")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "warped_stems")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_buttons")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_doors")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_fences")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_pressure_plates")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_slabs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_stairs")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wooden_trapdoors")),
    RequiredTag(TagType.ITEMS, NamespacedKey(value = "wool"))
)
