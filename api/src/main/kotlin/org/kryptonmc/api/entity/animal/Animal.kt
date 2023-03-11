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

import org.kryptonmc.api.entity.Ageable
import org.kryptonmc.api.item.ItemStack
import java.util.UUID

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
     * The time remaining that this animal will be in love for.
     */
    public val inLoveTime: Int

    /**
     * The cause of this animal being in love, if it is currently in love.
     *
     * If it is not in love, this will always be null.
     */
    public var loveCause: UUID?

    /**
     * If this animal can fall in love.
     *
     * @return true if this animal can fall in love
     */
    public fun canFallInLove(): Boolean

    /**
     * Whether this animal is currently in love, meaning it is looking for a
     * mate.
     *
     * @return true if this animal is in love
     */
    public fun isInLove(): Boolean

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
