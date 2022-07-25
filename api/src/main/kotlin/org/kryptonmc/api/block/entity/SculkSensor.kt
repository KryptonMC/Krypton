/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

/**
 * A sculk sensor.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface SculkSensor : BlockEntity {

    /**
     * The last vibration frequency of this sensor.
     *
     * Different activities detected by the sensor will produce different
     * frequencies and dictate the output of connected comparators.
     */
    @get:JvmName("lastVibrationFrequency")
    public var lastVibrationFrequency: Int
}
