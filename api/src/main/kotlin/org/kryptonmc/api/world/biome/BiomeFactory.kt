package org.kryptonmc.api.world.biome

import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface BiomeFactory {

    operator fun get(name: String): Biome
}
