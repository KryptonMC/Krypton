package org.kryptonmc.krypton.api.scheduling

import org.kryptonmc.krypton.api.plugin.Plugin

/**
 * A scheduled task
 */
interface Task {

    /**
     * The plugin that scheduled this task
     */
    val plugin: Plugin

    /**
     * The current state of the scheduled task
     */
    val state: TaskState

    /**
     * Attempt to cancel this scheduled task.
     */
    fun cancel()
}
