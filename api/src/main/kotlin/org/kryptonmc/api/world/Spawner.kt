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

/**
 * An object that can spawn entities.
 */
public interface Spawner {

    /**
     * The amount of time, in ticks, until the next entity is spawned.
     */
    public var spawnDelay: Int

    /**
     * The minimum amount of time, in ticks, between entities being spawned by
     * the spawner.
     */
    public var minimumSpawnDelay: Int

    /**
     * The maximum amount of time, in ticks, between entities being spawned by
     * the spawner.
     */
    public var maximumSpawnDelay: Int

    /**
     * The amount of entities that have been successfully spawned from this
     * spawner.
     */
    public var spawnCount: Int

    /**
     * The maximum range that an entity can be spawned from the spawner.
     */
    public var spawnRange: Double

    /**
     * The minimum range a player must be in proximity of the spawner for the
     * spawner to attempt to spawn entities.
     */
    public var requiredPlayerRange: Double
}
