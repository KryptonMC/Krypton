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
package org.kryptonmc.api.tags

import net.kyori.adventure.key.Key
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.resource.ResourceKeys

/**
 * This file is auto-generated. Do not edit this manually!
 */
public object FluidTags {

    // @formatter:off
    @JvmField
    public val WATER: TagKey<Fluid> = get("water")
    @JvmField
    public val LAVA: TagKey<Fluid> = get("lava")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): TagKey<Fluid> = TagKey.of(ResourceKeys.FLUID, Key.key(key))
}
