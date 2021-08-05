/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

/**
 * Information about the current platform this is running on.
 */
interface Platform {

    /**
     * The name of the platform.
     */
    val name: String

    /**
     * The version of the platform.
     */
    val version: String

    /**
     * If the platform is considered "stable".
     */
    val isStable: Boolean

    /**
     * The Minecraft version of the platform.
     */
    val minecraftVersion: String

    /**
     * The world version.
     */
    val worldVersion: Int

    /**
     * The protocol version.
     */
    val protocolVersion: Int

    /**
     * The data pack version.
     */
    val dataPackVersion: Int
}
