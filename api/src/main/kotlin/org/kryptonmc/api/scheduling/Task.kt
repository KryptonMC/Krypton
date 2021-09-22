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
 * A scheduled task.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Task {

    /**
     * The plugin that scheduled this task.
     */
    @get:JvmName("plugin")
    public val plugin: Any

    /**
     * The current state of the scheduled task.
     */
    @get:JvmName("state")
    public val state: TaskState

    /**
     * Attempts to cancel this scheduled task.
     */
    public fun cancel()
}
