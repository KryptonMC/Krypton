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
package org.kryptonmc.krypton.command.argument.serializer

import com.mojang.brigadier.arguments.IntegerArgumentType
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter

/**
 * Integer argument types are serialized with flags indicating if the minimum
 * value is equal to -[Int.MAX_VALUE], and the maximum value is equal to
 * [Int.MAX_VALUE], which are the minimum and maximum values that can be
 * used with Brigadier's integer argument type.
 *
 * We then further write the minimum and maximum values, if they are not equal
 * to -[Int.MAX_VALUE] and [Int.MAX_VALUE] respectively.
 *
 * See [here](https://wiki.vg/Command_Data#brigadier:integer)
 */
object IntegerArgumentSerializer : FlaggedArgumentSerializer<IntegerArgumentType> {

    override fun read(reader: BinaryReader, flags: Int): IntegerArgumentType {
        if (flags == 0) return IntegerArgumentType.integer() // No flags, so both min and max are absent
        val minimum = if (flags and 1 != 0) reader.readInt() else Int.MIN_VALUE
        val maximum = if (flags and 2 != 0) reader.readInt() else Int.MAX_VALUE
        return IntegerArgumentType.integer(minimum, maximum)
    }

    override fun write(writer: BinaryWriter, value: IntegerArgumentType) {
        val writeMin = value.minimum != Int.MIN_VALUE
        val writeMax = value.maximum != Int.MAX_VALUE
        writer.writeByte(createFlags(writeMin, writeMax))
        if (writeMin) writer.writeInt(value.minimum)
        if (writeMax) writer.writeInt(value.maximum)
    }
}
