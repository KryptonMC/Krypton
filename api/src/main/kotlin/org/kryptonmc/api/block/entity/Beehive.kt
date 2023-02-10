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
import org.kryptonmc.api.util.Vec3i

/**
 * A beehive.
 */
public interface Beehive : EntityStorageBlockEntity<Bee> {

    /**
     * The position of a flower that one of the bees has found, so that other
     * bees in the beehive can find it.
     */
    public var flower: Vec3i?

    /**
     * Whether this beehive is sedated due to a campfire underneath it.
     *
     * @return true if this beehive is sedated
     */
    public fun isSedated(): Boolean
}
