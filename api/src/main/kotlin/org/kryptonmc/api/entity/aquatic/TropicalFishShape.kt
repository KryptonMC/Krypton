/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.aquatic

import org.kryptonmc.api.util.StringSerializable

/**
 * A shape of a tropical fish.
 *
 * @param category the category ID of this shape
 * @param index the index of this shape within its category
 */
public enum class TropicalFishShape(
    @get:JvmName("serialized") override val serialized: String,
    public val category: Int,
    public val index: Int
) : StringSerializable {

    KOB("kob", 0, 0),
    SUNSTREAK("sunstreak", 0, 1),
    SNOOPER("snooper", 0, 2),
    DASHER("dasher", 0, 3),
    BRINLEY("brinley", 0, 4),
    SPOTTY("spotty", 0, 5),
    FLOPPER("flopper", 1, 0),
    STRIPEY("stripey", 1, 1),
    GLITTER("glitter", 1, 2),
    BLOCKFISH("blockfish", 1, 3),
    BETTY("betty", 1, 4),
    CLAYFISH("clayfish", 1, 5);

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the tropical fish shape with the given [name], or returns null
         * if there is no tropical fish shape with the given [name].
         *
         * @param name the name
         * @return the tropical fish shape with the name, or null if not
         * present
         */
        @JvmStatic
        public fun fromName(name: String): TropicalFishShape? = BY_NAME[name]

        /**
         * Gets the tropical fish shape with the given [id], or returns null
         * if there is no tropical fish shape with the given [id].
         *
         * @param id the ID
         * @return the tropical fish shape with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): TropicalFishShape? = VALUES.getOrNull(id)
    }
}
