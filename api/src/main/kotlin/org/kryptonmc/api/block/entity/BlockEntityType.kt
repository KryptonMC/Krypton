/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.util.CataloguedBy

/**
 * A type of block entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(BlockEntityTypes::class)
public interface BlockEntityType : Keyed {

    /**
     * All of the blocks that block entities of this type can be bound to.
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
