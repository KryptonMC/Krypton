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

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract

/**
 * A result that contains a [reason] for allowing/denying the event, as a
 * [Component].
 *
 * @param reason the reason for the result
 */
@JvmRecord
public data class ComponentResult(override val isAllowed: Boolean, public val reason: Component?) : ResultedEvent.Result {

    public companion object {

        private val ALLOWED = ComponentResult(true, null)
        private val DENIED = ComponentResult(false, null)

        /**
         * Gets the result that allows the event to continue as normal.
         *
         * @return the allowed result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun allowed(): ComponentResult = ALLOWED

        /**
         * Creates a new result that allows the event to continue, but
         * silently replaces the reason with the given [reason].
         *
         * @param reason the reason
         * @return a new allowed result
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun allowed(reason: Component): ComponentResult = ComponentResult(true, reason)

        /**
         * Gets the result that denies the event from continuing.
         *
         * @return the denied result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun denied(): ComponentResult = DENIED

        /**
         * Creates a new result that denies the event from continuing, with
         * the given [reason].
         *
         * @param reason the reason for denying the event
         * @return a new result
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun denied(reason: Component): ComponentResult = ComponentResult(false, reason)
    }
}
