package org.kryptonmc.krypton.entity.entities

import org.kryptonmc.krypton.world.Location
import java.util.*

class Player(val id: Int) {

    lateinit var uuid: UUID
    lateinit var name: String

    var location = Location(0.0, 70.0, 0.0)
}