package org.kryptonmc.api.entity.ai.goal

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
}
