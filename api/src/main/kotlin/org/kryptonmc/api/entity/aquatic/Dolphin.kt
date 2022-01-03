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
     * The skin moisture of this dolphin.
     *
     * Vanilla sets the dolphin's skin moisture to 2400 when the entity is in
     * water, being rained on, or in a bubble column. If they are not in one
     * of these environments, the dolphin will lose one moisture per tick.
     *
     * Once this value reaches 0, the dolphin will take one point of damage
     * from [dryout][DamageTypes.DRY_OUT] per tick, until the dolphin
     * perishes.
     */
    @get:JvmName("moistnessLevel")
    public var skinMoisture: Int
}
