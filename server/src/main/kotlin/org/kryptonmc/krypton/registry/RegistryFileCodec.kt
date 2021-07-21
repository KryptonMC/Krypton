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
package org.kryptonmc.krypton.registry

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.ops.RegistryReadOps
import org.kryptonmc.krypton.registry.ops.RegistryWriteOps
import java.util.function.Supplier

class RegistryFileCodec<E : Any>(
    private val registryKey: ResourceKey<out Registry<E>>,
    private val elementCodec: Codec<E>,
    private val allowInline: Boolean = true
) : Codec<Supplier<E>> {

    override fun <T> encode(input: Supplier<E>, ops: DynamicOps<T>, prefix: T): DataResult<T> =
        if (ops is RegistryWriteOps) ops.encode(input.get(), prefix, registryKey, elementCodec) else elementCodec.encode(input.get(), ops, prefix)

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Supplier<E>, T>> {
        return if (ops is RegistryReadOps) ops.decodeElement(input, registryKey, elementCodec, allowInline) else elementCodec.decode(ops, input).map { pair -> pair.mapFirst { Supplier { it } } }
    }

    override fun toString() = "RegistryFileCodec($registryKey, $elementCodec)"
}
