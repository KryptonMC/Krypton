/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

/**
 * A variant of an axolotl.
 */
public enum class AxolotlVariant {

    LUCY,
    WILD,
    GOLD,
    CYAN,
    BLUE;

    /**
     * Gets whether this axolotl variant is common to find.
     *
     * Currently, only the blue variant has this property set to false. All
     * other variants are common.
     *
     * @return true if common, false otherwise
     */
    public fun isCommon(): Boolean = this != BLUE
}
