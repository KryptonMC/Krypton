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

import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.util.Either

class ConstantInt private constructor(val value: Int) : IntProvider() {

    override val type: IntProviderType<*>
        get() = IntProviderTypes.CONSTANT
    override val minimumValue: Int
        get() = value
    override val maximumValue: Int
        get() = value

    override fun sample(random: RandomSource): Int = value

    override fun toString(): String = value.toString()

    companion object {

        @JvmField
        val ZERO: ConstantInt = ConstantInt(0)
        @JvmField
        val CODEC: Codec<Either<Int, ConstantInt>> = Codec.either(
            Codec.INT,
            RecordCodecBuilder.create { instance -> instance.group(Codec.INT.fieldOf("value").getting { it.value }).apply(instance) { of(it) } }
        )/*.xmap({ it.map(ConstantInt::of, Function.identity()) }, { Either.left(it.value) })*/
        // For why the above is commented out, see: https://youtrack.jetbrains.com/issue/KT-53478

        @JvmStatic
        fun of(value: Int): ConstantInt = ConstantInt(value)
    }
}
