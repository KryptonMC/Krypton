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

import com.google.common.collect.ImmutableList
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.item.meta.BannerMeta
import org.kryptonmc.krypton.util.collection.BuilderCollection
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPattern
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.list

class KryptonBannerMeta(data: CompoundTag) : AbstractItemMeta<KryptonBannerMeta>(data), BannerMeta {

    override val patterns: ImmutableList<BannerPattern> =
        mapToList(data, PATTERNS_TAG, CompoundTag.ID) { KryptonBannerPattern.load(it as CompoundTag) }

    override fun copy(data: CompoundTag): KryptonBannerMeta = KryptonBannerMeta(data)

    override fun withPatterns(patterns: List<BannerPattern>): BannerMeta = copy(put(data, PATTERNS_TAG, patterns, KryptonBannerPattern::save))

    override fun withPattern(pattern: BannerPattern): BannerMeta =
        copy(data.update(PATTERNS_TAG, CompoundTag.ID) { it.add(KryptonBannerPattern.save(pattern)) })

    override fun withoutPattern(index: Int): BannerMeta = copy(data.update(PATTERNS_TAG, CompoundTag.ID) { it.remove(index) })

    override fun withoutPattern(pattern: BannerPattern): BannerMeta =
        copy(data.update(PATTERNS_TAG, CompoundTag.ID) { it.remove(KryptonBannerPattern.save(pattern)) })

    override fun toBuilder(): BannerMeta.Builder = Builder()

    class Builder : KryptonItemMetaBuilder<BannerMeta.Builder, BannerMeta>, BannerMeta.Builder {

        private var patterns: MutableCollection<BannerPattern>

        constructor() : super() {
            patterns = BuilderCollection()
        }

        constructor(meta: KryptonBannerMeta) : super(meta) {
            patterns = BuilderCollection(meta.patterns)
        }

        override fun patterns(patterns: Collection<BannerPattern>): Builder = apply { this.patterns = BuilderCollection(patterns) }

        override fun addPattern(pattern: BannerPattern): Builder = apply { patterns.add(pattern) }

        override fun build(): KryptonBannerMeta = KryptonBannerMeta(buildData().build())

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (patterns.isNotEmpty()) list(PATTERNS_TAG) { patterns.forEach(KryptonBannerPattern::save) }
        }
    }

    companion object {

        private const val PATTERNS_TAG = "Patterns"
    }
}
