package org.kryptonmc.krypton.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.space.Vector
import java.util.*

// TODO: Actually implement some entities
abstract class Entity {

    abstract val uuid: UUID

    abstract val name: Component?

    abstract val isCustomNameVisible: Boolean

    abstract val location: Location

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