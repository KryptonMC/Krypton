/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.entity.attribute.RangedAttributeType
import org.kryptonmc.krypton.util.math.Maths

class KryptonRangedAttributeType(
    defaultValue: Double,
    sendToClient: Boolean,
    translationKey: String,
    override val minimum: Double,
    override val maximum: Double
) : KryptonAttributeType(defaultValue, sendToClient, translationKey), RangedAttributeType {

    init {
        require(minimum <= maximum) { "Minimum value cannot be larger than maximum value!" }
        require(defaultValue >= minimum) { "Default value cannot be less than minimum value!" }
        require(defaultValue <= maximum) { "Default value cannot be greater than maximum value!" }
    }

    override fun sanitizeValue(value: Double): Double = if (value.isNaN()) minimum else Maths.clamp(value, minimum, maximum)
}
