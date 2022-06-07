/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.aquatic

import org.kryptonmc.api.world.damage.type.DamageTypes
import org.spongepowered.math.vector.Vector3i

/**
 * A dolphin.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Dolphin : AquaticAnimal {

    /**
     * The position of this dolphin's treasure.
     */
    @get:JvmName("treasurePosition")
    public var treasurePosition: Vector3i

    /**
     * If this dolphin has got a fish.
     */
    @get:JvmName("gotFish")
    public var gotFish: Boolean

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
    @get:JvmName("skinMoisture")
    public var skinMoisture: Int
}
