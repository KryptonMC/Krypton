package org.kryptonmc.krypton.entity.memory

import net.kyori.adventure.nbt.CompoundBinaryTag

/**
 * Represents an entity's brain. For most entities, this will use [EmptyBrain].
 *
 * @author Callum Seabrook
 */
sealed class Brain<T : EntityMemories> {

    abstract val memories: T

    open fun write(): CompoundBinaryTag = CompoundBinaryTag.empty()
}

object EmptyBrain : Brain<EmptyMemories>() {

    override val memories = EmptyMemories

    override fun write() = CompoundBinaryTag.builder()
        .put("Brain", CompoundBinaryTag.builder()
            .put("memories", CompoundBinaryTag.empty())
            .build())
        .build()
}

data class PiglinBrain(override val memories: PiglinMemories) : Brain<PiglinMemories>()

data class VillagerBrain(override val memories: VillagerMemories) : Brain<VillagerMemories>()

interface EntityMemories

object EmptyMemories : EntityMemories

data class PiglinMemories(
    val admiringDisabled: AdmiringDisabledMemory,
    val admiringItem: AdmiringItemMemory,
    val angryAt: AngryAtMemory,
    val huntedRecently: HuntedRecentlyMemory
) : EntityMemories

data class VillagerMemories(
    val home: HomeMemory,
    val jobSite: JobSiteMemory,
    val lastSlept: LastSleptMemory,
    val lastWoken: LastWokenMemory,
    val lastWorked: LastWorkedMemory,
    val meetingPoint: MeetingPointMemory
) : EntityMemories