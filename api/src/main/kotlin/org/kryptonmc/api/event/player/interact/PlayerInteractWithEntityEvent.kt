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
package org.kryptonmc.api.event.player.interact

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.Hand

/**
 * Called when a player interacts with an entity as a whole.
 *
 * This is used for interactions where it only matters that an entity was
 * interacted with, not where on the entity the interaction occurred.
 */
public interface PlayerInteractWithEntityEvent : PlayerInteractEvent {

    /**
     * The entity that was interacted with.
     */
    public val target: Entity

    /**
     * The hand that the player used to interact with the target.
     */
    public val hand: Hand
}
