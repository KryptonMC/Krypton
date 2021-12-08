/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.kryptonmc.api.util.StringSerializable

/**
 * A modifier for temperature in a climate.
 */
public enum class TemperatureModifier(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    NONE("none"),
    FROZEN("frozen")
}
