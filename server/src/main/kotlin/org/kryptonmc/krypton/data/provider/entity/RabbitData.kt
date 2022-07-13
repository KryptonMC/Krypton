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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Keys
import org.kryptonmc.api.entity.animal.type.RabbitType
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.animal.KryptonRabbit
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object RabbitData {

    private val TYPES = RabbitType.values()

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata<KryptonRabbit, _, _>(Keys.RABBIT_TYPE, MetadataKeys.RABBIT.TYPE, ::type, RabbitType::ordinal)
    }

    @JvmStatic
    private fun type(id: Int): RabbitType {
        // Look, that rabbit's got a vicious streak a mile wide! It's a killer!
        if (id == 99) return RabbitType.KILLER
        return TYPES.getOrNull(id) ?: RabbitType.BROWN
    }
}
