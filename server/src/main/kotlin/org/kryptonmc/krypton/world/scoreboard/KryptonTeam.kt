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
