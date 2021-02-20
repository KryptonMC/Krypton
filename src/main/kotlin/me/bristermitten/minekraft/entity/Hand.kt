package me.bristermitten.minekraft.entity

enum class Hand(val id: Int) {

    MAIN(0),
    OFF(1)
}

enum class MainHand(val id: Int) {

    LEFT(0),
    RIGHT(1);

    companion object {

        private val VALUES = values()

        fun fromId(id: Int) = VALUES[id]
    }
}