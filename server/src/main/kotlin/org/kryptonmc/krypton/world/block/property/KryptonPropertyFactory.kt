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

import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.util.StringSerializable

object KryptonPropertyFactory : Property.Factory {

    override fun forBoolean(name: String) = BooleanProperty(name)

    override fun forInt(name: String, values: Set<Int>) = IntProperty(name, values)

    override fun <E> forEnum(
        name: String,
        type: Class<E>,
        values: Set<E>
    ): Property<E> where E : Enum<E>, E : StringSerializable = EnumProperty(name, type, values)
}
