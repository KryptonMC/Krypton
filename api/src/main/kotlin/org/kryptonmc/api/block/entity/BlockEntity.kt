/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.World

/**
 * A block entity is an entity that exists as a companion to a block, so that
 * block can store information that would violate the functionality of blocks
 * on their own.
 *
 * Despite the name, however, these do not behave anything like regular
 * entities. They do not actually exist to the end user, cannot be seen, and
 * only exist to hold data that blocks cannot.
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
    public val position: Vec3i
}
