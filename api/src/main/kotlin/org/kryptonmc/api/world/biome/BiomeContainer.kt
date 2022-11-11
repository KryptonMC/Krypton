/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.kryptonmc.api.util.Vec3i

/**
 * Something that contains biomes.
 *
 * The default value that will be returned instead of null if no biome is
 * found is [Biomes.PLAINS].
 */
public interface BiomeContainer {

    /**
     * Gets the biome at the given [x], [y], and [z] coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the biome
     */
    public fun getBiome(x: Int, y: Int, z: Int): Biome

    /**
     * Gets the biome at the given [position].
     *
     * @param position the position
     * @return the biome at the given position
     */
    public fun getBiome(position: Vec3i): Biome
}
