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
