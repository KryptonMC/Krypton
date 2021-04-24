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
