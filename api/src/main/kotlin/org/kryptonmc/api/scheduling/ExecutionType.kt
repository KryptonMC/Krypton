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
 * Used by tasks to indicate whether they will execute synchronously with the
 * server, or asynchronously from the server.
 *
 * Note: It is not always necessary or preferable to schedule a task
 * asynchronously. If you are unsure, don't set the type when building a task,
 * and the implementation will decide which to use.
 */
public enum class ExecutionType {

    SYNCHRONOUS,
    ASYNCHRONOUS
}
