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
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.animal.KryptonCat
import org.kryptonmc.krypton.entity.metadata.MetadataKeys

object CatData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMetadata<KryptonCat, _, _>(Keys.CAT_VARIANT, MetadataKeys.CAT.VARIANT, ::variant, Registries.CAT_VARIANT::idOf)
        registrar.registerMetadata<KryptonCat, _>(Keys.IS_LYING, MetadataKeys.CAT.LYING)
        registrar.registerMetadata<KryptonCat, _>(Keys.IS_RELAXED, MetadataKeys.CAT.RELAXED)
        registrar.registerMetadata<KryptonCat, _, _>(Keys.COLLAR_COLOR, MetadataKeys.CAT.COLLAR_COLOR, ::color, Registries.DYE_COLORS::idOf)
    }

    @JvmStatic
    private fun variant(id: Int): CatVariant = requireNotNull(Registries.CAT_VARIANT[id]) {
        "Cannot get cat variant with ID $id! Invalid entity metadata!"
    }

    @JvmStatic
    private fun color(id: Int): DyeColor = requireNotNull(Registries.DYE_COLORS[id]) {
        "Cannot get collar color (dye color) with ID $id! Invalid entity metadata!"
    }
}
