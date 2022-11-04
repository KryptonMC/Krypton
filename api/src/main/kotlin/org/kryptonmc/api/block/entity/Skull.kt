/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.auth.GameProfile

/**
 * A skull.
 */
public interface Skull : BlockEntity {

    /**
     * The owner of the skull.
     *
     * This determines what head is rendered on the skull.
     */
    public var owner: GameProfile?
}
