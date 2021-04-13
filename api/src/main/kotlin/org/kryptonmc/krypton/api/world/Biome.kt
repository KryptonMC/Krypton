package org.kryptonmc.krypton.api.world

import org.kryptonmc.krypton.api.Keyed
import org.kryptonmc.krypton.api.registry.NamespacedKey
import java.util.*

/**
 * The enum that holds all the biomes in Minecraft, including both their namespaced ID and
 * their numeric ID
 *
 * @author Callum Seabrook
 */
enum class Biome(val id: Int) : Keyed {

    /**
     * Overworld biomes
     */
    OCEAN(0),
    PLAINS(1),
    DESERT(2),
    MOUNTAINS(3),
    FOREST(4),
    TAIGA(5),
    SWAMP(6),
    RIVER(7),
    FROZEN_OCEAN(10),
    FROZEN_RIVER(11),
    SNOWY_TUNDRA(12),
    SNOWY_MOUNTAINS(13),
    MUSHROOM_FIELDS(14),
    MUSHROOM_FIELD_SHORE(15),
    BEACH(16),
    DESERT_HILLS(17),
    WOODED_HILLS(18),
    TAIGA_HILLS(19),
    MOUNTAIN_EDGE(20),
    JUNGLE(21),
    JUNGLE_HILLS(22),
    JUNGLE_EDGE(23),
    DEEP_OCEAN(24),
    STONE_SHORE(25),
    SNOWY_BEACH(26),
    BIRCH_FOREST(27),
    BIRCH_FOREST_HILLS(28),
    DARK_FOREST(29),
    SNOWY_TAIGA(30),
    SNOWY_TAIGA_HILLS(31),
    GIANT_TREE_TAIGA(32),
    GIANT_TREE_TAIGA_HILLS(33),
    WOODED_MOUNTAINS(34),
    SAVANNA(35),
    SAVANNA_PLATEAU(36),
    BADLANDS(37),
    WOODED_BADLANDS_PLATEAU(38),
    BADLANDS_PLATEAU(39),
    WARM_OCEAN(44),
    LUKEWARM_OCEAN(45),
    COLD_OCEAN(46),
    DEEP_WARM_OCEAN(47),
    DEEP_LUKEWARM_OCEAN(48),
    DEEP_COLD_OCEAN(49),
    DEEP_FROZEN_OCEAN(50),
    SUNFLOWER_PLAINS(129),
    DESERT_LAKES(130),
    GRAVELLY_MOUNTAINS(131),
    FLOWER_FOREST(132),
    TAIGA_MOUNTAINS(133),
    SWAMP_HILLS(134),
    ICE_SPIKES(140),
    MODIFIED_JUNGLE(149),
    MODIFIED_JUNGLE_EDGE(151),
    TALL_BIRCH_FOREST(155),
    TALL_BIRCH_HILLS(156),
    DARK_FOREST_HILLS(157),
    SNOWY_TAIGA_MOUNTAINS(158),
    GIANT_SPRUCE_TAIGA(160),
    GIANT_SPRUCE_TAIGA_HILLS(161),
    MODIFIED_GRAVELLY_MOUNTAINS(162),
    SHATTERED_SAVANNA(163),
    SHATTERED_SAVANNA_PLATEAU(164),
    ERODED_BADLANDS(165),
    MODIFIED_WOODED_BADLANDS_PLATEAU(166),
    MODIFIED_BADLANDS_PLATEAU(167),
    BAMBOO_JUNGLE(168),
    BAMBOO_JUNGLE_HILLS(169),

    /**
     * Nether biomes
     */
    NETHER_WASTES(8),
    SOUL_SAND_VALLEY(170),
    CRIMSON_FOREST(171),
    WARPED_FOREST(172),
    BASALT_DELTAS(173),

    /**
     * End biomes
     */
    THE_END(9),
    SMALL_END_ISLANDS(40),
    END_MIDLANDS(41),
    END_HIGHLANDS(42),
    END_BARRENS(43),

    /**
     * Miscellaneous biomes
     */
    THE_VOID(127);

    override val key = NamespacedKey(value = name.toLowerCase(Locale.ROOT))

    companion object {

        private val VALUES = values().associateBy { it.id }

        @JvmField
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

        @JvmStatic
        fun fromId(id: Int) = VALUES.getValue(id)
    }
}