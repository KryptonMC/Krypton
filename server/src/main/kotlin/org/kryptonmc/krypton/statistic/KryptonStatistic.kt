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
