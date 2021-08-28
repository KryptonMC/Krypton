/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import net.kyori.adventure.key.Key

/**
 * Manages block handlers for blocks.
 */
interface BlockManager {

    /**
     * The map of block type keys (e.g. "minecraft:air") to handlers.
     */
    val handlers: Map<String, BlockHandler>

    /**
     * Gets the handler for the given [key], or returns null if there is no
     * handler for the given key.
     *
     * @param key the key
     * @return the handler for the key, if present
     */
    fun handler(key: String): BlockHandler?

    /**
     * Gets the handler for the given [key], or returns null if there is no
     * handler for the given key.
     *
     * @param key the key
     * @return the handler for the key, if present
     */
    fun handler(key: Key): BlockHandler?

    /**
     * Gets the handler for the given [block], or returns null if there is no
     * handler for the given block.
     *
     * @param block the block
     * @return the handler for the block, if present
     */
    fun handler(block: Block): BlockHandler?

    /**
     * Registers the given [handler] for the given [key].
     *
     * @param key the key
     * @param handler the handler
     */
    fun register(key: String, handler: BlockHandler)

    /**
     * Registers the given [handler] for the given [key].
     *
     * @param key the key
     * @param handler the handler
     */
    fun register(key: Key, handler: BlockHandler)

    /**
     * Registers the given [handler] for the given [block].
     *
     * @param block the block
     * @param handler the handler
     */
    fun register(block: Block, handler: BlockHandler)
}
