package org.kryptonmc.krypton.api.world

interface WorldBorder {

    /**
     * The world that this border is bound to
     */
    val world: World

    /**
     * The size, or diameter, of the world border
     */
    val size: Double

    /**
     * The center position of the world border
     *
     * Note: This position's Y will always be -1
     */
    val center: Location

    /**
     * The damage multiplier for this border
     */
    val damageMultiplier: Double
}