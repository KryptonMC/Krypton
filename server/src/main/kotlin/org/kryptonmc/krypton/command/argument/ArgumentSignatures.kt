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
package org.kryptonmc.krypton.command.argument

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.readCollection
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeString

@JvmRecord
data class ArgumentSignatures(val entries: List<Entry>) : Writable {

    constructor(buf: ByteBuf) : this(buf.readCollection(ByteBufExtras.limitValue(::ArrayList, MAX_ARGUMENT_COUNT), ::Entry))

    fun get(name: String): MessageSignature? = entries.firstOrNull { it.name == name }?.signature

    override fun write(buf: ByteBuf) {
        buf.writeCollection(entries) { it.write(buf) }
    }

    @JvmRecord
    data class Entry(val name: String, val signature: MessageSignature) : Writable {

        constructor(buf: ByteBuf) : this(buf.readString(MAX_ARGUMENT_NAME_LENGTH), MessageSignature.read(buf))

        override fun write(buf: ByteBuf) {
            buf.writeString(name, MAX_ARGUMENT_NAME_LENGTH)
            MessageSignature.write(buf, signature)
        }
    }

    companion object {

        // This is from vanilla
        private const val MAX_ARGUMENT_COUNT = 8
        private const val MAX_ARGUMENT_NAME_LENGTH = 16
    }
}
