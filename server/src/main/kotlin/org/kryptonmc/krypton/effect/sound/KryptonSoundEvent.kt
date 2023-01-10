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
package org.kryptonmc.krypton.effect.sound

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent
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

        // TODO: Replace most usages of this with a new registry file codec.
        @JvmField
        val DIRECT_CODEC: Codec<SoundEvent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Keys.CODEC.fieldOf("sound_id").getting { it.key() },
                Codec.FLOAT.optionalFieldOf("range").getting { it.downcast().fixedRange() }
            ).apply(instance) { id, range -> if (range.isPresent) KryptonSoundEvent(id, range.get(), true) else createVariableRange(id) }
        }
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
