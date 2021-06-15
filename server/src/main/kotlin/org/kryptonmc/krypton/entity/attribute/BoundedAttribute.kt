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
package org.kryptonmc.krypton.entity.attribute

import org.spongepowered.math.GenericMath

/**
 * An attribute that is bound between a specific minimum and maximum value.
 */
class BoundedAttribute(name: String, default: Double, val minValue: Double, val maxValue: Double) : Attribute(name, default) {

    init {
        require(minValue <= maxValue) { "Minimum value cannot be larger than the maximum value!" }
        require(default in minValue..maxValue) { "Default must be between the minimum and maximum value!" }
    }

    override fun sanitize(value: Double) = GenericMath.clamp(value, minValue, maxValue)
}
