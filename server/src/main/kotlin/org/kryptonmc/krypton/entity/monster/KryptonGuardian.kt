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

import org.kryptonmc.api.entity.monster.Guardian
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonGuardian(world: KryptonWorld) : KryptonMonster(world), Guardian {

    override val type: KryptonEntityType<KryptonGuardian>
        get() = KryptonEntityTypes.GUARDIAN

    override var isMoving: Boolean
        get() = data.get(MetadataKeys.Guardian.MOVING)
        set(value) = data.set(MetadataKeys.Guardian.MOVING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Guardian.MOVING, false)
        data.define(MetadataKeys.Guardian.TARGET_ID, 0)
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes()
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, 6.0)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, 0.5)
            .add(KryptonAttributeTypes.FOLLOW_RANGE, 16.0)
            .add(KryptonAttributeTypes.MAX_HEALTH, 30.0)
    }
}
