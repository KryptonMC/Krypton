/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.world.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.scoreboard.CollisionRule
import org.kryptonmc.api.scoreboard.Visibility

class KryptonTeam(
    var scoreboard: KryptonScoreboard?,
    override val name: String,
    displayName: Component = Component.text(name),
    prefix: Component = Component.empty(),
    suffix: Component = Component.empty(),
    color: NamedTextColor = NamedTextColor.WHITE,
    allowFriendlyFire: Boolean = true,
    canSeeInvisibleMembers: Boolean = true,
    nameTagVisibility: Visibility = Visibility.ALWAYS,
    deathMessagesVisibility: Visibility = Visibility.ALWAYS,
    collisionRule: CollisionRule = CollisionRule.ALWAYS,
    override val members: MutableSet<Component> = mutableSetOf()
) : Team {

    override var displayName: Component = displayName
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var prefix: Component = prefix
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var suffix: Component = suffix
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var color: NamedTextColor = color
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var allowFriendlyFire = allowFriendlyFire
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var canSeeInvisibleMembers = canSeeInvisibleMembers
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var nameTagVisibility = nameTagVisibility
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var deathMessageVisibility = deathMessagesVisibility
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }
    override var collisionRule = collisionRule
        set(value) {
            field = value
            scoreboard?.onTeamUpdated(this)
        }

    override fun formatName(name: Component): Component = Component.empty().toBuilder()
        .append(prefix)
        .append(name)
        .append(suffix)
        .color(color)
        .build()

    override fun addMember(member: Component) {
        members.add(member)
    }

    override fun removeMember(member: Component) {
        members.remove(member)
    }

    override fun toBuilder(): Team.Builder = Builder(this)

    class Builder(private var name: String) : Team.Builder {

        private var scoreboard: KryptonScoreboard? = null
        private var displayName: Component = LegacyComponentSerializer.legacySection().deserialize(name)
        private var prefix: Component = Component.empty()
        private var suffix: Component = Component.empty()
        private var color: NamedTextColor = NamedTextColor.WHITE
        private var friendlyFire = true
        private var canSeeInvisibleMembers = true
        private var nameTags = Visibility.ALWAYS
        private var deathMessages = Visibility.ALWAYS
        private var collisionRule = CollisionRule.ALWAYS
        private val members = mutableSetOf<Component>()

        constructor(team: KryptonTeam) : this(team.name) {
            scoreboard = team.scoreboard
            displayName = team.displayName
            prefix = team.prefix
            suffix = team.suffix
            color = team.color
            friendlyFire = team.allowFriendlyFire
            canSeeInvisibleMembers = team.canSeeInvisibleMembers
            nameTags = team.nameTagVisibility
            deathMessages = team.deathMessageVisibility
            collisionRule = team.collisionRule
            members.addAll(team.members)
        }

        override fun name(name: String): Team.Builder = apply { this.name = name }

        override fun displayName(name: Component): Team.Builder = apply { displayName = name }

        override fun prefix(prefix: Component): Team.Builder = apply { this.prefix = prefix }

        override fun suffix(suffix: Component): Team.Builder = apply { this.suffix = suffix }

        override fun color(color: NamedTextColor): Team.Builder = apply { this.color = color }

        override fun friendlyFire(value: Boolean): Team.Builder = apply { friendlyFire = value }

        override fun canSeeInvisibleMembers(value: Boolean): Team.Builder = apply { canSeeInvisibleMembers = value }

        override fun nameTagVisibility(visibility: Visibility): Team.Builder = apply { nameTags = visibility }

        override fun deathMessageVisibility(visibility: Visibility): Team.Builder = apply { deathMessages = visibility }

        override fun collisionRule(rule: CollisionRule): Team.Builder = apply { collisionRule = rule }

        override fun addMember(member: Component): Team.Builder = apply { members.add(member) }

        override fun removeMember(member: Component): Team.Builder = apply { members.remove(member) }

        override fun build(): Team = KryptonTeam(
            scoreboard,
            name,
            displayName,
            prefix,
            suffix,
            color,
            friendlyFire,
            canSeeInvisibleMembers,
            nameTags,
            deathMessages,
            collisionRule,
            members
        )
    }

    object Factory : Team.Factory {

        override fun builder(name: String): Team.Builder = Builder(name)
    }
}
