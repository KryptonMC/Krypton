package org.kryptonmc.krypton.api.scheduling

/**
 * Represents the current state of a scheduled [Task]
 */
enum class TaskState {

    /**
     * This task is scheduled to be executed
     */
    SCHEDULED,

    /**
     * This task has been successfully executed
     */
    COMPLETED,

    /**
     * This task was interrupted whilst it was executing
     */
    INTERRUPTED
}
