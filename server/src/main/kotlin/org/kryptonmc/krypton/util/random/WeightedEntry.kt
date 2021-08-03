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
import com.mojang.serialization.codecs.RecordCodecBuilder

interface WeightedEntry {

    val weight: Weight

    open class IntrusiveBase(override val weight: Weight) : WeightedEntry {

        constructor(value: Int) : this(Weight.of(value))
    }

    class Wrapper<T>(val data: T, override val weight: Weight) : WeightedEntry {

        companion object {

            fun <E> codec(dataCodec: Codec<E>): Codec<Wrapper<E>> = RecordCodecBuilder.create {
                it.group(
                    dataCodec.fieldOf("data").forGetter(Wrapper<E>::data),
                    Weight.CODEC.fieldOf("weight").forGetter(Wrapper<E>::weight)
                ).apply(it, ::Wrapper)
            }
        }
    }

    companion object {

        fun <T> wrap(data: T, weight: Int) = Wrapper(data, Weight.of(weight))
    }
}
