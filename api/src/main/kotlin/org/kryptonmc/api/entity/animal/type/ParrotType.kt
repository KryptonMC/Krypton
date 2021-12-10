/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import org.kryptonmc.api.util.StringSerializable

/**
 * A type of parrot.
 */
public enum class ParrotType(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    RED_AND_BLUE("red_and_blue"),
    BLUE("blue"),
    GREEN("green"),
    YELLOW_AND_BLUE("yellow_and_blue"),
    GREY("grey");

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the parrot type with the given [name], or returns null if there
         * is no tropical fish shape with the given [name].
         *
         * @param name the name
         * @return the tropical fish shape with the name, or null if not
         * present
         */
        @JvmStatic
        public fun fromName(name: String): ParrotType? = BY_NAME[name]

        /**
         * Gets the tropical fish shape with the given [id], or returns null
         * if there is no tropical fish shape with the given [id].
         *
         * @param id the ID
         * @return the tropical fish shape with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): ParrotType? = VALUES.getOrNull(id)
    }
}
