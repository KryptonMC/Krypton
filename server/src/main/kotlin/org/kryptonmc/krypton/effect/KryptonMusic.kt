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
package org.kryptonmc.krypton.effect

import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class KryptonMusic(
    override val sound: SoundEvent,
    override val minimumDelay: Int,
    override val maximumDelay: Int,
    override val replaceCurrentMusic: Boolean
) : Music {

    object Factory : Music.Factory {

        override fun of(sound: SoundEvent, minimumDelay: Int, maximumDelay: Int, replaceCurrentMusic: Boolean): Music =
            KryptonMusic(sound, minimumDelay, maximumDelay, replaceCurrentMusic)
    }

    companion object {

        // Time in ticks constants
        private const val TEN_MINUTES = 12000
        private const val TWENTY_MINUTES = 24000

        @JvmField
        val CODEC: Codec<Music> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonSoundEvent.DIRECT_CODEC.fieldOf("sound").getting { it.sound },
                Codec.INT.fieldOf("min_delay").getting { it.minimumDelay },
                Codec.INT.fieldOf("max_delay").getting { it.maximumDelay },
                Codec.BOOLEAN.fieldOf("replace_current_music").getting { it.replaceCurrentMusic }
            ).apply(instance, ::KryptonMusic)
        }

        @JvmStatic
        fun game(sound: SoundEvent): KryptonMusic = KryptonMusic(sound, TEN_MINUTES, TWENTY_MINUTES, false)
    }
}
