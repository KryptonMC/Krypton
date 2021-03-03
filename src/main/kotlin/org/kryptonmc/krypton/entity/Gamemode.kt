package org.kryptonmc.krypton.entity

enum class Gamemode(val id: Int) {

    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);

    companion object {

        private val VALUES = values().associateBy { it.id }

        fun fromId(id: Int) = VALUES.getValue(id)
    }
}