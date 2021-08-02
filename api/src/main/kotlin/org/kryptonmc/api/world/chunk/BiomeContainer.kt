/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.chunk

import org.kryptonmc.api.world.biome.Biome

/**
 * Represents a container that holds biomes
 */
interface BiomeContainer {

    /**
     * The array of biomes in this chunk
     */
    val biomes: Array<out Biome>

    /**
     * Gets the biome located at the specified coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the biome
     */
    operator fun get(x: Int, y: Int, z: Int): Biome
}
