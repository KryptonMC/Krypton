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
package org.kryptonmc.krypton.entity.ambient

import org.kryptonmc.api.entity.ambient.Bat
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.ambient.BatSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonBat(world: KryptonWorld) : KryptonAmbientCreature(world), Bat {

    override val type: KryptonEntityType<KryptonBat>
        get() = KryptonEntityTypes.BAT
    override val serializer: EntitySerializer<KryptonBat>
        get() = BatSerializer

    override var isResting: Boolean
        get() = data.getFlag(MetadataKeys.Bat.FLAGS, FLAG_RESTING)
        set(value) = data.setFlag(MetadataKeys.Bat.FLAGS, FLAG_RESTING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Bat.FLAGS, 0)
    }

    companion object {

        private const val FLAG_RESTING = 0
        private const val DEFAULT_MAX_HEALTH = 6.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes().add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
    }
}
