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
package org.kryptonmc.krypton.util.serialization

import org.kryptonmc.serialization.MapCodec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.Optional
import java.util.OptionalLong
import java.util.function.Function

private val TO_OPTIONAL_LONG = Function<Optional<Long>, OptionalLong> { it.map(OptionalLong::of).orElseGet(OptionalLong::empty) }
private val FROM_OPTIONAL_LONG = Function<OptionalLong, Optional<Long>> { if (it.isPresent) Optional.of(it.asLong) else Optional.empty() }

fun MapCodec<Optional<Long>>.asOptionalLong(): MapCodec<OptionalLong> = xmap(TO_OPTIONAL_LONG, FROM_OPTIONAL_LONG)

inline fun <O, A : Any> MapCodec<Optional<A>>.gettingNullable(crossinline function: (O) -> A?): RecordCodecBuilder<O, Optional<A>> =
    getting { Optional.ofNullable(function(it)) }
