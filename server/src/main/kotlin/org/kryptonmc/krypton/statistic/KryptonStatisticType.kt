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
package org.kryptonmc.krypton.statistic

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticFormatter
import org.kryptonmc.api.statistic.StatisticType
import java.util.Collections
import java.util.IdentityHashMap

@JvmRecord
data class KryptonStatisticType<T : Any> private constructor(
    private val key: Key,
    override val registry: Registry<T>,
    private val statisticMap: MutableMap<T, Statistic<T>> = IdentityHashMap(),
    override val statistics: Map<T, Statistic<T>> = Collections.unmodifiableMap(statisticMap)
) : StatisticType<T> {

    override fun key(): Key = key

    override fun contains(key: T): Boolean = statisticMap.containsKey(key)

    override fun get(key: T): Statistic<T> = get(key, StatisticFormatter.DEFAULT)

    override fun get(key: T, formatter: StatisticFormatter): Statistic<T> = statisticMap.getOrPut(key) {
        KryptonStatistic(this, key, formatter)
    }

    override fun iterator(): Iterator<Statistic<T>> = statisticMap.values.iterator()

    object Factory : StatisticType.Factory {

        override fun <T : Any> of(key: Key, registry: Registry<T>): StatisticType<T> = KryptonStatisticType(key, registry)
    }
}
