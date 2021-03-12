package org.kryptonmc.krypton.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.entity.Abilities
import org.kryptonmc.krypton.api.entity.entities.Player
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.scoreboard.Scoreboard
import org.kryptonmc.krypton.command.KryptonSender
import java.net.InetSocketAddress
import java.util.*

class KryptonPlayer(
    val id: Int,
    override val address: InetSocketAddress = InetSocketAddress("127.0.0.1", 1)
) : Player, KryptonSender() {

    override lateinit var uuid: UUID
    override lateinit var name: String

    override var displayName: Component = Component.empty()
    override lateinit var abilities: Abilities

    override lateinit var location: Location

    override var velocity = Vector(0.0, 0.0, 0.0)

    override var isOnGround = false
    override var isCrouching = false
    override var isSprinting = false
    override var isFlying = false

    override var viewDistance = 10
    override var time = 0L

    override var scoreboard: Scoreboard? = null

    override lateinit var locale: Locale

    var gamemode = Gamemode.SURVIVAL
}