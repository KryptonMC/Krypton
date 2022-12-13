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
