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
 * A sheep.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Sheep : Animal {

    /**
     * If this sheep has been sheared.
     */
    public var isSheared: Boolean

    /**
     * The colour of this sheep's wool.
     */
    @get:JvmName("woolColor")
    public var color: DyeColor
}
