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
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.util.Either
import java.util.function.Function
import kotlin.random.Random

abstract class IntProvider {

    abstract val type: IntProviderType<*>
    abstract val minimumValue: Int
    abstract val maximumValue: Int

    abstract fun sample(random: Random): Int

    companion object {

        private val CONSTANT_OR_DISPATCH_CODEC: Codec<Either<Int, IntProvider>> = Codec.either(
            Codec.INT,
            InternalRegistries.INT_PROVIDER_TYPES.byNameCodec().dispatch(IntProvider::type, IntProviderType<*>::codec)
        )
        @JvmField
        val CODEC: Codec<IntProvider> = CONSTANT_OR_DISPATCH_CODEC.xmap(
            { it.map(ConstantInt::of, Function.identity()) },
            { if (it is ConstantInt) Either.left(it.value) else Either.right(it) }
        )

        @JvmStatic
        fun codec(min: Int, max: Int): Codec<IntProvider> {
            val checker = Function<IntProvider, DataResult<IntProvider>> {
                if (it.minimumValue < min) return@Function DataResult.error("Int provider lower bound too low! Minimum value must be >= $min!")
                if (it.maximumValue > max) return@Function DataResult.error("Int provider upper bound too high! Maximum value must be <= $max!")
                DataResult.success(it)
            }
            return CODEC.flatXmap(checker, checker)
        }
    }
}
