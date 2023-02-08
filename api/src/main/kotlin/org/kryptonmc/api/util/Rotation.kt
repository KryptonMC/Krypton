/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

/**
 * A three-dimensional rotation of an object.
 *
 * @property x The rotation on the X axis.
 * @property y The rotation on the Y axis.
 * @property z The rotation on the Z axis.
 */
@JvmRecord
public data class Rotation(public val x: Float, public val y: Float, public val z: Float) {

    public companion object {

        /**
         * A rotation with all values set to zero.
         */
        @JvmField
        public val ZERO: Rotation = Rotation(0F, 0F, 0F)
    }
}
