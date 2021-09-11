/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

/**
 * A functional interface used as a runnable for tasks that provides the
 * executing task as a parameter so that it can be cancelled.
 */
public fun interface TaskRunnable {

    /**
     * Runs this task runnable with the given [task].
     */
    public fun run(task: Task)
}
