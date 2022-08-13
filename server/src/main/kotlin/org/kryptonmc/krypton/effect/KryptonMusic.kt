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
package org.kryptonmc.krypton.effect

import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.krypton.util.serialization.Codecs
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
                Codecs.SOUND_EVENT.field("sound").getting(Music::sound),
                Codec.INT.field("min_delay").getting(Music::minimumDelay),
                Codec.INT.field("max_delay").getting(Music::maximumDelay),
                Codec.BOOLEAN.field("replace_current_music").getting(Music::replaceCurrentMusic)
            ).apply(instance, ::KryptonMusic)
        }

        @JvmStatic
        fun game(sound: SoundEvent): KryptonMusic = KryptonMusic(sound, TEN_MINUTES, TWENTY_MINUTES, false)
    }
}
