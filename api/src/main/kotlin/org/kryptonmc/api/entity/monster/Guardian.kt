/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.monster

/**
 * A monster that is found in ocean monuments.
 */
public interface Guardian : Monster {

    /**
     * Whether this guardian is moving, meaning its spikes are retracted.
     */
    public var isMoving: Boolean
}
