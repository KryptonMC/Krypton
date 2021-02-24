package org.kryptonmc.krypton.world

data class WorldBorder(
    val size: Double,
    val centerX: Double,
    val centerZ: Double,
    val damageMultiplier: Double,
    val safeZone: Double,
    val sizeLerpTarget: Double,
    val sizeLerpTime: Long,
    val warningBlocks: Double,
    val warningTime: Double
)