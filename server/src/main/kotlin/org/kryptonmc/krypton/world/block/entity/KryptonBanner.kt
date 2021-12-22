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
package org.kryptonmc.krypton.world.block.entity

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.Banner
import org.kryptonmc.api.block.entity.BlockEntityTypes
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.entity.banner.save
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

class KryptonBanner(
    world: KryptonWorld,
    block: Block,
    position: Vector3i,
    override var baseColor: DyeColor
) : KryptonNameableBlockEntity(BlockEntityTypes.BANNER, world, block, position, TRANSLATION), Banner {

    override val patterns = mutableListOf<BannerPattern>()
    private var cachedItem: ItemStack? = null

    override fun pattern(index: Int): BannerPattern = patterns[index]

    override fun setPattern(index: Int, pattern: BannerPattern) {
        patterns[index] = pattern
        cachedItem = null
    }

    override fun patterns(patterns: Iterable<BannerPattern>) {
        this.patterns.clear()
        this.patterns.addAll(patterns)
        cachedItem = null
    }

    override fun addPattern(pattern: BannerPattern) {
        patterns.add(pattern)
        cachedItem = null
    }

    override fun removePattern(index: Int): BannerPattern {
        val removed = patterns.removeAt(index)
        cachedItem = null
        return removed
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        tag.getList("Patterns", CompoundTag.ID).forEachCompound {
            val type = BannerPatternType.fromCode(it.getString("Pattern")) ?: return@forEachCompound
            val color = Registries.DYE_COLORS[it.getInt("Color")] ?: return@forEachCompound
            patterns.add(BannerPattern.of(type, color))
        }
    }

    override fun saveAdditional(tag: CompoundTag.Builder): CompoundTag.Builder = super.saveAdditional(tag).apply {
        if (patterns.isNotEmpty()) list("Patterns", CompoundTag.ID, patterns.map(BannerPattern::save))
    }

    override fun asItemStack(): ItemStack = ItemStack.empty() // TODO

    companion object {

        private val TRANSLATION = Component.translatable("block.minecraft.banner")
    }
}
