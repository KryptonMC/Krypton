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
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.RenderType
import org.kryptonmc.api.scoreboard.criteria.Criterion

@JvmRecord
data class KryptonObjective(
    override val name: String,
    override val displayName: Component,
    override val criterion: Criterion?,
    override val renderType: RenderType
) : Objective {

    object Factory : Objective.Factory {

        override fun of(
            name: String,
            displayName: Component,
            criterion: Criterion?,
            renderType: RenderType
        ): Objective = KryptonObjective(name, displayName, criterion, renderType)
    }
}
