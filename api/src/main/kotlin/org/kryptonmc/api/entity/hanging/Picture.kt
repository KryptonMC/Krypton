/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A picture that may appear on the canvas of a [Painting].
 *
 * This determines what image will actually appear on the painting when it is
 * rendered by the client.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(Pictures::class)
public interface Picture : Keyed {

    /**
     * The width of the image.
     *
     * This is measured in pixels, not blocks, and the pixel measurements are
     * relative to the default texture pack, which renders 16x16 block
     * textures, so a painting that is 16x16 is exactly one block wide.
     */
    @get:JvmName("width")
    public val width: Int

    /**
     * The height of the image.
     *
     * This is measured in pixels, not blocks, and the pixel measurements are
     * relative to the default texture pack, which renders 16x16 block
     * textures, so a painting that is 16x16 is exactly one block wide.
     */
    @get:JvmName("height")
    public val height: Int

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key, width: Int, height: Int): Picture
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new picture with the given values.
         *
         * @param key the key
         * @param width the width
         * @param height the height
         * @return a new picture
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key, width: Int, height: Int): Picture = FACTORY.of(key, width, height)
    }
}
