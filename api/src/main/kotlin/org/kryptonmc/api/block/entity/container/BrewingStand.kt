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
 * A brewing stand.
 */
public interface BrewingStand : ContainerBlockEntity, NameableBlockEntity {

    /**
     * The amount of time, in ticks, until the fuel in the brewing stand runs
     * out.
     */
    public var remainingFuel: Int

    /**
     * The amount of time, in ticks, until the potions being brewed in this
     * brewing stand are brewed.
     */
    public var remainingBrewTime: Int

    /**
     * Brews the potions in this brewing stand.
     *
     * @return if the potions were successfully brewed
     */
    public fun brew(): Boolean
}
