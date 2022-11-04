/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/9a15c7e170ba12ed3acb71257365ff7d1c775443/api/src/main/java/com/velocitypowered/api/event/ResultedEvent.java
 */
package org.kryptonmc.api.event

/**
 * An event that can return a result from its execution.
 *
 * @param R the type of the result
 */
public interface ResultedEvent<R : ResultedEvent.Result> {

    /**
     * The result of firing this event.
     */
    public var result: R

    /**
     * Represents the result of a resulted event.
     */
    public interface Result {

        /**
         * If this result represents an allowed (true), or denied (false)
         * state.
         */
        public val isAllowed: Boolean
    }
}
