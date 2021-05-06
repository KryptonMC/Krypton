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
package org.kryptonmc.krypton.entity.memory

import net.kyori.adventure.nbt.CompoundBinaryTag

/**
 * Represents an entity's brain. For most entities, this will use [EmptyBrain].
 *
 * @since 0.16
 */
sealed class Brain<T : EntityMemories> {

    abstract val memories: T

    open fun write(): CompoundBinaryTag = CompoundBinaryTag.empty()
}

/**
 * An empty brain. *Nothing to see here...*
 */
object EmptyBrain : Brain<EmptyMemories>() {

    override val memories = EmptyMemories

    override fun write() = CompoundBinaryTag.builder()
        .put("Brain", CompoundBinaryTag.builder()
            .put("memories", CompoundBinaryTag.empty())
            .build())
        .build()
}

/**
 * @since 0.16
 */
data class PiglinBrain(override val memories: PiglinMemories) : Brain<PiglinMemories>()

/**
 * @since 0.16
 */
data class VillagerBrain(override val memories: VillagerMemories) : Brain<VillagerMemories>()

/**
 * Base interface for memories classes
 */
interface EntityMemories

/**
 * Represents empty memories
 */
object EmptyMemories : EntityMemories

/**
 * Represents memories for a piglin
 */
data class PiglinMemories(
    val admiringDisabled: AdmiringDisabledMemory,
    val admiringItem: AdmiringItemMemory,
    val angryAt: AngryAtMemory,
    val huntedRecently: HuntedRecentlyMemory
) : EntityMemories

/**
 * Represents memories for a villager
 */
data class VillagerMemories(
    val home: HomeMemory,
    val jobSite: JobSiteMemory,
    val lastSlept: LastSleptMemory,
    val lastWoken: LastWokenMemory,
    val lastWorked: LastWorkedMemory,
    val meetingPoint: MeetingPointMemory
) : EntityMemories
