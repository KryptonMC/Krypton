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

    fun tick(time: Long) {
        for (goal in goals) {
            if (!goal.isRunning()) {
                if (goal.canUse()) goal.start()
                continue
            }
            if (goal.shouldStop()) {
                goal.stop()
            } else {
                goal.tick(time)
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

        override fun findTarget(): Entity? = finder.findTarget()
    }
}
