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

import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.player.Player

/**
 * An animal that may be tamed by an entity, usually a player.
 */
public interface Tamable : Animal {

    /**
     * If this tamable animal is in the sitting position.
     */
    public var isSitting: Boolean

    /**
     * If this tamable animal is currently tamed.
     */
    public var isTamed: Boolean

    /**
     * If the owner of this animal has ordered it to sit.
     */
    public var isOrderedToSit: Boolean

    /**
     * The entity that has tamed this animal, or null, if no entities own this
     * tamable animal.
     */
    public val owner: LivingEntity?

    /**
     * Tames this animal, making the owner the given [tamer].
     *
     * @param tamer the player taming the animal
     */
    public fun tame(tamer: Player)
}
