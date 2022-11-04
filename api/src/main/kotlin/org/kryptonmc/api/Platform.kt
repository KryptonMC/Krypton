/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import javax.annotation.concurrent.Immutable

/**
 * Provides information about the current platform this is running on, such as
 * the name and version, if it is considered stable, and the target Minecraft
 * version.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface Platform {

    /**
     * The name of the platform.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The version of the platform.
     */
    @get:JvmName("version")
    public val version: String

    /**
     * The version of vanilla Minecraft that this platform targets.
     */
    @get:JvmName("minecraftVersion")
    public val minecraftVersion: String

    /**
     * The version of the world format used by this platform.
     */
    @get:JvmName("worldVersion")
    public val worldVersion: Int

    /**
     * The version of the protocol used by this platform.
     */
    @get:JvmName("protocolVersion")
    public val protocolVersion: Int

    /**
     * The version of the data pack format used by this platform.
     */
    @get:JvmName("dataPackVersion")
    public val dataPackVersion: Int
}
