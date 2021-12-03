/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 */
package org.kryptonmc.api.plugin

/**
 * Methods for a KryptonPlugin
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface KryptonPlugin {

    /**
     * Triggered when plugin is enabled
     */
    public fun onEnable(){}
    /**
     * Triggered when plugin is disabled
     */
    public fun onDisable(){}
}
