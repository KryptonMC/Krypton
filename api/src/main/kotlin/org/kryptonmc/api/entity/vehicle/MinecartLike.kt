/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.vehicle

import org.kryptonmc.api.block.Block

/**
 * Something that shares some (or all) functionality with that of a [Minecart].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface MinecartLike : DamageableVehicle {

    /**
     * The type of this minecart like object.
     */
    @get:JvmName("minecartType")
    public val minecartType: MinecartType

    /**
     * If the custom block is shown on the minecart.
     */
    @get:JvmName("showCustomBlock")
    public var showCustomBlock: Boolean

    /**
     * The custom block shown within the minecart.
     */
    @get:JvmName("customBlock")
    public var customBlock: Block

    /**
     * The offset of the custom block.
     */
    @get:JvmName("customBlockOffset")
    public var customBlockOffset: Int
}
