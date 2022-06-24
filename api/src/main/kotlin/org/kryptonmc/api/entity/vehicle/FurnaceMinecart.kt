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
     * If this furnace minecart currently has fuel.
     */
    @get:JvmName("hasFuel")
    public var hasFuel: Boolean

    /**
     * The fuel this furnace minecart currently has.
     */
    @get:JvmName("fuel")
    public var fuel: Int
}
