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
 *
 * @param causesVibrations if this tilt causes vibrations that the sculk sensor
 * will pick up
 */
public enum class Tilt(@get:JvmName("causesVibrations") public val causesVibrations: Boolean) {

    /**
     * The dripleaf is flat, and not tilting.
     */
    NONE(true),

    /**
     * The dripleaf is unstable, and will partially tilt after 10 ticks.
     *
     * This tilt state does not cause sculk vibrations.
     */
    UNSTABLE(false),

    /**
     * The dripleaf is partially tilted, and will fully tilt after 10 ticks.
     */
    PARTIAL(true),

    /**
     * The dripleaf will fully tilt, and will return to none after 100 ticks.
     */
    FULL(true)
}
