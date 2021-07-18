package org.kryptonmc.krypton.world.fluid

import net.kyori.adventure.key.Key
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries

object Fluids {

    val EMPTY = register("empty", ItemTypes.AIR)
    val FLOWING_WATER = register("flowing_water", ItemTypes.WATER_BUCKET)
    val WATER = register("water", ItemTypes.WATER_BUCKET)
    val FLOWING_LAVA = register("flowing_lava", ItemTypes.LAVA_BUCKET)
    val LAVA = register("lava", ItemTypes.LAVA_BUCKET)

    private fun register(key: String, bucket: ItemType) = Registries.register(InternalRegistries.FLUID, key, Fluid(Key.key(key), bucket))
}
