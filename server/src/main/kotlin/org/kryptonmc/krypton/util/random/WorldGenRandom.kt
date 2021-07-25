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

class WorldGenRandom : Random, RandomSource {

    var count = 0
        private set

    constructor() : super()

    constructor(seed: Long) : super(seed)

    override fun next(bits: Int): Int {
        ++count
        return super.next(bits)
    }

    fun setBaseChunkSeed(x: Int, z: Int): Long {
        val chunkSeed = x.toLong() * 341873128712L + z.toLong() * 132897987541
        setSeed(chunkSeed)
        return chunkSeed
    }
}
