package org.kryptonmc.krypton.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.space.Rotation
import org.kryptonmc.krypton.space.Vector
import org.kryptonmc.krypton.world.Location
import java.util.*

// TODO: Actually implement some entities
abstract class Entity {

    abstract val uuid: UUID

    abstract val customName: Component?

    abstract val isCustomNameVisible: Boolean

    abstract val location: Location

    abstract val rotation: Rotation

    abstract val isOnGround: Boolean

    abstract val motion: Vector

    abstract val tags: List<String>

    abstract val passenger: Entity?

    abstract val airTicks: Short

    abstract val fallDistance: Float

    abstract val fireTicks: Short

    abstract val isGlowing: Boolean

    abstract val isInvulnerable: Boolean

    abstract val hasNoGravity: Boolean

    abstract val isSilent: Boolean

    abstract val portalCooldown: Int
}