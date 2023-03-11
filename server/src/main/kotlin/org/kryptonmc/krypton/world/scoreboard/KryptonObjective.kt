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
