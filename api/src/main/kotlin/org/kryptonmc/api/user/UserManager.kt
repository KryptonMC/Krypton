/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user

import java.util.UUID
import java.util.concurrent.CompletableFuture

/**
 * The manager for users.
 */
public interface UserManager {

    /**
     * Gets the user with the given [uuid], or returns null if there is no user
     * with the given [uuid], or this user is not loaded.
     *
     * @param uuid the UUID of the user
     * @return the user with the UUID, or null if not present
     */
    public fun get(uuid: UUID): User?

    /**
     * Gets the user with the given [name], or returns null if there is no user
     * with the given [name], or this user is not loaded.
     *
     * @param name the last known name of the user
     * @return the user with the UUID, or null if not present
     */
    public fun get(name: String): User?

    /**
     * Gets the user with the given [uuid], loading them from persistent
     * storage if not already cached, or returns null if there is no user with
     * the given [uuid] available.
     *
     * @param uuid the UUID of the user
     * @return the user with the UUID, or null if not present
     */
    public fun load(uuid: UUID): CompletableFuture<User?>

    /**
     * Gets the user with the given [name], loading them from persistent
     * storage if not already cached, or returns null if there is no user with
     * the given [name] available.
     *
     * @param name the last known name of the user
     * @return the user with the UUID, or null if not present
     */
    public fun load(name: String): CompletableFuture<User?>
}
