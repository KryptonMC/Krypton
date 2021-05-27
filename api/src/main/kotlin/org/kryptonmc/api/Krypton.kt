/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

// This is a big hack. This needs to be instantiated reflectively by implementations, and must not be reassigned.
private var server: Server? = null
    private set(value) {
        if (field != null) throw IllegalStateException("You can't reassign the server! Honestly, plugins these days...")
        field = value
    }

/**
 * Static singleton access for the server.
 *
 * This only exists for cases where dependency injection is impractical and messier than using this static singleton.
 * **Always prefer [org.kryptonmc.api.plugin.PluginContext.server] where possible.**
 */
object Krypton : Server by server!!
