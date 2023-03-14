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

import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

class UniformInt(override val minimumValue: Int, override val maximumValue: Int) : IntProvider() {

    override val type: IntProviderType<*>
        get() = IntProviderTypes.UNIFORM

    override fun sample(random: RandomSource): Int = Maths.randomBetween(random, minimumValue, maximumValue)

    override fun toString(): String = "[$minimumValue-$maximumValue]"

    companion object {

        @JvmField
        val CODEC: Codec<UniformInt> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("min_inclusive").getting { it.minimumValue },
                Codec.INT.fieldOf("max_inclusive").getting { it.maximumValue }
            ).apply(instance) { min, max -> UniformInt(min, max) }
        }/*.comapFlatMap({
            if (it.maximumValue >= it.minimumValue) return@comapFlatMap DataResult.success(it)
            DataResult.error("Maximum must be >= minimum! Maximum: ${it.maximumValue}, minimum: ${it.minimumValue}")
        }, Function.identity())*/
        // For why the above is commented out, see: https://youtrack.jetbrains.com/issue/KT-53478
    }
}
