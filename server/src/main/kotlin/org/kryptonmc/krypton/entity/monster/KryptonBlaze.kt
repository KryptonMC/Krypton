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

import org.kryptonmc.api.entity.monster.Blaze
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonBlaze(world: KryptonWorld) : KryptonMonster(world), Blaze {

    override val type: KryptonEntityType<KryptonBlaze>
        get() = KryptonEntityTypes.BLAZE

    override var isOnFire: Boolean
        get() = data.getFlag(MetadataKeys.Blaze.FLAGS, 0)
        set(value) = data.setFlag(MetadataKeys.Blaze.FLAGS, 0, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Blaze.FLAGS, 0)
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes()
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, 6.0)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, 0.23F.toDouble())
            .add(KryptonAttributeTypes.FOLLOW_RANGE, 48.0)
    }
}
