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
package org.kryptonmc.krypton.util.nbt

import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.Tag
import java.util.Spliterators
import java.util.stream.Stream
import java.util.stream.StreamSupport

fun Tag.isCollection(): Boolean = this is ListTag || this is ByteArrayTag || this is IntArrayTag || this is LongArrayTag

fun Tag.stream(): Stream<Tag> = when (this) {
    is ListTag -> data.stream()
    is ByteArrayTag -> StreamSupport.stream(Spliterators.spliterator(data.map(ByteTag::of), 0), false)
    is IntArrayTag -> StreamSupport.stream(Spliterators.spliterator(data.map(IntTag::of), 0), false)
    is LongArrayTag -> StreamSupport.stream(Spliterators.spliterator(data.map(LongTag::of), 0), false)
    else -> error("Cannot get a stream for a non-collection tag!")
}
