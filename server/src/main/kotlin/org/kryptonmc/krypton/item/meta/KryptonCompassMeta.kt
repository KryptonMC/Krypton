/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.item.meta

import org.kryptonmc.api.item.meta.CompassMeta
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.util.nbt.getBlockPos
import org.kryptonmc.krypton.util.nbt.getNullableCompound
import org.kryptonmc.krypton.util.nbt.putBlockPos
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.serialization.nbt.NbtOps

class KryptonCompassMeta(data: CompoundTag) : AbstractItemMeta<KryptonCompassMeta>(data), CompassMeta {

    override val isTrackingLodestone: Boolean = data.getBoolean(TRACKED_TAG)
    override val lodestoneDimension: ResourceKey<World>? = Codecs.DIMENSION.read(data.get(DIMENSION_TAG), NbtOps.INSTANCE).result().orElse(null)
    override val lodestonePosition: Vec3i? = data.getNullableCompound(POS_TAG)?.getBlockPos()

    override fun copy(data: CompoundTag): KryptonCompassMeta = KryptonCompassMeta(data)

    override fun withLodestone(dimension: ResourceKey<World>, position: Vec3i): KryptonCompassMeta =
        copy(data.putBoolean(TRACKED_TAG, true).putString(DIMENSION_TAG, dimension.location.asString()).putBlockPos(POS_TAG, position))

    override fun withoutLodestone(): KryptonCompassMeta = copy(CompoundTag.EMPTY)

    override fun toBuilder(): CompassMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<CompassMeta.Builder, CompassMeta>, CompassMeta.Builder {

        private var tracking = false
        private var dimension: ResourceKey<World>? = null
        private var position: Vec3i? = null

        constructor() : super()

        constructor(meta: KryptonCompassMeta) : super(meta) {
            tracking = meta.isTrackingLodestone
            dimension = meta.lodestoneDimension
            position = meta.lodestonePosition
        }

        override fun lodestone(dimension: ResourceKey<World>, position: Vec3i): Builder = apply {
            tracking = true
            this.dimension = dimension
            this.position = position
        }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            putBoolean(TRACKED_TAG, tracking)
            if (dimension != null) putString(DIMENSION_TAG, dimension!!.location.asString())
            if (position != null) putBlockPos(POS_TAG, position!!)
        }

        override fun build(): KryptonCompassMeta = KryptonCompassMeta(buildData().build())
    }

    companion object {

        private const val TRACKED_TAG = "LodestoneTracked"
        private const val DIMENSION_TAG = "LodestoneDimension"
        private const val POS_TAG = "LodestonePos"
    }
}
