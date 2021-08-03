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

class Weight private constructor(val value: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return value == (other as Weight).value
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = value.toString()

    companion object {

        private val ONE = Weight(1)
        val CODEC: Codec<Weight> = Codec.INT.xmap(Weight::of, Weight::value)

        fun of(value: Int) = if (value == 1) {
            ONE
        } else {
            require(value >= 0) { "Weight must be greater than or equal to zero!" }
            Weight(value)
        }
    }
}
