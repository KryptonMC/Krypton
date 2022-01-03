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
import org.kryptonmc.krypton.world.scoreboard.KryptonCriterion

class KryptonStatistic<T : Any>(
    override val type: StatisticType<T>,
    override val value: T,
    override val formatter: StatisticFormatter
) : KryptonCriterion(Key.key(type.criterionName(value)), type.criterionName(value)), Statistic<T> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return name == (other as KryptonStatistic<*>).name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "KryptonStatistic(name=$name, formatter=$formatter)"

    companion object {

        @JvmStatic
        private fun <T : Any> StatisticType<T>.criterionName(value: T): String {
            val key = key().asString().replace(':', '.')
            val path = registry[value]?.asString()?.replace(':', '.')
            return "$key:$path"
        }
    }
}
