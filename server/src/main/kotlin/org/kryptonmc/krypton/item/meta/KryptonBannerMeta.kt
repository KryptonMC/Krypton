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

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.item.meta.BannerMeta
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPattern
import org.kryptonmc.krypton.world.block.entity.banner.save
import org.kryptonmc.nbt.CompoundTag

class KryptonBannerMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: PersistentList<Component>,
    hideFlags: Int,
    canDestroy: ImmutableSet<Block>,
    canPlaceOn: ImmutableSet<Block>,
    override val patterns: PersistentList<BannerPattern>
) : AbstractItemMeta<KryptonBannerMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), BannerMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getName(),
        tag.getLore(),
        tag.getInt("HideFlags"),
        tag.getBlocks("CanDestroy"),
        tag.getBlocks("CanPlaceOn"),
        extractPatterns(tag)
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
    ): KryptonBannerMeta = copy(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, patterns)

    override fun saveData(): CompoundTag.Builder = super.saveData().apply {
        list("Patterns", CompoundTag.ID, patterns.map(BannerPattern::save))
    }

    override fun withPatterns(patterns: Iterable<BannerPattern>): BannerMeta = copy(patterns = patterns.toPersistentList())

    override fun addPattern(pattern: BannerPattern): BannerMeta = copy(patterns = patterns.add(pattern))

    override fun removePattern(index: Int): BannerMeta = copy(patterns = patterns.removeAt(index))

    override fun removePattern(pattern: BannerPattern): BannerMeta = copy(patterns = patterns.remove(pattern))

    override fun toBuilder(): BannerMeta.Builder = Builder(this)

    private fun copy(
        damage: Int = this.damage,
        isUnbreakable: Boolean = this.isUnbreakable,
        customModelData: Int = this.customModelData,
        name: Component? = this.name,
        lore: PersistentList<Component> = this.lore,
        hideFlags: Int = this.hideFlags,
        canDestroy: ImmutableSet<Block> = this.canDestroy,
        canPlaceOn: ImmutableSet<Block> = this.canPlaceOn,
        patterns: PersistentList<BannerPattern> = this.patterns
    ): KryptonBannerMeta = KryptonBannerMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, patterns)

    class Builder() : KryptonItemMetaBuilder<BannerMeta.Builder, BannerMeta>(), BannerMeta.Builder {

        private val patterns = persistentListOf<BannerPattern>().builder()

        constructor(meta: BannerMeta) : this() {
            patterns.addAll(meta.patterns)
        }

        override fun patterns(patterns: Iterable<BannerPattern>): BannerMeta.Builder = apply {
            this.patterns.clear()
            this.patterns.addAll(patterns)
        }

        override fun addPattern(pattern: BannerPattern): BannerMeta.Builder = apply { patterns.add(pattern) }

        override fun build(): BannerMeta = KryptonBannerMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore.build(),
            hideFlags,
            canDestroy.build(),
            canPlaceOn.build(),
            patterns.build()
        )
    }

    companion object {

        @JvmStatic
        private fun extractPatterns(tag: CompoundTag): PersistentList<BannerPattern> {
            val patterns = persistentListOf<BannerPattern>().builder()
            tag.getList("Patterns", CompoundTag.ID).forEachCompound { patterns.add(KryptonBannerPattern(it)) }
            return patterns.build()
        }
    }
}
