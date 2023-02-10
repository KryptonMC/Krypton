/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.shapes.collision

import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.krypton.shapes.Shapes
import kotlin.math.max
import kotlin.math.min

/**
 * This is a utility object for handling collisions between shapes and bounding
 * boxes. This contains optimized code from Paper.
 */
object Collisions {

    const val EPSILON: Double = Shapes.EPSILON

    @JvmStatic
    fun isEmpty(box: BoundingBox): Boolean {
        return box.maxX - box.minX < EPSILON &&
                box.maxY - box.minY < EPSILON &&
                box.maxZ - box.minZ < EPSILON
    }

    @JvmStatic
    fun voxelShapeIntersect(box1: BoundingBox, box2: BoundingBox): Boolean {
        return box1.minX - box2.maxX < -EPSILON &&
                box1.maxX - box2.minX > EPSILON &&
                box1.minY - box2.maxY < -EPSILON &&
                box1.maxY - box2.minY > EPSILON &&
                box1.minZ - box2.maxZ < -EPSILON &&
                box1.maxZ - box2.minZ > EPSILON
    }

    @JvmStatic
    fun voxelShapeIntersect(box: BoundingBox, minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
        return box.minX - maxX < -EPSILON && box.maxX - minX > EPSILON &&
                box.minY - maxY < -EPSILON && box.maxY - minY > EPSILON &&
                box.minZ - maxZ < -EPSILON && box.maxZ - minZ > EPSILON
    }

    @JvmStatic
    fun collideX(target: BoundingBox, source: BoundingBox, sourceMove: Double): Double {
        return collide(sourceMove, { target.minX - source.maxX }, { target.maxX - source.minX }) {
            source.minY - target.maxY < -EPSILON && source.maxY - target.minY > EPSILON &&
                    source.minZ - target.maxZ < -EPSILON && source.maxZ - target.minZ > EPSILON
        }
    }

    @JvmStatic
    fun collideY(target: BoundingBox, source: BoundingBox, sourceMove: Double): Double {
        return collide(sourceMove, { target.minY - source.maxY }, { target.maxY - source.minY }) {
            source.minX - target.maxX < -EPSILON && source.maxX - target.minX > EPSILON &&
                    source.minZ - target.maxZ < -EPSILON && source.maxZ - target.minZ > EPSILON
        }
    }

    @JvmStatic
    fun collideZ(target: BoundingBox, source: BoundingBox, sourceMove: Double): Double {
        return collide(sourceMove, { target.minZ - source.maxZ }, { target.maxZ - source.minZ }) {
            source.minX - target.maxX < -EPSILON && source.maxX - target.minX > EPSILON &&
                    source.minY - target.maxY < -EPSILON && source.maxY - target.minY > EPSILON
        }
    }

    @JvmStatic
    private inline fun collide(sourceMove: Double, positiveMaxMove: () -> Double, negativeMaxMove: () -> Double, runChecks: () -> Boolean): Double {
        if (sourceMove == 0.0) return 0.0
        if (runChecks()) {
            if (sourceMove >= 0.0) {
                val maxMove = positiveMaxMove()
                if (maxMove < -EPSILON) return sourceMove
                return min(maxMove, sourceMove)
            } else {
                val maxMove = negativeMaxMove()
                if (maxMove > EPSILON) return sourceMove
                return max(maxMove, sourceMove)
            }
        }
        return sourceMove
    }
}
