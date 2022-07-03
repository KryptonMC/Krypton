/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map.color

/**
 * The brightness of a map colour.
 *
 * @param modifier the modifier for this brightness
 */
public enum class MapColorBrightness(public val modifier: Int) {

    LOW(180),
    NORMAL(220),
    HIGH(255),
    LOWEST(135)
}
