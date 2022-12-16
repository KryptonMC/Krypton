/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
     * If this block has gravity, meaning that, when placed with one or more
     * air blocks beneath it, it will become a falling block entity, and fall
     * down towards the ground until it reaches a solid block, in which it will
     * stop falling and become a normal block.
     */
    @get:JvmName("hasGravity")
    public val hasGravity: Boolean

    /**
     * If this block can be respawned inside of.
     */
    @get:JvmName("canRespawnIn")
    public val canRespawnIn: Boolean

    /**
     * If this block has an associated block entity.
     */
    @get:JvmName("hasBlockEntity")
    public val hasBlockEntity: Boolean

    /**
     * The group of sounds for this block.
     */
    @get:JvmName("soundGroup")
    public val soundGroup: BlockSoundGroup

    override fun asBlock(): Block = this
}
