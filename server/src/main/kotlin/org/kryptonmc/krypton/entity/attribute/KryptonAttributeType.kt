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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.registry.Registries

open class KryptonAttributeType protected constructor(
    final override val defaultValue: Double,
    val sendToClient: Boolean,
    private val translationKey: String
) : AttributeType {

    override fun key(): Key = Registries.ATTRIBUTE.get(this)!!

    override fun translationKey(): String = translationKey

    override fun sanitizeValue(value: Double): Double = value
}
