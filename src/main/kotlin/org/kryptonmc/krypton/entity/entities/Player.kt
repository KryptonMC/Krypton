package org.kryptonmc.krypton.entity.entities

import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.world.Location
import java.util.*

class Player(val id: Int) {

    lateinit var uuid: UUID
    lateinit var name: String

    lateinit var location: Location

    var gamemode = Gamemode.SURVIVAL
}