package org.kryptonmc.krypton.world.block

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.world.World
import org.kryptonmc.krypton.world.block.blocks.BannerEntity

abstract class Block(open val key: NamespacedKey) {

    abstract val world: World

    abstract val position: BlockPosition
}

// TODO: Add the rest of these
abstract class BlockEntity(val id: String) {

    abstract val position: BlockPosition

    abstract val keepPacked: Boolean
}

fun CompoundBinaryTag.toBlockEntity(): BlockEntity {
    val position = BlockPosition(getInt("x"), getInt("y"), getInt("z"))
    val keepPacked = getBoolean("keepPacked")
    when (val id = getString("id")) {
        "banner" -> return BannerEntity.fromNBT(position, keepPacked, this)
        else -> throw IllegalArgumentException("Unknown block entity with id ${getString("id")}")
    }
}