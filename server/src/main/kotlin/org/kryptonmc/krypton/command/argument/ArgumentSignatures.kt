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
import org.kryptonmc.krypton.util.readMap
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeMap
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarIntByteArray

@JvmRecord
data class ArgumentSignatures(val salt: Long, val signatures: Map<String, ByteArray>) : Writable {

    constructor(buf: ByteBuf) : this(buf.readLong(), buf.readMap({ buf.readString(16) }, ByteBuf::readVarIntByteArray))

    fun get(argument: String): ByteArray? = signatures.get(argument)

    override fun write(buf: ByteBuf) {
        buf.writeLong(salt)
        buf.writeMap(signatures, { _, value -> buf.writeString(value, 16) }, ByteBuf::writeVarIntByteArray)
    }
}
