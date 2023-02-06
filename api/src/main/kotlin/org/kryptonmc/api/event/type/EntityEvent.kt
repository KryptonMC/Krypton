/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.type

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.event.Event

/**
 * An event for an entity.
 */
public interface EntityEvent : Event {

    /**
     * The entity that the event is for.
     */
    public val entity: Entity
}
