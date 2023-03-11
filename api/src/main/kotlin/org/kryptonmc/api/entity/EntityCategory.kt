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

import net.kyori.adventure.key.Keyed
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A category of entity that applies certain spawning mechanics and behaviours.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(EntityCategories::class)
@ImmutableType
public interface EntityCategory : Keyed {

    /**
     * If the mob will be friendly towards the player.
     */
    public val isFriendly: Boolean

    /**
     * The distance that the mob has to be from the player to be despawned.
     */
    @get:JvmName("despawnDistance")
    public val despawnDistance: Int
}
