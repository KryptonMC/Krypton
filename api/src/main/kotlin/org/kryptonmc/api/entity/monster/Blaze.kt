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
 * A flying monster that shoots fireballs.
 */
public interface Blaze : Monster {

    /**
     * Whether this blaze is currently on fire.
     *
     * This usually happens when the blaze is ready to attack.
     */
    override var isOnFire: Boolean
}
