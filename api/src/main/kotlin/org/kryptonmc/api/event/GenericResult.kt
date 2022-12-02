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

import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A generic allowed/denied result.
 */
@JvmRecord
@ImmutableType
public data class GenericResult(override val isAllowed: Boolean) : ResultedEvent.Result {

    public companion object {

        private val ALLOWED = GenericResult(true)
        private val DENIED = GenericResult(false)

        /**
         * Gets the result that allows the event to continue as normal.
         *
         * @return the allowed result
         */
        @JvmStatic
        public fun allowed(): GenericResult = ALLOWED

        /**
         * Gets the result that denies the event from continuing.
         *
         * @return the denied result
         */
        @JvmStatic
        public fun denied(): GenericResult = DENIED
    }
}
