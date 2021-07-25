package org.kryptonmc.krypton.util.noise

interface SurfaceNoise {

    fun getValue(x: Double, y: Double, yScale: Double, yMax: Double): Double
}
