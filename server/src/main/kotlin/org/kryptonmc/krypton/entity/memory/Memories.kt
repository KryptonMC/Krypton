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

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.krypton.world.LocationBuilder
import java.util.UUID

/**
 * Represents a memory of an entity. These are specific to the type of entity,
 * and may vary depending on what entity it is.
 */
sealed class EntityMemory<T>(val key: Key) {

    abstract val value: T
}

/**
 * If this piglin can admire an item at this moment, set when being converted, hurt, or just after admiring an item
 */
class AdmiringDisabledMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(key("admiring_disabled"))

/**
 * If this piglin is admiring an item
 */
class AdmiringItemMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(key("admiring_item"))

/**
 * The target of this piglin or piglin brute
 */
class AngryAtMemory(
    override val value: UUID,
    val ttl: Long
) : EntityMemory<UUID>(key("angry_at"))

/**
 * Where this villager's bed is or where this piglin brute's patrol point is
 */
class HomeMemory(override val value: Position) : EntityMemory<Position>(key("home"))

/**
 * If this piglin just hunted, and as such, won't for a while. Set after hunting or spawning in a bastion remnant
 */
class HuntedRecentlyMemory(
    override val value: Boolean,
    val ttl: Long
) : EntityMemory<Boolean>(key("hunted_recently"))

/**
 * Where this villager's job site block is
 */
class JobSiteMemory(override val value: Position) : EntityMemory<Position>(key("job_site"))

/**
 * The tick that this villager last slept in a bed
 */
class LastSleptMemory(override val value: Long) : EntityMemory<Long>(key("last_slept"))

/**
 * The tick that this villager last woke up from a bed
 */
class LastWokenMemory(override val value: Long) : EntityMemory<Long>(key("last_woken"))

/**
 * The tick that this villager last worked at their job site
 */
class LastWorkedMemory(override val value: Long) : EntityMemory<Long>(key("last_worked_at_poi"))

/**
 * Where this villager's meeting point is
 */
class MeetingPointMemory(override val value: Position) : EntityMemory<Position>(key("meeting_point"))

/**
 * Position information for a villager's meeting point
 */
data class Position(
    val dimension: Key,
    val position: LocationBuilder
)
