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
 * A generic allowed/denied result.
 */
@JvmRecord
public data class GenericResult(override val isAllowed: Boolean) : ResultedEvent.Result {

    override fun toString(): String = if (isAllowed) "allowed" else "denied"

    public companion object {

        private val ALLOWED = GenericResult(true)
        private val DENIED = GenericResult(false)

        /**
         * Returns a result that represents the event being allowed.
         */
        @JvmStatic
        public fun allowed(): GenericResult = ALLOWED

        /**
         * Returns a result that represents the event being denied.
         */
        @JvmStatic
        public fun denied(): GenericResult = DENIED
    }
}
