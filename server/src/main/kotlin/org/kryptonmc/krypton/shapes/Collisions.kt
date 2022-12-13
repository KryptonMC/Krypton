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
package org.kryptonmc.krypton.shapes

import org.kryptonmc.api.util.BoundingBox
import kotlin.math.max
import kotlin.math.min

/**
 * This is a utility object for handling collisions between shapes and bounding
 * boxes. This contains optimized code from Paper.
 */
object Collisions {

    const val EPSILON: Double = Shapes.EPSILON

    @JvmStatic
    fun isEmpty(box: BoundingBox): Boolean = box.maximumX - box.minimumX < EPSILON &&
            box.maximumY - box.minimumY < EPSILON &&
            box.maximumZ - box.minimumZ < EPSILON

    @JvmStatic
    fun voxelShapeIntersect(box1: BoundingBox, box2: BoundingBox): Boolean = box1.minimumX - box2.maximumX < -EPSILON &&
            box1.maximumX - box2.minimumX > EPSILON &&
            box1.minimumY - box2.maximumY < -EPSILON &&
            box1.maximumY - box2.minimumY > EPSILON &&
            box1.minimumZ - box2.maximumZ < -EPSILON &&
            box1.maximumZ - box2.minimumZ > EPSILON

    @JvmStatic
    fun voxelShapeIntersect(box: BoundingBox, minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean =
        box.minimumX - maxX < -EPSILON && box.maximumX - minX > EPSILON &&
                box.minimumY - maxY < -EPSILON && box.maximumY - minY > EPSILON &&
                box.minimumZ - maxZ < -EPSILON && box.maximumZ - minZ > EPSILON

    @JvmStatic
    fun collideX(target: BoundingBox, source: BoundingBox, sourceMove: Double): Double =
        collide(sourceMove, { target.minimumX - source.maximumX }, { target.maximumX - source.minimumX }) {
            source.minimumY - target.maximumY < -EPSILON && source.maximumY - target.minimumY > EPSILON &&
                    source.minimumZ - target.maximumZ < -EPSILON && source.maximumZ - target.minimumZ > EPSILON
        }

    @JvmStatic
    fun collideY(target: BoundingBox, source: BoundingBox, sourceMove: Double): Double =
        collide(sourceMove, { target.minimumY - source.maximumY }, { target.maximumY - source.minimumY }) {
            source.minimumX - target.maximumX < -EPSILON && source.maximumX - target.minimumX > EPSILON &&
                    source.minimumZ - target.maximumZ < -EPSILON && source.maximumZ - target.minimumZ > EPSILON
        }

    @JvmStatic
    fun collideZ(target: BoundingBox, source: BoundingBox, sourceMove: Double): Double =
        collide(sourceMove, { target.minimumZ - source.maximumZ }, { target.maximumZ - source.minimumZ }) {
            source.minimumX - target.maximumX < -EPSILON && source.maximumX - target.minimumX > EPSILON &&
                    source.minimumY - target.maximumY < -EPSILON && source.maximumY - target.minimumY > EPSILON
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
