package org.kryptonmc.krypton.entity

enum class Hand {

    MAIN,
    OFF;

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}

enum class MainHand {

    LEFT,
    RIGHT;

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}