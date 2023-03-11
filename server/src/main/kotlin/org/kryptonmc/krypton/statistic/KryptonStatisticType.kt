/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
