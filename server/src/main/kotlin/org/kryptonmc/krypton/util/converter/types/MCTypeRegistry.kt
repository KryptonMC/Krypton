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
package org.kryptonmc.krypton.util.converter.types

import org.kryptonmc.krypton.util.converter.versions.V100
import org.kryptonmc.krypton.util.converter.versions.V101
import org.kryptonmc.krypton.util.converter.versions.V102
import org.kryptonmc.krypton.util.converter.versions.V1022
import org.kryptonmc.krypton.util.converter.versions.V105
import org.kryptonmc.krypton.util.converter.versions.V106
import org.kryptonmc.krypton.util.converter.versions.V107
import org.kryptonmc.krypton.util.converter.versions.V108
import org.kryptonmc.krypton.util.converter.versions.V109
import org.kryptonmc.krypton.util.converter.versions.V110
import org.kryptonmc.krypton.util.converter.versions.V111
import org.kryptonmc.krypton.util.converter.versions.V1125
import org.kryptonmc.krypton.util.converter.versions.V113
import org.kryptonmc.krypton.util.converter.versions.V1344
import org.kryptonmc.krypton.util.converter.versions.V135
import org.kryptonmc.krypton.util.converter.versions.V143
import org.kryptonmc.krypton.util.converter.versions.V1446
import org.kryptonmc.krypton.util.converter.versions.V1450
import org.kryptonmc.krypton.util.converter.versions.V1451
import org.kryptonmc.krypton.util.converter.versions.V1456
import org.kryptonmc.krypton.util.converter.versions.V1458
import org.kryptonmc.krypton.util.converter.versions.V1460
import org.kryptonmc.krypton.util.converter.versions.V1466
import org.kryptonmc.krypton.util.converter.versions.V147
import org.kryptonmc.krypton.util.converter.versions.V1470
import org.kryptonmc.krypton.util.converter.versions.V1474
import org.kryptonmc.krypton.util.converter.versions.V1475
import org.kryptonmc.krypton.util.converter.versions.V1480
import org.kryptonmc.krypton.util.converter.versions.V1483
import org.kryptonmc.krypton.util.converter.versions.V1484
import org.kryptonmc.krypton.util.converter.versions.V1486
import org.kryptonmc.krypton.util.converter.versions.V1487
import org.kryptonmc.krypton.util.converter.versions.V1488
import org.kryptonmc.krypton.util.converter.versions.V1490
import org.kryptonmc.krypton.util.converter.versions.V1492
import org.kryptonmc.krypton.util.converter.versions.V1494
import org.kryptonmc.krypton.util.converter.versions.V1496
import org.kryptonmc.krypton.util.converter.versions.V1500
import org.kryptonmc.krypton.util.converter.versions.V1501
import org.kryptonmc.krypton.util.converter.versions.V1502
import org.kryptonmc.krypton.util.converter.versions.V1506
import org.kryptonmc.krypton.util.converter.versions.V1510
import org.kryptonmc.krypton.util.converter.versions.V1514
import org.kryptonmc.krypton.util.converter.versions.V1515
import org.kryptonmc.krypton.util.converter.versions.V1624
import org.kryptonmc.krypton.util.converter.versions.V165
import org.kryptonmc.krypton.util.converter.versions.V1800
import org.kryptonmc.krypton.util.converter.versions.V1801
import org.kryptonmc.krypton.util.converter.versions.V1802
import org.kryptonmc.krypton.util.converter.versions.V1803
import org.kryptonmc.krypton.util.converter.versions.V1904
import org.kryptonmc.krypton.util.converter.versions.V1905
import org.kryptonmc.krypton.util.converter.versions.V1906
import org.kryptonmc.krypton.util.converter.versions.V1911
import org.kryptonmc.krypton.util.converter.versions.V1917
import org.kryptonmc.krypton.util.converter.versions.V1918
import org.kryptonmc.krypton.util.converter.versions.V1920
import org.kryptonmc.krypton.util.converter.versions.V1925
import org.kryptonmc.krypton.util.converter.versions.V1928
import org.kryptonmc.krypton.util.converter.versions.V1929
import org.kryptonmc.krypton.util.converter.versions.V1931
import org.kryptonmc.krypton.util.converter.versions.V1936
import org.kryptonmc.krypton.util.converter.versions.V1946
import org.kryptonmc.krypton.util.converter.versions.V1948
import org.kryptonmc.krypton.util.converter.versions.V1953
import org.kryptonmc.krypton.util.converter.versions.V1955
import org.kryptonmc.krypton.util.converter.versions.V1961
import org.kryptonmc.krypton.util.converter.versions.V1963
import org.kryptonmc.krypton.util.converter.versions.V2100
import org.kryptonmc.krypton.util.converter.versions.V2202
import org.kryptonmc.krypton.util.converter.versions.V2209
import org.kryptonmc.krypton.util.converter.versions.V2211
import org.kryptonmc.krypton.util.converter.versions.V2218
import org.kryptonmc.krypton.util.converter.versions.V2501
import org.kryptonmc.krypton.util.converter.versions.V2502
import org.kryptonmc.krypton.util.converter.versions.V2503
import org.kryptonmc.krypton.util.converter.versions.V2505
import org.kryptonmc.krypton.util.converter.versions.V2508
import org.kryptonmc.krypton.util.converter.versions.V2509
import org.kryptonmc.krypton.util.converter.versions.V2511
import org.kryptonmc.krypton.util.converter.versions.V2514
import org.kryptonmc.krypton.util.converter.versions.V2516
import org.kryptonmc.krypton.util.converter.versions.V2518
import org.kryptonmc.krypton.util.converter.versions.V2519
import org.kryptonmc.krypton.util.converter.versions.V2522
import org.kryptonmc.krypton.util.converter.versions.V2523
import org.kryptonmc.krypton.util.converter.versions.V2527
import org.kryptonmc.krypton.util.converter.versions.V2528
import org.kryptonmc.krypton.util.converter.versions.V2529
import org.kryptonmc.krypton.util.converter.versions.V2531
import org.kryptonmc.krypton.util.converter.versions.V2533
import org.kryptonmc.krypton.util.converter.versions.V2535
import org.kryptonmc.krypton.util.converter.versions.V2550
import org.kryptonmc.krypton.util.converter.versions.V2551
import org.kryptonmc.krypton.util.converter.versions.V2552
import org.kryptonmc.krypton.util.converter.versions.V2553
import org.kryptonmc.krypton.util.converter.versions.V2558
import org.kryptonmc.krypton.util.converter.versions.V2568
import org.kryptonmc.krypton.util.converter.versions.V2671
import org.kryptonmc.krypton.util.converter.versions.V2679
import org.kryptonmc.krypton.util.converter.versions.V2680
import org.kryptonmc.krypton.util.converter.versions.V2686
import org.kryptonmc.krypton.util.converter.versions.V2688
import org.kryptonmc.krypton.util.converter.versions.V2690
import org.kryptonmc.krypton.util.converter.versions.V2691
import org.kryptonmc.krypton.util.converter.versions.V2696
import org.kryptonmc.krypton.util.converter.versions.V2700
import org.kryptonmc.krypton.util.converter.versions.V2701
import org.kryptonmc.krypton.util.converter.versions.V2702
import org.kryptonmc.krypton.util.converter.versions.V2707
import org.kryptonmc.krypton.util.converter.versions.V2710
import org.kryptonmc.krypton.util.converter.versions.V2717
import org.kryptonmc.krypton.util.converter.versions.V501
import org.kryptonmc.krypton.util.converter.versions.V502
import org.kryptonmc.krypton.util.converter.versions.V505
import org.kryptonmc.krypton.util.converter.versions.V700
import org.kryptonmc.krypton.util.converter.versions.V701
import org.kryptonmc.krypton.util.converter.versions.V702
import org.kryptonmc.krypton.util.converter.versions.V703
import org.kryptonmc.krypton.util.converter.versions.V704
import org.kryptonmc.krypton.util.converter.versions.V705
import org.kryptonmc.krypton.util.converter.versions.V804
import org.kryptonmc.krypton.util.converter.versions.V806
import org.kryptonmc.krypton.util.converter.versions.V808
import org.kryptonmc.krypton.util.converter.versions.V813
import org.kryptonmc.krypton.util.converter.versions.V816
import org.kryptonmc.krypton.util.converter.versions.V820
import org.kryptonmc.krypton.util.converter.versions.V99

