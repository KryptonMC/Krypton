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
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla mob categories.
 */
@Catalogue(EntityCategory::class)
public object EntityCategories {

    @JvmField
    public val MONSTER: RegistryReference<EntityCategory> = of("monster")
    @JvmField
    public val CREATURE: RegistryReference<EntityCategory> = of("creature")
    @JvmField
    public val AMBIENT: RegistryReference<EntityCategory> = of("ambient")
    @JvmField
    public val UNDERGROUND_WATER_CREATURE: RegistryReference<EntityCategory> = of("underground_water_creature")
    @JvmField
    public val WATER_CREATURE: RegistryReference<EntityCategory> = of("water_creature")
    @JvmField
    public val WATER_AMBIENT: RegistryReference<EntityCategory> = of("water_ambient")
    @JvmField
    public val MISC: RegistryReference<EntityCategory> = of("misc")

    @JvmStatic
    private fun of(name: String): RegistryReference<EntityCategory> = RegistryReference.of(Registries.ENTITY_CATEGORIES, Key.key(name))
}
