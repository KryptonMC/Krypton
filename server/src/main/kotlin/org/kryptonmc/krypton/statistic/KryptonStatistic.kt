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
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticFormatter
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.world.scoreboard.KryptonCriterion

class KryptonStatistic<T>(
    override val type: StatisticType<T>,
    override val value: T,
    override val formatter: StatisticFormatter
) : KryptonCriterion(getCriterionName(type, value)), Statistic<T> {

    override fun equals(other: Any?): Boolean = this === other || other is KryptonStatistic<*> && name == other.name

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "KryptonStatistic(name=$name, formatter=$formatter)"

    companion object {

        @JvmStatic
        private fun <T> getCriterionName(type: StatisticType<T>, value: T): String =
            locationToKey(KryptonRegistries.STATISTIC_TYPE.getKey(type)) + ":" + locationToKey(type.registry.getKey(value))

        @JvmStatic
        private fun locationToKey(key: Key?): String = key.toString().replace(':', '.')
    }
}
