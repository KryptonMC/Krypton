/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.CompassMeta
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.entity.getVector3i
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.spongepowered.math.vector.Vector3i

class KryptonCompassMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val isTrackingLodestone: Boolean,
    override val lodestoneDimension: ResourceKey<World>?,
    override val lodestonePosition: Vector3i?
) : AbstractItemMeta<KryptonCompassMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), CompassMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getDisplay<StringTag, Component>("Name", StringTag.ID, null) { GsonComponentSerializer.gson().deserialize(it.value) },
        tag.getDisplay<ListTag, List<Component>>("Lore", ListTag.ID, emptyList()) { list ->
            list.map { GsonComponentSerializer.gson().deserialize((it as StringTag).value) }
        }!!,
        tag.getInt("HideFlags"),
        tag.getList("CanDestroy", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! },
        tag.getList("CanPlaceOn", StringTag.ID).mapTo(mutableSetOf()) { Registries.BLOCK[Key.key((it as StringTag).value)]!! },
        tag.getBoolean("LodestoneTracking"),
        tag["LodestoneDimension"]?.parseDimension(),
        tag.getVector3i("LodestonePos")
    )

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonCompassMeta = copy(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn)

    override fun withLodestone(
        dimension: ResourceKey<World>,
        position: Vector3i
    ): KryptonCompassMeta = copy(tracking = true, dimension = dimension, position = position)

    override fun withoutLodestone(): KryptonCompassMeta = copy(tracking = false, dimension = null, position = null)

    private fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: List<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: Set<Block> = this.canDestroy,
        canPlaceOn: Set<Block> = this.canPlaceOn,
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

    override fun toBuilder(): CompassMeta.Builder = Builder(this)

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
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            tracking,
            dimension,
            position
        )
    }
}
