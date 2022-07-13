/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.player.Player

/**
 * An animal that may be tamed by an entity, usually a player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Tamable : Animal {

    /**
     * If this tamable animal is currently tamed by an entity.
     */
    public var isTamed: Boolean

    /**
     * If this tamable animal is in the sitting position.
     */
    public var isSitting: Boolean

    /**
     * The entity that has tamed this animal, or null, if no entities own this
     * tamable animal.
     */
    @get:JvmName("tamer")
    public val tamer: LivingEntity?

    /**
     * Tames this animal, making the owner the given [tamer].
     *
     * @param tamer the player taming the animal
     */
    public fun tame(tamer: Player)
}
