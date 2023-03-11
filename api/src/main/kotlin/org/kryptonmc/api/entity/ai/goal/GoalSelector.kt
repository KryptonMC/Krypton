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
package org.kryptonmc.api.entity.ai.goal

import org.kryptonmc.api.entity.Entity

/**
 * Something that selects goals for an entity.
 */
public interface GoalSelector {

    /**
     * All the goals that this selector will process.
     */
    public val availableGoals: Collection<Goal>

    /**
     * All the target finders that this selector will try to find targets
     * with.
     *
     * This works like a fallback system. It will search all the finders in
     * order and use the first one that finds a target.
     */
    public val targetFinders: Collection<TargetFinder>

    /**
     * Adds the given [goal] to this goal selector with the given [priority].
     *
     * The priority system is very simple. The lower the priority, the higher
     * the goal is on the list of goals to be executed.
     *
     * For example, a goal with priority 0 will be executed before a goal with
     * priority 1.
     *
     * @param priority the priority of the goal
     * @param goal the goal to add
     */
    public fun addGoal(priority: Int, goal: Goal)

    /**
     * Removes the given [goal] from this goal selector.
     *
     * @param goal the goal to remove
     */
    public fun removeGoal(goal: Goal)

    /**
     * Adds the given target [finder] to this goal selector with the
     * given [priority].
     *
     * The priority system is very simple. The lower the priority, the higher
     * the target finder is on the list when searching for targets.
     *
     * For example, a target finder with priority 0 will be queried for a
     * target before a target finder with priority 1.
     *
     * @param priority the priority of the target finder
     * @param finder the target finder to add
     */
    public fun addTargetFinder(priority: Int, finder: TargetFinder)

    /**
     * Removes the given target [finder] from this goal selector.
     *
     * @param finder the target finder to remove
     */
    public fun removeTargetFinder(finder: TargetFinder)

    /**
     * Finds the target for this goal selector, trying all target finders
     * currently in use until it finds one that returns a non-null target.
     *
     * This is intended to be used by goals to easily find targets without
     * having to search the finders manually.
     *
     * This will return null if no target could be found. This could be
     * because there are no target finders in use, or because none of the
     * target finders produced a non-null target.
     *
     * @return the target, or null if no target could be found
     */
    public fun findTarget(): Entity?
}
