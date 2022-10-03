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
import org.kryptonmc.api.entity.animal.Goat
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonGoat(world: KryptonWorld) : KryptonAnimal(world, EntityTypes.GOAT), Goat {

    override var canScream: Boolean
        get() = data.get(MetadataKeys.Goat.SCREAMING)
        set(value) = data.set(MetadataKeys.Goat.SCREAMING, value)

    init {
        data.add(MetadataKeys.Goat.SCREAMING, false)
    }

    override fun onAgeTransformation() {
        attribute(AttributeTypes.ATTACK_DAMAGE)?.baseValue = if (isBaby) BABY_ATTACK_DAMAGE else ADULT_ATTACK_DAMAGE
    }

    companion object {

        private const val BABY_ATTACK_DAMAGE = 1.0
        private const val ADULT_ATTACK_DAMAGE = 2.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.2)
            .add(AttributeTypes.ATTACK_DAMAGE, 2.0)
    }
}
