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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.projectile.FireworkRocket
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonFireworkRocket(world: KryptonWorld) : KryptonProjectile(world, EntityTypes.FIREWORK_ROCKET), FireworkRocket {

    override var attachedEntity: Entity? = null
    override var life = 0
    override var lifetime = 0

    override val item: ItemStack
        get() {
            val raw = data[MetadataKeys.FIREWORK_ROCKET.ITEM]
            return if (raw.isEmpty()) DEFAULT_ITEM else raw
        }

    override var wasShotAtAngle: Boolean
        get() = data[MetadataKeys.FIREWORK_ROCKET.SHOT_AT_ANGLE]
        set(value) = data.set(MetadataKeys.FIREWORK_ROCKET.SHOT_AT_ANGLE, value)

    init {
        data.add(MetadataKeys.FIREWORK_ROCKET.ITEM)
        data.add(MetadataKeys.FIREWORK_ROCKET.ATTACHED)
        data.add(MetadataKeys.FIREWORK_ROCKET.SHOT_AT_ANGLE)
    }

    companion object {

        private val DEFAULT_ITEM = KryptonItemStack(ItemTypes.FIREWORK_ROCKET, 1, KryptonItemMeta.DEFAULT)
    }
}
