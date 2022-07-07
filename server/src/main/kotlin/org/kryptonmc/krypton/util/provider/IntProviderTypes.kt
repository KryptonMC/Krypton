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
package org.kryptonmc.krypton.util.provider

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.serialization.Encoder
import org.kryptonmc.nbt.Tag

object IntProviderTypes {

    @JvmField
    val CONSTANT: IntProviderType<ConstantIntProvider> = register("constant", ConstantIntProvider.ENCODER)
    @JvmField
    val UNIFORM: IntProviderType<UniformIntProvider> = register("uniform", UniformIntProvider.ENCODER)

    @JvmStatic
    private fun <P : IntProvider> register(name: String, encoder: Encoder<P, out Tag>): IntProviderType<P> =
        InternalRegistries.INT_PROVIDER_TYPES.register(Key.key(name), IntProviderType { encoder })
}
