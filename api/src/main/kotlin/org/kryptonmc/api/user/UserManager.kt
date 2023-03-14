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
    public fun getUser(uuid: UUID): User?

    /**
     * Gets the user with the given [name], or returns null if there is no user
     * with the given [name], or this user is not loaded.
     *
     * @param name the last known name of the user
     * @return the user with the UUID, or null if not present
     */
    public fun getUser(name: String): User?

    /**
     * Gets the user with the given [uuid], loading them from persistent
     * storage if not already cached, or returns null if there is no user with
     * the given [uuid] available.
     *
     * @param uuid the UUID of the user
     * @return the user with the UUID, or null if not present
     */
    public fun loadUser(uuid: UUID): CompletableFuture<User?>

    /**
     * Gets the user with the given [name], loading them from persistent
     * storage if not already cached, or returns null if there is no user with
     * the given [name] available.
     *
     * @param name the last known name of the user
     * @return the user with the UUID, or null if not present
     */
    public fun loadUser(name: String): CompletableFuture<User?>
}
