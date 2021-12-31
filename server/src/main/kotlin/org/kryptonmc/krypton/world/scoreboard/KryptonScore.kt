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
import org.kryptonmc.api.adventure.toLegacySectionText
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.Score

class KryptonScore(
    var scoreboard: KryptonScoreboard?,
    override val objective: Objective?,
    override val name: Component
) : Score {

    private var forceUpdate = true
    val nameString: String = name.toLegacySectionText()
    override var score = 0
        set(value) {
            val old = field
            field = value
            if (old != value || forceUpdate) {
                forceUpdate = false
                scoreboard?.onScoreUpdated(this)
            }
        }
    override var isLocked = true

    fun add(amount: Int) {
        if (objective == null || !objective.criterion.isMutable) return
        score += amount
    }

    fun subtract(amount: Int) {
        if (objective == null || !objective.criterion.isMutable) return
        score -= amount
    }

    fun reset() {
        score = 0
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<KryptonScore> = Comparator { o1, o2 ->
            if (o1.score > o2.score) return@Comparator 1
            if (o1.score < o2.score) return@Comparator -1
            o1.nameString.compareTo(o2.nameString, true)
        }
    }
}
