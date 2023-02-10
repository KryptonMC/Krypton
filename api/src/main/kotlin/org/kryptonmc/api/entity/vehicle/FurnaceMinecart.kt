/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.vehicle

/**
 * A minecart with a furnace in it.
 *
 * The way this minecart behaves is quite strange. It consumes coal as a fuel
 * and burns the coal to propel itself forward.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FurnaceMinecart : MinecartLike {

    /**
     * The fuel this furnace minecart currently has.
     */
    public val fuel: Int

    /**
     * If this furnace minecart currently has fuel.
     *
     * @return true if this furnace minecart has fuel
     */
    public fun hasFuel(): Boolean

    /**
     * Adds the given [amount] of fuel to this furnace minecart.
     *
     * @param amount the amount of fuel to add
     */
    public fun addFuel(amount: Int)

    /**
     * Removes the given [amount] of fuel from this furnace minecart.
     *
     * @param amount the amount of fuel to remove
     */
    public fun removeFuel(amount: Int)

    /**
     * Resets the amount of fuel in this furnace minecart back to 0.
     */
    public fun resetFuel()
}
