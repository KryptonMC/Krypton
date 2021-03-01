package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.registry.NamespacedKey
import java.util.*

/**
 * The enum that holds all the biomes in Minecraft, including both their namespaced ID and
 * their numeric ID
 *
 * @author Callum Seabrook
 */
enum class Biome(val key: NamespacedKey, val id: Int) {

    /**
     * Overworld biomes
     */
    OCEAN(NamespacedKey(value = "ocean"), 0),
    PLAINS(NamespacedKey(value = "plains"), 1),
    DESERT(NamespacedKey(value = "desert"), 2),
    MOUNTAINS(NamespacedKey(value = "mountains"), 3),
    FOREST(NamespacedKey(value = "forest"), 4),
    TAIGA(NamespacedKey(value = "taiga"), 5),
    SWAMP(NamespacedKey(value = "swamp"), 6),
    RIVER(NamespacedKey(value = "river"), 7),
    FROZEN_OCEAN(NamespacedKey(value = "frozen_ocean"), 10),
    FROZEN_RIVER(NamespacedKey(value = "frozen_river"), 11),
    SNOWY_TUNDRA(NamespacedKey(value = "snowy_tundra"), 12),
    SNOWY_MOUNTAINS(NamespacedKey(value = "snowy_mountains"), 13),
    MUSHROOM_FIELDS(NamespacedKey(value = "mushroom_fields"), 14),
    MUSHROOM_FIELD_SHORE(NamespacedKey(value = "mushroom_field_shore"), 15),
    BEACH(NamespacedKey(value = "beach"), 16),
    DESERT_HILLS(NamespacedKey(value = "desert_hills"), 17),
    WOODED_HILLS(NamespacedKey(value = "wooded_hills"), 18),
    TAIGA_HILLS(NamespacedKey(value = "taiga_hills"), 19),
    MOUNTAIN_EDGE(NamespacedKey(value = "mountain_edge"), 20),
    JUNGLE(NamespacedKey(value = "jungle"), 21),
    JUNGLE_HILLS(NamespacedKey(value = "jungle_hills"), 22),
    JUNGLE_EDGE(NamespacedKey(value = "jungle_edge"), 23),
    DEEP_OCEAN(NamespacedKey(value = "deep_ocean"), 24),
    STONE_SHORE(NamespacedKey(value = "stone_shore"), 25),
    SNOWY_BEACH(NamespacedKey(value = "snowy_beach"), 26),
    BIRCH_FOREST(NamespacedKey(value = "birch_forest"), 27),
    BIRCH_FOREST_HILLS(NamespacedKey(value = "birch_forest_hills"), 28),
    DARK_FOREST(NamespacedKey(value = "dark_forest"), 29),
    SNOWY_TAIGA(NamespacedKey(value = "snowy_taiga"), 30),
    SNOWY_TAIGA_HILLS(NamespacedKey(value = "snowy_taiga_hills"), 31),
    GIANT_TREE_TAIGA(NamespacedKey(value = "giant_tree_taiga"), 32),
    GIANT_TREE_TAIGA_HILLS(NamespacedKey(value = "giant_tree_taiga_hills"), 33),
    WOODED_MOUNTAINS(NamespacedKey(value = "wooded_mountains"), 34),
    SAVANNA(NamespacedKey(value = "savanna"), 35),
    SAVANNA_PLATEAU(NamespacedKey(value = "savanna_plateau"), 36),
    BADLANDS(NamespacedKey(value = "badlands"), 37),
    WOODED_BADLANDS_PLATEAU(NamespacedKey(value = "wooded_badlands_plateau"), 38),
    BADLANDS_PLATEAU(NamespacedKey(value = "badlands_plateau"), 39),
    WARM_OCEAN(NamespacedKey(value = "warm_ocean"), 44),
    LUKEWARM_OCEAN(NamespacedKey(value = "lukewarm_ocean"), 45),
    COLD_OCEAN(NamespacedKey(value = "cold_ocean"), 46),
    DEEP_WARM_OCEAN(NamespacedKey(value = "deep_warm_ocean"), 47),
    DEEP_LUKEWARM_OCEAN(NamespacedKey(value = "deep_lukewarm_ocean"), 48),
    DEEP_COLD_OCEAN(NamespacedKey(value = "deep_cold_ocean"), 49),
    DEEP_FROZEN_OCEAN(NamespacedKey(value = "deep_frozen_ocean"), 50),
    SUNFLOWER_PLAINS(NamespacedKey(value = "sunflower_plains"), 129),
    DESERT_LAKES(NamespacedKey(value = "desert_lakes"), 130),
    GRAVELLY_MOUNTAINS(NamespacedKey(value = "gravelly_mountains"), 131),
    FLOWER_FOREST(NamespacedKey(value = "flower_forest"), 132),
    TAIGA_MOUNTAINS(NamespacedKey(value = "taiga_mountains"), 133),
    SWAMP_HILLS(NamespacedKey(value = "swamp_hills"), 134),
    ICE_SPIKES(NamespacedKey(value = "ice_spikes"), 140),
    MODIFIED_JUNGLE(NamespacedKey(value = "modified_jungle"), 149),
    MODIFIED_JUNGLE_EDGE(NamespacedKey(value = "modified_jungle_edge"), 151),
    TALL_BIRCH_FOREST(NamespacedKey(value = "tall_birch_forest"), 155),
    TALL_BIRCH_HILLS(NamespacedKey(value = "tall_birch_hills"), 156),
    DARK_FOREST_HILLS(NamespacedKey(value = "dark_forest_hills"), 157),
    SNOWY_TAIGA_MOUNTAINS(NamespacedKey(value = "snowy_taiga_mountains"), 158),
    GIANT_SPRUCE_TAIGA(NamespacedKey(value = "giant_spruce_taiga"), 160),
    GIANT_SPRUCE_TAIGA_HILLS(NamespacedKey(value = "giant_spruce_taiga_hills"), 161),
    MODIFIED_GRAVELLY_MOUNTAINS(NamespacedKey(value = "modified_gravelly_mountains"), 162),
    SHATTERED_SAVANNA(NamespacedKey(value = "shattered_savanna"), 163),
    SHATTERED_SAVANNA_PLATEAU(NamespacedKey(value = "shattered_savanna_plateau"), 164),
    ERODED_BADLANDS(NamespacedKey(value = "eroded_badlands"), 165),
    MODIFIED_WOODED_BADLANDS_PLATEAU(NamespacedKey(value = "modified_wooded_badlands_plateau"), 166),
    MODIFIED_BADLANDS_PLATEAU(NamespacedKey(value = "modified_badlands_plateau"), 167),
    BAMBOO_JUNGLE(NamespacedKey(value = "bamboo_jungle"), 168),
    BAMBOO_JUNGLE_HILLS(NamespacedKey(value = "bamboo_jungle_hills"), 169),

