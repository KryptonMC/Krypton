/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.effect.sound.SoundEvent

/**
 * An entity that can be picked up in a bucket.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Bucketable {

    /**
     * The type of the bucket this entity can be captured in.
     */
    public val bucketType: ItemType

    /**
     * The sound that is played when the entity is picked up in a bucket.
     */
    public val bucketPickupSound: SoundEvent

    /**
     * If this entity was spawned from a bucket.
     *
     * Certain mobs, such as fish, can be captured in a water bucket by the
     * player. When a mob is captured, it is removed from the world, and it is
     * stored on the bucket item. It can be placed back in to the world, in
     * which it would be spawned from a bucket.
     *
     * @return true if this entity was spawned from a bucket
     */
    public fun wasSpawnedFromBucket(): Boolean

    /**
     * Creates a new bucket from this entity. Does not remove the entity from
     * the world.
     */
    public fun asBucket(): ItemStack

    /**
     * Buckets this entity, removing it from the world and returning the
     * created bucket containing this entity.
     */
    public fun bucket(): ItemStack
}
