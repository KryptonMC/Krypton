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
 * A type of cat.
 */
public enum class CatType(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    TABBY("tabby"),
    BLACK("black"),
    RED("red"),
    SIAMESE("siamese"),
    BRITISH_SHORTHAIR("british_shorthair"),
    CALICO("calico"),
    PERSIAN("persian"),
    RAGDOLL("ragdoll"),
    WHITE("white"),
    JELLIE("jellie"),
    ALL_BLACK("all_black");

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the cat type with the given [name], or returns null if there
         * is no cat type with the given [name].
         *
         * @param name the name
         * @return the cat type with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): CatType? = BY_NAME[name]

        /**
         * Gets the cat type with the given [id], or returns null if there is
         * no cat type with the given [id].
         *
         * @param id the ID
         * @return the cat type with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): CatType? = VALUES.getOrNull(id)
    }
}
