/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.item.meta.DyeColor

/**
 * A wolf.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Wolf : Tamable {

    /**
     * If this wolf is currently angry.
     */
    public var isAngry: Boolean

    /**
     * The colour of this wolf's collar.
     */
    @get:JvmName("collarColor")
    public var collarColor: DyeColor

    /**
     * If this wolf is currently begging for food.
     */
    public var isBeggingForFood: Boolean
}
