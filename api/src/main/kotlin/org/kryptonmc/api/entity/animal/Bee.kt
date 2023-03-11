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
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.util.Vec3i

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
    public var hive: Vec3i?

    /**
     * The location of this bee's flower.
     */
    public var flower: Vec3i?
}
