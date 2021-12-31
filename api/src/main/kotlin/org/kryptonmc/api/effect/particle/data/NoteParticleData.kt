/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.Contract

/**
 * Holds data for note particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface NoteParticleData : ParticleData {

    /**
     * The note that will be displayed. Must be between 0 and 24 (inclusive).
     */
    @get:JvmName("note")
    public val note: Byte

    public companion object {

        /**
         * Creates new note particle data with the given [note].
         *
         * @param note the note that will be displayed
         * @return new note particle data
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(note: Byte): NoteParticleData = ParticleData.FACTORY.note(note)
    }
}
