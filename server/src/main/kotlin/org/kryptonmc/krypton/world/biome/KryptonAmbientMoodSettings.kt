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
package org.kryptonmc.krypton.world.biome

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.krypton.util.Codecs

@JvmRecord
data class KryptonAmbientMoodSettings(
    override val sound: SoundEvent,
    override val tickDelay: Int,
    override val blockSearchExtent: Int,
    override val offset: Double
) : AmbientMoodSettings {

    object Factory : AmbientMoodSettings.Factory {

        override fun of(sound: SoundEvent, tickDelay: Int, blockSearchExtent: Int, offset: Double): AmbientMoodSettings =
            KryptonAmbientMoodSettings(sound, tickDelay, blockSearchExtent, offset)
    }

    companion object {

        val CAVE = KryptonAmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0)
        val CODEC: Codec<AmbientMoodSettings> = RecordCodecBuilder.create {
            it.group(
                Codecs.SOUND_EVENT.fieldOf("sound").forGetter(AmbientMoodSettings::sound),
                Codec.INT.fieldOf("tick_delay").forGetter(AmbientMoodSettings::tickDelay),
                Codec.INT.fieldOf("block_search_extent").forGetter(AmbientMoodSettings::blockSearchExtent),
                Codec.DOUBLE.fieldOf("offset").forGetter(AmbientMoodSettings::offset)
            ).apply(it, ::KryptonAmbientMoodSettings)
        }
    }
}
