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

import org.kryptonmc.api.entity.animal.Pig
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.PigSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonPig(world: KryptonWorld) : KryptonAnimal(world), Pig {

    override val type: KryptonEntityType<Pig>
        get() = KryptonEntityTypes.PIG
    override val serializer: EntitySerializer<KryptonPig>
        get() = PigSerializer

    override var isSaddled: Boolean
        get() = data.get(MetadataKeys.Pig.SADDLE)
        set(value) = data.set(MetadataKeys.Pig.SADDLE, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Pig.SADDLE, false)
        data.define(MetadataKeys.Pig.BOOST_TIME, 0)
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.25)
    }
}
