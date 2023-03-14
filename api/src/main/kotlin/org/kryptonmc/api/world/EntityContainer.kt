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
package org.kryptonmc.api.world

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.player.Player
import java.util.function.Predicate

/**
 * Something that contains entities.
 */
public interface EntityContainer {

    /**
     * All entities contained within this container.
     */
    public val entities: Collection<Entity>

    /**
     * All players contained within this container.
     */
    public val players: Collection<Player>

    /**
     * Gets all entities of the given [type] contained within this container.
     *
     * @param E the entity type
     * @param type the entity type
     * @return all entities of the given type
     */
    public fun <E : Entity> getEntitiesOfType(type: Class<E>): Collection<E>

    /**
     * Gets all entities of the given [type] matching the given [predicate]
     * contained within this container.
     *
     * @param E the entity type
     * @param type the entity type
     * @param predicate the predicate to filter entities with
     * @return the entities
     */
    public fun <E : Entity> getEntitiesOfType(type: Class<E>, predicate: Predicate<E>): Collection<E>
}
