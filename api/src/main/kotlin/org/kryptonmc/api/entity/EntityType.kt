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
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.translation.Translatable
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of entity.
 *
 * @param T the type of entity
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(EntityTypes::class)
@ImmutableType
public interface EntityType<out T : Entity> : Keyed, Translatable {

    /**
     * The category entities of this type are part of.
     */
    @get:JvmName("category")
    public val category: EntityCategory

    /**
     * If this entity type can be summoned, with the /summon command, or by
     * spawning the entity through [org.kryptonmc.api.world.World.spawnEntity].
     */
    public val isSummonable: Boolean

    /**
     * If entities of this type are immune to all types of fire damage.
     */
    public val isImmuneToFire: Boolean

    /**
     * The radius of the circle in which the client will track the movement of
     * entities of this type.
     */
    @get:JvmName("clientTrackingRange")
    public val clientTrackingRange: Int

    /**
     * The interval between when entities of this type will be updated.
     */
    @get:JvmName("updateInterval")
    public val updateInterval: Int

    /**
     * The base width of entities of this type.
     */
    @get:JvmName("width")
    public val width: Float

    /**
     * The base height of entities of this type.
     */
    @get:JvmName("height")
    public val height: Float

    /**
     * All blocks that entities of this type will not take damage from.
     */
    @get:JvmName("immuneTo")
    public val immuneTo: Set<Block>

    /**
     * The identifier for the loot table that entities of this type will use to
     * determine what drops they will have when they are killed.
     */
    // TODO: Ideally, replace this with something better when loot tables are implemented
    @get:JvmName("lootTable")
    public val lootTable: Key

    /**
     * Checks if entities of this type are immune (they will not be damaged by)
     * the given [block].
     *
     * @param block the block to check
     * @return true if entities are immune to the block, false otherwise
     */
    public fun isImmuneTo(block: BlockState): Boolean
}
