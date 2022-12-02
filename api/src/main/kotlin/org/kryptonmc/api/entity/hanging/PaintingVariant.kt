/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * A variant of a painting.
 *
 * This determines what image will actually appear on the painting when it is
 * rendered by the client.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(PaintingVariants::class)
@ImmutableType
public interface PaintingVariant : Keyed {

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
}
