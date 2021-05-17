/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

import org.kryptonmc.api.plugin.Plugin

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
