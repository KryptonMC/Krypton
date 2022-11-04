/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.aquatic

import org.kryptonmc.api.item.data.DyeColor

/**
 * A tropical fish.
 */
public interface TropicalFish : SchoolingFish {

    /**
     * The base colour of this tropical fish.
     */
    public var baseColor: DyeColor

    /**
     * The colour of the pattern on this tropical fish.
     */
    public var patternColor: DyeColor

    /**
     * The variant that this tropical fish is, which determines its shape and
     * pattern.
     */
    public var variant: TropicalFishVariant
}
