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
package org.kryptonmc.krypton.util.converter

import ca.spottedleaf.dataconverter.converters.DataConverter
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntConsumer
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet
import it.unimi.dsi.fastutil.ints.IntRBTreeSet
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet
import org.kryptonmc.krypton.util.logger

object MCVersionRegistry {

    private val LOGGER = logger<MCVersionRegistry>()

    val VERSION_NAMES = Int2ObjectLinkedOpenHashMap<String>()
    val VERSION_LIST: IntArrayList
    val DATA_VERSION_LIST: LongArrayList

    val DATACONVERTER_VERSIONS_LIST: IntArrayList
    val DATACONVERTER_VERSIONS_MAJOR = IntLinkedOpenHashSet()
    val DATACONVERTER_VERSIONS = LongLinkedOpenHashSet()
    val SUBVERSIONS = Int2ObjectLinkedOpenHashMap<IntArrayList>()
    val BREAKPOINTS = LongArrayList()

    val MAXIMUM_VERSION: Int
        get() = VERSION_LIST.getInt(VERSION_LIST.lastIndex)

    init {
        // Note: Some of these are nameless.
        // Unless a data version is specified here, it will NOT have converters ran for it. Please add them on update!
        val converterVersions = intArrayOf(
            99,
            100,
            101,
            102,
            105,
            106,
            107,
            108,
            109,
            110,
            111,
            113,
            135,
            143,
            147,
            165,
            501,
            502,
            505,
            700,
            701,
            702,
            703,
            704,
            705,
            804,
            806,
            808,
            808,
            813,
            816,
            820,
            1022,
            1125,
            1344,
            1446,
            1450,
            1451,
            1451,
            1451,
            1451,
            1451,
            1451,
            1451,
            1451,
            1451,
            1456,
            1458,
            1460,
            1466,
            1470,
            1474,
            1475,
            1480,
            1481,
            1483,
            1484,
            1486,
            1487,
            1488,
            1490,
            1492,
            1494,
            1496,
            1500,
            1501,
            1502,
            1506,
            1510,
            1514,
            1515,
            1624,
            1800,
            1801,
            1802,
            1803,
            1904,
            1905,
            1906,
            1909,
            1911,
            1917,
            1918,
            1920,
            1925,
            1928,
            1929,
            1931,
            1936,
            1946,
            1948,
            1953,
            1955,
            1961,
            1963,
            2100,
            2202,
            2209,
            2211,
            2218,
            2501,
            2502,
            2503,
            2505,
            2508,
            2509,
            2511,
            2514,
            2516,
            2518,
            2519,
            2522,
            2523,
            2527,
            2528,
            2529,
            2531,
            2533,
            2535,
            2550,
            2551,
            2552,
            2553,
            2558,
            2568,
            2671,
            2679,
            2680,
            2684,
            2686,
            2688,
            2690,
            2691,
            2696,
            2700,
            2701,
            2702,
            2704,
            2707,
            2710,
            2717 // All up to 1.17.1
        ).apply { sort() }

        DATACONVERTER_VERSIONS_LIST = IntArrayList(converterVersions)
        DATACONVERTER_VERSIONS_MAJOR.addAll(DATACONVERTER_VERSIONS_LIST)

        // add sub versions
        registerSubVersion(MCVersions.V16W38A + 1, 1)

        registerSubVersion(MCVersions.V17W47A, 1)
        registerSubVersion(MCVersions.V17W47A, 2)
        registerSubVersion(MCVersions.V17W47A, 3)
        registerSubVersion(MCVersions.V17W47A, 4)
        registerSubVersion(MCVersions.V17W47A, 5)
        registerSubVersion(MCVersions.V17W47A, 6)
        registerSubVersion(MCVersions.V17W47A, 7)

        // register breakpoints here
        // for all major releases after 1.16, add them here. this reduces the work required to determine if a breakpoint
        // is needed for new converters

        // Too much changed in this version.
        registerBreakpoint(MCVersions.V17W47A)
        registerBreakpoint(MCVersions.V17W47A, Integer.MAX_VALUE)

        // final release of major version
        registerBreakpoint(MCVersions.V1_17_1, Integer.MAX_VALUE)
    }

    init {
        MCVersions::class.java.declaredFields.forEach {
            val name = it.name
            val value = try {
                it.getInt(null)
            } catch (exception: Exception) {
                throw RuntimeException(exception)
            }

            if (VERSION_NAMES.containsKey(value) && value != MCVersions.V15W33B) { // Mojang registered 15w33a and 15w33b under the same id.
                LOGGER.warn("Error registering version \"$name\", version number '$value' is already associated with \"${VERSION_NAMES[value]}\"")
            }

            VERSION_NAMES[value] = name.substring(1)
                .replace("_PRE", "-PRE")
                .replace("_RC", "-RC")
                .replace("_", ".")
                .lowercase()
        }

        for (version in DATACONVERTER_VERSIONS_MAJOR.intIterator()) {
            if (VERSION_NAMES.containsKey(version)) continue

            // find closest greatest version above this one
            var closest = Int.MAX_VALUE
            var closestName: String? = null
            VERSION_NAMES.keys.forEach {
                if (it in (version + 1) until closest) {
                    closest = it
                    closestName = VERSION_NAMES[it]
                }
            }

            VERSION_NAMES[version] = if (closestName == null) "unregistered_v$version" else "$closestName-dev${closest - version}"
        }

        // Explicit override for V99, as 99 is very special.
        VERSION_NAMES[99] = "pre_converter"
        VERSION_LIST = IntArrayList(IntRBTreeSet(VERSION_NAMES.keys))
        DATA_VERSION_LIST = LongArrayList()

        for (version in VERSION_LIST.intIterator()) {
            DATA_VERSION_LIST.add(DataConverter.encodeVersions(version, 0))

            val subVersions = SUBVERSIONS[version] ?: continue
            for (step in subVersions.intIterator()) {
                DATA_VERSION_LIST.add(DataConverter.encodeVersions(version, step))
            }
        }
        DATA_VERSION_LIST.sortWith(naturalOrder())

        for (version in DATACONVERTER_VERSIONS_MAJOR.intIterator()) {
            DATACONVERTER_VERSIONS.add(DataConverter.encodeVersions(version, 0))

            val subVersions = SUBVERSIONS[version] ?: continue
            for (step in subVersions.intIterator()) {
                DATACONVERTER_VERSIONS.add(DataConverter.encodeVersions(version, step))
            }
        }
    }

    fun hasConverters(version: Int) = DATACONVERTER_VERSIONS_MAJOR.contains(version)

    operator fun contains(version: Int) = VERSION_NAMES.containsKey(version)

    operator fun get(version: Int): String = VERSION_NAMES[version]

    fun checkVersion(version: Long) = check(DATACONVERTER_VERSIONS.contains(version)) {
        "Version ${DataConverter.encodeToString(version)} is not registered to have data converters, yet has a data converter"
    }

    private fun registerSubVersion(version: Int, step: Int) = SUBVERSIONS.getOrPut(version) { IntArrayList() }.add(step)

    private fun registerBreakpoint(version: Int, step: Int = 0) = BREAKPOINTS.add(DataConverter.encodeVersions(version, step))
}
