/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/plugin/Dependency.java
 */
package org.kryptonmc.api.plugin.annotation

/**
 * Declares a dependency on another plugin.
 *
 * @param id the plugin ID of the dependency
 * @param optional if this dependency is optional or not. This is false by default,
 * meaning that this dependency is required for this plugin to enable. If
 * set to true, the dependency will be soft, and the plugin will still load
 * without it just fine
 */
@Target
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class Dependency(
    public val id: String,
    public val optional: Boolean = false
)
