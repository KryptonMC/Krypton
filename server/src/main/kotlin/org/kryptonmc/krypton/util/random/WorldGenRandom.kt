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
