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
import org.kryptonmc.api.world.scoreboard.CollisionRule
import org.kryptonmc.api.world.scoreboard.Team
import org.kryptonmc.api.world.scoreboard.Visibility

@JvmRecord
data class KryptonTeam(
    override val name: String,
    override val displayName: Component,
    override val prefix: Component,
    override val suffix: Component,
    override val color: NamedTextColor,
    override val allowFriendlyFire: Boolean,
    override val canSeeInvisibleMembers: Boolean,
    override val nameTagVisibility: Visibility,
    override val deathMessageVisibility: Visibility,
    override val collisionRule: CollisionRule,
    override val members: MutableSet<Component>
) : Team {

    override fun addMember(member: Component) {
        members.add(member)
    }

    override fun removeMember(member: Component) {
        members.remove(member)
    }
}
