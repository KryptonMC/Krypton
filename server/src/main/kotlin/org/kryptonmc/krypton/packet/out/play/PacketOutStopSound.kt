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
package org.kryptonmc.krypton.packet.out.play

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet

@JvmRecord
data class PacketOutStopSound(val source: Sound.Source?, val sound: Key?) : Packet {

    constructor(reader: BinaryReader) : this(reader, reader.readByte().toInt())

    private constructor(reader: BinaryReader, flags: Int) : this(
        if (flags and 1 > 0) reader.readEnum<Sound.Source>() else null,
        if (flags and 2 > 0) reader.readKey() else null
    )

    override fun write(writer: BinaryWriter) {
        if (source != null) {
            if (sound != null) {
                writer.writeByte(3)
                writer.writeEnum(source)
                writer.writeKey(sound)
                return
            }
            writer.writeByte(1)
            writer.writeEnum(source)
            return
        }
        if (sound != null) {
            writer.writeByte(2)
            writer.writeKey(sound)
            return
        }
        writer.writeByte(0)
    }

    companion object {

        @JvmStatic
        fun create(stop: SoundStop): PacketOutStopSound = PacketOutStopSound(stop.source(), stop.sound())
    }
}
