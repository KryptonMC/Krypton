package org.kryptonmc.krypton.entity

/**
 * Represents a player's main hand, either left or right.
 *
 * @author Callum Seabrook
 */
enum class MainHand {

    LEFT,
    RIGHT;

    companion object {

        fun fromId(id: Int) = values()[id]
    }
}