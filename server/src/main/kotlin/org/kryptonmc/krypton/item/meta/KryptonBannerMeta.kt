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
