/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * The sounds that a block will make when specific actions are taken, such as
 * breaking it, stepping on it, or falling on it.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface BlockSoundGroup {

    /**
     * The volume that the sounds in this group will be played at.
     */
    @get:JvmName("volume")
    public val volume: Float

    /**
     * The pitch that the sounds in this group will be played at.
     */
    @get:JvmName("pitch")
    public val pitch: Float

    /**
     * The sound that is played when the block is broken.
     */
    @get:JvmName("breakSound")
    public val breakSound: SoundEvent

    /**
     * The sound that is played when the block is stepped on.
     */
    @get:JvmName("stepSound")
    public val stepSound: SoundEvent

    /**
     * The sound that is played when the block is placed.
     */
    @get:JvmName("placeSound")
    public val placeSound: SoundEvent

    /**
     * The sound that is played when the block is hit.
     */
    @get:JvmName("hitSound")
    public val hitSound: SoundEvent

    /**
     * The sound that is played when the block falls.
     */
    @get:JvmName("fallSound")
    public val fallSound: SoundEvent
}
