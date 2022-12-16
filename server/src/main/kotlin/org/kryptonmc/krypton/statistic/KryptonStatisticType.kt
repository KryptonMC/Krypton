/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticFormatter
import org.kryptonmc.api.statistic.StatisticType
import java.util.IdentityHashMap

class KryptonStatisticType<T>(private val key: Key, override val registry: Registry<T>) : StatisticType<T> {

    private val statistics = IdentityHashMap<T, Statistic<T>>()
    private var displayName: Component? = null

    override fun key(): Key = key

    override fun translationKey(): String = "stat_type.${key().asString().replace(':', '.')}"

    fun displayName(): Component {
        if (displayName == null) displayName = Component.translatable(translationKey())
        return displayName!!
    }

    override fun hasStatistic(key: T): Boolean = statistics.containsKey(key)

    override fun getStatistic(key: T): Statistic<T> = getStatistic(key, StatisticFormatter.DEFAULT)

    override fun getStatistic(key: T, formatter: StatisticFormatter): Statistic<T> =
        statistics.computeIfAbsent(key) { KryptonStatistic(this, it, formatter) }

    override fun iterator(): Iterator<Statistic<T>> = statistics.values.iterator()
}
