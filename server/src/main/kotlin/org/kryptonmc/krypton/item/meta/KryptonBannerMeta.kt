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

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.item.meta.BannerMeta
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPattern
import org.kryptonmc.krypton.world.block.entity.banner.save
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonBannerMeta(data: CompoundTag) : AbstractItemMeta<KryptonBannerMeta>(data), BannerMeta {

    override val patterns: PersistentList<BannerPattern> = data.getList("Items", CompoundTag.ID).mapCompound(KryptonBannerPattern::from)

    override fun copy(data: CompoundTag): KryptonBannerMeta = KryptonBannerMeta(data)

    override fun withPatterns(patterns: List<BannerPattern>): BannerMeta = KryptonBannerMeta(data.setPatterns(patterns))

    override fun addPattern(pattern: BannerPattern): BannerMeta = withPatterns(patterns.add(pattern))

    override fun removePattern(index: Int): BannerMeta = withPatterns(patterns.removeAt(index))

    override fun removePattern(pattern: BannerPattern): BannerMeta = withPatterns(patterns.remove(pattern))

    override fun toBuilder(): BannerMeta.Builder = Builder()

    override fun toString(): String = "KryptonBannerMeta(${partialToString()}, patterns=$patterns)"

    class Builder() : KryptonItemMetaBuilder<BannerMeta.Builder, BannerMeta>(), BannerMeta.Builder {

        private val patterns = persistentListOf<BannerPattern>().builder()

        constructor(meta: BannerMeta) : this() {
            copyFrom(meta)
            patterns.addAll(meta.patterns)
        }

        override fun patterns(patterns: List<BannerPattern>): BannerMeta.Builder = apply {
            this.patterns.clear()
            this.patterns.addAll(patterns)
        }

        override fun addPattern(pattern: BannerPattern): BannerMeta.Builder = apply { patterns.add(pattern) }

        override fun build(): BannerMeta = KryptonBannerMeta(buildData().build())

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (patterns.isNotEmpty()) list("Patterns") { patterns.forEach(BannerPattern::save) }
        }
    }
}

private fun CompoundTag.setPatterns(patterns: List<BannerPattern>): CompoundTag {
    if (patterns.isEmpty()) return remove("Patterns")
    return put("Patterns", list { patterns.forEach(BannerPattern::save) })
}
