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
 * A variant of an axolotl.
 *
 * @param isCommon if this axolotl variant is common to find, currently only
 * applies to the blue variant
 */
public enum class AxolotlVariant(
    @get:JvmName("serialized") override val serialized: String,
    public val isCommon: Boolean = true
) : StringSerializable {

    LUCY("lucy"),
    WILD("wild"),
    GOLD("gold"),
    CYAN("cyan"),
    BLUE("blue", false);

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the axolotl variant with the given [name], or returns null if
         * there is no axolotl variant with the given [name].
         *
         * @param name the name
         * @return the axolotl variant with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): AxolotlVariant? = BY_NAME[name]

        /**
         * Gets the axolotl variant with the given [id], or returns null if
         * there is no axolotl variant with the given [id].
         *
         * @param id the ID
         * @return the axolotl variant with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): AxolotlVariant? = VALUES.getOrNull(id)
    }
}
