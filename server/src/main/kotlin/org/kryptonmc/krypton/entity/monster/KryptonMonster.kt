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
package org.kryptonmc.krypton.entity.monster

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.monster.Monster
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonMonster(
    world: KryptonWorld,
    type: EntityType<out Monster>,
    attributeSupplier: AttributeSupplier
) : KryptonMob(world, type, attributeSupplier), Monster {

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes().add(AttributeTypes.ATTACK_DAMAGE)
    }
}
