/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api

import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Provides information about the current platform this is running on, such as
 * the name and version, if it is considered stable, and the target Minecraft
 * version.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
