/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * A team on a [Scoreboard].
 *
 * Teams are groups of entities that have a name, prefix, suffix, colour,
 * and a specific set of rules they follow.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Team {

    /**
     * The name of this team.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The name that is displayed on the scoreboard to clients.
     */
    @get:JvmName("displayName")
    public val displayName: Component

    /**
     * The prefix prepended to the display name of members of this team.
     */
    @get:JvmName("prefix")
    public val prefix: Component

    /**
     * The suffix appended to the display name of members of this team.
     */
    @get:JvmName("suffix")
    public val suffix: Component

    /**
     * The colour of the team that is displayed on the scoreboard.
     */
    @get:JvmName("color")
    public val color: NamedTextColor

    /**
     * If this team allows members to attack each other.
     *
     * *Well that's not very nice, is it! They're there to help you...*
     */
    @get:JvmName("allowFriendlyFire")
    public val allowFriendlyFire: Boolean

    /**
     * If this team allows members to see members that are invisible.
     */
    @get:JvmName("canSeeInvisibleMembers")
    public val canSeeInvisibleMembers: Boolean

    /**
     * The visibility of name tags in the team.
     */
    @get:JvmName("nameTagVisibility")
    public val nameTagVisibility: Visibility

    /**
     * The visibility of death messages in the team.
     */
    @get:JvmName("deathMessageVisibility")
    public val deathMessageVisibility: Visibility

    /**
     * The collision rule for the team.
     */
    @get:JvmName("collisionRule")
    public val collisionRule: CollisionRule

    /**
     * All of the members in this team.
     */
    @get:JvmName("members")
    public val members: Set<Component>

    /**
     * Adds a member to the list of members in this team.
     *
     * @param member the member to add
     */
    public fun addMember(member: Component)

    /**
     * Removes a member from the list of members in this team.
     */
    public fun removeMember(member: Component)
}
