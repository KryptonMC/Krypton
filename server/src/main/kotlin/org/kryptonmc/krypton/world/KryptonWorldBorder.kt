package org.kryptonmc.krypton.world

import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.WorldBorder

data class KryptonWorldBorder(
    override val size: Double,
    override val center: Location,
    override val damageMultiplier: Double,
    val safeZone: Double,
    val sizeLerpTarget: Double,
    val sizeLerpTime: Long,
    val warningBlocks: Double,
    val warningTime: Double
) : WorldBorder

/**
 * This is to avoid cyclic dependencies by allowing a world to build its own border
 */
data class BorderBuilder(
    val centerX: Double,
    val centerZ: Double,
    val damagePerBlock: Double,
    val size: Double,
    val safeZone: Double,
    val sizeLerpTarget: Double,
    val sizeLerpTime: Long,
    val warningBlocks: Double,
    val warningTime: Double
)