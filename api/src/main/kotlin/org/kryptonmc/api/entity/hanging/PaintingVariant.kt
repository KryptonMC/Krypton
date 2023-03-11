/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.entity.hanging

import net.kyori.adventure.key.Keyed
import org.kryptonmc.internal.annotations.CataloguedBy
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
