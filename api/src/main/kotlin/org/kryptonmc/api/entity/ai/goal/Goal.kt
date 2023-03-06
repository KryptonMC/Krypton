package org.kryptonmc.api.entity.ai.goal

/**
 * A goal that an entity can achieve.
 */
public interface Goal {

    /**
     * Whether this goal can be used by the entity.
     *
     * @return true if this goal can be used
     */
    public fun canUse(): Boolean

    /**
     * Starts this goal.
     */
    public fun start()

    /**
     * Whether this goal should be stopped if it is currently running.
     *
     * @return true if this goal should be stopped
     */
    public fun shouldStop(): Boolean

    /**
     * Stops this goal.
     */
    public fun stop()

    /**
     * Called when the entity this goal is registered to is ticked.
     *
     * @param time the time of the tick
     */
    public fun tick(time: Long)
}
