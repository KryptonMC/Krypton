package me.bristermitten.minekraft.entities

import kotlin.math.pow
import kotlin.math.sqrt

class Location(
    var world: World?,
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Float,
    var pitch: Float
) : Cloneable {

    val length: Double
        get() = sqrt(square(x) + square(y) + square(z))

    val lengthSquared: Double
        get() = square(x) + square(y) + square(z)

    constructor(world: World?, x: Double, y: Double, z: Double) : this(world, x, y, z, 0.0f, 0.0f)
}

fun square(double: Double) = double.pow(2)