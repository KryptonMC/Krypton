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
package org.kryptonmc.krypton.entity.components

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType

interface NameableTeamMember : Entity {

    override val type: KryptonEntityType<KryptonEntity>

    override val name: String
        get() = PlainTextComponentSerializer.plainText().serialize(nameOrDescription())
    override val displayName: Component
        get() {
            val team = team ?: return nameOrDescription()
            return team.formatName(nameOrDescription()).style {
                it.hoverEvent(asHoverEvent())
                it.insertion(uuid.toString())
            }
        }
    override val teamRepresentation: Component
        get() = Component.text(uuid.toString())
    override val team: Team?
        get() = world.scoreboard.getMemberTeam(teamRepresentation)

    fun nameOrDescription(): Component = customName ?: type.description()
}
