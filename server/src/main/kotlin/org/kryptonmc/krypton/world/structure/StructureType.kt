package org.kryptonmc.krypton.world.structure

import org.kryptonmc.krypton.api.world.Biome
import java.util.*

enum class StructureType(val structureName: String, val biomes: Set<Biome> = emptySet()) {

    /**
     * Overworld structures
     */
    MINESHAFT("Mineshaft", EnumSet.allOf(Biome::class.java)),
    STRONGHOLD("Stronghold", EnumSet.allOf(Biome::class.java)),
    FOSSIL("Fossil", EnumSet.of(Biome.DESERT, Biome.SWAMP)),
    BURIED_TREASURE("Buried Treasure", EnumSet.of(Biome.BEACH, Biome.SNOWY_BEACH, Biome.STONE_SHORE, Biome.MUSHROOM_FIELD_SHORE)),
    RUINED_PORTAL_OVERWORLD("Ruined Portal", EnumSet.allOf(Biome::class.java)),
    OCEAN_RUINS("Ocean Ruins", Biome.OCEANS + Biome.BEACH),
    SHIPWRECK("Shipwreck", Biome.OCEANS + EnumSet.of(Biome.BEACH, Biome.STONE_SHORE, Biome.SNOWY_BEACH, Biome.MUSHROOM_FIELD_SHORE)),
    OCEAN_MONUMENT("Ocean Monument", EnumSet.of(Biome.DEEP_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_WARM_OCEAN)),
    DESERT_PYRAMID("Desert Pyramid", EnumSet.of(Biome.DESERT, Biome.DESERT_HILLS)),
    IGLOO("Igloo", EnumSet.of(Biome.SNOWY_TUNDRA, Biome.SNOWY_TAIGA)),
    JUNGLE_PYRAMID("Jungle Pyramid", EnumSet.of(Biome.JUNGLE, Biome.JUNGLE_HILLS, Biome.BAMBOO_JUNGLE, Biome.BAMBOO_JUNGLE_HILLS)),
    PILLAGER_OUTPOST("Pillager Outpost", EnumSet.of(Biome.PLAINS, Biome.DESERT, Biome.SAVANNA, Biome.TAIGA, Biome.SNOWY_TUNDRA)),
    SWAMP_HUT("Swamp Hut", EnumSet.of(Biome.SWAMP)),
    VILLAGE("Village", EnumSet.of(Biome.PLAINS, Biome.DESERT, Biome.SAVANNA, Biome.TAIGA, Biome.SNOWY_TUNDRA)),
    WOODLAND_MANSION("Woodland Mansion", EnumSet.of(Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS)),
    DUNGEON("Dungeon", EnumSet.allOf(Biome::class.java)),
    DESERT_WELL("Desert well", EnumSet.of(Biome.DESERT)),

    /**
     * Nether structures
     */
    NETHER_FORTRESS("Nether Fortress", EnumSet.allOf(Biome::class.java)),
    BASTION_REMNANT("Bastion Remnant", EnumSet.of(Biome.NETHER_WASTES, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.SOUL_SAND_VALLEY)),
    NETHER_FOSSIL("Nether Fossil", EnumSet.of(Biome.SOUL_SAND_VALLEY)),
    RUINED_PORTAL_NETHER("Ruined Portal", EnumSet.allOf(Biome::class.java)),

    /**
     * End structures
     */
    END_CITY("End City", EnumSet.of(Biome.END_MIDLANDS, Biome.END_HIGHLANDS)),

    /**
     * Miscellaneous
     */
    INVALID("INVALID"),
}