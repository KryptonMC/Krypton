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

import com.mojang.brigadier.arguments.ArgumentType
import io.netty.buffer.ByteBuf

sealed interface FlaggedArgumentSerializer<T : ArgumentType<*>> : ArgumentSerializer<T> {

    fun read(buf: ByteBuf, flags: Int): T

    override fun read(buf: ByteBuf): T = read(buf, buf.readByte().toInt())

    /**
     * Packs the minimum and maximum values in to a byte, where the first bit
     * (LSB) indicates the presence of a minimum value, and the second bit
     * (from LSB) represents the presence of a maximum value.
     */
    fun createFlags(minimum: Boolean, maximum: Boolean): Int {
        var flags = 0
        if (minimum) flags = flags or 1
        if (maximum) flags = flags or 2
        return flags
    }
}
