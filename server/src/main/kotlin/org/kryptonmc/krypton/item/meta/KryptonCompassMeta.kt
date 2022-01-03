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

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentList
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.CompassMeta
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.entity.getVector3i
import org.kryptonmc.krypton.entity.vector3i
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

@Suppress("EqualsOrHashCode")
class KryptonCompassMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: PersistentList<Component>,
    hideFlags: Int,
    canDestroy: ImmutableSet<Block>,
    canPlaceOn: ImmutableSet<Block>,
    override val isTrackingLodestone: Boolean,
    override val lodestoneDimension: ResourceKey<World>?,
    override val lodestonePosition: Vector3i?
) : AbstractItemMeta<KryptonCompassMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), CompassMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getName(),
        tag.getLore(),
        tag.getInt("HideFlags"),
        tag.getBlocks("CanDestroy"),
        tag.getBlocks("CanPlaceOn"),
        tag.getBoolean("LodestoneTracking"),
        tag["LodestoneDimension"]?.parseDimension(),
        tag.getVector3i("LodestonePos")
    )

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: PersistentList<Component>,
        hideFlags: Int,
        canDestroy: ImmutableSet<Block>,
        canPlaceOn: ImmutableSet<Block>
    ): KryptonCompassMeta = copy(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)

    override fun saveData(): CompoundTag.Builder = super.saveData().apply {
        boolean("LodestoneTracked", isTrackingLodestone)
        if (lodestoneDimension != null) string("LodestoneDimension", lodestoneDimension.location.asString())
        if (lodestonePosition != null) vector3i("LodestonePos", lodestonePosition)
    }

    override fun withLodestone(
        dimension: ResourceKey<World>,
        position: Vector3i
    ): KryptonCompassMeta = copy(tracking = true, dimension = dimension, position = position)

    override fun withoutLodestone(): KryptonCompassMeta = copy(tracking = false, dimension = null, position = null)

    override fun toBuilder(): CompassMeta.Builder = Builder(this)

    private fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: PersistentList<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: ImmutableSet<Block> = this.canDestroy,
        canPlaceOn: ImmutableSet<Block> = this.canPlaceOn,
        tracking: Boolean = isTrackingLodestone,
        dimension: ResourceKey<World>? = lodestoneDimension,
        position: Vector3i? = lodestonePosition
    ): KryptonCompassMeta = KryptonCompassMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        tracking,
        dimension,
        position
    )

    override fun equalTo(other: KryptonCompassMeta): Boolean = super.equalTo(other) &&
            isTrackingLodestone == other.isTrackingLodestone &&
            lodestoneDimension == other.lodestoneDimension &&
            lodestonePosition == other.lodestonePosition

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isTrackingLodestone.hashCode()
        result = 31 * result + lodestoneDimension.hashCode()
        result = 31 * result + lodestonePosition.hashCode()
        return result
    }

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

        override fun build(): KryptonCompassMeta = KryptonCompassMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore.build(),
            hideFlags,
            canDestroy.build(),
            canPlaceOn.build(),
            tracking,
            dimension,
            position
        )
    }
}
