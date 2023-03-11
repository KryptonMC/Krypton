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
package org.kryptonmc.krypton.entity.tracking

import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.entity.KryptonEntity

/**
 * A callback for updates to an entity's view tracking.
 *
 * This is used to decouple the logic for entity viewing from that of entity tracking.
 */
interface EntityViewCallback<E : KryptonEntity> {

    /**
     * Called when the entity comes in to view.
     */
    fun add(entity: E)

    /**
     * Called when the entity goes out of view.
     */
    fun remove(entity: E)

    /**
     * Called when the entity moves, and so the reference has changed.
     *
     * This is used by the viewing engine so it knows when to update its tracked location.
     */
    fun referenceUpdate(position: Position, tracker: EntityTracker) {
        // Do nothing by default
    }
}
