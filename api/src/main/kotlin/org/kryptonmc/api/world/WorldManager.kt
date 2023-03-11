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
package org.kryptonmc.api.world

import net.kyori.adventure.key.Key
import org.kryptonmc.api.Server
import org.kryptonmc.api.resource.ResourceKey
import java.util.concurrent.CompletableFuture

/**
 * The world manager for this server. Can be used to retrieve loaded worlds,
 * or to load, save and update existing worlds.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WorldManager {

    /**
     * The server this world manager is bound to.
     */
    public val server: Server

    /**
     * The map of all currently loaded worlds by dimension.
     */
    public val worlds: Map<ResourceKey<World>, World>

    /**
     * The default world for this server.
     *
     * What the default world is will be defined by the implementation.
     */
    public val default: World

    /**
     * Gets the loaded world with the given resource [key], or null if there is
     * no world loaded with the given resource [key].
     *
     * @param key the resource key
     * @return the loaded world with the key, or null if not present
     */
    public fun getWorld(key: Key): World?

    /**
     * Checks if there is currently a world loaded with the given [key].
     *
     * @param key the resource key for the world
     * @return true if there is a world loaded with the given key, false
     * otherwise
     */
    public fun isLoaded(key: Key): Boolean

    /**
     * Loads a world by its resource key.
     *
     * @param key the resource key for the world
     * @return a future representing the result of loading the world with the
     * given key
     */
    public fun loadWorld(key: Key): CompletableFuture<out World>

    /**
     * Saves the given [world] to disk.
     *
     * @param world the world to save
     */
    public fun saveWorld(world: World): CompletableFuture<Void>
}
