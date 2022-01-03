/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

/**
 * The current state of a scheduled [Task].
 */
public enum class TaskState {

    /**
     * This task is scheduled to be executed.
     */
    SCHEDULED,

    /**
     * This task has been successfully executed.
     */
    COMPLETED,

    /**
     * This task was interrupted whilst it was executing.
     */
    INTERRUPTED
}
