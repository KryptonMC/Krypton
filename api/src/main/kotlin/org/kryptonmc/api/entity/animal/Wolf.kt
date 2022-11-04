/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.item.data.DyeColor

/**
 * A wolf.
 */
public interface Wolf : Tamable {

    /**
     * If this wolf is currently angry.
     */
    public var isAngry: Boolean

    /**
     * The colour of this wolf's collar.
     */
    public var collarColor: DyeColor

    /**
     * If this wolf is currently begging for food.
     */
    public var isBeggingForFood: Boolean
}
