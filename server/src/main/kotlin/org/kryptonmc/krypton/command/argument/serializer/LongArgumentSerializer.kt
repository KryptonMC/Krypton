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

import com.mojang.brigadier.arguments.LongArgumentType
import io.netty.buffer.ByteBuf

/**
 * Long argument types are serialized with flags indicating if the minimum
 * value is equal to -[Long.MAX_VALUE], and the maximum value is equal to
 * [Long.MAX_VALUE], which are the minimum and maximum values that can be
 * used with Brigadier's long argument type.
 *
 * We then further write the minimum and maximum values, if they are not equal
 * to -[Long.MAX_VALUE] and [Long.MAX_VALUE] respectively.
 *
 * See [here](https://wiki.vg/Command_Data#brigadier:long)
 */
object LongArgumentSerializer : FlaggedArgumentSerializer<LongArgumentType> {

    override fun read(buf: ByteBuf, flags: Int): LongArgumentType {
        if (flags == 0) return LongArgumentType.longArg()
        val minimum = if (flags and 1 != 0) buf.readLong() else Long.MIN_VALUE
        val maximum = if (flags and 2 != 0) buf.readLong() else Long.MAX_VALUE
        return LongArgumentType.longArg(minimum, maximum)
    }

    override fun write(buf: ByteBuf, value: LongArgumentType) {
        val writeMin = value.minimum != Long.MIN_VALUE
        val writeMax = value.maximum != Long.MAX_VALUE
        buf.writeByte(createFlags(writeMin, writeMax))
        if (writeMin) buf.writeLong(value.minimum)
        if (writeMax) buf.writeLong(value.maximum)
    }
}
