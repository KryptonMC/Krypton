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
public interface Platform {

    /**
     * The name of the platform.
     */
    public val name: String

    /**
     * The version of the platform.
     */
    public val version: String

    /**
     * If the platform is considered "stable".
     */
    public val isStable: Boolean

    /**
     * The Minecraft version of the platform.
     */
    public val minecraftVersion: String

    /**
     * The world version.
     */
    public val worldVersion: Int

    /**
     * The protocol version.
     */
    public val protocolVersion: Int

    /**
     * The data pack version.
     */
    public val dataPackVersion: Int
}
