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
package org.kryptonmc.krypton.world.biome

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.CompoundCodecBuilder

@JvmRecord
data class KryptonAmbientMoodSettings(
    override val sound: SoundEvent,
    override val tickDelay: Int,
    override val blockSearchExtent: Int,
    override val offset: Double
) : AmbientMoodSettings {

    override fun toBuilder(): AmbientMoodSettings.Builder = Builder(this)

    class Builder(private var sound: SoundEvent) : AmbientMoodSettings.Builder {

        private var tickDelay = 0
        private var searchExtent = 0
        private var offset = 0.0

        constructor(settings: AmbientMoodSettings) : this(settings.sound) {
            tickDelay = settings.tickDelay
            searchExtent = settings.blockSearchExtent
            offset = settings.offset
        }

        override fun sound(sound: SoundEvent): AmbientMoodSettings.Builder = apply { this.sound = sound }

        override fun delay(delay: Int): AmbientMoodSettings.Builder = apply { tickDelay = delay }

        override fun searchExtent(extent: Int): AmbientMoodSettings.Builder = apply { searchExtent = extent }

        override fun offset(offset: Double): AmbientMoodSettings.Builder = apply { this.offset = offset }

        override fun build(): AmbientMoodSettings = KryptonAmbientMoodSettings(sound, tickDelay, searchExtent, offset)
    }

    object Factory : AmbientMoodSettings.Factory {

        override fun of(sound: SoundEvent, tickDelay: Int, blockSearchExtent: Int, offset: Double): AmbientMoodSettings =
            KryptonAmbientMoodSettings(sound, tickDelay, blockSearchExtent, offset)

        override fun builder(sound: SoundEvent): AmbientMoodSettings.Builder = Builder(sound)
    }

    companion object {

        @JvmField
        val CAVE: KryptonAmbientMoodSettings = KryptonAmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0)

        @JvmField
        val CODEC: Codec<AmbientMoodSettings> = CompoundCodecBuilder.create { instance ->
            instance.group(
                Codecs.SOUND_EVENT.field("sound").getting(AmbientMoodSettings::sound),
                Codec.INT.field("tick_delay").getting(AmbientMoodSettings::tickDelay),
                Codec.INT.field("block_search_extent").getting(AmbientMoodSettings::blockSearchExtent),
                Codec.DOUBLE.field("offset").getting(AmbientMoodSettings::offset)
            ).apply(instance, ::KryptonAmbientMoodSettings)
        }
    }
}
