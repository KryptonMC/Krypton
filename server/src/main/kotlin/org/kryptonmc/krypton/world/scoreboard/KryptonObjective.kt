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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.criteria.Criterion

class KryptonObjective(
    var scoreboard: KryptonScoreboard?,
    override val name: String,
    override val criterion: Criterion,
    displayName: Component,
    renderType: ObjectiveRenderType
) : Objective {

    override var displayName: Component = displayName
        set(value) {
            field = value
            scoreboard?.onObjectiveUpdated(this)
        }
    override var renderType: ObjectiveRenderType = renderType
        set(value) {
            field = value
            scoreboard?.onObjectiveUpdated(this)
        }

    override fun toBuilder(): Objective.Builder = Builder(this)

    class Builder(private var name: String, private var criterion: Criterion) : Objective.Builder {

        private var scoreboard: KryptonScoreboard? = null
        private var displayName: Component = LegacyComponentSerializer.legacySection().deserialize(name)
        private var renderType = ObjectiveRenderType.INTEGER

        constructor(objective: KryptonObjective) : this(objective.name, objective.criterion) {
            scoreboard = objective.scoreboard
            displayName = objective.displayName
            renderType = objective.renderType
        }

        override fun name(name: String): Objective.Builder = apply { this.name = name }

        override fun displayName(name: Component): Objective.Builder = apply { displayName = name }

        override fun criterion(criterion: Criterion): Objective.Builder = apply { this.criterion = criterion }

        override fun renderType(type: ObjectiveRenderType): Objective.Builder = apply { renderType = type }

        override fun build(): KryptonObjective = KryptonObjective(scoreboard, name, criterion, displayName, renderType)
    }

    object Factory : Objective.Factory {

        override fun builder(name: String, criterion: Criterion): Objective.Builder = Builder(name, criterion)
    }
}
