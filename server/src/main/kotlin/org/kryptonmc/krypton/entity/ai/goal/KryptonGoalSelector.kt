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
package org.kryptonmc.krypton.entity.ai.goal

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.ai.goal.Goal
import org.kryptonmc.api.entity.ai.goal.GoalSelector
import org.kryptonmc.api.entity.ai.goal.TargetFinder
import java.util.Collections
import java.util.TreeSet

class KryptonGoalSelector : GoalSelector {

    private val goals = TreeSet<PrioritizedGoal>(Comparator.comparingInt { it.priority })
    private val finders = TreeSet<PrioritizedTargetFinder>(Comparator.comparingInt { it.priority })

    override val availableGoals: Collection<Goal>
        get() = Collections.unmodifiableCollection(goals)
    override val targetFinders: Collection<TargetFinder>
        get() = Collections.unmodifiableCollection(finders)

    override fun addGoal(priority: Int, goal: Goal) {
        goals.add(PrioritizedGoal(goal, priority))
    }

    override fun removeGoal(goal: Goal) {
        goals.forEach {
            if (it.goal !== goal || !it.isRunning()) return@forEach
            it.stop()
        }
        goals.removeIf { it.goal === goal }
    }

    override fun addTargetFinder(priority: Int, finder: TargetFinder) {
        finders.add(PrioritizedTargetFinder(finder, priority))
    }

    override fun removeTargetFinder(finder: TargetFinder) {
        finders.removeIf { it.finder === finder }
    }

    override fun findTarget(): Entity? {
        if (finders.isEmpty()) return null
        for (finder in finders) {
            if (!finder.isInUse()) continue
            val target = finder.findTarget()
            if (target != null) return target
        }
        return null
    }

    fun tick(time: Long) {
        for (goal in goals) {
            if (!goal.isRunning()) {
                // If the goal isn't running, try to see if we can start it
                if (goal.canUse()) goal.start()
                continue
            }
            if (goal.shouldStop()) {
                // If the goal should be stopped, stop it
                goal.stop()
            } else {
                // Update the goal
                goal.tick(time)
            }
        }
        for (finder in finders) {
            if (!finder.isInUse()) {
                // If the finder isn't being used, try to see if we can start using it
                if (finder.canUse()) finder.startUsing()
                continue
            }
            if (finder.shouldRemove()) {
                finder.remove()
            }
        }
    }

    private class PrioritizedGoal(val goal: Goal, val priority: Int) : Goal {

        private var running = false

        override fun canUse(): Boolean = goal.canUse()

        override fun start() {
            if (running) return
            running = true
            goal.start()
        }

        override fun shouldStop(): Boolean = goal.shouldStop()

        override fun stop() {
            if (!running) return
            running = false
            goal.stop()
        }

        override fun tick(time: Long) {
            goal.tick(time)
        }

        fun isRunning(): Boolean = running

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            return goal == (other as PrioritizedGoal).goal
        }

        override fun hashCode(): Int = goal.hashCode()
    }

    private class PrioritizedTargetFinder(val finder: TargetFinder, val priority: Int) : TargetFinder {

        private var inUse = false

        override fun findTarget(): Entity? = finder.findTarget()

        override fun canUse(): Boolean = finder.canUse()

        fun isInUse(): Boolean = inUse

        fun startUsing() {
            if (inUse) return
            inUse = true
            onStartUsing()
        }

        override fun onStartUsing() {
            finder.onStartUsing()
        }

        override fun shouldRemove(): Boolean = finder.shouldRemove()

        fun remove() {
            if (!inUse) return
            inUse = false
            onRemove()
        }

        override fun onRemove() {
            finder.onRemove()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            return finder == (other as PrioritizedTargetFinder).finder
        }

        override fun hashCode(): Int = finder.hashCode()
    }
}
