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
