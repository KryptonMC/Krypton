package org.kryptonmc.krypton.entity

enum class MainHand {

    LEFT,
    RIGHT;

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}