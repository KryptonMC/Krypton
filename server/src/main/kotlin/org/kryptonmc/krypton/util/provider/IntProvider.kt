/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util.provider

import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.DataResult
import org.kryptonmc.util.Either
import java.util.function.Function

@Suppress("UnnecessaryAbstractClass")
abstract class IntProvider {

    abstract val type: IntProviderType<*>
    abstract val minimumValue: Int
    abstract val maximumValue: Int

    abstract fun sample(random: RandomSource): Int

    companion object {

        private val CONSTANT_OR_DISPATCH_CODEC = Codec.either(
            Codec.INT,
            KryptonRegistries.INT_PROVIDER_TYPE.byNameCodec().dispatch({ it.type }, { it.codec() })
        )
        @JvmField
        val CODEC: Codec<IntProvider> = CONSTANT_OR_DISPATCH_CODEC.xmap(
            { either -> either.map({ ConstantInt.of(it) }, Function.identity()) },
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
