package me.bristermitten.minekraft.entity.entities

import me.bristermitten.minekraft.world.Location
import java.util.*

class Player(val id: Int) {

    lateinit var uuid: UUID
    lateinit var name: String

    var location = Location(7.5, 16.0, 7.5)
}