/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.DynamicRegistryReference
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla dimension types.
 */
@Catalogue(DimensionType::class)
public object DimensionTypes {

    // @formatter:off
    @JvmField
    public val OVERWORLD: DynamicRegistryReference<DimensionType> = of("overworld")
    @JvmField
    public val OVERWORLD_CAVES: DynamicRegistryReference<DimensionType> = of("overworld_caves")
    @JvmField
    public val THE_NETHER: DynamicRegistryReference<DimensionType> = of("the_nether")
    @JvmField
    public val THE_END: DynamicRegistryReference<DimensionType> = of("the_end")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): DynamicRegistryReference<DimensionType> = DynamicRegistryReference.of(ResourceKeys.DIMENSION_TYPE, Key.key(name))
}
