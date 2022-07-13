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
import org.kryptonmc.api.entity.animal.Sheep
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonSheep(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.SHEEP, ATTRIBUTES), Sheep {

    override var isSheared: Boolean
        get() = data.getFlag(MetadataKeys.SHEEP.FLAGS, 4)
        set(value) = data.setFlag(MetadataKeys.SHEEP.FLAGS, 4, value)
    override var woolColor: DyeColor
        get() = Registries.DYE_COLORS[data[MetadataKeys.SHEEP.FLAGS].toInt() and 15]!!
        set(value) {
            val old = data[MetadataKeys.SHEEP.FLAGS].toInt()
            data[MetadataKeys.SHEEP.FLAGS] = ((old and 240) or (Registries.DYE_COLORS.idOf(value) and 15)).toByte()
        }

    init {
        data.add(MetadataKeys.SHEEP.FLAGS, 0)
    }

    companion object {

        private val ATTRIBUTES = attributes().add(AttributeTypes.MAX_HEALTH, 8.0).add(AttributeTypes.MOVEMENT_SPEED, 0.23).build()
    }
}
