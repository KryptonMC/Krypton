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
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.jetbrains.annotations.Contract

/**
 * A team on a [Scoreboard].
 *
 * Teams are groups of entities that have a name, prefix, suffix, colour,
 * and a specific set of rules they follow.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Team : ScoreboardBound {

    /**
     * The name of this team.
     */
    public val name: String

    /**
     * The name that is displayed on the scoreboard to clients.
     */
    public var displayName: Component

    /**
     * The prefix prepended to the display name of members of this team.
     */
    public var prefix: Component

    /**
     * The suffix appended to the display name of members of this team.
     */
    public var suffix: Component

    /**
     * The colour of the team that is displayed on the scoreboard.
     */
    public var color: NamedTextColor

    /**
     * If this team allows members to attack each other.
     *
     * *Well that's not very nice, is it! They're there to help you...*
     */
    @get:JvmName("allowFriendlyFire")
    public var allowFriendlyFire: Boolean

    /**
     * If this team allows members to see members that are invisible.
     */
    @get:JvmName("canSeeInvisibleMembers")
    public var canSeeInvisibleMembers: Boolean

    /**
     * The visibility of name tags in the team.
     */
    public var nameTagVisibility: Visibility

    /**
     * The visibility of death messages in the team.
     */
    public var deathMessageVisibility: Visibility

    /**
     * The collision rule for the team.
     */
    public var collisionRule: CollisionRule

    /**
     * All of the members in this team.
     */
    public val members: List<Component>

    /**
     * Adds a member to the list of members in this team.
     *
     * @param member the member to add
     * @return whether the member was added
     */
    public fun addMember(member: Component): Boolean

    /**
     * Removes a member from the list of members in this team.
     *
     * @return whether the member was removed
     */
    public fun removeMember(member: Component): Boolean

    /**
     * Formats the given [name] according to the options in this team.
     *
     * The format returned by this function should be [prefix] [name] [suffix],
     * excluding the spaces in between the terms, and it will be coloured with
     * the [team color][color].
     *
     * For example, if the prefix was "Krypton", the name was "is", and the
     * suffix was "cool", the full name would be "Kryptoniscool".
     *
     * @param name the name to format
     * @return the formatted name
     */
    public fun formatName(name: Component): Component

    /**
     * A builder for building teams.
     */
    public interface Builder {

        /**
         * Sets the display name for the team to the given [name].
         *
         * @param name the display name
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun displayName(name: Component): Builder

        /**
         * Sets the prefix for the team to the given [prefix].
         *
         * @param prefix the prefix
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun prefix(prefix: Component): Builder

        /**
         * Sets the suffix for the team to the given [suffix].
         *
         * @param suffix the suffix
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun suffix(suffix: Component): Builder

        /**
         * Allows friendly fire for the team.
         *
         * @return this builder
         */
        @Contract("-> this", mutates = "this")
        public fun friendlyFire(): Builder

        /**
         * Allows all team members to see invisible team members.
         *
         * @return this builder
         */
        @Contract("-> this", mutates = "this")
        public fun seeInvisibleMembers(): Builder

        /**
         * Sets the name tag visibility for the team to the given [visibility].
         *
         * @param visibility the visibility
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun nameTagVisibility(visibility: Visibility): Builder

        /**
         * Sets the death message visibility for the team to the given
         * [visibility].
         *
         * @param visibility the visibility
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun deathMessageVisibility(visibility: Visibility): Builder

        /**
         * Sets the team colour to the given [color].
         *
         * @param color the colour
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun color(color: NamedTextColor): Builder

        /**
         * Sets the collision rule for the team to the given [rule].
         *
         * @param rule the collision rule
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun collisionRule(rule: CollisionRule): Builder

        /**
         * Adds the given [member] to the team's list of members.
         *
         * @param member the member
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addMember(member: Component): Builder

        /**
         * Builds the team and adds it to the scoreboard.
         *
         * @return the built team
         */
        @Contract("-> new", pure = true)
        public fun buildAndRegister(): Team
    }
}
