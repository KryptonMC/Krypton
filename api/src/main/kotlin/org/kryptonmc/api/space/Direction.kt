/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.space

import net.kyori.adventure.util.Index
import org.kryptonmc.api.space.Direction.DOWN
import org.kryptonmc.api.space.Direction.UP
import org.kryptonmc.api.util.asLong
import org.kryptonmc.api.util.floor
import org.spongepowered.math.TrigMath
import org.spongepowered.math.vector.Vector3i
import java.util.function.Predicate
import kotlin.math.PI
import kotlin.math.abs
import kotlin.random.Random

/**
 * Represents a three-dimensional
 * [Cardinal direction](https://en.wikipedia.org/wiki/Cardinal_direction).
 *
 * As this is three-dimensional, this also includes the directions [UP]
 * and [DOWN].
 */
enum class Direction(
    val data3D: Int,
    val oppositeIndex: Int,
    val data2D: Int,
    val key: String,
    val axis: Axis,
    val axisDirection: AxisDirection,
    val normal: Vector3i
) {

    DOWN(0, 1, -1, "down", Axis.Y, AxisDirection.NEGATIVE, Vector3i(0, -1, 0)),
    UP(1, 0, -1, "up", Axis.Y, AxisDirection.POSITIVE, Vector3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", Axis.Z, AxisDirection.NEGATIVE, Vector3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", Axis.Z, AxisDirection.POSITIVE, Vector3i(0, 0, 1)),
    WEST(4, 5, 1, "west", Axis.X, AxisDirection.NEGATIVE, Vector3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", Axis.X, AxisDirection.POSITIVE, Vector3i(1, 0, 0));

    val normalX = normal.x()
    val normalY = normal.y()
    val normalZ = normal.z()

    val pitch = ((data2D and 3) * 90).toFloat()

    val opposite by lazy { from3D(oppositeIndex) }

    override fun toString() = key

    enum class Axis(val key: String) : (Direction?) -> Boolean, Predicate<Direction?> {

        X("x") {
            override fun choose(x: Int, y: Int, z: Int) = x
            override fun choose(x: Double, y: Double, z: Double) = x
        },
        Y("y") {
            override fun choose(x: Int, y: Int, z: Int) = y
            override fun choose(x: Double, y: Double, z: Double) = y
        },
        Z("z") {
            override fun choose(x: Int, y: Int, z: Int) = z
            override fun choose(x: Double, y: Double, z: Double) = z
        };

        abstract fun choose(x: Int, y: Int, z: Int): Int

        abstract fun choose(x: Double, y: Double, z: Double): Double

        val isVertical: Boolean
            get() = this == Y

        val isHorizontal: Boolean
            get() = this == X || this == Z

        override fun invoke(direction: Direction?) = direction != null && direction.axis == this

        override fun test(t: Direction?) = invoke(t)

        override fun toString() = key

        companion object {

            @JvmOverloads
            fun random(random: Random = Random.Default) = values()[random.nextInt(values().size)]
        }
    }

    enum class AxisDirection(val step: Int, val key: String) {

        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        val opposite by lazy { if (this == POSITIVE) NEGATIVE else POSITIVE }

        override fun toString() = key
    }

    enum class Plane(
        private val faces: Array<Direction>,
        private val axes: Array<Axis>
    ) : Iterable<Direction>, (Direction?) -> Boolean, Predicate<Direction?> {

        ;

        @JvmOverloads
        fun randomDirection(random: Random = Random.Default) = faces[random.nextInt(faces.size)]
    }

    companion object {

        private val KEYS = Index.create(Direction::class.java) { it.key }
        private val BY_3D_DATA = values().sortedArrayWith { o1, o2 -> o1.data3D.compareTo(o2.data3D) }
        private val BY_2D_DATA: Array<Direction> = values().asSequence()
            .filter { it.axis.isHorizontal }
            .sortedWith { o1, o2 -> o1.data2D.compareTo(o2.data2D) }
            .toList()
            .toTypedArray()
        private val BY_NORMAL = values().associateBy { it.normal.asLong() }

        fun fromNormal(position: Vector3i) = BY_NORMAL[position.asLong()]

        fun fromNormal(x: Int, y: Int, z: Int) = BY_NORMAL[asLong(x, y, z)]

        fun fromPitch(pitch: Double) = from2D((pitch / 90.0 + 0.5).floor() and 3)

        fun from3D(value: Int) = BY_3D_DATA[abs(value % BY_3D_DATA.size)]

        fun from2D(value: Int) = BY_2D_DATA[abs(value % BY_2D_DATA.size)]
    }
}
