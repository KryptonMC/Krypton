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
package org.kryptonmc.krypton.world.block.property

import com.mojang.serialization.Codec
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.krypton.util.successOrError

sealed class KryptonProperty<T : Comparable<T>>(
    override val name: String,
    override val type: Class<T>,
    override val values: Set<T>
) : Property<T> {

    val codec: Codec<T> = Codec.STRING.comapFlatMap(
        { key -> fromString(key).successOrError("Unable to read property $this with value $key") },
        ::toString
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as KryptonProperty<*>
        return name == other.name && type == other.type
    }

    override fun hashCode(): Int = 31 * type.hashCode() + name.hashCode()

    override fun toString(): String = "${javaClass.simpleName}(name=$name, type=${type.simpleName}, values=$values)"
}
