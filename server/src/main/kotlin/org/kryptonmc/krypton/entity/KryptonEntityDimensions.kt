/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.space.BoundingBox

@JvmRecord
data class KryptonEntityDimensions(
    override val width: Float,
    override val height: Float,
    override val isFixed: Boolean
) : EntityDimensions {

    override fun scale(width: Float, height: Float): EntityDimensions {
        if (isFixed || width == 1F && height == 1F) return this
        return KryptonEntityDimensions(this.width * width, this.height * height, false)
    }

    override fun toBoundingBox(x: Double, y: Double, z: Double): BoundingBox {
        val center = width / 2.0
        return BoundingBox(
            x - center,
            y,
            z - center,
            x + center,
            y + height,
            z + center
        )
    }

    object Factory : EntityDimensions.Factory {

        override fun scalable(width: Float, height: Float): EntityDimensions = KryptonEntityDimensions(width, height, false)

        override fun fixed(width: Float, height: Float): EntityDimensions = KryptonEntityDimensions(width, height, true)
    }
}
