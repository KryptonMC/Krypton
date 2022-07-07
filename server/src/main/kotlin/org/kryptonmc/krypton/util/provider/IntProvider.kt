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

import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.serialization.Encoder
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.compound

interface IntProvider {

    val type: IntProviderType<*>
    val minimumValue: Int
    val maximumValue: Int

    companion object {

        @JvmField
        @Suppress("UNCHECKED_CAST")
        val ENCODER: Encoder<IntProvider, Tag> = Encoder {
            if (it.type == IntProviderTypes.CONSTANT) return@Encoder IntTag.of((it as ConstantIntProvider).value)
            compound {
                val type = (it.type as IntProviderType<IntProvider>)
                put("value", type.encoder().encode(it))
                string("type", InternalRegistries.INT_PROVIDER_TYPES[type]!!.asString())
            }
        }

        @JvmStatic
        fun encoder(minimum: Int, maximum: Int): Encoder<IntProvider, Tag> = Encoder {
            require(it.minimumValue >= minimum) { "Int provider lower bound too low! Minimum value must be >= $minimum!" }
            require(it.maximumValue <= maximum) { "Int provider upper bound too high! Maximum value must be <= $maximum!" }
            ENCODER.encode(it)
        }
    }
}
