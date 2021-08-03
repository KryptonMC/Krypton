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
package org.kryptonmc.krypton.util.random

import java.util.Random

fun List<WeightedEntry>.totalWeight(): Int {
    var temp = 0L
    forEach { temp += it.weight.value }
    require(temp <= Int.MAX_VALUE.toLong()) { "Sum of weights must be less than or equal to the integer maximum value! (${Int.MAX_VALUE})" }
    return temp.toInt()
}

fun <T : WeightedEntry> List<T>.randomItem(random: Random, totalWeight: Int): T? {
    require(totalWeight >= 0) { "Negative total weights are not allowed!" }
    if (totalWeight == 0) return null
    val next = random.nextInt(totalWeight)
    return weightedItem(next)
}

fun <T : WeightedEntry> List<T>.randomItem(random: Random) = randomItem(random, totalWeight())

fun <T : WeightedEntry> List<T>.weightedItem(index: Int): T? {
    var temp = index
    forEach {
        temp -= it.weight.value
        if (temp < 0) return it
    }
    return null
}
