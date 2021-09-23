package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.world.dimension.DimensionEffect

@JvmRecord
data class KryptonDimensionEffect(
    private val key: Key,
    override val clouds: Boolean,
    override val celestialBodies: Boolean,
    override val fog: Boolean,
    override val endSky: Boolean
) : DimensionEffect {

    override fun key(): Key = key
}
