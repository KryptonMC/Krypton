/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.dimension.DimensionType

/**
 * Holder of all of the dynamic built-in registries.
 */
@Suppress("UndocumentedPublicProperty")
public object DynamicRegistries {

    @JvmField
    public val DIMENSION_TYPE: Registry<DimensionType> = builtin(ResourceKeys.DIMENSION_TYPE)
    @JvmField
    public val BIOME: Registry<Biome> = builtin(ResourceKeys.BIOME)

    @JvmStatic
    private fun <T> builtin(key: ResourceKey<out Registry<T>>): Registry<T> =
        requireNotNull(Registries.getRegistry(key)) { "Cannot find built-in registry $key!" }
}
