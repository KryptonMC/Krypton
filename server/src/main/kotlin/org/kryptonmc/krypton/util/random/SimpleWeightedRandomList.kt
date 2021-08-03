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

import com.google.common.collect.ImmutableList
import com.mojang.serialization.Codec
import java.util.Random

class SimpleWeightedRandomList<E>(items: List<WeightedEntry.Wrapper<E>>) : WeightedRandomList<WeightedEntry.Wrapper<E>>(items) {

    fun randomValue(random: Random) = random(random)?.data

    class Builder<E> {

        private val items = ImmutableList.builder<WeightedEntry.Wrapper<E>>()

        fun add(element: E, weight: Int) = apply { items.add(WeightedEntry.Wrapper(element, Weight.of(weight))) }

        fun build() = SimpleWeightedRandomList(items.build())
    }

    companion object {

        fun <E> wrappedCodec(elementCodec: Codec<E>): Codec<SimpleWeightedRandomList<E>> =
            WeightedEntry.Wrapper.codec(elementCodec).listOf().xmap(::SimpleWeightedRandomList, SimpleWeightedRandomList<E>::unwrap)
    }
}
