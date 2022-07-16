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
package org.kryptonmc.krypton.world.damage

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.damage.EntityDamageSource
import org.kryptonmc.api.world.damage.IndirectEntityDamageSource
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.krypton.entity.downcast

object KryptonDamageSourceFactory : DamageSource.Factory {

    override fun of(type: DamageType): DamageSource = KryptonDamageSource(type)

    override fun entity(type: DamageType, entity: Entity): EntityDamageSource = KryptonEntityDamageSource(type, entity.downcast())

    override fun indirectEntity(type: DamageType, entity: Entity, indirectEntity: Entity): IndirectEntityDamageSource =
        KryptonIndirectEntityDamageSource(type, entity.downcast(), indirectEntity.downcast())
}
