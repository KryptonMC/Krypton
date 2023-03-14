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
package org.kryptonmc.api.effect.particle.data

import org.jetbrains.annotations.Contract
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Holds data for note particle effects.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
        public fun of(note: Byte): NoteParticleData = ParticleData.factory().note(note)
    }
}
