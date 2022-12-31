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
package org.kryptonmc.krypton.item.data

import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.krypton.registry.KryptonRegistries

object Instruments {

    @JvmField
    val PONDER_GOAT_HORN: Instrument = register("ponder_goat_horn", SoundEvents.GOAT_HORN_0)
    @JvmField
    val SING_GOAT_HORN: Instrument = register("sing_goat_horn", SoundEvents.GOAT_HORN_1)
    @JvmField
    val SEEK_GOAT_HORN: Instrument = register("seek_goat_horn", SoundEvents.GOAT_HORN_2)
    @JvmField
    val FEEL_GOAT_HORN: Instrument = register("feel_goat_horn", SoundEvents.GOAT_HORN_3)
    @JvmField
    val ADMIRE_GOAT_HORN: Instrument = register("admire_goat_horn", SoundEvents.GOAT_HORN_4)
    @JvmField
    val CALL_GOAT_HORN: Instrument = register("call_goat_horn", SoundEvents.GOAT_HORN_5)
    @JvmField
    val YEARN_GOAT_HORN: Instrument = register("yearn_goat_horn", SoundEvents.GOAT_HORN_6)
    @JvmField
    val DREAM_GOAT_HORN: Instrument = register("dream_goat_horn", SoundEvents.GOAT_HORN_7)

    @JvmStatic
    @Suppress("MagicNumber")
    private fun register(name: String, sound: RegistryReference<SoundEvent>): Instrument =
        KryptonRegistries.register(KryptonRegistries.INSTRUMENT, Key.key(name), Instrument(sound.get(), 140, 256F))
}
