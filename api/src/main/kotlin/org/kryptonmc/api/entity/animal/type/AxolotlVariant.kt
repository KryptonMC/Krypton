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
 *
 * @param isCommon if this axolotl variant is common to find, currently only
 * applies to the blue variant
 */
public enum class AxolotlVariant(public val isCommon: Boolean) {

    LUCY(true),
    WILD(true),
    GOLD(true),
    CYAN(true),
    BLUE(false)
}
