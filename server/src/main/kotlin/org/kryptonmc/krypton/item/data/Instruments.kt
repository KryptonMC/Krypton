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
