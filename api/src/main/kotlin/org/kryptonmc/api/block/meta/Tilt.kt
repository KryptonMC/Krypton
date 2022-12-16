/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.meta

/**
 * Indicates the tilt of a big dripleaf this property is applied to.
 *
 * Large dripleaf plants can tilt downwards when significant force is applied
 * to the top of them, for example, when a player stands on them.
 */
public enum class Tilt {

    /**
     * The dripleaf is flat, and not tilting.
     */
    NONE,

    /**
     * The dripleaf is unstable, and will partially tilt after 10 ticks.
     *
     * This tilt state does not cause sculk vibrations.
     */
    UNSTABLE,

    /**
     * The dripleaf is partially tilted, and will fully tilt after 10 ticks.
     */
    PARTIAL,

    /**
     * The dripleaf will fully tilt, and will return to none after 100 ticks.
     */
    FULL;

    /**
     * Gets whether this tilt causes vibrations that sculk sensors in the area
     * will respond to.
     *
     * This determines whether jumping on a big dripleaf with this tilt will
     * alert nearby sculk sensors.
     *
     * @return true if causes vibrations, false otherwise
     */
    public fun causesVibrations(): Boolean = this != UNSTABLE
}
