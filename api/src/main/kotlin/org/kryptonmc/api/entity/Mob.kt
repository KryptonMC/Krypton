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

import org.kryptonmc.api.entity.ai.goal.GoalSelector
import org.kryptonmc.api.entity.ai.pathfinding.Navigator

/**
 * An entity with a simple artificial intelligence that can drop items.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Mob : LivingEntity, Equipable {

    /**
     * If this mob is persistent (will have its data saved on removal).
     */
    public var isPersistent: Boolean

    /**
     * If this mob can pick up loot.
     *
     * For example, if this mob can wear armour/use weapons it picks up.
     */
    @get:JvmName("canPickUpLoot")
    public var canPickUpLoot: Boolean

    /**
     * If this mob is hostile.
     */
    public var isAggressive: Boolean

    /**
     * The main hand of this mob.
     */
    public var mainHand: MainHand

    /**
     * If this mob has artificial intelligence.
     */
    @get:JvmName("hasAI")
    public var hasAI: Boolean

    /**
     * The goal selector for selecting goals and targets.
     */
    public val goalSelector: GoalSelector

    /**
     * The navigator used for pathfinding.
     */
    public val navigator: Navigator
}
