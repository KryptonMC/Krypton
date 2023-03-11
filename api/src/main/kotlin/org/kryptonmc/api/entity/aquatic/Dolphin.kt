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
package org.kryptonmc.api.entity.aquatic

import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.damage.type.DamageTypes

/**
 * A dolphin.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Dolphin : AquaticAnimal {

    /**
     * The position of this dolphin's treasure.
     */
    public var treasurePosition: Vec3i

    /**
     * If this dolphin has got a fish.
     */
    @get:JvmName("hasGotFish")
    public var hasGotFish: Boolean

    /**
     * The skin moisture of this dolphin. This determines how long a dolphin
     * can remain out of water for.
     *
     * Dolphins, despite being mostly sea creatures, cannot actually breathe
     * underwater, meaning they need to rise out of the ocean to take another
     * breath every eight to ten minutes (some species can do up to fifteen),
     * so it should come as no surprise that dolphins are good at surviving on
     * land.
     * In the real world, dolphins can last a few hours on land if they are
     * kept wet enough, which is what this property determines.
     *
     * A dolphin's skin moisture determines if it is wet enough to survive out
     * of water, and how long it can last out of water for before it takes dry
     * out damage and eventually dies.
     *
     * The dolphin's skin moisture will, by default, be set to 2400 when the
     * dolphin is in water, being rained on, or in a bubble column, and it
     * will decrease by one for every tick the dolphin is not in one of these
     * environments.
     *
     * When this value is 0, the dolphin will take one point of
     * [dry out][DamageTypes.DRY_OUT] damage every tick, until the dolphin
     * perishes.
     */
    public var skinMoisture: Int
}
