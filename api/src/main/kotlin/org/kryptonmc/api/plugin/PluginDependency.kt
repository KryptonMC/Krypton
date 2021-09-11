/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/PluginDescription.java
 */
package org.kryptonmc.api.plugin

/**
 * Holder class for plugin dependency metadata.
 *
 * @param id the ID of the dependency
 * @param isOptional if this dependency is optional
 */
@JvmRecord
public data class PluginDependency(
    public val id: String,
    public val isOptional: Boolean
)
