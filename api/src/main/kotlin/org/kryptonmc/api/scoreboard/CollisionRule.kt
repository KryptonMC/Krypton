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
 * A rule for collision between members of a team.
 */
public enum class CollisionRule(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    ALWAYS("always"),
    NEVER("never"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam");

    public companion object {

        private val VALUES = values()
        private val BY_NAME = VALUES.associateBy { it.serialized }

        /**
         * Gets the collision rule with the given [name], or returns null if
         * there is no collision rule with the given [name].
         *
         * @param name the name
         * @return the collision rule with the name, or null if not present
         */
        @JvmStatic
        public fun fromName(name: String): CollisionRule? = BY_NAME[name]

        /**
         * Gets the collision rule with the given [id], or returns null if
         * there is no collision rule with the given [id].
         *
         * @param id the ID
         * @return the collision rule with the ID, or null if not present
         */
        @JvmStatic
        public fun fromId(id: Int): CollisionRule? = VALUES.getOrNull(id)
    }
}
