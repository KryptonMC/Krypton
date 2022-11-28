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

import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.world.entity.EntityPredicates
import org.kryptonmc.krypton.world.entity.EntityTypeTest
import java.util.function.Predicate

// TODO: Revise when we change the entity management
interface EntityGetter {

    fun getEntities(except: KryptonEntity?, area: BoundingBox, filter: Predicate<in KryptonEntity>): List<KryptonEntity> = emptyList()

    fun getEntities(area: BoundingBox, filter: Predicate<in KryptonEntity>): List<KryptonEntity> = getEntities(null, area, filter)

    fun getEntities(except: KryptonEntity?, area: BoundingBox): List<KryptonEntity> = getEntities(except, area, EntityPredicates.NO_SPECTATORS)

    fun getEntities(area: BoundingBox): List<KryptonEntity> = getEntities(null, area)

    fun <T : KryptonEntity> getEntities(test: EntityTypeTest<KryptonEntity, T>, area: BoundingBox, filter: Predicate<in T>): List<T> = emptyList()

    fun <T : KryptonEntity> getEntitiesOfType(type: Class<T>, area: BoundingBox, filter: Predicate<in T>): List<T> = emptyList()

    fun <T : KryptonEntity> getEntitiesOfType(type: Class<T>, area: BoundingBox): List<T> =
        getEntitiesOfType(type, area, EntityPredicates.NO_SPECTATORS)
}
