package org.kryptonmc.krypton.effect

import com.mojang.serialization.Codec
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.krypton.util.KEY_CODEC

val SOUND_EVENT_CODEC: Codec<SoundEvent> = KEY_CODEC.xmap(::SoundEvent) { it.key }
