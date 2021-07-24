/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.effect.sound.SoundEvent

data class Music(
    val sound: SoundEvent,
    val minimumDelay: Int,
    val maximumDelay: Int,
    val replace: Boolean
) {

    companion object {

        val CODEC: Codec<Music> = RecordCodecBuilder.create {
            it.group(
                SOUND_EVENT_CODEC.fieldOf("sound").forGetter(Music::sound),
                Codec.INT.fieldOf("min_delay").forGetter(Music::minimumDelay),
                Codec.INT.fieldOf("max_delay").forGetter(Music::maximumDelay),
                Codec.BOOL.fieldOf("replace_current_music").forGetter(Music::replace)
            ).apply(it, ::Music)
        }
    }
}
