/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key

/**
 * Manages item handlers for item types.
 */
interface ItemManager {

    /**
     * The map of item type keys (e.g. "minecraft:air") to handlers.
     */
    val handlers: Map<String, ItemHandler>

    /**
     * Gets the handler for the given [key], or returns null if there is no
     * handler for the given key.
     *
     * @param key the key
     * @return the handler for the key, if present
     */
    fun handler(key: String): ItemHandler?

    /**
     * Gets the handler for the given [key], or returns null if there is no
     * handler for the given key.
     *
     * @param key the key
     * @return the handler for the key, if present
     */
    fun handler(key: Key): ItemHandler? = handler(key.asString())

    /**
     * Gets the handler for the given item [type], or returns null if there is
     * no handler for the given item type.
     *
     * @param type the item type
     * @return the handler for the type, if present
     */
    fun handler(type: ItemType): ItemHandler? = handler(type.key.asString())

    /**
     * Registers the given [handler] for the given [key].
     *
     * @param key the key
     * @param handler the handler
     */
    fun register(key: String, handler: ItemHandler)

    /**
     * Registers the given [handler] for the given [key].
     *
     * @param key the key
     * @param handler the handler
     */
    fun register(key: Key, handler: ItemHandler) = register(key.asString(), handler)

    /**
     * Registers the given [handler] for the given item [type].
     *
     * @param type the item type
     * @param handler the handler
     */
    fun register(type: ItemType, handler: ItemHandler) = register(type.key.asString(), handler)
}
