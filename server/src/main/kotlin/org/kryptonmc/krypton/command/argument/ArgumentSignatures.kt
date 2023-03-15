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
package org.kryptonmc.krypton.command.argument

import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.network.chat.MessageSignature
import org.kryptonmc.krypton.util.ByteBufExtras

@JvmRecord
data class ArgumentSignatures(val entries: List<Entry>) : Writable {

    constructor(reader: BinaryReader) : this(reader.readCollection(ByteBufExtras.limitValue(::ArrayList, MAX_ARGUMENT_COUNT), ::Entry))

    fun get(name: String): MessageSignature? = entries.firstOrNull { it.name == name }?.signature

    override fun write(writer: BinaryWriter) {
        writer.writeCollection(entries) { it.write(writer) }
    }

    @JvmRecord
    data class Entry(val name: String, val signature: MessageSignature) : Writable {

        init {
            require(name.length <= MAX_ARGUMENT_NAME_LENGTH) { "Name too long! Max: $MAX_ARGUMENT_NAME_LENGTH" }
        }

        constructor(reader: BinaryReader) : this(reader.readString(), MessageSignature.read(reader))

        override fun write(writer: BinaryWriter) {
            writer.writeString(name)
            MessageSignature.write(writer, signature)
        }
    }

    companion object {

        // This is from vanilla
        private const val MAX_ARGUMENT_COUNT = 8
        private const val MAX_ARGUMENT_NAME_LENGTH = 16
    }
}
