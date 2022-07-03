/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map.marker

import org.spongepowered.math.vector.Vector3i

/**
 * A marker that can appear on a map.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public sealed interface MapMarker {

    /**
     * The position of the marker in the world.
     */
    @get:JvmName("position")
    public val position: Vector3i
}
