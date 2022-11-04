/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scheduling

import javax.annotation.concurrent.ThreadSafe

/**
 * A scheduled task.
 */
@ThreadSafe
public interface Task {

    /**
     * The plugin that scheduled this task.
     */
    public val plugin: Any

    /**
     * The current state of the scheduled task.
     */
    public val state: TaskState

    /**
     * Attempts to cancel this scheduled task.
     */
    public fun cancel()
}
