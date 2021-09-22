/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Key

/**
 * Manages fluid handlers for fluids.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FluidManager {

    /**
     * The map of fluid type keys (e.g. "minecraft:air") to handlers.
     */
    @get:JvmName("handlers")
    public val handlers: Map<String, FluidHandler>

    /**
     * Gets the handler for the given [key], or returns null if there is no
     * handler for the given key.
     *
     * @param key the key
     * @return the handler for the key, or null if not present
     */
    public fun handler(key: String): FluidHandler?

    /**
     * Gets the handler for the given [key], or returns null if there is no
     * handler for the given key.
     *
     * @param key the key
     * @return the handler for the key, or null if not present
     */
    public fun handler(key: Key): FluidHandler?

    /**
     * Gets the handler for the given [fluid], or returns null if there is no
     * handler for the given fluid.
     *
     * @param fluid the fluid
     * @return the handler for the fluid, or null if not present
     */
    public fun handler(fluid: Fluid): FluidHandler?

    /**
     * Registers the given [handler] for the given [key].
     *
     * @param key the key
     * @param handler the handler
     */
    public fun register(key: String, handler: FluidHandler)

    /**
     * Registers the given [handler] for the given [key].
     *
     * @param key the key
     * @param handler the handler
     */
    public fun register(key: Key, handler: FluidHandler)

    /**
     * Registers the given [handler] for the given [fluid].
     *
     * @param fluid the fluid
     * @param handler the handler
     */
    public fun register(fluid: Fluid, handler: FluidHandler)
}
