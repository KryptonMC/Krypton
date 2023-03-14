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
