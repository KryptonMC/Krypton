/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
