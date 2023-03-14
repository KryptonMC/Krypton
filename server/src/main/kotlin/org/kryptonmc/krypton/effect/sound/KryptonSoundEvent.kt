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
package org.kryptonmc.krypton.effect.sound

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.network.RegistryFileCodec
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder
import java.util.Optional

@JvmRecord
data class KryptonSoundEvent(private val key: Key, override val range: Float, private val newSystem: Boolean) : SoundEvent {

    fun getRange(volume: Float): Float {
        if (newSystem) return range
        return if (volume > 1F) DEFAULT_RANGE * volume else DEFAULT_RANGE
    }

    override fun key(): Key = key

    private fun fixedRange(): Optional<Float> = if (newSystem) Optional.of(range) else Optional.empty()

    companion object {

        // TODO: Replace most usages of this with the registry file codec.
        @JvmField
        val DIRECT_CODEC: Codec<SoundEvent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Keys.CODEC.fieldOf("sound_id").getting { it.key() },
                Codec.FLOAT.optionalFieldOf("range").getting { it.downcast().fixedRange() }
            ).apply(instance) { id, range -> if (range.isPresent) KryptonSoundEvent(id, range.get(), true) else createVariableRange(id) }
        }
        @JvmField
        val CODEC: Codec<Holder<SoundEvent>> = RegistryFileCodec.create(ResourceKeys.SOUND_EVENT, DIRECT_CODEC)
        // This is the default range that sounds travel (from vanilla) that the majority of sounds have from before ranged sounds were added.
        const val DEFAULT_RANGE: Float = 16F

        @JvmStatic
        fun createVariableRange(key: Key): KryptonSoundEvent = KryptonSoundEvent(key, DEFAULT_RANGE, false)

        @JvmStatic
        fun read(buf: ByteBuf): KryptonSoundEvent {
            val key = buf.readKey()
            val newSystem = buf.readBoolean()
            val range = if (newSystem) buf.readFloat() else DEFAULT_RANGE
            return KryptonSoundEvent(key, range, newSystem)
        }

        @JvmStatic
        fun write(buf: ByteBuf, inputEvent: SoundEvent) {
            val event = inputEvent.downcast()
            buf.writeKey(event.key)
            buf.writeBoolean(event.newSystem)
            if (event.newSystem) buf.writeFloat(event.range)
        }
    }
}
