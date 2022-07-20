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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Mooshroom
import org.kryptonmc.api.entity.animal.type.MooshroomVariant
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonMooshroom(world: KryptonWorld) : KryptonCow(world, EntityTypes.MOOSHROOM), Mooshroom {

    override var variant: MooshroomVariant
        get() = deserializeType(data.get(MetadataKeys.Mooshroom.TYPE))
        set(value) = data.set(MetadataKeys.Mooshroom.TYPE, value.name.lowercase())

    init {
        data.add(MetadataKeys.Mooshroom.TYPE, MooshroomVariant.RED.name.lowercase())
    }

    companion object {

        private val TYPE_NAMES = MooshroomVariant.values().associateBy { it.name.lowercase() }

        @JvmStatic
        fun deserializeType(name: String): MooshroomVariant = TYPE_NAMES.getOrDefault(name, MooshroomVariant.RED)
    }
}
