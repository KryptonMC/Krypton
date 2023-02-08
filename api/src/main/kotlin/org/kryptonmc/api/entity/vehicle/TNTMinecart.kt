/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.vehicle

/**
 * A minecart holding TNT.
 */
public interface TNTMinecart : MinecartLike {

    /**
     * If this TNT minecart is primed for explosion.
     */
    public val isPrimed: Boolean

    /**
     * The fuse of this TNT minecart.
     */
    public var fuse: Int

    /**
     * Primes this TNT minecart for explosion.
     */
    public fun prime()
}