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
package org.kryptonmc.krypton.world.generation

import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.registry.network.RegistryCodecs
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.ImmutableSets
import org.kryptonmc.krypton.world.dimension.WorldDimension
import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.function.Function
import java.util.stream.Stream

@JvmRecord
data class WorldDimensions(val dimensions: KryptonRegistry<WorldDimension>) {

    init {
        checkNotNull(dimensions.get(WorldDimension.OVERWORLD)) { "Overworld dimension missing!" }
    }

    fun get(key: ResourceKey<WorldDimension>): WorldDimension? = dimensions.get(key)

    companion object {

        @JvmField
        val CODEC: MapCodec<WorldDimensions> = RecordCodecBuilder.createMap { instance ->
            instance.group(
                RegistryCodecs.full(KryptonResourceKeys.DIMENSION, WorldDimension.CODEC).fieldOf("dimensions").getting { it.dimensions }
            ).apply(instance, RecordCodecBuilder.stable(Function { WorldDimensions(it) }))
        }
        private val BUILTIN_ORDER = ImmutableSets.of(WorldDimension.OVERWORLD, WorldDimension.NETHER, WorldDimension.END)

        @JvmStatic
        fun keysInOrder(keys: Stream<ResourceKey<WorldDimension>>): Stream<ResourceKey<WorldDimension>> =
            Stream.concat(BUILTIN_ORDER.stream(), keys.filter { !BUILTIN_ORDER.contains(it) })
    }
}
