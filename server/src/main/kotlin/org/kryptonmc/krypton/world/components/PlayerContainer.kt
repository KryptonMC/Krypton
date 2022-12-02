/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.world.components

import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.entity.EntityPredicates
import java.util.UUID
import java.util.function.Predicate

interface PlayerContainer {

    val players: List<KryptonPlayer>

    fun getPlayerById(id: UUID): KryptonPlayer? = players.firstOrNull { it.uuid == id }

    fun getNearestPlayer(x: Double, y: Double, z: Double, distance: Double, filter: Predicate<KryptonEntity>?): KryptonPlayer? {
        var nearestDistance = -1.0
        var nearestPlayer: KryptonPlayer? = null

        players.forEach {
            if (filter != null && !filter.test(it)) return@forEach
            val distanceToPlayer = it.position.distanceSquared(x, y, z)
            if ((x < 0.0 || distanceToPlayer < distance * distance) && (nearestDistance == -1.0 || distanceToPlayer < nearestDistance)) {
                nearestDistance = distanceToPlayer
                nearestPlayer = it
            }
        }
        return nearestPlayer
    }

    fun getNearestPlayer(x: Double, y: Double, z: Double, distance: Double, noCreative: Boolean): KryptonPlayer? {
        val filter = if (noCreative) EntityPredicates.NO_CREATIVE_OR_SPECTATOR else EntityPredicates.NO_SPECTATORS
        return getNearestPlayer(x, y, z, distance, filter)
    }

    fun getNearestPlayer(target: KryptonEntity, distance: Double): KryptonPlayer? =
        getNearestPlayer(target.position.x, target.position.y, target.position.z, distance, false)

    fun hasNearbyAlivePlayer(x: Double, y: Double, z: Double, distance: Double): Boolean {
        players.forEach {
            if (EntityPredicates.NO_SPECTATORS.test(it) && EntityPredicates.LIVING_ENTITY_STILL_ALIVE.test(it)) {
                val distanceTo = it.position.distanceSquared(x, y, z)
                if (distance < 0.0 || distanceTo < distance * distance) return true
            }
        }
        return false
    }
}
