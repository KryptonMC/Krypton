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
package org.kryptonmc.api.block.entity

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.block.Block
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of block entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(BlockEntityTypes::class)
@ImmutableType
public interface BlockEntityType<T : BlockEntity> : Keyed {

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
