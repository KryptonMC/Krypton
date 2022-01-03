/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.CataloguedBy

/**
 * A category of entity that applies certain spawning mechanics and behaviours.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(EntityCategories::class)
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
