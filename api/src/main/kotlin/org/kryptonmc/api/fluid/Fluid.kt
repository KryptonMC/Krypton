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
import org.kryptonmc.api.block.BlockLike
import org.kryptonmc.api.block.property.PropertyHolder
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.util.CataloguedBy

/**
 * A fluid with certain properties.
 *
 * The design of this is very similar to that of the [Block].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Fluids::class)
public interface Fluid : PropertyHolder<Fluid>, FluidLike, BlockLike, Keyed {

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

    /**
     * If this fluid is a source fluid.
     */
    public val isSource: Boolean

    /**
     * The height of this fluid.
     */
    @get:JvmName("height")
    public val height: Float

    /**
     * The level of this fluid.
     *
     * Should be either a constant value, such as 0 for the empty fluid, or 8
     * for source fluids, or the value of the
     * [level][org.kryptonmc.api.block.property.Properties.LIQUID_LEVEL]
     * property for flowing fluids.
     */
    @get:JvmName("level")
    public val level: Int

    override fun asFluid(): Fluid = this

    override fun asBlock(): Block
}
