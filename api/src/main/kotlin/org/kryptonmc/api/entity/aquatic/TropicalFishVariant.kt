/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.aquatic

/**
 * A variant of a tropical fish.
 *
 * The shape of a tropical fish is always one of two unnamed options, and the
 * pattern is one of six unnamed options, and together, they form the 12
 * different variants of tropical fish, excluding colouring.
 *
 * @param shape the shape of the fish
 * @param pattern the pattern displayed on the fish
 */
public enum class TropicalFishVariant(public val shape: Int, public val pattern: Int) {

    KOB(0, 0),
    SUNSTREAK(0, 1),
    SNOOPER( 0, 2),
    DASHER(0, 3),
    BRINLEY(0, 4),
    SPOTTY(0, 5),
    FLOPPER(1, 0),
    STRIPEY(1, 1),
    GLITTER(1, 2),
    BLOCKFISH(1, 3),
    BETTY(1, 4),
    CLAYFISH(1, 5)
}
