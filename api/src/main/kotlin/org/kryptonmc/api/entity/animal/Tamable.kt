/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player
import java.util.UUID

/**
 * An animal that may be tamed by an entity, usually a player.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Tamable : Animal {

    /**
     * If this tamable animal is currently tame.
     */
    public var isTame: Boolean

    /**
     * If this tamable animal is in the sitting position.
     */
    public var isSitting: Boolean

    /**
     * If the owner of this animal has ordered it to sit.
     */
    public var isOrderedToSit: Boolean

    /**
     * The UUID of the entity that has tamed this animal.
     */
    @get:JvmName("ownerUUID")
    public var ownerUUID: UUID

    /**
     * The entity that has tamed this animal.
     */
    @get:JvmName("owner")
    public val owner: Entity

    /**
     * Tames this animal, making the owner the given [tamer].
     *
     * @param tamer the player taming the animal
     */
    public fun tame(tamer: Player)
}
