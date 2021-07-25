package org.kryptonmc.krypton.util.noise

import org.kryptonmc.api.util.floor
import org.kryptonmc.krypton.util.fade
import org.kryptonmc.krypton.util.triLerp
import org.kryptonmc.krypton.util.random.RandomSource

class ImprovedNoise(random: RandomSource) {

    private val permutations = ByteArray(256) { it.toByte() }
    val xOffset = random.nextDouble() * 256
    val yOffset = random.nextDouble() * 256
    val zOffset = random.nextDouble() * 256

    init {
        // Randomise the permutation table
        for (i in 0 until 256) {
            val offset = random.nextInt(256 - i)
            val old = permutations[i]
            permutations[i] = permutations[i + offset]
            permutations[i + offset] = old
        }
    }

    fun noise(x: Double, y: Double, z: Double, yScale: Double = 0.0, yMax: Double = 0.0): Double {
        val offX = x + xOffset
        val offY = y + yOffset
        val offZ = z + zOffset
        val floorX = offX.floor()
        val floorY = offY.floor()
        val floorZ = offZ.floor()
        val relX = offX - floorX
        val fadeRelX = offY - floorY
        val relZ = offZ - floorZ
        val scaleY = if (yScale != 0.0) {
            val max = if (yMax >= 0.0 && yMax < fadeRelX) yMax else fadeRelX
            (max / yScale + SHIFT_UP_EPSILON).floor() * yScale
        } else 0.0
        val relY = fadeRelX - scaleY
        val a = permute(floorX)
        val aa = permute(floorX + 1)
        val b = permute(a + floorY)
        val bb = permute(a + floorY + 1)
        val c = permute(aa + floorY)
        val cc = permute(aa + floorY + 1)
        val d = permute(b + floorZ).gradDot(relX, relY, relZ)
        val e = permute(c + floorZ).gradDot(relX - 1, relY, relZ)
        val f = permute(bb + floorZ).gradDot(relX, relY - 1, relZ)
        val g = permute(cc + floorZ).gradDot(relX - 1, relY - 1, relZ)
        val h = permute(b + floorZ + 1).gradDot(relX, relY, relZ - 1)
        val i = permute(c + floorZ + 1).gradDot(relX - 1, relY, relZ - 1)
        val j = permute(bb + floorZ + 1).gradDot(relX, relY - 1, relZ - 1)
        val k = permute(cc + floorZ + 1).gradDot(relX - 1, relY - 1, relZ - 1)
        val fadeX = relX.fade()
        val fadeY = fadeRelX.fade()
        val fadeZ = relZ.fade()
        return triLerp(fadeX, fadeY, fadeZ, d, e, f, g, h, i, j, k)
    }

    private fun permute(hash: Int) = permutations[hash and 255].toInt() and 255

    companion object {

        private const val SHIFT_UP_EPSILON = 1.0E-7F
    }
}
