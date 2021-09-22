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
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BiomeContainer {

    /**
     * The array of biomes in this chunk
     */
    @get:JvmName("biomes")
    public val biomes: Array<out Biome>

    /**
     * Gets the biome located at the specified coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the biome
     */
    public operator fun get(x: Int, y: Int, z: Int): Biome

    /**
     * Sets the biome located at the specified coordinates to the specified
     * [biome].
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param biome the new biome
     */
    public operator fun set(x: Int, y: Int, z: Int, biome: Biome)
}
