/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.item.data.DyeColor

/**
 * A cat.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Cat : Tamable {

    /**
     * The variant of this cat.
     */
    @get:JvmName("catType")
    public var variant: CatVariant

    /**
     * If this cat is currently lying down.
     */
    public var isLying: Boolean

    /**
     * If this cat is currently relaxed.
     */
    public var isRelaxed: Boolean

    /**
     * The colour of this cat's collar.
     */
    @get:JvmName("collarColor")
    public var collarColor: DyeColor

    /**
     * Orders the cat to hiss, playing the sound [SoundEvents.CAT_HISS] to
     * surrounding entities.
     */
    public fun hiss()
}
