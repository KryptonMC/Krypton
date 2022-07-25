/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.entity.animal.Bee
import org.spongepowered.math.vector.Vector3i

/**
 * A beehive.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Beehive : EntityStorageBlockEntity<Bee> {

    /**
     * Whether this beehive is sedated due to a campfire underneath it.
     */
    public val isSedated: Boolean

    /**
     * The position of a flower that one of the bees has found, so that other
     * bees in the beehive can find it.
     */
    @get:JvmName("flower")
    public var flower: Vector3i?
}
