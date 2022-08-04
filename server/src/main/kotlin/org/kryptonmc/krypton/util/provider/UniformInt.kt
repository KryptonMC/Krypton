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

import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.CompoundCodecBuilder
import java.util.function.Function

class UniformInt(override val minimumValue: Int, override val maximumValue: Int) : IntProvider() {

    override val type: IntProviderType<*>
        get() = IntProviderTypes.UNIFORM

    override fun toString(): String = "[$minimumValue-$maximumValue]"

    companion object {

        @JvmField
        val CODEC: Codec<UniformInt> = CompoundCodecBuilder.create {
            it.group(
                Codec.INT.field("min_inclusive").getting(UniformInt::minimumValue),
                Codec.INT.field("max_inclusive").getting(UniformInt::maximumValue)
            ).apply(it, ::UniformInt)
        }/*.xmap({
            check(it.minimumValue <= it.maximumValue) { "Maximum must be >= minimum! Maximum: ${it.maximumValue}, minimum: ${it.minimumValue}" }
            it
        }, Function.identity())*/
    }
}
