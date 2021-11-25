/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.item.ItemTypes

/**
 * An axolotl.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Axolotl : Animal {

    /**
     * The variant of this axolotl.
     */
    @get:JvmName("variant")
    public var variant: AxolotlVariant

    /**
     * If this axolotl is currently playing dead.
     */
    public var isPlayingDead: Boolean

    /**
     * If this axolotl was spawned from a [bucket][ItemTypes.AXOLOTL_BUCKET].
     */
    @get:JvmName("spawnedFromBucket")
    public var spawnedFromBucket: Boolean
}
