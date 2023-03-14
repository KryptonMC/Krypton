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
package org.kryptonmc.krypton.world.biome.data

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.codecs.RecordCodecBuilder

@JvmRecord
data class KryptonAmbientMoodSettings(
    override val sound: SoundEvent,
    override val tickDelay: Int,
    override val blockSearchExtent: Int,
    override val offset: Double
) : AmbientMoodSettings {

    class Builder(private var sound: SoundEvent) : AmbientMoodSettings.Builder {

        private var tickDelay = 0
        private var searchExtent = 0
        private var offset = 0.0

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
        val CAVE: KryptonAmbientMoodSettings = KryptonAmbientMoodSettings(SoundEvents.AMBIENT_CAVE.get(), 6000, 8, 2.0)

        @JvmField
        val CODEC: Codec<AmbientMoodSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                KryptonSoundEvent.DIRECT_CODEC.fieldOf("sound").getting { it.sound },
                Codec.INT.fieldOf("tick_delay").getting { it.tickDelay },
                Codec.INT.fieldOf("block_search_extent").getting { it.blockSearchExtent },
                Codec.DOUBLE.fieldOf("offset").getting { it.offset }
            ).apply(instance, ::KryptonAmbientMoodSettings)
        }
    }
}
