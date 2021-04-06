package org.kryptonmc.krypton.world.block.tile

import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.world.block.blocks.BannerEntity

abstract class BlockEntity(val id: String) {

    abstract val position: Vector

    abstract val keepPacked: Boolean
}

fun CompoundBinaryTag.toBlockEntity(): BlockEntity {
    val position = Vector(getInt("x").toDouble(), getInt("y").toDouble(), getInt("z").toDouble())
    val keepPacked = getBoolean("keepPacked")
    when (val id = getString("id")) {
        "banner" -> return BannerEntity.fromNBT(position, keepPacked, this)
        else -> throw IllegalArgumentException("Unknown block entity with id $id")
    }
}