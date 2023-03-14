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
package org.kryptonmc.api.block

import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.item.ItemLike
import org.kryptonmc.api.state.StateHolder
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A block with certain properties.
 *
 * These are immutable and do not contain any state-specific
 * information, such as the world or location they are in, so
 * they can be easily reused in many places, which from a
 * technical standpoint, reduces allocations, but also makes
 * them much more thread-safe.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Blocks::class)
@ImmutableType
public interface Block : StateHolder<BlockState>, BlockLike, ItemLike, Translatable, Keyed {

    /**
     * How resistant this block is to explosions. Higher
     * means more resistant.
     */
    @get:JvmName("explosionResistance")
    public val explosionResistance: Double

    /**
     * The friction of this block.
     */
    @get:JvmName("friction")
    public val friction: Double

    /**
     * The group of sounds for this block.
     */
    @get:JvmName("soundGroup")
    public val soundGroup: BlockSoundGroup

    /**
     * If this block has gravity.
     *
     * If a block has gravity, and it is placed with one or more air blocks
     * beneath it, it will become a falling block entity, and fall down
     * towards the ground with an acceleration of ~9.8 m/s^2 until it reaches
     * a solid block, in which it will stop falling and become a normal block.
     *
     * @return true if this block has gravity
     */
    public fun hasGravity(): Boolean

    /**
     * If this block can be respawned inside of.
     *
     * @return true if this block can be respawned in
     */
    public fun canRespawnIn(): Boolean

    /**
     * If this block has an associated block entity.
     *
     * @return true if this block has a block entity
     */
    public fun hasBlockEntity(): Boolean

    override fun asBlock(): Block = this
}
