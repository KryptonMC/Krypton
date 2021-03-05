package org.kryptonmc.krypton.entity.memory

import org.kryptonmc.krypton.registry.NamespacedKey
import org.kryptonmc.krypton.world.Location
import java.util.*

sealed class EntityMemory<T>(val key: NamespacedKey) {

    abstract val value: T
}

class AdmiringDisabledMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(NamespacedKey(value = "admiring_disabled"))

class AdmiringItemMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(NamespacedKey(value = "admiring_item"))

class AngryAtMemory(
    override val value: UUID,
    val ttl: Long
) : EntityMemory<UUID>(NamespacedKey(value = "angry_at"))

class HomeMemory(override val value: Position) : EntityMemory<Position>(NamespacedKey(value = "home"))

class HuntedRecentlyMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(NamespacedKey(value = "hunted_recently"))

class JobSiteMemory(override val value: Position) : EntityMemory<Position>(NamespacedKey(value = "job_site"))

class LastSleptMemory(override val value: Long) : EntityMemory<Long>(NamespacedKey(value = "last_slept"))

class LastWokenMemory(override val value: Long) : EntityMemory<Long>(NamespacedKey(value = "last_woken"))

class LastWorkedMemory(override val value: Long) : EntityMemory<Long>(NamespacedKey(value = "last_worked_at_poi"))

class MeetingPointMemory(override val value: Position) : EntityMemory<Position>(NamespacedKey(value = "meeting_point"))

data class Position(
    val dimension: NamespacedKey,
    val position: Location
)