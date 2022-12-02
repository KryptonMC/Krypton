/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.fluid

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.state.StateHolder
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A fluid with certain properties.
 *
 * The design of this is very similar to that of the [Block].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Fluids::class)
@ImmutableType
public interface Fluid : StateHolder<FluidState>, Keyed {

    /**
     * The type of the bucket this fluid can be held in.
     */
    @get:JvmName("bucket")
    public val bucket: ItemType

    /**
     * If this fluid is an empty fluid.
     */
    public val isEmpty: Boolean

    /**
     * The value for this fluid's resistance to explosions.
     */
    @get:JvmName("explosionResistance")
    public val explosionResistance: Double
}
