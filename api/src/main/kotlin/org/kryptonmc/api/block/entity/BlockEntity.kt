package org.kryptonmc.api.block.entity

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

/**
 * A block entity is an entity that exists as a companion to a block, so that
 * block can store information that would violate the functionality of blocks
 * on their own.
 *
 * These used to be known as tile entities, for all of you folks who remember
 * those days.
 */
public interface BlockEntity {

    /**
     * The type of this block entity.
     */
    public val type: BlockEntityType

    /**
     * The world this block entity is in.
     */
    public val world: World

    /**
     * The block that this entity is bound to.
     */
    public val block: Block

    /**
     * The position of this block entity.
     *
     * This will be identical to the position of the associated block.
     */
    public val position: Vector3i

    /**
     * If this block entity is currently valid.
     */
    public val isValid: Boolean
}
