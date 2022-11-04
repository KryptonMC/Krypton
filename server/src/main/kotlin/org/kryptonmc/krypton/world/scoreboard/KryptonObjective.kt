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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.criteria.Criterion

class KryptonObjective(
    override val scoreboard: KryptonScoreboard,
    override val name: String,
    override val criterion: Criterion,
    displayName: Component,
    renderType: ObjectiveRenderType
) : Objective {

    override var displayName: Component = displayName
        set(value) {
            field = value
            scoreboard.onObjectiveUpdated(this)
        }
    override var renderType: ObjectiveRenderType = renderType
        set(value) {
            field = value
            scoreboard.onObjectiveUpdated(this)
        }

    class Builder(private val scoreboard: KryptonScoreboard) : Objective.Builder, Objective.Builder.NamedStep, Objective.Builder.EndStep {

        private var name: String? = null
        private var criterion: Criterion? = null
        private var displayName: Component? = null
        private var renderType = ObjectiveRenderType.INTEGER

        override fun name(name: String): Builder = apply { this.name = name }

        override fun displayName(name: Component): Builder = apply { displayName = name }

        override fun criterion(criterion: Criterion): Builder = apply { this.criterion = criterion }

        override fun renderType(type: ObjectiveRenderType): Builder = apply { renderType = type }

        override fun buildAndRegister(): Objective = scoreboard.addObjective(name!!, criterion!!, getName(displayName, name!!), renderType)

        companion object {

            @JvmStatic
            private fun getName(displayName: Component?, name: String): Component =
                displayName ?: LegacyComponentSerializer.legacySection().deserialize(name)
        }
    }
}
