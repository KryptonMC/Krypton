/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.aquatic

/**
 * A glow squid.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface GlowSquid : Squid {

    /**
     * The number of ticks remaining until this glow squid will start glowing.
     */
    @get:JvmName("remainingDarkTicks")
    public var remainingDarkTicks: Int
}
