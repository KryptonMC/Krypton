/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity.ai.pathfinding

import com.extollit.gaming.ai.path.HydrazinePathFinder
import com.extollit.gaming.ai.path.PathOptions
import com.extollit.gaming.ai.path.model.IPath
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
    private var currentPath: IPath? = null
    override var target: Vec3d? = null

    override fun hasReachedTarget(): Boolean = currentPath == null || currentPath!!.done()

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
        val path = pathfinder.initiatePathTo(position.x, position.y, position.z, pathOptions) ?: return false

        currentPath = path
        target = position
        return true
    }

    fun tick() {
        if (currentPath == null) return
        if (entity.isDead) return // Dead entities cannot pathfind
        currentPath = pathfinder.updatePathFor(pathingEntity)
        if (hasReachedTarget()) reset()
    }

    private fun reset() {
        currentPath = null
        target = null
        pathfinder.reset()
    }
}
