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

import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.io.TagCompression
import org.kryptonmc.nbt.util.Types
import org.kryptonmc.nbt.visitor.StreamingTagVisitor
import org.kryptonmc.nbt.visitor.StreamingTagVisitor.ValueResult
import java.io.DataInput
import java.io.DataInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

object TagIOExtras {

    @JvmStatic
    fun parse(path: Path, visitor: StreamingTagVisitor, compression: TagCompression) {
        parse(Files.newInputStream(path), visitor, compression)
    }

    @JvmStatic
    fun parse(input: InputStream, visitor: StreamingTagVisitor, compression: TagCompression) {
        DataInputStream(compression.decompress(input)).use { parse(it, visitor) }
    }

    @JvmStatic
    fun parse(input: DataInput, visitor: StreamingTagVisitor) {
        val type = Types.of(input.readByte().toInt())
        if (type == EndTag.TYPE) {
            if (visitor.visitRootEntry(EndTag.TYPE) == ValueResult.CONTINUE) visitor.visitEnd()
            return
        }
        when (visitor.visitRootEntry(type)) {
            ValueResult.BREAK -> {
                input.skipBytes(input.readUnsignedShort()) // from StringTagImpl.skipString
                type.skip(input)
            }
            ValueResult.CONTINUE -> {
                input.skipBytes(input.readUnsignedShort()) // from StringTagImpl.skipString
                type.parse(input, visitor)
            }
            else -> Unit
        }
    }
}
