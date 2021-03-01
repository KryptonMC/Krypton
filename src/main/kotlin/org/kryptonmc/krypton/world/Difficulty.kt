package org.kryptonmc.krypton.world

enum class Difficulty(val id: Int) {

    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3);

    companion object {

        private val VALUES = values().associateBy { it.id }

        fun fromId(id: Int) = VALUES.getValue(id)
    }
}