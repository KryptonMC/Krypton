/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.monster

/**
 * A zombie
 */
public interface Zombie : Monster {

    /**
     * If this zombie is a baby
     */
    public val isBaby: Boolean

    /**
     * If this zombie is currently converting to a drowned
     */
    public val isConverting: Boolean
}
