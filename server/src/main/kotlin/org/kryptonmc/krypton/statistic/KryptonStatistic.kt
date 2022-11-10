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

import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.statistic.Statistic
import org.kryptonmc.api.statistic.StatisticFormatter
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.krypton.world.scoreboard.KryptonCriterion

class KryptonStatistic<T>(
    override val type: StatisticType<T>,
    override val value: T,
    override val formatter: StatisticFormatter
) : KryptonCriterion(getCriterionName(type, value), false, ObjectiveRenderType.INTEGER), Statistic<T> {

    override fun equals(other: Any?): Boolean = this === other || other is KryptonStatistic<*> && name == other.name

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "KryptonStatistic(name=$name, formatter=$formatter)"

    companion object {

        @JvmStatic
        private fun <T> getCriterionName(type: StatisticType<T>, value: T): String {
            val key = type.key().asString().replace(':', '.')
            val path = type.registry.getKey(value)?.asString()?.replace(':', '.')
            return "$key:$path"
        }
    }
}
