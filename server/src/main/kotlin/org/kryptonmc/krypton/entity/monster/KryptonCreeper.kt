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

import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.monster.Creeper
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.monster.CreeperSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCreeper(world: KryptonWorld) : KryptonMonster(world), Creeper {

    override val type: KryptonEntityType<Creeper>
        get() = KryptonEntityTypes.CREEPER
    override val serializer: EntitySerializer<KryptonCreeper>
        get() = CreeperSerializer

    override var fuse: Short = 0
    override var explosionRadius: Int = 0

    override var isCharged: Boolean
        get() = data.get(MetadataKeys.Creeper.CHARGED)
        set(value) = data.set(MetadataKeys.Creeper.CHARGED, value)
    override var isIgnited: Boolean
        get() = data.get(MetadataKeys.Creeper.IGNITED)
        set(value) = data.set(MetadataKeys.Creeper.IGNITED, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Creeper.STATE, -1)
        data.define(MetadataKeys.Creeper.CHARGED, false)
        data.define(MetadataKeys.Creeper.IGNITED, false)
    }

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes().add(AttributeTypes.MOVEMENT_SPEED, 0.25)
    }
}
