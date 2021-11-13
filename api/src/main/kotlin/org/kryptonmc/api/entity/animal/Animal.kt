/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.Ageable
import org.kryptonmc.api.item.ItemStack

/**
 * A creature that can fall in love with others of its kind, breed, and
 * produce offspring.
 *
 * The love mechanic can be a bit confusing, but what it actually means for an
 * animal to be "in love" is that they are looking for a mate. For most
 * animals, a player feeding them will trigger this effect.
 *
 * The animal will remain in love until either it successfully finds a mate,
 * breeds, and produces offspring, or the timer runs out.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Animal : Ageable {

    /**
     * Whether this animal is currently in love, meaning it is looking for a
     * mate.
     */
    @get:JvmName("inLove")
    public val inLove: Boolean

    /**
     * The time remaining that this animal will be in love for.
     */
    @get:JvmName("inLoveTime")
    public val inLoveTime: Int

    /**
     * If this animal can fall in love.
     */
    @get:JvmName("canFallInLove")
    public val canFallInLove: Boolean

    /**
     * Returns true if this animal is eligible to mate with the given [target]
     * animal, or false otherwise.
     *
     * @param target the target
     * @return true if this animal can mate with the target, false otherwise
     */
    public fun canMate(target: Animal): Boolean

    /**
     * Returns true if the given [item] is considered food for this animal, or
     * false otherwise.
     *
     * @param item the item
     * @return true if the item is food, false otherwise
     */
    public fun isFood(item: ItemStack): Boolean
}
