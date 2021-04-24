package org.kryptonmc.krypton.entity.memory

import org.kryptonmc.krypton.api.registry.NamespacedKey
import org.kryptonmc.krypton.world.LocationBuilder
import java.util.UUID

/**
 * Represents a memory of an entity. These are specific to the type of entity,
 * and may vary depending on what entity it is.
 */
sealed class EntityMemory<T>(val key: NamespacedKey) {

    abstract val value: T
}

/**
 * If this piglin can admire an item at this moment, set when being converted, hurt, or just after admiring an item
 */
class AdmiringDisabledMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(NamespacedKey(value = "admiring_disabled"))

/**
 * If this piglin is admiring an item
 */
class AdmiringItemMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(NamespacedKey(value = "admiring_item"))

/**
 * The target of this piglin or piglin brute
 */
class AngryAtMemory(
    override val value: UUID,
    val ttl: Long
) : EntityMemory<UUID>(NamespacedKey(value = "angry_at"))

/**
 * Where this villager's bed is or where this piglin brute's patrol point is
 */
class HomeMemory(override val value: Position) : EntityMemory<Position>(NamespacedKey(value = "home"))

/**
 * If this piglin just hunted, and as such, won't for a while. Set after hunting or spawning in a bastion remnant
 */
class HuntedRecentlyMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(NamespacedKey(value = "hunted_recently"))

/**
 * Where this villager's job site block is
 */
class JobSiteMemory(override val value: Position) : EntityMemory<Position>(NamespacedKey(value = "job_site"))

/**
 * The tick that this villager last slept in a bed
 */
class LastSleptMemory(override val value: Long) : EntityMemory<Long>(NamespacedKey(value = "last_slept"))

/**
 * The tick that this villager last woke up from a bed
 */
class LastWokenMemory(override val value: Long) : EntityMemory<Long>(NamespacedKey(value = "last_woken"))

/**
 * The tick that this villager last worked at their job site
 */
class LastWorkedMemory(override val value: Long) : EntityMemory<Long>(NamespacedKey(value = "last_worked_at_poi"))

/**
 * Where this villager's meeting point is
 */
class MeetingPointMemory(override val value: Position) : EntityMemory<Position>(NamespacedKey(value = "meeting_point"))

/**
 * Position information for a villager's meeting point
 */
data class Position(
    val dimension: NamespacedKey,
    val position: LocationBuilder
)