object MCTypeRegistry {

    val LEVEL = MCDataType("Level")
    val PLAYER = MCDataType("Player")
    val CHUNK = MCDataType("Chunk")
    val HOTBAR = MCDataType("CreativeHotbar")
    val OPTIONS = MCDataType("Options")
    val STRUCTURE = MCDataType("Structure")
    val STATS = MCDataType("Stats")
    val SAVED_DATA = MCDataType("SavedData")
    val ADVANCEMENTS = MCDataType("Advancements")
    val POI_CHUNK = MCDataType("PoiChunk")
    val ENTITY_CHUNK = MCDataType("EntityChunk")
    val TILE_ENTITY = KeyDataType("TileEntity")
    val ITEM_STACK = KeyDataType("ItemStack")
    val BLOCK_STATE = MCDataType("BlockState")
    val ENTITY_NAME = MCValueType("EntityName")
    val ENTITY = KeyDataType("Entity")
    val BLOCK_NAME = MCValueType("BlockName")
    val ITEM_NAME = MCValueType("ItemName")
    val UNTAGGED_SPAWNER = MCDataType("Spawner")
    val STRUCTURE_FEATURE = MCDataType("StructureFeature")
    val OBJECTIVE = MCDataType("Objective")
    val TEAM = MCDataType("Team")
    val RECIPE = MCValueType("RecipeName")
    val BIOME = MCValueType("Biome")
    val WORLD_GEN_SETTINGS = MCDataType("WorldGenSettings")

