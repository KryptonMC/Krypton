/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world

/**
 * An object that can spawn entities.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Spawner {

    /**
     * The amount of time, in ticks, until the next entity is spawned.
     */
    @get:JvmName("spawnDelay")
    public var spawnDelay: Int

    /**
     * The minimum amount of time, in ticks, between entities being spawned by
     * the spawner.
     */
    @get:JvmName("minimumSpawnDelay")
    public var minimumSpawnDelay: Int

    /**
     * The maximum amount of time, in ticks, between entities being spawned by
     * the spawner.
     */
    @get:JvmName("maximumSpawnDelay")
    public var maximumSpawnDelay: Int

    /**
     * The amount of entities that have been successfully spawned from this
     * spawner.
     */
    @get:JvmName("spawnCount")
    public var spawnCount: Int

    /**
     * The maximum range that an entity can be spawned from the spawner.
     */
    @get:JvmName("spawnRange")
    public var spawnRange: Double

    /**
     * The minimum range a player must be in proximity of the spawner for the
     * spawner to attempt to spawn entities.
     */
    @get:JvmName("requiredPlayerRange")
    public var requiredPlayerRange: Double
}
