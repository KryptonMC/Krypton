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
@Suppress("INAPPLICABLE_JVM_NAME")
public interface TropicalFish : SchoolingFish {

    /**
     * The base colour of this tropical fish.
     */
    @get:JvmName("baseColor")
    public var baseColor: DyeColor

    /**
     * The colour of the pattern on this tropical fish.
     */
    @get:JvmName("patternColor")
    public var patternColor: DyeColor

    /**
     * The variant that this tropical fish is, which determines its shape and
     * pattern.
     */
    @get:JvmName("variant")
    public var variant: TropicalFishVariant
}