    init {
        // General notes:
        // - Structure converters run before everything.
        // - ID specific converters run after structure converters.
        // - Structure walkers run after id specific converters.
        // - ID specific walkers run after structure walkers.

        V99.register() // all legacy data before converters existed
        V100.register() // first version with version id
        V101.register()
        V102.register()
        V105.register()
        V106.register()
        V107.register()
        V108.register()
        V109.register()
        V110.register()
        V111.register()
        V113.register()
        V135.register()
        V143.register()
        V147.register()
        V165.register()
        V501.register()
        V502.register()
        V505.register()
        V700.register()
        V701.register()
        V702.register()
        V703.register()
        V704.register()
        V705.register()
        V804.register()
        V806.register()
        V808.register()
        V813.register()
        V816.register()
        V820.register()
        V1022.register()
        V1125.register()
        // END OF LEGACY DATA CONVERTERS

        // V1.13
        V1344.register()
        V1446.register()
        // START THE FLATTENING
        V1450.register()
        V1451.register()
        // END THE FLATTENING

        V1456.register()
        V1458.register()
        V1460.register()
        V1466.register()
        V1470.register()
        V1474.register()
        V1475.register()
        V1480.register()
        // V1481 is adding simple block entity
        V1483.register()
        V1484.register()
        V1486.register()
        V1487.register()
        V1488.register()
        V1490.register()
        V1492.register()
        V1494.register()
        V1496.register()
        V1500.register()
        V1501.register()
        V1502.register()
        V1506.register()
        V1510.register()
        V1514.register()
        V1515.register()
        V1624.register()
        // V1.14
        V1800.register()
        V1801.register()
        V1802.register()
        V1803.register()
        V1904.register()
        V1905.register()
        V1906.register()
        // V1909 is just adding a simple block entity (jigsaw)
        V1911.register()
        V1917.register()
        V1918.register()
        V1920.register()
        V1925.register()
        V1928.register()
        V1929.register()
        V1931.register()
        V1936.register()
        V1946.register()
        V1948.register()
        V1953.register()
        V1955.register()
        V1961.register()
        V1963.register()
        // V1.15
        V2100.register()
        V2202.register()
        V2209.register()
        V2211.register()
        V2218.register()
        // V1.16
        V2501.register()
        V2502.register()
        V2503.register()
        V2505.register()
        V2508.register()
        V2509.register()
        V2511.register()
        V2514.register()
        V2516.register()
        V2518.register()
        V2519.register()
        V2522.register()
        V2523.register()
        V2527.register()
        V2528.register()
        V2529.register()
        V2531.register()
        V2533.register()
        V2535.register()
        V2550.register()
        V2551.register()
        V2552.register()
        V2553.register()
        V2558.register()
        V2568.register()
        // V1.17
        // WARN: Mojang registers V2671 under 2571, but that version predates 1.16.5? So it looks like a typo...
        // I changed it to 2671, just so that it's after 1.16.5, but even then this looks misplaced... Thankfully this is
        // the first datafixer, and all it does is add a walker, so I think even if the version here is just wrong it will
        // work.
        V2671.register()
        V2679.register()
        V2680.register()
        // V2684 is registering a simple tile entity (skulk sensor)
        V2686.register()
        V2688.register()
        V2690.register()
        V2691.register()
        V2696.register()
        V2700.register()
        V2701.register()
        V2702.register()
        // In reference to V2671, why the fuck is goat being registered again? For this obvious reason, V2704 is absent.
        V2707.register()
        V2710.register()
        V2717.register()
    }
}
