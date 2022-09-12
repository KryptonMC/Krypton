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
package org.kryptonmc.krypton.item.meta

import org.kryptonmc.api.item.meta.CompassMeta
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.entity.getVector3i
import org.kryptonmc.krypton.entity.vector3i
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.spongepowered.math.vector.Vector3i

@Suppress("EqualsOrHashCode")
class KryptonCompassMeta(data: CompoundTag) : AbstractItemMeta<KryptonCompassMeta>(data), CompassMeta {

    override val isTrackingLodestone: Boolean = data.getBoolean("LodestoneTracking")
    override val lodestoneDimension: ResourceKey<World>? = data.get("LodestoneDimension")?.parseDimension()
    override val lodestonePosition: Vector3i? = data.getVector3i("LodestonePos")

    override fun copy(data: CompoundTag): KryptonCompassMeta = KryptonCompassMeta(data)

    override fun withLodestone(dimension: ResourceKey<World>, position: Vector3i): KryptonCompassMeta {
        val positionData = compound {
            int("X", position.x())
            int("Y", position.y())
            int("Z", position.z())
        }
        val newData = data.putBoolean("LodestoneTracked", true)
            .putString("LodestoneDimension", dimension.location.asString())
            .put("LodestonePos", positionData)
        return KryptonCompassMeta(newData)
    }

    override fun withoutLodestone(): KryptonCompassMeta =
        KryptonCompassMeta(data.putBoolean("LodestoneTracked", false).remove("LodestoneDimension").remove("LodestonePos"))

    override fun toBuilder(): CompassMeta.Builder = Builder(this)

    override fun toString(): String = "KryptonCompassMeta(${partialToString()}, isTrackingLodestone=$isTrackingLodestone, " +
            "lodestoneDimension=$lodestoneDimension, lodestonePosition=$lodestonePosition)"

    class Builder() : KryptonItemMetaBuilder<CompassMeta.Builder, CompassMeta>(), CompassMeta.Builder {

        private var tracking = false
        private var dimension: ResourceKey<World>? = null
        private var position: Vector3i? = null

        constructor(meta: CompassMeta) : this() {
            copyFrom(meta)
            tracking = meta.isTrackingLodestone
            dimension = meta.lodestoneDimension
            position = meta.lodestonePosition
        }

        override fun lodestone(dimension: ResourceKey<World>, position: Vector3i): CompassMeta.Builder = apply {
            tracking = true
            this.dimension = dimension
            this.position = position
        }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            boolean("LodestoneTracked", tracking)
            if (dimension != null) string("LodestoneDimension", dimension!!.location.asString())
            if (position != null) vector3i("LodestonePos", position!!)
        }

        override fun build(): KryptonCompassMeta = KryptonCompassMeta(buildData().build())
    }
}
