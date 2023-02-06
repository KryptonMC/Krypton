/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.type

import org.kryptonmc.api.event.Event

/**
 * A type of event that can optionally return a result to modify the behaviour
 * of the event.
 */
public interface EventWithResult<T> : Event {

    /**
     * The result that is returned by this event.
     */
    public var result: T?
}
