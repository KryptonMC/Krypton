/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.inventory.InventoryHolder
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
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BlockEntity : InventoryHolder {

    /**
     * The type of this block entity.
     */
    @get:JvmName("type")
    public val type: BlockEntityType

    /**
     * The world this block entity is in.
     */
    @get:JvmName("world")
    public val world: World

    /**
     * The block that this entity is bound to.
     */
    @get:JvmName("block")
    public val block: Block

    /**
     * The position of this block entity.
     *
     * This will be identical to the position of the associated block.
     */
    @get:JvmName("position")
    public val position: Vector3i

    /**
     * If this block entity is currently valid.
     */
    public val isValid: Boolean
}
