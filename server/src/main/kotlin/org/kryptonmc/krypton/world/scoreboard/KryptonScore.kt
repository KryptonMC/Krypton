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
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.Score

class KryptonScore(override val scoreboard: KryptonScoreboard, override val objective: Objective, override val member: Component) : Score {

    override var score: Int = 0
        set(value) {
            val old = field
            field = value
            if (old != value || forceUpdate) {
                forceUpdate = false
                scoreboard.onScoreUpdated(this)
            }
        }
    override var isLocked: Boolean = true
    private var forceUpdate = true
}
