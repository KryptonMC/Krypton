package org.kryptonmc.krypton.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

object KryptonDimensionEffects {

    val OVERWORLD = register("overworld", true, true, false, false)
    val THE_NETHER = register("the_nether", false, false, true, false)
    val THE_END = register("the_end", false, false, false, true)

    private fun register(
        name: String,
        clouds: Boolean,
        celestialBodies: Boolean,
        fog: Boolean,
        endSky: Boolean
    ): KryptonDimensionEffect {
        val key = Key.key(name)
        return Registries.register(
            Registries.DIMENSION_EFFECTS,
            key,
            KryptonDimensionEffect(key, clouds, celestialBodies, fog, endSky)
        ) as KryptonDimensionEffect
    }
}
