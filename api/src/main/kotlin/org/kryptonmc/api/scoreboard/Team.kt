/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.builder.AbstractBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.Buildable
import org.kryptonmc.api.util.provide

/**
 * A team on a [Scoreboard].
 *
 * Teams are groups of entities that have a name, prefix, suffix, colour,
 * and a specific set of rules they follow.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Team : Buildable<Team.Builder, Team> {

    /**
     * The name of this team.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The name that is displayed on the scoreboard to clients.
     */
    @get:JvmName("displayName")
    public var displayName: Component

    /**
     * The prefix prepended to the display name of members of this team.
     */
    @get:JvmName("prefix")
    public var prefix: Component

    /**
     * The suffix appended to the display name of members of this team.
     */
    @get:JvmName("suffix")
    public var suffix: Component

    /**
     * The colour of the team that is displayed on the scoreboard.
     */
    @get:JvmName("color")
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
    @get:JvmName("nameTagVisibility")
    public var nameTagVisibility: Visibility

    /**
     * The visibility of death messages in the team.
     */
    @get:JvmName("deathMessageVisibility")
    public var deathMessageVisibility: Visibility

    /**
     * The collision rule for the team.
     */
    @get:JvmName("collisionRule")
    public var collisionRule: CollisionRule

    /**
     * All of the members in this team.
     */
    @get:JvmName("members")
    public val members: Set<Component>

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
    @ScoreboardDsl
    public interface Builder : AbstractBuilder<Team> {

        /**
         * Sets the name for the team to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): Builder

        /**
         * Sets the display name for the team to the given [name].
         *
         * @param name the display name
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun displayName(name: Component): Builder

        /**
         * Sets the prefix for the team to the given [prefix].
         *
         * @param prefix the prefix
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun prefix(prefix: Component): Builder

        /**
         * Sets the suffix for the team to the given [suffix].
         *
         * @param suffix the suffix
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun suffix(suffix: Component): Builder

        /**
         * Sets the team colour to the given [color].
         *
         * @param color the colour
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun color(color: NamedTextColor): Builder

        /**
         * Allows friendly fire for the team.
         *
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("-> this", mutates = "this")
        public fun allowFriendlyFire(): Builder = friendlyFire(true)

        /**
         * Disallows friendly fire for the team.
         *
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("-> this", mutates = "this")
        public fun disallowFriendlyFire(): Builder = friendlyFire(false)

        /**
         * Sets the friendly fire setting for the team to the given [value].
         *
         * @param value the value of the setting
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun friendlyFire(value: Boolean): Builder

        /**
         * Allows friendly fire for the team.
         *
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("-> this", mutates = "this")
        public fun allowSeeingInvisibleMembers(): Builder = canSeeInvisibleMembers(true)

        /**
         * Allows friendly fire for the team.
         *
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("-> this", mutates = "this")
        public fun disallowSeeingInvisibleMembers(): Builder = canSeeInvisibleMembers(false)

        /**
         * Sets whether members can see other members that are invisible to the
         * given [value].
         *
         * @param value the value of the setting
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun canSeeInvisibleMembers(value: Boolean): Builder

        /**
         * Sets the name tag visibility for the team to the given [visibility].
         *
         * @param visibility the visibility
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun nameTagVisibility(visibility: Visibility): Builder

        /**
         * Sets the death message visibility for the team to the given
         * [visibility].
         *
         * @param visibility the visibility
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun deathMessageVisibility(visibility: Visibility): Builder

        /**
         * Sets the collision rule for the team to the given [rule].
         *
         * @param rule the collision rule
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun collisionRule(rule: CollisionRule): Builder

        /**
         * Adds the given [member] to the team's list of members.
         *
         * @param member the member
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun addMember(member: Component): Builder

        /**
         * Removes the given [member] from the team's list of members.
         *
         * @param member the member
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun removeMember(member: Component): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(name: String): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building a team.
         *
         * @param name the name of the team
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(name: String): Builder = Krypton.factoryProvider.provide<Factory>().builder(name)
    }
}
