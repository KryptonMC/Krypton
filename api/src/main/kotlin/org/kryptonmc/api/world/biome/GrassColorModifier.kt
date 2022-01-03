/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.kryptonmc.api.util.StringSerializable

/**
 * A modifier for the colouring of grass within a biome.
 */
public enum class GrassColorModifier(@get:JvmName("serialized") override val serialized: String) : StringSerializable {

    NONE("none"),
    DARK_FOREST("dark_forest"),
    SWAMP("swamp")
}
