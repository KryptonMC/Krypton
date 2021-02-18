package me.bristermitten.minekraft.entity

import me.bristermitten.minekraft.world.Location
import java.util.*

class Player(val id: Int) {

    lateinit var uuid: UUID
    lateinit var name: String

    var location = Location(0.0, 0.0, 0.0)
}