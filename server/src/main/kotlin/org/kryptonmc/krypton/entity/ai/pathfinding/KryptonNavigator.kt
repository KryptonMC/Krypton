package org.kryptonmc.krypton.entity.ai.pathfinding

import com.extollit.gaming.ai.path.HydrazinePathFinder
import com.extollit.gaming.ai.path.PathOptions
import org.kryptonmc.api.entity.ai.pathfinding.Navigator
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.entity.KryptonMob

class KryptonNavigator(override val entity: KryptonMob) : Navigator {

    private val pathingEntity = KryptonPathingEntity(this)
    private var pathfinder = HydrazinePathFinder(pathingEntity, KryptonInstanceSpace(entity.world))
    override var target: Vec3d? = null

    override fun hasReachedTarget(): Boolean = target != null && entity.position.distanceSquared(target!!) < 0.0001

    fun moveTowards(direction: Vec3d) {
        val position = entity.position
        val dx = direction.x - position.x
        val dy = direction.y - position.y
        val dz = direction.z - position.z

        val yaw = Positioning.calculateLookYaw(dx, dz)
        val pitch = Positioning.calculateLookPitch(dx, dy, dz)
        val newPos = Position(direction.x, direction.y, direction.z, yaw, pitch)
        if (newPos == entity.position) return // Don't move if we don't need to
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
