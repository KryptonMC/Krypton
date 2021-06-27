package org.kryptonmc.krypton.world.dimension

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.krypton.util.nbt.setBoolean

fun DimensionType.toNBT() = NBTCompound()
    .setBoolean("piglin_safe", isPiglinSafe)
    .setBoolean("natural", isNatural)
    .setBoolean("ultrawarm", isUltrawarm)
    .setBoolean("has_skylight", hasSkylight)
    .setBoolean("has_ceiling", hasCeiling)
    .setBoolean("has_raids", hasRaids)
    .setBoolean("bed_works", bedWorks)
    .setBoolean("respawn_anchor_works", respawnAnchorWorks)
    .setFloat("ambient_light", ambientLight)
    .apply { fixedTime?.let { setLong("fixed_time", it) } }
    .setString("infiniburn", infiniburn.asString())
    .setString("effects", effects.asString())
    .setInt("min_y", minimumY)
    .setInt("height", height)
    .setInt("logical_height", logicalHeight)
    .setDouble("coordinate_scale", coordinateScale)
