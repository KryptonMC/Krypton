/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.CataloguedBy

/**
 * The canvas of a painting.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Canvases::class)
public interface Canvas : Keyed {

    /**
     * The width of this motive.
     */
    @get:JvmName("width")
    public val width: Int

    /**
     * The height of this motive.
     */
    @get:JvmName("height")
    public val height: Int
}
