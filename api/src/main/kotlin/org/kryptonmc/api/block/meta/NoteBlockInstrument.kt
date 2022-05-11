/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents

/**
 * Indicates the instrument that a note block this property is applied to will
 * play when it is attacked or a redstone signal is applied to it.
 *
 * @param sound the corresponding sound event
 */
public enum class NoteBlockInstrument(@get:JvmName("sound") public val sound: SoundEvent) {

    HARP(SoundEvents.NOTE_BLOCK_HARP),
    BASEDRUM(SoundEvents.NOTE_BLOCK_BASEDRUM),
    SNARE(SoundEvents.NOTE_BLOCK_SNARE),
    HAT(SoundEvents.NOTE_BLOCK_HAT),
    BASS(SoundEvents.NOTE_BLOCK_BASS),
    FLUTE(SoundEvents.NOTE_BLOCK_FLUTE),
    BELL(SoundEvents.NOTE_BLOCK_BELL),
    GUITAR(SoundEvents.NOTE_BLOCK_GUITAR),
    CHIME(SoundEvents.NOTE_BLOCK_CHIME),
    XYLOPHONE(SoundEvents.NOTE_BLOCK_XYLOPHONE),
    IRON_XYLOPHONE(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE),
    COW_BELL(SoundEvents.NOTE_BLOCK_COW_BELL),
    DIDGERIDOO(SoundEvents.NOTE_BLOCK_DIDGERIDOO),
    BIT(SoundEvents.NOTE_BLOCK_BIT),
    BANJO(SoundEvents.NOTE_BLOCK_BANJO),
    PLING(SoundEvents.NOTE_BLOCK_PLING)
}
