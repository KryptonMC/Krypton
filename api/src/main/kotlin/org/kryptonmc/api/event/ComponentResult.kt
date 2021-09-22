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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

/**
 * A result that contains a [reason] for allowing/denying the event, as a
 * [Component].
 *
 * @param reason the reason for the result
 */
@JvmRecord
public data class ComponentResult(override val isAllowed: Boolean, public val reason: Component) : ResultedEvent.Result {

    override fun toString(): String {
        if (isAllowed) return "allowed"
        if (reason !== Component.empty()) return "denied: ${PlainTextComponentSerializer.plainText().serialize(reason)}"
        return "denied"
    }

    public companion object {

        private val ALLOWED = ComponentResult(true, Component.empty())

        /**
         * Returns a result that represents the event being allowed with no
         * reason.
         *
         * @return an allowed result
         */
        @JvmStatic
        public fun allowed(): ComponentResult = ALLOWED

        /**
         * Returns a result that represents the event being allowed for the
         * given [reason].
         *
         * @param reason the reason for allowing the event
         * @return a new result
         */
        @JvmStatic
        public fun allowed(reason: Component): ComponentResult = ComponentResult(true, reason)

        /**
         * Returns a result that represents the event being denied for the
         * given [reason].
         *
         * @param reason the reason for denying the event
         * @return a new result
         */
        @JvmStatic
        public fun denied(reason: Component): ComponentResult = ComponentResult(false, reason)
    }
}
