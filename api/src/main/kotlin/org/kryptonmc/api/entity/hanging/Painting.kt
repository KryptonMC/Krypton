/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

/**
 * A painting that may display a [Picture].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Painting : HangingEntity {

    /**
     * The picture displayed on this painting, or null if this painting is
     * blank.
     */
    @get:JvmName("canvas")
    public val picture: Picture?
}
