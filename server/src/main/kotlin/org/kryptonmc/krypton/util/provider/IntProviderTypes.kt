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
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.util.Either
import java.util.function.Function

object IntProviderTypes {

    // We put the XMAPs here because otherwise we get a compilation error. See https://youtrack.jetbrains.com/issue/KT-53478
    @JvmField
    val CONSTANT: IntProviderType<ConstantInt> = register("constant", ConstantInt.CODEC
        .xmap({ it.map(ConstantInt::of, Function.identity()) }, { Either.left(it.value) }))
    @JvmField
    val UNIFORM: IntProviderType<UniformInt> = register("uniform", UniformInt.CODEC.comapFlatMap({
        if (it.maximumValue >= it.minimumValue) return@comapFlatMap DataResult.success(it)
        DataResult.error("Maximum must be >= minimum! Maximum: ${it.maximumValue}, minimum: ${it.minimumValue}")
    }, Function.identity()))

    @JvmStatic
    private fun <P : IntProvider> register(name: String, codec: Codec<P>): IntProviderType<P> =
        InternalRegistries.INT_PROVIDER_TYPES.register(Key.key(name), IntProviderType { codec })
}
