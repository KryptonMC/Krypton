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
import org.kryptonmc.api.registry.RegistryReference

/**
 * Indicates the instrument that a note block this property is applied to will
 * play when it is attacked or a redstone signal is applied to it.
 */
public enum class NoteBlockInstrument(
    /**
     * The corresponding sound event that will be played when note blocks with
     * this instrument are interacted with.
     */
    public val sound: RegistryReference<SoundEvent>,
    /**
     * The type of instrument this instrument is.
     */
    public val type: Type
) {

    HARP(SoundEvents.NOTE_BLOCK_HARP, Type.BLOCK),
    BASEDRUM(SoundEvents.NOTE_BLOCK_BASEDRUM, Type.BLOCK),
    SNARE(SoundEvents.NOTE_BLOCK_SNARE, Type.BLOCK),
    HAT(SoundEvents.NOTE_BLOCK_HAT, Type.BLOCK),
    BASS(SoundEvents.NOTE_BLOCK_BASS, Type.BLOCK),
    FLUTE(SoundEvents.NOTE_BLOCK_FLUTE, Type.BLOCK),
    BELL(SoundEvents.NOTE_BLOCK_BELL, Type.BLOCK),
    GUITAR(SoundEvents.NOTE_BLOCK_GUITAR, Type.BLOCK),
    CHIME(SoundEvents.NOTE_BLOCK_CHIME, Type.BLOCK),
    XYLOPHONE(SoundEvents.NOTE_BLOCK_XYLOPHONE, Type.BLOCK),
    IRON_XYLOPHONE(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, Type.BLOCK),
    COW_BELL(SoundEvents.NOTE_BLOCK_COW_BELL, Type.BLOCK),
    DIDGERIDOO(SoundEvents.NOTE_BLOCK_DIDGERIDOO, Type.BLOCK),
    BIT(SoundEvents.NOTE_BLOCK_BIT, Type.BLOCK),
    BANJO(SoundEvents.NOTE_BLOCK_BANJO, Type.BLOCK),
    PLING(SoundEvents.NOTE_BLOCK_PLING, Type.BLOCK),
    ZOMBIE(SoundEvents.NOTE_BLOCK_IMITATE_ZOMBIE, Type.MOB_HEAD),
    SKELETON(SoundEvents.NOTE_BLOCK_IMITATE_SKELETON, Type.MOB_HEAD),
    CREEPER(SoundEvents.NOTE_BLOCK_IMITATE_CREEPER, Type.MOB_HEAD),
    DRAGON(SoundEvents.NOTE_BLOCK_IMITATE_ENDER_DRAGON, Type.MOB_HEAD),
    WITHER_SKELETON(SoundEvents.NOTE_BLOCK_IMITATE_WITHER_SKELETON, Type.MOB_HEAD),
    PIGLIN(SoundEvents.NOTE_BLOCK_IMITATE_PIGLIN, Type.MOB_HEAD),
    CUSTOM_HEAD(SoundEvents.UI_BUTTON_CLICK, Type.CUSTOM);

    /**
     * The type of note block instrument.
     */
    public enum class Type {

        BLOCK,
        MOB_HEAD,
        CUSTOM
    }
}
