/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.scoreboard.CollisionRule
import org.kryptonmc.api.scoreboard.Visibility
import java.util.Collections

class KryptonTeam(override val scoreboard: KryptonScoreboard, override val name: String) : Team {

    private var _displayName: Component = Component.text(name)
    private var _prefix: Component = Component.empty()
    private var _suffix: Component = Component.empty()
    private var _allowFriendlyFire = true
    private var _canSeeInvisibleMembers = true
    private var _nameTagVisibility = Visibility.ALWAYS
    private var _deathMessageVisibility = Visibility.ALWAYS
    private var _color = NamedTextColor.WHITE
    private var _collisionRule = CollisionRule.ALWAYS
    private val _members = ArrayList<Component>()

    override var displayName: Component
        get() = _displayName
        set(value) {
            _displayName = value
            scoreboard.onTeamUpdated(this)
        }
    override var prefix: Component
        get() = _prefix
        set(value) {
            _prefix = value
            scoreboard.onTeamUpdated(this)
        }
    override var suffix: Component
        get() = _suffix
        set(value) {
            _suffix = value
            scoreboard.onTeamUpdated(this)
        }
    override var allowFriendlyFire: Boolean
        get() = _allowFriendlyFire
        set(value) {
            _allowFriendlyFire = value
            scoreboard.onTeamUpdated(this)
        }
    override var canSeeInvisibleMembers: Boolean
        get() = _canSeeInvisibleMembers
        set(value) {
            _canSeeInvisibleMembers = value
            scoreboard.onTeamUpdated(this)
        }
    override var nameTagVisibility: Visibility
        get() = _nameTagVisibility
        set(value) {
            _nameTagVisibility = value
            scoreboard.onTeamUpdated(this)
        }
    override var deathMessageVisibility: Visibility
        get() = _deathMessageVisibility
        set(value) {
            _deathMessageVisibility = value
            scoreboard.onTeamUpdated(this)
        }
    override var color: NamedTextColor
        get() = _color
        set(value) {
            _color = value
            scoreboard.onTeamUpdated(this)
        }
    override var collisionRule: CollisionRule
        get() = _collisionRule
        set(value) {
            _collisionRule = value
            scoreboard.onTeamUpdated(this)
        }
    override val members: List<Component> = Collections.unmodifiableList(_members)

    override fun formatName(name: Component): Component = Component.text().append(prefix).append(name).append(suffix).color(color).build()

    override fun addMember(member: Component): Boolean = _members.add(member)

    override fun removeMember(member: Component): Boolean = _members.remove(member)

    class Builder(private val scoreboard: KryptonScoreboard, private val name: String) : Team.Builder {

        private var displayName: Component = Component.text(name)
        private var prefix: Component = Component.empty()
        private var suffix: Component = Component.empty()
        private var friendlyFire = true
        private var seeInvisibles = true
        private var nameTags = Visibility.ALWAYS
        private var deathMessages = Visibility.ALWAYS
        private var color = NamedTextColor.WHITE
        private var collision = CollisionRule.ALWAYS
        private val members = HashSet<Component>()

        override fun displayName(name: Component): Team.Builder = apply { displayName = name }

        override fun prefix(prefix: Component): Team.Builder = apply { this.prefix = prefix }

        override fun suffix(suffix: Component): Team.Builder = apply { this.suffix = suffix }

        override fun friendlyFire(): Team.Builder = apply { friendlyFire = true }

        override fun seeInvisibleMembers(): Team.Builder = apply { seeInvisibles = true }

        override fun nameTagVisibility(visibility: Visibility): Team.Builder = apply { nameTags = visibility }

        override fun deathMessageVisibility(visibility: Visibility): Team.Builder = apply { deathMessages = visibility }

        override fun color(color: NamedTextColor): Team.Builder = apply { this.color = color }

        override fun collisionRule(rule: CollisionRule): Team.Builder = apply { collision = rule }

        override fun addMember(member: Component): Team.Builder = apply { members.add(member) }

        override fun buildAndRegister(): Team {
            val team = scoreboard.addTeam(name)
            team._displayName = displayName
            team._prefix = prefix
            team._suffix = suffix
            team._allowFriendlyFire = friendlyFire
            team._canSeeInvisibleMembers = seeInvisibles
            team._nameTagVisibility = nameTags
            team._deathMessageVisibility = deathMessages
            team._color = color
            team._collisionRule = collision
            team._members.addAll(members)
            return team
        }
    }
}