    /**
     * Nether biomes
     */
    NETHER_WASTES(NamespacedKey(value = "nether_wastes"), 8),
    SOUL_SAND_VALLEY(NamespacedKey(value = "soul_sand_valley"), 170),
    CRIMSON_FOREST(NamespacedKey(value = "crimson_forest"), 171),
    WARPED_FOREST(NamespacedKey(value = "warped_forest"), 172),
    BASALT_DELTAS(NamespacedKey(value = "basalt_deltas"), 173),

    /**
     * End biomes
     */
    THE_END(NamespacedKey(value = "the_end"), 9),
    SMALL_END_ISLANDS(NamespacedKey(value = "small_end_islands"), 40),
    END_MIDLANDS(NamespacedKey(value = "end_midlands"), 41),
    END_HIGHLANDS(NamespacedKey(value = "end_highlands"), 42),
    END_BARRENS(NamespacedKey(value = "end_barrens"), 43),

    /**
     * Miscellaneous biomes
     */
    THE_VOID(NamespacedKey(value = "the_void"), 127);

    companion object {

        private val VALUES = values().associateBy { it.id }

        val OCEANS: EnumSet<Biome> = EnumSet.of(
            OCEAN,
            COLD_OCEAN,
            DEEP_OCEAN,
            DEEP_WARM_OCEAN,
            FROZEN_OCEAN,
            DEEP_LUKEWARM_OCEAN,
            DEEP_COLD_OCEAN,
            DEEP_FROZEN_OCEAN,
            WARM_OCEAN,
            LUKEWARM_OCEAN
        )

        fun fromId(id: Int) = VALUES.getValue(id)
    }
}