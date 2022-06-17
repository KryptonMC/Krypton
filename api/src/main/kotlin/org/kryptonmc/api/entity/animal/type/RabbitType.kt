/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import org.kryptonmc.api.util.StringSerializable

/**
 * A type of rabbit.
 */
public enum class RabbitType(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    BROWN("brown"),
    WHITE("white"),
    BLACK("black"),
    BLACK_AND_WHITE("black_and_white"),
    GOLD("gold"),
    SALT_AND_PEPPER("salt_and_pepper"),
    // That rabbit's got a vicious streak a mile wide! It's a killer!
    KILLER("killer");

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the rabbit type with the given [name], or returns null if
         * there is no rabbit type with the given [name].
         *
         * @param name the name
         * @return the rabbit type with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): RabbitType? = BY_NAME[name]

        /**
         * Gets the rabbit type with the given [id], or returns null if there
         * is no rabbit type with the given [id].
         *
         * @param id the ID
         * @return the rabbit type with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): RabbitType? = VALUES.getOrNull(id)
    }
}
