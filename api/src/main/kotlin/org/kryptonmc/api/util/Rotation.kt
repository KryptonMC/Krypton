/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
