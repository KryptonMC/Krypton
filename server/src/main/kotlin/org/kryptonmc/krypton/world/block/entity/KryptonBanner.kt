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
import org.kryptonmc.krypton.world.block.entity.banner.save
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

class KryptonBanner(
    position: Vector3i,
    block: Block,
    override var baseColor: DyeColor
) : KryptonNameableBlockEntity(BlockEntityTypes.BANNER, position, block, TRANSLATION), Banner {

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
