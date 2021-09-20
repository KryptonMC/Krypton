package org.kryptonmc.krypton.effect.sound

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent

@JvmRecord
data class KryptonSoundEvent(private val key: Key) : SoundEvent {

    override fun key(): Key = key

    object Factory : SoundEvent.Factory {

        override fun of(key: Key) = KryptonSoundEvent(key)
    }
}
