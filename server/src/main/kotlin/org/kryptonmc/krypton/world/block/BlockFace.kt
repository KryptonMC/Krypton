package org.kryptonmc.krypton.world.block

import net.kyori.adventure.util.Index

enum class BlockFace(val id: Int) {

    TOP(1),
    BOTTOM(0),
    NORTH(2),
    SOUTH(3),
    EAST(5),
    WEST(4);

    companion object {

        private val KEYS = Index.create(BlockFace::class.java) { it.id }

        fun fromId(id: Int) = requireNotNull(KEYS.value(id))
    }
}