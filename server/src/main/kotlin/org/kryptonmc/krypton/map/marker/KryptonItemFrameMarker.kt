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
package org.kryptonmc.krypton.map.marker

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.map.marker.ItemFrameMarker
import org.kryptonmc.krypton.entity.getVector3i
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class KryptonItemFrameMarker(override val position: Vector3i, override val entity: Entity, override val rotation: Int) : ItemFrameMarker {

    fun createId(): String = "frame-${position.x()},${position.y()},${position.z()}"

    object Factory : ItemFrameMarker.Factory {

        override fun of(position: Vector3i, entity: Entity, rotation: Int): ItemFrameMarker = KryptonItemFrameMarker(position, entity, rotation)
    }

    companion object {

        @JvmStatic
        fun from(world: KryptonWorld, data: CompoundTag): KryptonItemFrameMarker {
            val position = data.getVector3i("Pos") ?: Vector3i.ZERO
            val entity = requireNotNull(world.entityManager[data.getInt("EntityId")]) { "Cannot find entity with ID ${data.getInt("EntityId")}!" }
            return KryptonItemFrameMarker(position, entity, data.getInt("Rotation"))
        }
    }
}
