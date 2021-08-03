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

import com.mojang.serialization.Codec
import java.util.Random

open class WeightedRandomList<E : WeightedEntry>(items: List<E> = emptyList()) : List<E> by items {

    private val items = items.toList()
    private val totalWeight = items.totalWeight()

    constructor(vararg items: E) : this(items.toList())

    fun random(random: Random): E? {
        if (totalWeight == 0) return null
        val next = random.nextInt(totalWeight)
        return items.weightedItem(next)
    }

    fun unwrap() = items

    companion object {

        fun <E : WeightedEntry> codec(elementCodec: Codec<E>): Codec<WeightedRandomList<E>> = elementCodec.listOf().xmap(::WeightedRandomList, WeightedRandomList<E>::unwrap)
    }
}
