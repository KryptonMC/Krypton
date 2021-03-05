package org.kryptonmc.krypton.entity.memory

sealed class Brain<T : EntityMemories> {

    abstract val memories: T
}

object EmptyBrain : Brain<EmptyMemories>() {

    override val memories = EmptyMemories
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