/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.container

import org.kryptonmc.api.block.entity.NameableBlockEntity

/**
 * A furnace block entity.
 */
public interface FurnaceBlockEntity : ContainerBlockEntity, NameableBlockEntity {

    /**
     * The amount of time, in ticks, until the fuel in the furnace runs out.
     */
    public var remainingFuel: Int

    /**
     * The total amount of time, in ticks, that the fuel in the furnace will
     * burn for.
     */
    public val fuelDuration: Int

    /**
     * The current progress of the item being cooked.
     */
    public var cookingProgress: Int

    /**
     * The amount of time, in ticks, until the item in the furnace is cooked.
     */
    public val cookingDuration: Int
}
