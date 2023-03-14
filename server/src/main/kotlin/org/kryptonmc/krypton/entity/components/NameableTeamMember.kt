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
