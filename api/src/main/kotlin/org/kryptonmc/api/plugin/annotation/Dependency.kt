/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.plugin.annotation

/**
 * Declares a dependency on another plugin.
 *
 * @param id the plugin ID of the dependency
 * @param optional if this dependency is optional or not. This is false by default,
 *        meaning that this dependency is required for this plugin to enable. If
 *        set to true, the dependency will be soft, and the plugin will still load
 *        without it just fine
 */
@Target
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Dependency(
    val id: String,
    val optional: Boolean = false
)
