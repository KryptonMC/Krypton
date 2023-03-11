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
