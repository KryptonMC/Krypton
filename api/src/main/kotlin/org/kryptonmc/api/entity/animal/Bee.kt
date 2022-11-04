/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.spongepowered.math.vector.Vector3i

/**
 * A bee.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Bee : Animal {

    /**
     * If this bee is currently angry at a player.
     */
    public var isAngry: Boolean

    /**
     * If this bee has stung a player.
     */
    @get:JvmName("hasStung")
    public var hasStung: Boolean

    /**
     * If this bee has nectar to deposit back to the hive.
     */
    @get:JvmName("hasNectar")
    public var hasNectar: Boolean

    /**
     * The amount of ticks this bee can't enter its hive for.
     */
    public var cannotEnterHiveTicks: Int

    /**
     * The location of this bee's hive.
     */
    public var hive: Vector3i?

    /**
     * The location of this bee's flower.
     */
    public var flower: Vector3i?
}
