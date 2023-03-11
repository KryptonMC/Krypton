/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.command.arguments.entities

object EntityArguments {

    @JvmField
    val ALL_SELECTORS: List<String> = listOf("@p", "@r", "@a", "@e", "@s")
    @JvmField
    val PLAYER_SELECTORS: List<String> = listOf("@p", "@r", "@a", "@s")
    @JvmField
    val SINGLE_PLAYER_SELECTORS: List<String> = listOf("@p", "@r", "@s")
    @JvmField
    val VALID: List<String> = listOf(
        "x", "y", "z", "distance", "dx", "dy", "dz", "scores", "tag", "team", "limit", "sort", "level", "gamemode", "name", "x_rotation",
        "y_rotation", "type", "nbt", "advancements", "predicate"
    )
    @JvmField
    val EXCLUDE_ARGUMENTS: List<String> = listOf("team", "tag", "gamemode", "name", "predicate")

    enum class Sorter {

        /**
         * Sort by increasing distance.
         */
        NEAREST,

        /**
         * Sort by decreasing distance.
         */
        FURTHEST,

        /**
         * Sort randomly.
         */
        RANDOM,

        /**
         * Sort by time created.
         */
        ARBITRARY;

        companion object {

            private val BY_NAME = values().associateBy { it.name.lowercase() }

            /**
             * Gets the sorter with the given [name].
             */
            @JvmStatic
            fun fromName(name: String): Sorter? = BY_NAME.get(name)
        }
    }
}
