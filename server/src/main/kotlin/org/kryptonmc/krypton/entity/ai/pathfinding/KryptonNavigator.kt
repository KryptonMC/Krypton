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
package org.kryptonmc.krypton.entity.ai.pathfinding

import com.extollit.gaming.ai.path.HydrazinePathFinder
import com.extollit.gaming.ai.path.PathOptions
import org.kryptonmc.api.entity.ai.pathfinding.Navigator
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.entity.KryptonMob
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class KryptonNavigator(override val entity: KryptonMob) : Navigator {

    private val pathingEntity = KryptonPathingEntity(this)
    private var pathfinder = HydrazinePathFinder(pathingEntity, KryptonInstanceSpace(entity.world))
    override var target: Vec3d? = null

    override fun hasReachedTarget(): Boolean = target != null && entity.position.distanceSquared(target!!) < 0.0001

    fun moveTowards(direction: Vec3d, speed: Double) {
        val position = entity.position
        val dx = position.x - direction.x
        val dy = position.y - direction.y
        val dz = position.z - direction.z

        val radians = atan2(dz, dx)
        val speedX = cos(radians) * speed
        val speedY = dy * speed
        val speedZ = sin(radians) * speed

        val yaw = Positioning.calculateLookYaw(direction.x - position.x, direction.z - position.z)
        val pitch = Positioning.calculateLookPitch(direction.x - position.x, direction.y - position.y, direction.z - position.z)

        val newPos = Position(position.x - speedX, position.y - speedY, position.z - speedZ, yaw, pitch)
        if (newPos == position) return // Don't move if we don't need to
        entity.teleport(newPos)
    }

    override fun pathTo(x: Double, y: Double, z: Double) {
        tryPathTo(Vec3d(x, y, z))
    }

    override fun pathTo(position: Vec3d) {
        tryPathTo(position)
    }

    override fun pathTo(position: Vec3i) {
        tryPathTo(position.asVec3d())
    }

    override fun pathTo(position: Position) {
        tryPathTo(position.asVec3d())
    }

    fun tryPathTo(position: Vec3d?): Boolean {
        if (position != null && target != null && position == target) return false
        pathfinder.reset()
        if (position == null) return false

        val pathOptions = PathOptions().targetingStrategy(PathOptions.TargetingStrategy.gravitySnap)
        val path = pathfinder.initiatePathTo(position.x, position.y, position.z, pathOptions)

        val success = path != null
        target = if (success) position else null
        return success
    }

    fun tick() {
        if (target == null) return
        if (entity.isDead) return // Dead entities cannot pathfind
        if (pathfinder.updatePathFor(pathingEntity) == null || hasReachedTarget()) reset()
    }

    private fun reset() {
        target = null
        pathfinder.reset()
    }
}
