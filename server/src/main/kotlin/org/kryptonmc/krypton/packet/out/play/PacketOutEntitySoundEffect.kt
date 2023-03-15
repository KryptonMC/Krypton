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

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.holder.Holder

@JvmRecord
data class PacketOutEntitySoundEffect(
    val event: Holder<SoundEvent>,
    val source: Sound.Source,
    override val entityId: Int,
    val volume: Float,
    val pitch: Float,
    val seed: Long
) : EntityPacket {

    constructor(reader: BinaryReader) : this(reader.readById(KryptonRegistries.SOUND_EVENT.asHolderIdMap(), KryptonSoundEvent::read),
        reader.readEnum(), reader.readVarInt(), reader.readFloat(), reader.readFloat(), reader.readLong())

    override fun write(writer: BinaryWriter) {
        writer.writeId(KryptonRegistries.SOUND_EVENT.asHolderIdMap(), event, KryptonSoundEvent::write)
        writer.writeEnum(source)
        writer.writeVarInt(entityId)
        writer.writeFloat(volume)
        writer.writeFloat(pitch)
        writer.writeLong(seed)
    }

    companion object {

        @JvmStatic
        fun create(source: KryptonEntity, sound: Sound, event: Holder<SoundEvent>, seed: Long): PacketOutEntitySoundEffect =
            PacketOutEntitySoundEffect(event, sound.source(), source.id, sound.volume(), sound.pitch(), seed)
    }
}
