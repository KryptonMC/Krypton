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
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/event/ResultedEvent.java
 */
package org.kryptonmc.api.event

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

/**
 * An event that can return a result from its execution.
 *
 * @param R the type of the result
 */
interface ResultedEvent<R : Result> {

    /**
     * The result of firing this event.
     */
    var result: R
}

/**
 * Represents the result of a resulted event.
 */
interface Result {

    /**
     * If this result represents an allowed (true), or denied (false) state.
     */
    val isAllowed: Boolean
}

/**
 * A generic allowed/denied result.
 */
class GenericResult(override val isAllowed: Boolean) : Result {

    override fun toString() = if (isAllowed) "allowed" else "denied"

    companion object {

        private val ALLOWED = GenericResult(true)
        private val DENIED = GenericResult(false)

        /**
         * Returns a result that represents the event being allowed.
         */
        @JvmStatic
        fun allowed() = ALLOWED

        /**
         * Returns a result that represents the event being denied.
         */
        @JvmStatic
        fun denied() = DENIED
    }
}

/**
 * A result that contains a [reason] for allowing/denying the event, as a [Component].
 *
 * @param reason the reason for the result
 */
class ComponentResult(override val isAllowed: Boolean, val reason: Component) : Result {

    override fun toString(): String {
        if (isAllowed) return "allowed"
        if (reason !== Component.empty()) return "denied: ${PlainTextComponentSerializer.plainText().serialize(reason)}"
        return "denied"
    }

    companion object {

        private val ALLOWED = ComponentResult(true, Component.empty())

        /**
         * Returns a result that represents the event being allowed with no reason.
         *
         * @return a result that represents the event being allowed with no reason
         */
        @JvmStatic
        fun allowed() = ALLOWED

        /**
         * Returns a result that represents the event being allowed for the given [reason].
         *
         * @param reason the reason for allowing the event
         * @return a result that represents the event being allowed for the given [reason]
         */
        @JvmStatic
        fun allowed(reason: Component) = ComponentResult(true, reason)

        /**
         * Returns a result that represents the event being denied for the given [reason].
         *
         * @param reason the reason for denying the event
         * @return a result that represents the event being denied for the given [reason]
         */
        @JvmStatic
        fun denied(reason: Component) = ComponentResult(false, reason)
    }
}
