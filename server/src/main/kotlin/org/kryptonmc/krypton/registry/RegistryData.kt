package org.kryptonmc.krypton.registry

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey

class RegistryData<E : Any>(
    val key: ResourceKey<out Registry<E>>,
    val values: List<RegistryEntry<E>>
)
