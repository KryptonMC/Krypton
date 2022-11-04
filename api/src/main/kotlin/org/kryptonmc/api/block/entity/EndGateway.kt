/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.spongepowered.math.vector.Vector3i

/**
 * An end gateway.
 */
public interface EndGateway : BlockEntity {

    /**
     * The position that a player will be teleported to when they enter this
     * gateway.
     */
    public var exitPosition: Vector3i

    /**
     * Whether this gateway will teleport a player to the exact exit position.
     *
     * If this is false, the gateway will attempt to find the closest possible
     * safe exit location to the exit position.
     */
    public var isExactTeleport: Boolean

    /**
     * The age, in ticks, of this gateway.
     *
     * If this age is less than 200 ticks, the beam will be magenta.
     * If this age is a multiple of 2400 ticks, the beam will be purple.
     */
    public var age: Int
}
