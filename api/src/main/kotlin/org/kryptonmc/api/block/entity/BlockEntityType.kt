package org.kryptonmc.api.block.entity

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.CataloguedBy

/**
 * A type of block entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(BlockEntityTypes::class)
public interface BlockEntityType {

    /**
     * The set of blocks that block entities of this type can be bound to.
     */
    @get:JvmName("applicableBlocks")
    public val applicableBlocks: Set<Block>

    /**
     * Returns true if the given [block] is applicable to block entities of
     * this type, or false otherwise.
     *
     * @param block the block
     * @return true if the block is applicable, false otherwise
     */
    public fun isApplicable(block: Block): Boolean
}
