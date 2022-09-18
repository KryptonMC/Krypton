/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy

/**
 * A type of sound.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(SoundEvents::class)
public interface SoundEvent : Sound.Type {

    /**
     * The range that this sound can be heard from.
     *
     * A value of 0 indicates the sound does not have a range.
     */
    @get:JvmName("range")
    public val range: Float

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, range: Float): SoundEvent
    }

    public companion object {

        /**
         * Creates a new sound event with the given [key].
         *
         * @param key the key
         * @return a new sound event
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key): SoundEvent = of(key, 0F)

        /**
         * Creates a new sound event with the given [key] and [range].
         *
         * @param key the key
         * @param range the range the sound can be heard
         * @return a new sound event
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(key: Key, range: Float): SoundEvent = Krypton.factory<Factory>().of(key, range)
    }
}
