/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A type of sound.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(SoundEvents::class)
@ImmutableType
public interface SoundEvent : Sound.Type {

    /**
     * The range that this sound can be heard from.
     *
     * A value of 0 indicates the sound does not have a range.
     */
    @get:JvmName("range")
    public val range: Float
}
