/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
