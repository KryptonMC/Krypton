package org.kryptonmc.krypton.world.block

import org.kryptonmc.api.block.Block
import org.kryptonmc.krypton.registry.tags.TagManager
import org.kryptonmc.krypton.registry.tags.TagType

private val BLOCK_TAGS = TagManager.tags[TagType.BLOCKS.identifier]!!

private val LEAVES_TAGS = BLOCK_TAGS.first { it.name.value() == "leaves" }.values

val Block.isLeaves: Boolean
    get() = key in LEAVES_TAGS

private val NEEDS_STONE_TAGS = BLOCK_TAGS.first { it.name.value() == "needs_stone_tool" }.values
private val NEEDS_IRON_TAGS = BLOCK_TAGS.first { it.name.value() == "needs_iron_tool" }.values
private val NEEDS_DIAMOND_TAGS = BLOCK_TAGS.first { it.name.value() == "needs_diamond_tool" }.values

val Block.needsStone: Boolean
    get() = key in NEEDS_STONE_TAGS

val Block.needsIron: Boolean
    get() = key in NEEDS_IRON_TAGS

val Block.needsDiamond: Boolean
    get() = key in NEEDS_DIAMOND_TAGS
