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

import org.kryptonmc.api.entity.animal.Sheep
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.SheepSerializer
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonSheep(world: KryptonWorld) : KryptonAnimal(world), Sheep {

    override val type: KryptonEntityType<Sheep>
        get() = KryptonEntityTypes.SHEEP
    override val serializer: EntitySerializer<KryptonSheep>
        get() = SheepSerializer

    override var isSheared: Boolean
        get() = data.getFlag(MetadataKeys.Sheep.FLAGS, FLAG_SHEARED)
        set(value) = data.setFlag(MetadataKeys.Sheep.FLAGS, FLAG_SHEARED, value)
    override var woolColor: DyeColor
        get() = KryptonRegistries.DYE_COLORS.get(data.get(MetadataKeys.Sheep.FLAGS).toInt() and WOOL_COLOR_MASK)!!
        set(value) {
            val old = data.get(MetadataKeys.Sheep.FLAGS).toInt() and CLEAR_WOOL_COLOR_MASK
            data.set(MetadataKeys.Sheep.FLAGS, (old or (KryptonRegistries.DYE_COLORS.idOf(value) and WOOL_COLOR_MASK)).toByte())
        }

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Sheep.FLAGS, 0)
    }

    companion object {

        private const val FLAG_SHEARED = 4
        private const val WOOL_COLOR_MASK = 15
        private const val CLEAR_WOOL_COLOR_MASK = 240

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(AttributeTypes.MAX_HEALTH, 8.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.23)
    }
}
