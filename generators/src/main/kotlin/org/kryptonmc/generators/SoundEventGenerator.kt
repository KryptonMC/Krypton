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
package org.kryptonmc.generators

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import java.nio.file.Path

class SoundEventGenerator(path: Path) : StandardGenerator(path) {

    fun run() {
        run<SoundEvents, SoundEvent>(BuiltInRegistries.SOUND_EVENT, "effect.sound.SoundEvents", "effect.sound.SoundEvent", "SOUND_EVENT")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <S, T> collectFields(catalogueType: Class<S>, type: Class<T>): Sequence<CollectedField<T>> {
        val goatHorns = SoundEvents.GOAT_HORN_SOUND_VARIANTS.mapIndexed { index, sound ->
            CollectedField("GOAT_HORN_$index", type.cast(sound.value()))
        }

        val holdersToValues = catalogueType.staticFields()
            .filter { type.isAssignableFrom(it.type) || Holder::class.java.isAssignableFrom(it.type) }
            .map {
                val fieldValue = it.get(null)
                val value = if (Holder::class.java.isAssignableFrom(it.type)) (fieldValue as Holder<T>).value() else fieldValue as T
                CollectedField(it.name, value)
            }
        return holdersToValues.plus(goatHorns)
    }
}
