/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import org.kryptonmc.api.util.StringSerializable

/**
 * A visibility for a team option, such as for name tag visibility.
 */
public enum class Visibility(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the visibility with the given [name], or returns null if there
         * is no visibility with the given [name].
         *
         * @param name the name
         * @return the visibility with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): Visibility? = BY_NAME[name]

        /**
         * Gets the visibility with the given [id], or returns null if there
         * is no visibility with the given [id].
         *
         * @param id the ID
         * @return the visibility with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): Visibility? = VALUES.getOrNull(id)
    }
}
