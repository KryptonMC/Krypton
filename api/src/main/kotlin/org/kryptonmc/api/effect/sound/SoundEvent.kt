/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide

/**
 * A type of sound.
 */
public interface SoundEvent : Sound.Type {

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key): SoundEvent
    }

    public companion object {

        private val FACTORY = FactoryProvider.INSTANCE.provide<Factory>()

        /**
         * Creates a new sound event with the given [key].
         *
         * @param key the key
         * @return a new sound event
         */
        @JvmStatic
        public fun of(key: Key): SoundEvent = FACTORY.of(key)
    }
}
