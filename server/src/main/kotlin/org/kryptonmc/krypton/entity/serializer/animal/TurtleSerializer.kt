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
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.krypton.entity.animal.KryptonTurtle
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

object TurtleSerializer : EntitySerializer<KryptonTurtle> {

    override fun load(entity: KryptonTurtle, data: CompoundTag) {
        AgeableSerializer.load(entity, data)
        entity.home = Vector3i.from(data.getInt("HomePosX"), data.getInt("HomePosY"), data.getInt("HomePosZ"))
        entity.destination = Vector3i.from(data.getInt("TravelPosX"), data.getInt("TravelPosY"), data.getInt("TravelPosZ"))
        entity.hasEgg = data.getBoolean("HasEgg")
    }

    override fun save(entity: KryptonTurtle): CompoundTag.Builder = AgeableSerializer.save(entity).apply {
        putInt("HomePosX", entity.home.x())
        putInt("HomePosY", entity.home.y())
        putInt("HomePosZ", entity.home.z())
        putBoolean("HasEgg", entity.hasEgg)
        putInt("TravelPosX", entity.destination.x())
        putInt("TravelPosY", entity.destination.y())
        putInt("TravelPosZ", entity.destination.z())
    }
}
