package org.kryptonmc.krypton.api.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import java.net.InetSocketAddress
import java.util.*

interface Player : Sender {

    val uuid: UUID

    var displayName: Component

    val address: InetSocketAddress

    val abilities: Abilities

    val location: Location

    val velocity: Vector

    val isOnGround: Boolean

    val isCrouching: Boolean

    val isSprinting: Boolean

    val isFlying: Boolean

    val viewDistance: Int

    val time: Long

    var scoreboard: Scoreboard?

    val locale: Locale
}